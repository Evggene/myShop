package org.bea.my_shop.controller;

import org.bea.my_shop.application.handler.OrderHandler;
import org.bea.my_shop.application.mapper.OrderMapper;
import org.bea.my_shop.controller.BaseControllerTest;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.infrastructure.input.controller.OrderController;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest extends BaseControllerTest {

    @Test
    void getOrder_shouldReturnOrderViewWithOrderDetails() throws Exception {
        // Given
        UUID orderId = UUID.randomUUID();
        OrderEntity order = createTestOrder(orderId, BigDecimal.valueOf(150));

        when(orderHandler.getById(orderId)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("order", OrderMapper.entityToRequest(order)))
                .andExpect(model().attribute("newOrder", "false"));

        verify(orderHandler, times(1)).getById(orderId);
    }

    @Test
    void getOrder_withNewOrderParam_shouldReturnOrderViewWithNewOrderFlag() throws Exception {
        // Given
        UUID orderId = UUID.randomUUID();
        OrderEntity order = createTestOrder(orderId, BigDecimal.valueOf(150));

        when(orderHandler.getById(orderId)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/orders/{id}?newOrder=true", orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("order", OrderMapper.entityToRequest(order)))
                .andExpect(model().attribute("newOrder", "true"));

        verify(orderHandler, times(1)).getById(orderId);
    }

    private OrderEntity createTestOrder(UUID orderId, BigDecimal totalSum) {
        // Create items
        ItemEntity item1 = ItemEntity.builder()
                .id(UUID.randomUUID())
                .title("Test Item 1")
                .description("Description 1")
                .imagePath("/images/item1.jpg")
                .price(BigDecimal.valueOf(50))
                .build();
        item1.setCountAndAuditFields(5);

        ItemEntity item2 = ItemEntity.builder()
                .id(UUID.randomUUID())
                .title("Test Item 2")
                .description("Description 2")
                .imagePath("/images/item2.jpg")
                .price(BigDecimal.valueOf(100))
                .build();
        item2.setCountAndAuditFields(5);

        // Create cart with items
        CartEntity cart = CartEntity.builder()
                .id(UUID.randomUUID())
                .cartState(CartStateType.BUY)
                .build();

        Map<ItemEntity, Integer> positions = new HashMap<>();
        positions.put(item1, 1);
        positions.put(item2, 1);
        cart.setPositions(positions);

        // Create order

        return OrderEntity.builder()
                .id(orderId)
                .cart(cart)
                .totalSum(totalSum)
                .build();
    }
}
