package org.bea.my_shop.controller;

import org.bea.my_shop.application.dto.ItemAndPriceInfo;
import org.bea.my_shop.application.handler.cart.ActionCartHandler;
import org.bea.my_shop.application.handler.cart.GetCartHandler;
import org.bea.my_shop.application.handler.cart.OrderCartHandler;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.input.controller.CartController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = CartController.class)
class CartControllerTest extends BaseControllerTest {

    @Test
    void getItemsInCart_shouldReturnCartViewWithModelAttributes() throws Exception {
        // Arrange
        UUID cartId = UUID.randomUUID();
        List<Item> items = List.of(
                Item.builder()
                        .id(UUID.randomUUID())
                        .title("Item 1")
                        .description("Desc 1")
                        .imagePath("img1.jpg")
                        .price(new Money(BigDecimal.valueOf(100)))
                        .count(2)
                        .build(),
                Item.builder()
                        .id(UUID.randomUUID())
                        .title("Item 2")
                        .description("Desc 2")
                        .imagePath("img2.jpg")
                        .price(new Money(BigDecimal.valueOf(200)))
                        .count(1)
                        .build()
        );
        BigDecimal total = new BigDecimal("400.00");

        when(getCartHandler.getCartStatePrepare())
                .thenReturn(new ItemAndPriceInfo(cartId, items, total));

        // Act & Assert
        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("items", items))
                .andExpect(model().attribute("total", total))
                .andExpect(model().attribute("empty", false))
                .andExpect(model().attribute("cartId", cartId));

        verify(getCartHandler).getCartStatePrepare();
    }

    @Test
    void buy_shouldProcessOrderAndRedirect() throws Exception {
        // Arrange
        UUID cartId = UUID.randomUUID();
        UUID newOrderId = UUID.randomUUID();

        when(orderCartHandler.orderCart(cartId))
                .thenReturn(newOrderId);

        // Act & Assert
        mockMvc.perform(post("/buy/{id}", cartId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/" + newOrderId + "?newOrder=true"));

        verify(orderCartHandler).orderCart(cartId);
    }

    @Test
    void actionItems_shouldHandlePlusAction() throws Exception {
        // Arrange
        UUID itemId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(post("/cart/items/{id}", itemId)
                        .param("action", "PLUS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));

        verify(actionCartHandler).handleAction(itemId, ActionType.PLUS);
    }

    @Test
    void actionItems_shouldHandleMinusAction() throws Exception {
        // Arrange
        UUID itemId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(post("/cart/items/{id}", itemId)
                        .param("action", "MINUS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));

        verify(actionCartHandler).handleAction(itemId, ActionType.MINUS);
    }

    @Test
    void actionItems_shouldHandleDeleteAction() throws Exception {
        // Arrange
        UUID itemId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(post("/cart/items/{id}", itemId)
                        .param("action", "DELETE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));

        verify(actionCartHandler).handleAction(itemId, ActionType.DELETE);
    }

    @Test
    void actionItems_shouldUseDefaultActionWhenNotSpecified() throws Exception {
        // Arrange
        UUID itemId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(post("/cart/items/{id}", itemId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));

        verify(actionCartHandler).handleAction(itemId, null);
    }
}
