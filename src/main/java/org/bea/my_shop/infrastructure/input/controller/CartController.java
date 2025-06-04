package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.handler.ActionCartHandler;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final ActionCartHandler actionCartHandler;

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
        var prepareCart = cartRepository.findFirstByCartState(CartStateType.PREPARE);
        if (prepareCart.isEmpty()) {
            model.addAttribute("items", null);
            model.addAttribute("total", 0);
            model.addAttribute("empty", true);
        } else {
            var total = prepareCart.get()
                    .getPositions()
                    .entrySet()
                    .stream()
                    .map(it -> it.getKey().getPrice().multiply(BigDecimal.valueOf(it.getValue())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            var items = prepareCart
                    .get()
                    .getPositions()
                    .entrySet()
                    .stream()
                    .peek(it -> {
                        it.getKey().getItemCountEntity().setCount(it.getValue());
                    })
                    .map(it -> ItemMapper.to(it.getKey()))
                    .sorted(Comparator.comparing(Item::getTitle))
                    .toList();
            model.addAttribute("items", items);
            model.addAttribute("total", total);
            model.addAttribute("empty", false);
        }
        return "cart";
    }

    /**
     * купить товары в корзине (выполняет покупку товаров в корзине и очищает ее)
     */
    @PostMapping
    public String buy() {
        return "redirect:/orders/{id}?newOrder=true";
    }

    /**
     изменить количество товара в корзине
     Параматры:
     action - значение из перечисления PLUS|MINUS|DELETE
     (PLUS - добавить один товар, MINUS - удалить один товар, DELETE - удалить товар из корзины)
     */
    @PostMapping(value = "cart/items/{id}")
    public String editItems(
            @PathVariable("id") UUID id,
            @RequestParam(value = "action", required = false) ActionType actionType) {
        actionCartHandler.handleAction(id, actionType);
        return "redirect:/cart/items";
    }

}
