package org.bea.showcase.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     список заказов
     Возвращает:
         используется модель для заполнения шаблона:
         "orders" - List<Order> - список заказов
         "id" - идентификатор заказа
         "items" - List<Item> - список товаров в заказе (id, title, decription, imgPath, count, price)
     */
    @GetMapping(path = "/orders")
    public Mono<Rendering> getOrders() {
        return orderService.getAll()
                .map(orders -> Rendering.view("orders")
                        .modelAttribute("orders", orders)
                        .build());
    }

    /**
     карточка заказа
     Параматры:
         newOrder - true, если переход со страницы оформления заказа (по умолчанию, false)
         Возвращает:
             используется модель для заполнения шаблона:
             "order" - заказ Order
             "id" - идентификатор заказа
             "items" - List<Item> - список товаров в заказе (id, title, decription, imgPath, count, price)
             "newOrder" - true, если переход со страницы оформления заказа (по умолчанию, false)
     */
    @GetMapping(path = "/orders/{id}")
    public Mono<Rendering> getOrder(
            @PathVariable("id") UUID id,
            @RequestParam(value = "newOrder", required = false, defaultValue = "false") String newOrder) {
        return orderService.getById(id)
                .map(order -> Rendering.view("order")
                .modelAttribute("order", order)
                .modelAttribute("newOrder", newOrder)
                .build());
    }
}
