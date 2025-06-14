package org.bea.my_shop.infrastructure.input.controller;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.service.item.SearchItemService;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.application.type.SearchType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final SearchItemService searchItemService;
  //  private final ActionCartService actionCartService;

    @GetMapping(path = "main/items")
    public Mono<Rendering> search(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false) SearchType searchType,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {

        return searchItemService.search(search, searchType, pageSize, pageNumber)
                .map(it -> {
                    var splittedForUI = Lists.partition(it.items(), 3);
                    return Rendering.view("main")
                            .modelAttribute("items", splittedForUI)
                            .modelAttribute("paging", it.pageInfo())
                            .build();
                });
    }

    /**
     * Измменить количество товара в корзине
     */
//    @PostMapping(value = "/main/items/{id}")
//    public Mono<Rendering> mainActionItems(
//            @PathVariable("id") UUID id,
//            @RequestParam(value = "action", required = false) ActionType actionType) {
//        actionCartService.handleAction(id, actionType);
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
    @GetMapping(path = "items/{id}")
    public Mono<Rendering> findById(@PathVariable("id") UUID id) {
        return searchItemService.findById(id)
                .map(it -> Rendering.view("item")
                        .modelAttribute("item", it)
                        .build());
    }
}
