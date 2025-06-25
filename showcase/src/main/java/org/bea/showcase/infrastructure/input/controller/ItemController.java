package org.bea.showcase.infrastructure.input.controller;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.service.cart.ActionCartService;
import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.application.type.SearchType;
import org.bea.showcase.infrastructure.input.dto.ActionTypeRequest;
import org.bea.showcase.application.port.output.CartRepository;
import org.springframework.stereotype.Controller;
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
public class ItemController {

    private final SearchItemService searchItemService;
    private final ActionCartService actionCartService;
    private final CartRepository cartRepository;

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
    @PostMapping(value = "/main/items/{id}")
    public Mono<Rendering> mainActionItems(
            @PathVariable("id") UUID id,
            @ModelAttribute ActionTypeRequest actionType) {
        return actionCartService.handleAction(id, actionType.getAction())
                .map(it -> Rendering.redirectTo("/main/items").build());
    }


    @PostMapping(value = "/items/{id}")
    public Mono<Rendering> editItems(
            @PathVariable("id") UUID id,
            @ModelAttribute ActionTypeRequest actionType) {
        return actionCartService.handleAction(id, actionType.getAction())
                .map(it -> Rendering.redirectTo("/items/" + id).build());
    }

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
