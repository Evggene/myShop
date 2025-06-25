package org.bea.showcase.controller;

import org.bea.showcase.application.dto.ItemAndPageInfo;
import org.bea.showcase.application.type.ActionType;
import org.bea.showcase.application.type.SearchType;
import org.bea.showcase.configuration.BaseControllerTest;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.bea.showcase.infrastructure.input.controller.ItemController;
import org.bea.showcase.infrastructure.input.dto.PageOfItemsResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@WebFluxTest(ItemController.class)
class ItemControllerTest extends BaseControllerTest {

    private final UUID testItemId = UUID.randomUUID();
    private final Item testItem = Item.builder()
            .id(testItemId)
            .title("Test Item")
            .description("Test Description")
            .imagePath("/test.jpg")
            .price(new Money(new BigDecimal("99.99")))
            .count(5)
            .build();

    @Test
    void search_ShouldReturnMainViewWithItems() {
        var pageInfo = new PageOfItemsResponse(1L, 1, 10, "", SearchType.NO.name());
        var itemAndPageInfo = new ItemAndPageInfo(List.of(testItem), pageInfo);

        Mockito.when(searchItemService.search(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(itemAndPageInfo));

        webTestClient.get()
                .uri("/main/items?search=test&sort=ALPHA&pageSize=10&pageNumber=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class) // Ожидаем строку (HTML)
                .value(html -> {
                    assertTrue(html.contains("Test Item")); // Проверяем наличие данных в HTML
                    assertTrue(html.contains("99.99"));
                });
    }


    @Test
    void mainActionItems_ShouldRedirectAfterAction() {
        Mockito.when(actionCartService.handleAction(any(), any()))
                .thenReturn(Mono.just(ActionType.PLUS));

        webTestClient.post()
                .uri("/main/items/" + testItemId)
                .contentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("action=PLUS")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/main/items");
    }

    @Test
    void editItems_ShouldRedirectAfterAction() {
        Mockito.when(actionCartService.handleAction(any(), any()))
                .thenReturn(Mono.just(ActionType.DELETE));

        webTestClient.post()
                .uri("/items/" + testItemId)
                .contentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("action=DELETE")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/items/" + testItemId);
    }

    @Test
    void findById_ShouldReturnItemView() {
        Mockito.when(searchItemService.findById(testItemId))
                .thenReturn(Mono.just(testItem));

        webTestClient.get()
                .uri("/items/" + testItemId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    assertTrue(html.contains("Test Item"));
                    assertTrue(html.contains("Test Description"));
                });
    }

    @Test
    void search_ShouldUseDefaultParametersWhenNotProvided() {
        var pageInfo = new PageOfItemsResponse(1L, 1, 10, "", SearchType.NO.name());
        var itemAndPageInfo = new ItemAndPageInfo(List.of(testItem), pageInfo);

        Mockito.when(searchItemService.search("", SearchType.NO, 10, 0))
                .thenReturn(Mono.just(itemAndPageInfo));

        webTestClient.get()
                .uri("/main/items")
                .exchange()
                .expectStatus().isOk();
    }
}
