package org.bea.showcase.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.service.OrderService;
import org.bea.showcase.application.service.cart.ActionCartService;
import org.bea.showcase.application.service.cart.GetCartService;
import org.bea.showcase.infrastructure.input.dto.ActionTypeRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final ActionCartService actionCartService;
    private final GetCartService getCartService;
    private final OrderService orderService;

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
        return getCartService.getCartAndBalance()
                .map(it -> Rendering.view("cart")
                        .modelAttribute("empty", it.getItems().isEmpty() || it.getBalance().compareTo(BigDecimal.ZERO) < 0)
                        .modelAttribute("items", it.getItems())
                        .modelAttribute("total", it.getTotalPrice())
                        .modelAttribute("cartId", it.getCartId())
                        .build());
    }

    /**
     * купить товары в корзине (выполняет покупку товаров в корзине и очищает ее)
     * рест-запрос в сервис платежей
     */

    @PostMapping(value = "buy/{id}")
    public Mono<Rendering> buy(@PathVariable("id") UUID id) {
        return orderService.tryPay(id)
                .map(result -> Rendering.redirectTo("/orders/" + result.getId() + "?newOrder=true").build());
    }


    /**
     * изменить количество товара в корзине
     * Параматры:
     * action - значение из перечисления PLUS|MINUS|DELETE
     * (PLUS - добавить один товар, MINUS - удалить один товар, DELETE - удалить товар из корзины)
     */
    @PostMapping(value = "cart/items/{id}")
    public Mono<Rendering> actionItems(
            @PathVariable("id") UUID id,
            @ModelAttribute ActionTypeRequest actionType) {
        return actionCartService.handleAction(id, actionType.getAction())
                .thenReturn(Rendering.redirectTo("/cart/items").build());
    }

}
