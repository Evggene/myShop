package org.bea.showcase.repository;

import org.bea.showcase.configuration.BaseRepositoryTest;
import org.bea.showcase.domain.Cart;
import org.bea.showcase.domain.CartStateType;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.junit.jupiter.api.*;
import org.bea.showcase.domain.Order;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

class OrderRepositoryTest extends BaseRepositoryTest {

    @BeforeEach
    void setup() {
                databaseClient.sql("DELETE FROM orders").then()
                .then(databaseClient.sql("DELETE FROM cart_items").then())
                .then(databaseClient.sql("DELETE FROM cart").then())
                .then(databaseClient.sql("DELETE FROM item").then())
                .then(databaseClient.sql("DELETE FROM item_count").then())
                        .block();
    }

    @Test
    void save_shouldPersistOrder() {
        var item = Item.builder()
                .id(UUID.randomUUID())
                .title("Test Item")
                .description("Test Description")
                .imagePath("/test.jpg")
                .price(new Money(new BigDecimal("99.99")))
                .count(5)
                .build();

        var order = new Order();
        order.setId(UUID.randomUUID());
        order.setTotalSum(new Money(new BigDecimal("99.99")));

        var cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setCartState(CartStateType.BUY);
        cart.setPositions(new HashMap<>(){{put(item, 5);}});
        order.setCart(cart);

        Mono.when(itemRepository.save(item), cartRepository.save(cart), orderRepository.save(order))
                .then(orderRepository.getById(order.getId()))
                .as(StepVerifier::create)
                .assertNext(savedOrder -> {
                    Assertions.assertEquals(order.getId(), savedOrder.getId());
                    Assertions.assertTrue(savedOrder.getTotalSum().getPrice().compareTo(BigDecimal.valueOf(99.99)) == 0);
                })
                .verifyComplete();
    }

    @Test
    void getAll_shouldReturnAllOrders() {
        var item = Item.builder()
                .id(UUID.randomUUID())
                .title("Test Item")
                .description("Test Description")
                .imagePath("/test.jpg")
                .price(new Money(new BigDecimal("99.99")))
                .count(5)
                .build();

        var order = new Order();
        order.setId(UUID.randomUUID());
        order.setTotalSum(new Money(new BigDecimal("99.99")));

        var cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setCartState(CartStateType.BUY);
        cart.setPositions(new HashMap<>(){{put(item, 5);}});
        order.setCart(cart);

        itemRepository.save(item)
                .then(cartRepository.save(cart))
                .then(orderRepository.save(order))
                .thenMany(orderRepository.getAll())
                .collectList()
                .as(StepVerifier::create)
                .expectNextMatches(ord -> {
                    var o = ord.size();
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void getById_whenNotExists_shouldReturnEmpty() {
        orderRepository.getById(UUID.randomUUID())
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
