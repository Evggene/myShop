package org.bea.my_shop.repository;

import org.bea.my_shop.configuration.BaseRepositoryTest;
import org.bea.my_shop.domain.Cart;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepositoryImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

class CartRepositoryTest extends BaseRepositoryTest {

    private UUID testCartId;
    private UUID testItemId;

    @BeforeEach
    void setup() {
        testCartId = UUID.randomUUID();
        testItemId = UUID.randomUUID();

        Mono.when(
                databaseClient.sql("DELETE FROM cart_items").then(),
                databaseClient.sql("DELETE FROM cart").then(),
                databaseClient.sql("DELETE FROM item").then(),
                databaseClient.sql("""
            INSERT INTO item (id, title, description, image_path, price)
            VALUES (:id, 'test title', 'desc', 'path', 5)
                        """).bind("id", testItemId).then(),
                databaseClient.sql("""
                        INSERT INTO cart (id, cart_state) 
                        VALUES (:id, 'PREPARE')
                        """).bind("id", testCartId).then(),
                databaseClient.sql("""
                        INSERT INTO cart_items (cart_id, item_id, count) 
                        VALUES (:cartId, :itemId, 2)
                        """).bind("cartId", testCartId).bind("itemId", testItemId).then()
        ).block();
    }

    @Test
    void findByIdWithAllItems_shouldReturnCartWithItems() {
        cartRepository.findByIdWithAllItems(testCartId)
                .as(StepVerifier::create)
                .assertNext(cart -> {
                    Assertions.assertEquals(testCartId, cart.getId());
                    Assertions.assertEquals(1, cart.getPositions().size());
                    Assertions.assertEquals(2, cart.getPositions().values().iterator().next());
                })
                .verifyComplete();
    }

    @Test
    void findFirstByCartState_shouldReturnCart() {
        cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE)
                .as(StepVerifier::create)
                .assertNext(cart -> {
                    Assertions.assertEquals(testCartId, cart.getId());
                    Assertions.assertFalse(cart.getPositions().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void save_shouldUpdateCart() {
        var item = new Item();
        item.setId(testItemId);
        Cart cart = new Cart();
        cart.setId(testCartId);
        cart.setCartState(CartStateType.BUY);
        var pos = new HashMap<Item, Integer>(){{put(item, 5);}};
        cart.setPositions(pos);

        cartRepository.save(cart)
                .thenMany(cartRepository.findByIdWithAllItems(testCartId))
                .as(StepVerifier::create)
                .assertNext(updatedCart -> {
                    Assertions.assertEquals(CartStateType.BUY, updatedCart.getCartState());
                    Assertions.assertEquals(5, updatedCart.getPositions().get(item));
                })
                .verifyComplete();
    }

    @Test
    void deleteCartItems_shouldClearCart() {
        cartRepository.deleteCartItems(testCartId)
                .thenMany(databaseClient.sql("SELECT count(*) FROM cart_items WHERE cart_id = :cartId")
                        .bind("cartId", testCartId)
                        .fetch()
                        .first())
                .as(StepVerifier::create)
                .assertNext(result ->
                        Assertions.assertEquals(0L, result.get("count")))
                .verifyComplete();
    }

    @Test
    void findFirstByCartState_whenNoCarts_shouldReturnNewCart() {
        Mono.when(
                databaseClient.sql("DELETE FROM cart_items").then(),
                databaseClient.sql("DELETE FROM cart").then()
        ).block();

        cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE)
                .as(StepVerifier::create)
                .assertNext(cart -> {
                    Assertions.assertNotNull(cart.getId());
                    Assertions.assertTrue(cart.getPositions().isEmpty());
                })
                .verifyComplete();
    }

    private Mono<Void> insertTestItem(UUID id, String title, String description,
                                      String imagePath, BigDecimal price, int count) {
        return databaseClient.sql("""
            INSERT INTO item (id, title, description, image_path, price)
            VALUES (:id, :title, :desc, :path, :price)
            """)
                .bind("id", id)
                .bind("title", title)
                .bind("desc", description)
                .bind("path", imagePath)
                .bind("price", price)
                .then()
                .then(databaseClient.sql("""
                INSERT INTO item_count (item_id, count)
                VALUES (:id, :count)
                """)
                        .bind("id", id)
                        .bind("count", count)
                        .then());
    }
}
