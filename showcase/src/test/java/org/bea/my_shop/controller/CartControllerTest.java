package org.bea.my_shop.controller;

import org.bea.my_shop.application.service.cart.*;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.configuration.BaseControllerTest;
import org.bea.my_shop.domain.*;
import org.bea.my_shop.application.dto.ItemAndPriceInfo;
import org.bea.my_shop.infrastructure.input.controller.CartController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@WebFluxTest(CartController.class)
class CartControllerTest extends BaseControllerTest {

    private final UUID testItemId = UUID.randomUUID();
    private final UUID testCartId = UUID.randomUUID();
    private final Item testItem = Item.builder()
            .id(testItemId)
            .title("Test Item")
            .price(new Money(new BigDecimal("99.99")))
            .count(2)
            .build();

    @Test
    void getItemsInCart_ShouldReturnCartViewWithItems() {
        var cartInfo = new ItemAndPriceInfo(
                testCartId,
                List.of(testItem),
                new BigDecimal("199.98")
        );
        Mockito.when(getCartService.getCartStatePrepare())
                .thenReturn(Mono.just(cartInfo));

        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    Assertions.assertTrue(html.contains("Test Item"));
                    Assertions.assertTrue(html.contains("199.98"));
                    Assertions.assertTrue(html.contains("cart"));
                });
    }

    @Test
    void buy_ShouldRedirectToOrderPage() {
        UUID orderId = UUID.randomUUID();
        Mockito.when(orderCartService.orderCart(testCartId))
                .thenReturn(Mono.just(orderId));

        webTestClient.post()
                .uri("/buy/" + testCartId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/orders/" + orderId + "?newOrder=true");
    }

    @Test
    void actionItems_ShouldHandlePlusAction() {
        Mockito.when(actionCartService.handleAction(testItemId, ActionType.PLUS))
                .thenReturn(Mono.just(ActionType.PLUS));

        webTestClient.post()
                .uri("/cart/items/" + testItemId)
                .contentType(APPLICATION_FORM_URLENCODED)
                .bodyValue("action=PLUS")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/cart/items");
    }

    @Test
    void actionItems_ShouldHandleMinusAction() {
        Mockito.when(actionCartService.handleAction(testItemId, ActionType.MINUS))
                .thenReturn(Mono.just(ActionType.MINUS));

        webTestClient.post()
                .uri("/cart/items/" + testItemId)
                .contentType(APPLICATION_FORM_URLENCODED)
                .bodyValue("action=MINUS")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/cart/items");
    }

    @Test
    void actionItems_ShouldHandleDeleteAction() {
        Mockito.when(actionCartService.handleAction(testItemId, ActionType.DELETE))
                .thenReturn(Mono.just(ActionType.DELETE));

        webTestClient.post()
                .uri("/cart/items/" + testItemId)
                .contentType(APPLICATION_FORM_URLENCODED)
                .bodyValue("action=DELETE")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/cart/items");
    }
}
