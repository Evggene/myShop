package org.bea.my_shop.controller;

import org.bea.my_shop.application.dto.ItemAndPageInfo;
import org.bea.my_shop.application.handler.cart.ActionCartHandler;
import org.bea.my_shop.application.handler.item.SearchItemHandler;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.domain.Item;

import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.input.controller.ItemController;
import org.bea.my_shop.infrastructure.input.dto.PageOfItemsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest extends BaseControllerTest{

    @Test
    void search_shouldReturnMainViewWithModelAttributes() throws Exception {
        // Arrange
        Item item1 = Item.builder()
                .id(UUID.randomUUID())
                .title("Item 1")
                .price(new Money(BigDecimal.valueOf(100)))
                .build();

        Item item2 = Item.builder()
                .id(UUID.randomUUID())
                .title("Item 2")
                .price(new Money(BigDecimal.valueOf(200)))
                .build();

        PageOfItemsResponse pageInfo = new PageOfItemsResponse(2L, 1, 10, "search", "ALPHA");
        when(searchItemHandler.search(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(new ItemAndPageInfo(List.of(item1, item2), pageInfo));

        // Act & Assert
        mockMvc.perform(get("/main/items")
                        .param("search", "test")
                        .param("sort", "ALPHA")
                        .param("pageSize", "10")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("paging"));
    }

    @Test
    void search_shouldUseDefaultParameters() throws Exception {
        // Arrange
        when(searchItemHandler.search(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(new ItemAndPageInfo(List.of(),
                        new PageOfItemsResponse(0L, 1, 10, "", "NO")));

        // Act & Assert
        mockMvc.perform(get("/main/items"))
                .andExpect(status().isOk());
    }

    @Test
    void mainActionItems_shouldHandleActionAndRedirect() throws Exception {
        // Arrange
        UUID itemId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(post("/main/items/{id}", itemId)
                        .param("action", "PLUS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items"));

        verify(actionCartHandler).handleAction(itemId, ActionType.PLUS);
    }

    @Test
    void editItems_shouldHandleActionAndRedirect() throws Exception {
        // Arrange
        UUID itemId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(post("/items/{id}", itemId)
                        .param("action", "MINUS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/" + itemId));

        verify(actionCartHandler).handleAction(itemId, ActionType.MINUS);
    }

    @Test
    void getItemDetails_shouldReturnItemView() throws Exception {
        // Arrange
        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
                .id(itemId)
                .title("Test Item")
                .price(new Money(BigDecimal.valueOf(100)))
                .build();

        when(searchItemHandler.findById(itemId)).thenReturn(item);

        // Act & Assert
        mockMvc.perform(get("/items/{id}", itemId))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attribute("item", item));
    }
}
