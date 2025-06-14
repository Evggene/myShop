//package org.bea.my_shop.mapper;
//
//import org.bea.my_shop.application.mapper.OrderMapper;
//import org.bea.my_shop.infrastructure.input.dto.ItemInCartRequest;
//import org.bea.my_shop.infrastructure.input.dto.OrderRequest;
//import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
//import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
//import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
//import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class OrderMapperTest {
//
//    @Test
//    void entityToRequest_shouldMapOrderEntityToOrderRequest() {
//        UUID itemId1 = UUID.randomUUID();
//        UUID itemId2 = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        BigDecimal totalSum = new BigDecimal("199.98");
//
//        ItemEntity item1 = ItemEntity.builder()
//                .id(itemId1)
//                .title("Item 1")
//                .price(new BigDecimal("99.99"))
//                .description("Description 1")
//                .imagePath("image1.jpg")
//                .itemCountEntity(ItemCountEntity.builder().build())
//                .build();
//
//        ItemEntity item2 = ItemEntity.builder()
//                .id(itemId2)
//                .title("Item 2")
//                .price(new BigDecimal("50.00"))
//                .description("Description 2")
//                .imagePath("image2.jpg")
//                .itemCountEntity(ItemCountEntity.builder().build())
//                .build();
//
//        Map<ItemEntity, Integer> positions = new HashMap<>();
//        positions.put(item1, 1);
//        positions.put(item2, 2);
//
//        CartEntity cart = CartEntity.builder()
//                .positions(positions)
//                .build();
//
//        OrderEntity orderEntity = OrderEntity.builder()
//                .id(orderId)
//                .cart(cart)
//                .totalSum(totalSum)
//                .build();
//
//        OrderRequest orderRequest = OrderMapper.entityToRequest(orderEntity);
//
//        assertNotNull(orderRequest);
//        assertEquals(orderId, orderRequest.getId());
//        assertEquals(totalSum, orderRequest.getTotalSum());
//
//        List<ItemInCartRequest> items = orderRequest.getItems();
//        assertNotNull(items);
//        assertEquals(2, items.size());
//
//        ItemInCartRequest itemRequest1 = items.stream()
//                .filter(item -> item.getId().equals(itemId1))
//                .findFirst()
//                .orElseThrow();
//        assertEquals(1, itemRequest1.getCountInCart());
//        assertEquals("Item 1", itemRequest1.getTitle());
//
//        ItemInCartRequest itemRequest2 = items.stream()
//                .filter(item -> item.getId().equals(itemId2))
//                .findFirst()
//                .orElseThrow();
//        assertEquals(2, itemRequest2.getCountInCart());
//        assertEquals("Item 2", itemRequest2.getTitle());
//    }
//
//    @Test
//    void entityToRequest_shouldHandleEmptyCart() {
//        UUID orderId = UUID.randomUUID();
//        BigDecimal totalSum = new BigDecimal("0.00");
//
//        CartEntity cart = CartEntity.builder()
//                .positions(new HashMap<>())
//                .build();
//
//        OrderEntity orderEntity = OrderEntity.builder()
//                .id(orderId)
//                .cart(cart)
//                .totalSum(totalSum)
//                .build();
//
//        OrderRequest orderRequest = OrderMapper.entityToRequest(orderEntity);
//
//        assertNotNull(orderRequest);
//        assertEquals(orderId, orderRequest.getId());
//        assertEquals(totalSum, orderRequest.getTotalSum());
//        assertNotNull(orderRequest.getItems());
//        assertTrue(orderRequest.getItems().isEmpty());
//    }
//}
