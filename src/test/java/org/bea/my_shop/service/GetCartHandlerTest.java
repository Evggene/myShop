//package org.bea.my_shop.service;
//
//
//import org.bea.my_shop.application.dto.ItemAndPriceInfo;
//import org.bea.my_shop.application.handler.cart.GetCartHandler;
//import org.bea.my_shop.domain.CartStateType;
//import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
//import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.math.BigDecimal;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GetCartHandlerTest extends BaseHandlerTest{
//
//    @Autowired
//    private GetCartHandler getCartHandler;
//
//    private UUID prepareCartId;
//
//    @BeforeEach
//    void setUp() {
//        var item1 = createItem("Apple iPhone 13", new BigDecimal("999.99"));
//        itemRepository.save(item1);
//        var item2 = createItem("Samsung Galaxy S22", new BigDecimal("899.99"));
//        itemRepository.save(item2);
//
//        var prepareCart = new CartEntity();
//        prepareCart.setCartState(CartStateType.PREPARE);
//        prepareCart.setPositions(Map.of(item1, 2, item2, 1));
//        prepareCart = cartRepository.save(prepareCart);
//        prepareCartId = prepareCart.getId();
//
//        // Создаем корзину в другом состоянии для проверки фильтрации
//        CartEntity completedCart = new CartEntity();
//        completedCart.setCartState(CartStateType.BUY);
//        completedCart.setPositions(Map.of(item1, 1));
//        cartRepository.save(completedCart);
//    }
//
//    private ItemEntity createItem(String title, BigDecimal price) {
//        ItemEntity item = new ItemEntity();
//        item.setTitle(title);
//        item.setPrice(price);
//        item.setDescription("Description for " + title);
//        item.setImagePath(title.toLowerCase().replace(" ", "-") + ".jpg");
//        item.setCountAndAuditFields(6);
//        return item;
//    }
//
//    @Test
//    void getCartStatePrepare_shouldReturnCartInfoWhenPrepareCartExists() {
//        ItemAndPriceInfo result = getCartHandler.getCartStatePrepare();
//
//        assertNotNull(result);
//        assertEquals(prepareCartId, result.cartId());
//
//        BigDecimal expectedTotal = new BigDecimal("999.99")
//                .multiply(BigDecimal.valueOf(2))
//                .add(new BigDecimal("899.99"));
//        assertEquals(0, expectedTotal.compareTo(result.totalPrice()));
//
//        assertNotNull(result.items());
//        assertEquals(2, result.items().size());
//
//        assertEquals("Apple iPhone 13", result.items().get(0).getTitle());
//        assertEquals("Samsung Galaxy S22", result.items().get(1).getTitle());
//
//        assertEquals(2, result.items().get(0).getCount()); // iPhone count = 2
//        assertEquals(1, result.items().get(1).getCount()); // Galaxy count = 1
//    }
//
//    @Test
//    void getCartStatePrepare_shouldReturnEmptyInfoWhenNoPrepareCart() {
//        cartRepository.deleteAll();
//
//        ItemAndPriceInfo result = getCartHandler.getCartStatePrepare();
//
//        assertNotNull(result);
//        assertNull(result.cartId());
//        assertNull(result.items());
//        assertEquals(BigDecimal.ZERO, result.totalPrice());
//    }
//
//    @Test
//    void getCartStatePrepare_shouldHandleEmptyCart() {
//        cartRepository.deleteAll();
//        CartEntity emptyCart = new CartEntity();
//        emptyCart.setCartState(CartStateType.PREPARE);
//        emptyCart.setPositions(Map.of());
//        cartRepository.save(emptyCart);
//
//        ItemAndPriceInfo result = getCartHandler.getCartStatePrepare();
//
//        assertNotNull(result);
//        assertNotNull(result.cartId());
//        assertTrue(result.items().isEmpty());
//        assertEquals(BigDecimal.ZERO, result.totalPrice());
//    }
//}
