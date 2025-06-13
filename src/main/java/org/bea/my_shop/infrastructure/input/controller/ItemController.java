package org.bea.my_shop.infrastructure.input.controller;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.handler.cart.ActionCartHandler;
import org.bea.my_shop.application.handler.item.SearchItemHandler;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.application.type.SearchType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final SearchItemHandler searchItemHandler;
    private final ActionCartHandler actionCartHandler;

//    @GetMapping(path = "main/items")
//    public String search(
//            @RequestParam(value = "search", required = false) String search,
//            @RequestParam(value = "sort", required = false) SearchType searchType,
//            @RequestParam(value = "pageSize", required = false) Integer pageSize,
//            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
//            Model model) {
//        var res = searchItemHandler.search(search, searchType, pageSize, pageNumber);
//        var splittedForUI = Lists.partition(res.items(), 3);
//        model.addAttribute("items", splittedForUI);
//        model.addAttribute("paging", res.pageInfo());
//        return "main";
//    }

    /**
     * Измменить количество товара в корзине
     */
//    @PostMapping(value = "/main/items/{id}")
//    public String mainActionItems(
//            @PathVariable("id") UUID id,
//            @RequestParam(value = "action", required = false) ActionType actionType) {
//        actionCartHandler.handleAction(id, actionType);
//        return "redirect:/main/items";
//    }


//    @PostMapping(value = "/items/{id}")
//    public String editItems(
//            @PathVariable("id") UUID id,
//            @RequestParam(value = "action", required = false) ActionType actionType) {
//        actionCartHandler.handleAction(id, actionType);
//        return "redirect:/items/" + id;
//    }

    /**
     карточка товара
     Возвращает:
         используется модель для заполнения шаблона:
         "item" - товаров (id, title, decription, imgPath, count, price)
     */
//    @GetMapping(path = "items/{id}")
//    public String search(@PathVariable("id") UUID id, Model model) {
//        var res = searchItemHandler.findById(id);
//        model.addAttribute("item", res);
//        return "item";
//    }

}
