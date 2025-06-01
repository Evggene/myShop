package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CartController {

    /**
     список товаров в корзине
     Возвращает:
         используется модель для заполнения шаблона:
         "items" - List<Item> - список товаров в корзине (id, title, decription, imgPath, count, price)
         "total" - суммарная стоимость заказа
         "empty" - true, если в корзину не добавлен ни один товар
     */
    @GetMapping(path = "cart/items")
    public String getItemsInCart() {
        return "cart";
    }

    /**
     купить товары в корзине (выполняет покупку товаров в корзине и очищает ее)
     */
    @PostMapping
    public String buy() {
        return "redirect:/orders/{id}?newOrder=true";
    }

}
