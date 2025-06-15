package org.bea.my_shop.infrastructure.output.db.repository;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.domain.Cart;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository{

    private final DatabaseClient client;

    @Override
    public Mono<Cart> findFirstByCartStateWithAllItems(CartStateType type) {
        return client.sql("""
            SELECT 
                c.id AS cart_id,
                c.cart_state AS cart_state,
                i.id AS item_id,
                i.title AS item_title,
                i.description AS item_description,
                i.image_path AS item_image_path,
                i.price AS item_price,
                ic.count AS item_stock_count,
                ci.count AS cart_item_count
            FROM cart c
            JOIN cart_items ci ON c.id = ci.cart_id
            JOIN item i ON ci.item_id = i.id
            LEFT JOIN item_count ic ON i.id = ic.item_id
            WHERE c.cart_state = :cartState
            ORDER BY c.id
            LIMIT 1
            """)
                .bind("cartState", type.name())
                .fetch()
                .all()
                .collectList()
                .flatMap(rows -> {
                    if (rows.isEmpty()) {
                        return Mono.empty();
                    }

                    // Создаем корзину
                    Cart cart = new Cart();
                    Map<Item, Integer> positions = new HashMap<>();

                    // Заполняем данные корзины из первой строки
                    Map<String, Object> firstRow = rows.get(0);
                    cart.setId(UUID.fromString(firstRow.get("cart_id").toString()));
                    cart.setCartState(CartStateType.valueOf(firstRow.get("cart_state").toString()));

                    // Заполняем позиции корзины
                    for (Map<String, Object> row : rows) {
                        Item item = new Item();
                        item.setId(UUID.fromString(row.get("item_id").toString()));
                        item.setTitle(row.get("item_title").toString());
                        item.setDescription(row.get("item_description").toString());
                        item.setImagePath(row.get("item_image_path").toString());
                        item.setPrice(new Money(new BigDecimal(row.get("item_price").toString())));

                        // Устанавливаем общее количество из item_count
                        Object stockCount = row.get("item_stock_count");
                        if (stockCount != null) {
                            item.setCount(Integer.parseInt(stockCount.toString()));
                        }

                        // Добавляем товар в корзину с количеством из cart_items
                        int cartItemCount = Integer.parseInt(row.get("cart_item_count").toString());
                        positions.put(item, cartItemCount);
                    }

                    cart.setPositions(positions);
                    return Mono.just(cart);
                });
    }

    public Mono<Cart> save(Cart cart) {
        return client.sql("""
            INSERT INTO cart (id, cart_state) 
            VALUES (:id, :cartState)
            ON CONFLICT (id) 
            DO UPDATE SET cart_state = EXCLUDED.cart_state
            """)
                .bind("id", cart.getId())
                .bind("cartState", cart.getCartState().name())
                .then()
                .thenMany(saveCartItemsWithStockUpdate(cart))
                .then(Mono.just(cart));
    }

    private Flux<Void> saveCartItemsWithStockUpdate(Cart cart) {
        return Flux.fromIterable(cart.getPositions().entrySet())
                .flatMap(entry -> {
                    UUID itemId = entry.getKey().getId();
                    int newCount = entry.getValue();

                    // 1. Сохраняем/обновляем позицию в корзине
                    return client.sql("""
                    INSERT INTO cart_items (cart_id, item_id, count) 
                    VALUES (:cartId, :itemId, :count)
                    ON CONFLICT (cart_id, item_id) 
                    DO UPDATE SET count = EXCLUDED.count
                    """)
                            .bind("cartId", cart.getId())
                            .bind("itemId", itemId)
                            .bind("count", newCount)
                            .then()
                            // 2. Обновляем общее количество товара
                            .then(updateItemCount(itemId, newCount));
                });
    }

    private Mono<Void> updateItemCount(UUID itemId, int newCount) {
        return client.sql("""
            INSERT INTO item_count (item_id, count) 
            VALUES (:itemId, :count)
            ON CONFLICT (item_id) 
            DO UPDATE SET count = EXCLUDED.count
            """)
                .bind("itemId", itemId)
                .bind("count", newCount)
                .then();
    }
}
