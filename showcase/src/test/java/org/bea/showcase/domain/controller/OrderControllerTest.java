package org.bea.showcase.domain.controller;

import org.bea.showcase.domain.configuration.BaseControllerConfiguration;
import org.bea.showcase.infrastructure.input.controller.OrderController;
import org.bea.showcase.infrastructure.input.dto.ItemInCartResponse;
import org.bea.showcase.infrastructure.input.dto.OrderResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@WebFluxTest(OrderController.class)
class OrderControllerTest extends BaseControllerConfiguration {

    private final UUID testOrderId = UUID.randomUUID();
    private final UUID testItemId = UUID.randomUUID();

    private OrderResponse createTestOrderResponse() {
        ItemInCartResponse item = new ItemInCartResponse(
                testItemId,
                "Test Item",
                "Test Description",
                "/test.jpg",
                2,
                2,
                new BigDecimal("99.99")
        );

        return new OrderResponse(
                testOrderId,
                List.of(item),
                new BigDecimal("199.98")
        );
    }

    @Test
    void getOrders_ShouldReturnOrdersViewWithOrders() {
        var arr = List.of(createTestOrderResponse());
        Mockito.when(orderService.getAll())
                .thenReturn(Mono.just(arr));

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    assertTrue(html.contains("orders"));
                    assertTrue(html.contains("Test Item"));
                    assertTrue(html.contains("199.98"));
                });
    }

    @Test
    void getOrder_ShouldReturnOrderView() {
        OrderResponse testOrder = createTestOrderResponse();
        Mockito.when(orderService.getById(testOrderId))
                .thenReturn(Mono.just(testOrder));

        webTestClient.get()
                .uri("/orders/" + testOrderId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    assertTrue(html.contains("order"));
                    assertTrue(html.contains("Test Item"));
                    assertTrue(html.contains("199.98"));
                });
    }

    @Test
    void getOrder_ShouldShowNewOrderNotification() {
        OrderResponse testOrder = createTestOrderResponse();
        Mockito.when(orderService.getById(testOrderId))
                .thenReturn(Mono.just(testOrder));

        webTestClient.get()
                .uri("/orders/" + testOrderId + "?newOrder=true")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @Test
    void getOrder_ShouldReturnNotFoundForInvalidId() {
        Mockito.when(orderService.getById(Mockito.any(UUID.class)))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/orders/" + UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @Test
    void getOrders_ShouldReturnEmptyViewWhenNoOrders() {
        Mockito.when(orderService.getAll())
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }
}
