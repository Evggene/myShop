package org.bea.showcase.service;

import org.bea.showcase.configuration.BaseServiceSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GetCartServiceTest extends BaseServiceSpringBootTest {

    private UUID testCartId;
    private UUID testItemId;

    @BeforeEach
    void setUp() {
        testCartId = UUID.randomUUID();
        testItemId = UUID.randomUUID();
        clearData().block();
    }

    private Mono<Void> clearData() {
        return databaseClient.sql("DELETE FROM cart_items").then()
                .then(databaseClient.sql("DELETE FROM cart").then())
                .then(databaseClient.sql("DELETE FROM item").then())
                .then(databaseClient.sql("DELETE FROM item_count").then());
    }

    private Mono<Void> createTestCartWithItem() {
        return databaseClient.sql("""
                INSERT INTO item (id, title, description, image_path, price)
                VALUES (:id, 'Test Item', 'Desc', '/test.jpg', 99.99)
                """)
                .bind("id", testItemId)
                .then()
                .then(databaseClient.sql("""
                        INSERT INTO cart (id, cart_state) 
                        VALUES (:id, 'PREPARE')
                        """)
                        .bind("id", testCartId)
                        .then()
                        .then(databaseClient.sql("""
                        INSERT INTO cart_items (cart_id, item_id, count) 
                        VALUES (:cartId, :itemId, 2)
                        """)
                                .bind("cartId", testCartId)
                                .bind("itemId", testItemId)
                                .then()));
    }

    @Test
    void getCartStatePrepare_ShouldReturnCartWithItems() {
        createTestCartWithItem().block();

        StepVerifier.create(getCartService.getCartAndBalance())
                .assertNext(result -> {
                    assertEquals(testCartId, result.cartId());
                    assertEquals(1, result.items().size());
                    assertEquals(0, new BigDecimal("199.98").compareTo(result.totalPrice()));
                    assertEquals("Test Item", result.items().get(0).getTitle());
                    assertEquals(2, result.items().get(0).getCount()); // Проверяем что count установлен правильно
                })
                .verifyComplete();
    }

    @Test
    void getCartStatePrepare_ShouldReturnEmptyForNoCart() {
        StepVerifier.create(getCartService.getCartAndBalance())
                .assertNext(result -> {
                    assertTrue(result.items().isEmpty());
                    assertEquals(0, BigDecimal.ZERO.compareTo(result.totalPrice()));
                })
                .verifyComplete();
    }

    @Test
    void getCartStatePrepare_ShouldCalculateTotalCorrectly() {
        // Создаем второй товар для проверки расчета суммы
        UUID secondItemId = UUID.randomUUID();

        createTestCartWithItem()
                .then(databaseClient.sql("""
                        INSERT INTO item (id, title, description, image_path, price)
                        VALUES (:id, 'Second Item', 'Desc 2', '/second.jpg', 50.00)
                        """)
                        .bind("id", secondItemId)
                        .then())
                .then(databaseClient.sql("""
                        INSERT INTO cart_items (cart_id, item_id, count) 
                        VALUES (:cartId, :itemId, 3)
                        """)
                        .bind("cartId", testCartId)
                        .bind("itemId", secondItemId)
                        .then())
                .block();

        StepVerifier.create(getCartService.getCartAndBalance())
                .assertNext(result -> {
                    assertEquals(2, result.items().size());
                    // 99.99 * 2 + 50.00 * 3 = 199.98 + 150.00 = 349.98
                    assertEquals(0, new BigDecimal("349.98").compareTo(result.totalPrice()));
                })
                .verifyComplete();
    }

    @Test
    void getCartStatePrepare_ShouldSortItemsByTitle() {
        UUID itemBId = UUID.randomUUID();
        UUID itemAId = UUID.randomUUID();

        createTestCartWithItem()
                .then(databaseClient.sql("""
                        INSERT INTO item (id, title, description, image_path, price)
                        VALUES (:id, 'B Item', 'Desc B', '/b.jpg', 10.00)
                        """)
                        .bind("id", itemBId)
                        .then())
                .then(databaseClient.sql("""
                        INSERT INTO item (id, title, description, image_path, price)
                        VALUES (:id, 'A Item', 'Desc A', '/a.jpg', 20.00)
                        """)
                        .bind("id", itemAId)
                        .then())
                .then(databaseClient.sql("""
                        INSERT INTO cart_items (cart_id, item_id, count) 
                        VALUES (:cartId, :itemId, 1)
                        """)
                        .bind("cartId", testCartId)
                        .bind("itemId", itemBId)
                        .then())
                .then(databaseClient.sql("""
                        INSERT INTO cart_items (cart_id, item_id, count) 
                        VALUES (:cartId, :itemId, 1)
                        """)
                        .bind("cartId", testCartId)
                        .bind("itemId", itemAId)
                        .then())
                .block();

        StepVerifier.create(getCartService.getCartAndBalance())
                .assertNext(result -> {
                    assertEquals(3, result.items().size());
                    // Проверяем сортировку по title
                    assertEquals("A Item", result.items().get(0).getTitle());
                    assertEquals("B Item", result.items().get(1).getTitle());
                    assertEquals("Test Item", result.items().get(2).getTitle());
                })
                .verifyComplete();
    }
}
