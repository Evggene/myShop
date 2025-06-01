package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.handler.SearchItemHandler;
import org.bea.my_shop.infrastructure.input.type.SearchType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final SearchItemHandler searchItemHandler;

    @GetMapping(path = "main/items")
    public String search(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false) SearchType searchType,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            Model model) {
        var res = searchItemHandler.search(search, searchType, pageSize, pageNumber);
        model.addAttribute("items", res.items());
        model.addAttribute("paging", res.pageInfo());
        return "main";
    }
}
