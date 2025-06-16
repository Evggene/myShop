package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.service.OrderService;
import org.bea.my_shop.domain.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
//    @GetMapping(path = "/orders/{id}")
//    public String getOrder(
//            @PathVariable("id") UUID id,
//            @RequestParam(value = "newOrder", required = false, defaultValue = "false") String newOrder,
//            Model model) {
//        var order = orderHandler.getById(id);
//        var orderRequest = OrderMapper.entityToRequest(order);
//
//        model.addAttribute("order", orderRequest);
//        model.addAttribute("newOrder", newOrder);
//        return "order";
//    }
}
