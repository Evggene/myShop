package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.handler.cart.ActionCartHandler;
import org.bea.my_shop.application.handler.cart.GetCartHandler;
import org.bea.my_shop.application.handler.cart.OrderCartHandler;
import org.bea.my_shop.application.type.ActionType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final ActionCartHandler actionCartHandler;
    private final GetCartHandler getCartHandler;
    private final OrderCartHandler orderCartHandler;

    /**
     * список товаров в корзине
     * Возвращает:
     * используется модель для заполнения шаблона:
     * "items" - List<Item> - список товаров в корзине (id, title, decription, imgPath, count, price)
     * "total" - суммарная стоимость заказа
     * "empty" - true, если в корзину не добавлен ни один товар
     */
    @GetMapping(path = "cart/items")
    public String getItemsInCart(Model model) {
        var res = getCartHandler.getCartStatePrepare();
            model.addAttribute("items", res.items());
            model.addAttribute("total", res.totalPrice());
            model.addAttribute("empty", res.items().isEmpty());
            model.addAttribute("cartId", res.cartId());
        return "cart";
    }

    /**
     * купить товары в корзине (выполняет покупку товаров в корзине и очищает ее)
     */
    @PostMapping(value = "buy/{id}")
    public String buy(
            @PathVariable("id") UUID id) {
        var newOrderId = orderCartHandler.orderCart(id);
        return "redirect:/orders/" + newOrderId + "?newOrder=true";
    }

    /**
     изменить количество товара в корзине
     Параматры:
     action - значение из перечисления PLUS|MINUS|DELETE
     (PLUS - добавить один товар, MINUS - удалить один товар, DELETE - удалить товар из корзины)
     */
    @PostMapping(value = "cart/items/{id}")
    public String ationaItems(
            @PathVariable("id") UUID id,
            @RequestParam(value = "action", required = false) ActionType actionType) {
        actionCartHandler.handleAction(id, actionType);
        return "redirect:/cart/items";
    }

}
