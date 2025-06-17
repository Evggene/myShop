package org.bea.my_shop.service;


import org.bea.my_shop.application.service.cart.ActionCartService;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.configuration.BaseServiceTest;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ActionCartHandlerPlusTest extends BaseServiceTest {

    @Autowired
    ActionCartService actionCartHandler;
    @Autowired
    ItemRepository itemRepository;
    private UUID itemId;

    @Test
    void a() {
        Item item = Item.builder()
                .id(UUID.randomUUID())
                .title("Test Item")
                .description("Test Description")
                .imagePath("/test.jpg")
                .price(new Money(new BigDecimal("99.99")))
                .count(5)
                .build();
        itemRepository.save(item);

    }

//    @BeforeEach
//    void setUp() {
//        // Создаем тестовый товар
//        ItemEntity item = new ItemEntity();
//        item.setTitle("Test Item");
//        item.setPrice(new BigDecimal("100.00"));
//        item.setDescription("Test Description");
//        item.setImagePath("test.jpg");
//        item.setCountAndAuditFields(5);
//
//        item = itemRepository.save(item);
//        itemId = item.getId();
//
//        // Создаем корзину в состоянии PREPARE
//        CartEntity cart = new CartEntity();
//        cart.setCartState(CartStateType.PREPARE);
//        cartRepository.save(cart);
//    }
//
//    @Test
//    void handleAction_shouldIncreaseQuantityInCartAndDecreaseItemCount() {
//        actionCartHandler.handleAction(itemId, ActionType.PLUS);
//
//        ItemEntity updatedItem = itemRepository.findById(itemId).orElseThrow();
//
//        CartEntity cart = cartRepository.findFirstByCartState(CartStateType.PREPARE).orElseThrow();
//        Map<ItemEntity, Integer> positions = cart.getPositions();
//        assertEquals(1, positions.size());
//        assertEquals(1, positions.get(updatedItem));
//    }
//
//    @Test
//    void handleAction_shouldNotModifyWhenItemOutOfStock() {
//        // Given - устанавливаем нулевое количество товара
//        ItemEntity item = itemRepository.findById(itemId).orElseThrow();
//        item.getItemCountEntity().setCount(0);
//        itemRepository.save(item);
//
//        // When
//        actionCartHandler.handleAction(itemId, ActionType.PLUS);
//
//        // Then
//        // Проверяем что количество не изменилось (осталось 0)
//        ItemEntity updatedItem = itemRepository.findById(itemId).orElseThrow();
//        assertEquals(0, updatedItem.getItemCountEntity().getCount());
//
//        // Проверяем что корзина не изменилась
//        CartEntity cart = cartRepository.findFirstByCartState(CartStateType.PREPARE).orElseThrow();
//        assertTrue(cart.getPositions().isEmpty());
//    }
//
//    @Test
//    void handleAction_shouldIncrementExistingPositionInCart() {
//        // Given - добавляем товар в корзину один раз
//        actionCartHandler.handleAction(itemId, ActionType.PLUS);
//
//        // When - добавляем тот же товар еще раз
//        actionCartHandler.handleAction(itemId, ActionType.PLUS);
//
//        // Then
//        // Проверяем количество на складе
//        ItemEntity updatedItem = itemRepository.findById(itemId).orElseThrow();
//        assertEquals(5 - 2, updatedItem.getItemCountEntity().getCount());
//
//        // Проверяем корзину
//        CartEntity cart = cartRepository.findFirstByCartState(CartStateType.PREPARE).orElseThrow();
//        assertEquals(1, cart.getPositions().size());
//        assertEquals(2, cart.getPositions().get(updatedItem));
//    }
//
//    @Test
//    void handleAction_shouldCreateNewCartIfNotExists() {
//        // Given - удаляем все корзины
//        cartRepository.deleteAll();
//
//        // When
//        actionCartHandler.handleAction(itemId, ActionType.PLUS);
//
//        // Then
//        // Проверяем что корзина создана
//        Optional<CartEntity> cartOpt = cartRepository.findFirstByCartState(CartStateType.PREPARE);
//        assertTrue(cartOpt.isPresent());
//
//        // Проверяем содержимое корзины
//        CartEntity cart = cartOpt.get();
//        assertEquals(1, cart.getPositions().size());
//    }
}
