package org.bea.showcase.service;

import org.bea.showcase.application.exception.MyShopException;
import org.bea.showcase.application.type.ActionType;
import org.bea.showcase.configuration.BaseServiceTest;
import org.bea.showcase.domain.CartStateType;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

class ActionCartServicePlusTest extends BaseServiceTest {

    private UUID itemId;
    private Item testItem;

    @BeforeEach
    void setUp() {
        itemId = UUID.randomUUID();

        testItem = Item.builder()
                .id(itemId)
                .title("Test Item")
                .description("Test Description")
                .imagePath("/test.jpg")
                .price(new Money(new BigDecimal("99.99")))
                .count(10)
                .build();

        clearData().block();
    }

    private Mono<Void> clearData() {
        return databaseClient.sql("DELETE FROM cart_items").then()
                .then(databaseClient.sql("DELETE FROM cart").then())
                .then(databaseClient.sql("DELETE FROM item").then())
                .then(databaseClient.sql("DELETE FROM item_count").then());
    }

    @Test
    void handleAction_ShouldAddItemToCart() {
        // Подготовка тестовых данных
        itemRepository.save(testItem).block();

        StepVerifier.create(actionCartService.handleAction(itemId, ActionType.PLUS))
                .expectNext(ActionType.PLUS)
                .verifyComplete();

        // Проверяем, что товар добавился в корзину
        StepVerifier.create(cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE))
                .assertNext(cart -> {
                    Assertions.assertEquals(1, cart.getPositions().size());
                    Assertions.assertTrue(cart.getPositions().containsKey(testItem));
                })
                .verifyComplete();
    }

    @Test
    void handleAction_ShouldFailWhenItemNotFound() {
        UUID nonExistentItemId = UUID.randomUUID();

        StepVerifier.create(actionCartService.handleAction(nonExistentItemId, ActionType.PLUS))
                .expectErrorMatches(ex -> ex instanceof MyShopException &&
                        ex.getMessage().equals("Item not found"))
                .verify();
    }

    @Test
    void handleAction_ShouldCreateNewCartIfNotExists() {
        // Убедимся, что корзины нет
        databaseClient.sql("DELETE FROM cart").then().block();

        itemRepository.save(testItem).block();

        StepVerifier.create(actionCartService.handleAction(itemId, ActionType.PLUS))
                .expectNext(ActionType.PLUS)
                .verifyComplete();

        // Проверяем, что корзина создалась
        StepVerifier.create(cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE))
                .assertNext(cart -> {
                    Assertions.assertNotNull(cart.getId());
                    Assertions.assertEquals(1, cart.getPositions().size());
                })
                .verifyComplete();
    }

    @Test
    void handleAction_ShouldUpdateItemCountWhenAdding() {
        // Подготовка - добавляем товар и создаем корзину с этим товаром
        itemRepository.save(testItem).block();
        actionCartService.handleAction(itemId, ActionType.PLUS).block();

        // Повторное добавление того же товара
        StepVerifier.create(actionCartService.handleAction(itemId, ActionType.PLUS))
                .expectNext(ActionType.PLUS)
                .verifyComplete();

        // Проверяем, что количество увеличилось
        StepVerifier.create(cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE))
                .assertNext(cart -> {
                    Assertions.assertEquals(1, cart.getPositions().size());
                    Assertions.assertEquals(2, cart.getPositions().get(testItem));
                })
                .verifyComplete();
    }

}
