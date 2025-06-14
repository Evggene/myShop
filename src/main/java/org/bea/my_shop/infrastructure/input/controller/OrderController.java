package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.service.OrderService;
import org.springframework.stereotype.Controller;

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
 //   @GetMapping(path = "/orders")
//    public String getOrders(Model model) {
//        var ordersEntity = orderHandler.getAll();
//        var orders = ordersEntity.stream()
//                .map(OrderMapper::entityToRequest)
//                .toList();
//        model.addAttribute("orders", orders);
//        return "orders";
//    }

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
