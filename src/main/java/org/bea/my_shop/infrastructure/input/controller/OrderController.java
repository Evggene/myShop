package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class OrderController {

    /**
     список заказов
     Возвращает:
         используется модель для заполнения шаблона:
         "orders" - List<Order> - список заказов
         "id" - идентификатор заказа
         "items" - List<Item> - список товаров в заказе (id, title, decription, imgPath, count, price)
     */
    @GetMapping(path = "/orders")
    public String getOrders() {
        return "orders";
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
    public String getOrder(
            @PathVariable("id") UUID id,
            @RequestParam(value = "newOrder", required = false, defaultValue = "false") String newOrder) {
        return "order";
    }
}
