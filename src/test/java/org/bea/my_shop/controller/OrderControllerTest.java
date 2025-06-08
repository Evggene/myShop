package org.bea.my_shop.controller;

package org.bea.my_shop.infrastructure.input.controller;

import org.bea.my_shop.application.handler.OrderHandler;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.domain.Order;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.input.controller.OrderController;
import org.bea.my_shop.infrastructure.input.dto.ItemInCartRequest;
import org.bea.my_shop.infrastructure.input.dto.OrderRequest;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest extends BaseControllerTest{

    @Test
    void getOrders_shouldReturnOrdersViewWithModelAttributes() throws Exception {
        // Arrange
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();

        OrderRequest order1 = OrderRequest.builder()
                .id(orderId1)
                .items(List.of(
                        createTestItem(orderId1, "Item 1"),
                        createTestItem(orderId1, "Item 2")
                ))
                .build();

        OrderRequest order2 = OrderRequest.builder()
                .id(orderId2)
                .items(List.of(
                        createTestItem(orderId2, "Item 3")
                ))
                .build();

        when(orderHandler.getAll()).thenReturn(List.of(
                createTestOrder(orderId1),
                createTestOrder(orderId2)
        ));

        // Act & Assert
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", List.of(order1, order2)));
    }

    @Test
    void getOrder_shouldReturnOrderViewWithModelAttributes() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order testOrder = createTestOrder(orderId);
        OrderRequest expectedOrder = OrderRequest.builder()
                .id(orderId)
                .items(List.of(
                        createTestItem(orderId, "Test Item")
                ))
                .build();

        when(orderHandler.getById(orderId)).thenReturn(testOrder);

        // Act & Assert
        mockMvc.perform(get("/orders/{id}", orderId)
                        .param("newOrder", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attribute("order", expectedOrder))
                .andExpect(model().attribute("newOrder", "true"));
    }

    @Test
    void getOrder_shouldUseDefaultNewOrderValue() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderHandler.getById(orderId)).thenReturn(createTestOrder(orderId));

        // Act & Assert
        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("newOrder", "false"));
    }

    private OrderEntity createTestOrder(UUID id) {
        CartEntity.builder().id(id).
        return OrderEntity.builder()
                .id(id)
                .cart(List.of(
                        Item.builder()
                                .id(UUID.randomUUID())
                                .title("Test Item")
                                .price(new Money(BigDecimal.TWO))
                                .count(1)
                                .build()
                ))
                .build();
    }

    private ItemInCartRequest createTestItem(UUID orderId, String title) {
        return ItemInCartRequest.builder()
                .id(UUID.randomUUID())
                .title(title)
                .price(BigDecimal.TEN)
                .count(1)
                .build();
    }
}
