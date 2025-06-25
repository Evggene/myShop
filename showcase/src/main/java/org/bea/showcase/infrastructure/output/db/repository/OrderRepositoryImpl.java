package org.bea.showcase.infrastructure.output.db.repository;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.mapper.OrderMapper;
import org.bea.showcase.application.port.output.OrderRepository;
import org.bea.showcase.domain.Cart;
import org.bea.showcase.domain.CartStateType;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.bea.showcase.domain.Order;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final DatabaseClient client;

    private static final String saveOrderSql = """
        INSERT INTO orders (id, cart_id, total_sum)
        VALUES (:id, :cartId, :totalSum)
        ON CONFLICT (id)
        DO UPDATE SET
            cart_id = EXCLUDED.cart_id,
            total_sum = EXCLUDED.total_sum
        """;

    private static final String getAllSql = """
                SELECT o.id AS order_id,
                       o.total_sum AS order_total_sum,
                       c.id AS cart_id,
                       c.cart_state AS cart_state
                FROM orders o
                JOIN cart c ON o.cart_id = c.id
                """;

    private static final String getById =
                getAllSql + """
                where o.id = :id
                """;

    @Override
    public Mono<Order> save(Order order) {
        var orderEntity = OrderMapper.fromModelToEntity(order);
        var orderToSave = client.sql(saveOrderSql)
                .bindProperties(orderEntity)
                .then();
        return Mono.when(orderToSave).thenReturn(order);
    }

    @Override
    public Mono<Order> getById(UUID id) {
        return client.sql(getById)
                .bind("id", id)
                .fetch()
                .one()
                .flatMap(orderRow -> {
                    var order = buildOrder(orderRow);
                    var cart = buildCart(orderRow, order);
                    return loadCartItems(cart.getId())
                            .collectMap(Function.identity(), Item::getCount)
                            .map(itemsMap -> {
                                cart.setPositions(new HashMap<>(itemsMap));
                                return order;
                            });
                });
    }

    @Override
    public Flux<Order> getAll() {
        return client.sql(getAllSql)
                .fetch()
                .all()
                .flatMap(orderRow -> {
                    var order = buildOrder(orderRow);
                    var cart = buildCart(orderRow, order);
                    return loadCartItems(cart.getId())
                            .collectMap(Function.identity(), Item::getCount)
                            .map(itemsMap -> {
                                cart.setPositions(new HashMap<>(itemsMap));
                                return order;
                            });
                });
    }

    private static Cart buildCart(Map<String, Object> orderRow, Order order) {
        var cart = new Cart();
        cart.setId((UUID) orderRow.get("cart_id"));
        cart.setCartState(CartStateType.valueOf(orderRow.get("cart_state").toString()));
        cart.setPositions(new HashMap<>());
        order.setCart(cart);
        return cart;
    }

    private Order buildOrder(Map<String, Object> orderRow) {
        var order = new Order();
        order.setId((UUID) orderRow.get("order_id"));
        order.setTotalSum(new Money((BigDecimal) orderRow.get("order_total_sum")));
        return order;
    }

    private Flux<Item> loadCartItems(UUID cartId) {
        return client.sql("""
            SELECT i.id, i.title, i.description, 
                   i.image_path, i.price, ci.count
            FROM cart_items ci
            JOIN item i ON ci.item_id = i.id
            WHERE ci.cart_id = :cartId
            """)
                .bind("cartId", cartId)
                .fetch()
                .all()
                .map(row -> {
                    Item item = new Item();
                    item.setId((UUID) row.get("id"));
                    item.setTitle((String) row.get("title"));
                    item.setDescription((String) row.get("description"));
                    item.setImagePath((String) row.get("image_path"));
                    item.setPrice(new Money((BigDecimal) row.get("price")));
                    item.setCount((Integer) row.get("count"));
                    return item;
                });
    }
}
