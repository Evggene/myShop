package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.service.cart.ActionCartService;
import org.bea.my_shop.application.service.cart.GetCartService;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.infrastructure.input.dto.ActionTypeRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CartController {

     private final ActionCartService actionCartService;
    private final GetCartService getCartService;
    // private final OrderCartHandler orderCartHandler;

    /**
     * список товаров в корзине
     * Возвращает:
     * используется модель для заполнения шаблона:
     * "items" - List<Item> - список товаров в корзине (id, title, decription, imgPath, count, price)
     * "total" - суммарная стоимость заказа
     * "empty" - true, если в корзину не добавлен ни один товар
     */
    @GetMapping(path = "cart/items")
    public Mono<Rendering> getItemsInCart() {
        return getCartService.getCartStatePrepare()
                .map(it -> Rendering.view("cart")
                        .modelAttribute("items", it.items())
                        .modelAttribute("total", it.totalPrice())
                        .modelAttribute("empty", it.items().isEmpty())
                        .modelAttribute("cartId", it.cartId())
                        .build());
    }

    /**
     * купить товары в корзине (выполняет покупку товаров в корзине и очищает ее)
     */
//    @PostMapping(value = "buy/{id}")
//    public String buy(
//            @PathVariable("id") UUID id) {
//        var newOrderId = orderCartHandler.orderCart(id);
//        return "redirect:/orders/" + newOrderId + "?newOrder=true";
//    }

    /**
     изменить количество товара в корзине
     Параматры:
     action - значение из перечисления PLUS|MINUS|DELETE
     (PLUS - добавить один товар, MINUS - удалить один товар, DELETE - удалить товар из корзины)
     */
    @PostMapping(value = "cart/items/{id}")
    public Mono<Rendering> ationItems(
            @PathVariable("id") UUID id,
            @ModelAttribute ActionTypeRequest actionType) {
        return actionCartService.handleAction(id, actionType.getAction())
                .thenReturn(Rendering.redirectTo("/cart/items").build());
    }

}
