package org.bea.my_shop.infrastructure.output.db.repository;

import io.r2dbc.spi.Statement;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final DatabaseClient client;

    private final static String selectCartWithAllItemsByCartState = """
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
                """;

    private final static String insertCart = """
                INSERT INTO cart (id, cart_state) 
                VALUES (:id, :cartState)
                ON CONFLICT (id) 
                DO UPDATE SET cart_state = EXCLUDED.cart_state
                """;

    private final static String insertCartItem = """
                INSERT INTO cart_items (cart_id, item_id, count) 
                VALUES (:cartId, :itemId, :count)
                ON CONFLICT (cart_id, item_id) 
                DO UPDATE SET count = EXCLUDED.count
                """;

    @Override
    public Mono<Cart> findFirstByCartStateWithAllItems(CartStateType type) {
        return client.sql(selectCartWithAllItemsByCartState)
                .bind("cartState", type.name())
                .fetch()
                .all()
                .collectList()
                .flatMap(rows -> {
                    if (rows.isEmpty()) {
                        var emptyCart = Cart.builder()
                                .id(UUID.randomUUID())
                                .cartState(CartStateType.PREPARE)
                                .positions(new HashMap<>())
                                .build();
                        return Mono.just(emptyCart);
                    }
                    Cart cart = buildCart(rows);
                    return Mono.just(cart);
                });
    }

    private Cart buildCart(List<Map<String, Object>> rows) {
        var cart = new Cart();
        Map<Item, Integer> positions = new HashMap<>();

        Map<String, Object> firstRow = rows.getFirst();
        cart.setId(UUID.fromString(firstRow.get("cart_id").toString()));
        cart.setCartState(CartStateType.valueOf(firstRow.get("cart_state").toString()));

        for (Map<String, Object> row : rows) {
            var item = new Item();
            item.setId(UUID.fromString(row.get("item_id").toString()));
            item.setTitle(row.get("item_title").toString());
            item.setDescription(row.get("item_description").toString());
            item.setImagePath(row.get("item_image_path").toString());
            item.setPrice(new Money(new BigDecimal(row.get("item_price").toString())));
            var stockCount = row.get("item_stock_count");
            if (stockCount != null) {
                item.setCount(Integer.parseInt(stockCount.toString()));
            }
            int cartItemCount = Integer.parseInt(row.get("cart_item_count").toString());
            positions.put(item, cartItemCount);
        }
        cart.setPositions(positions);
        return cart;
    }

    public Mono<Cart> save(Cart cart) {
        var cartS = client.sql(insertCart)
                .bind("id", cart.getId())
                .bind("cartState", cart.getCartState().name())
                .then();

        var cartItem = saveCartItemsSimple(cart);
        return Mono.when(cartS, cartItem).thenReturn(cart);
    }

    private Mono<Cart> saveCartItemsSimple(Cart cart) {
        return Flux.fromIterable(cart.getPositions().entrySet())
                .flatMap(entry -> client.sql("""
                INSERT INTO cart_items (cart_id, item_id, count)
                VALUES (:cartId, :itemId, :count)
                ON CONFLICT (cart_id, item_id) 
                DO UPDATE SET count = :count
                """)
                        .bind("cartId", cart.getId())
                        .bind("itemId", entry.getKey().getId())
                        .bind("count", entry.getValue())
                        .then()
                )
                .then()
                .thenReturn(cart);
    }
}
