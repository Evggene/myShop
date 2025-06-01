package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.handler.SearchItemHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final SearchItemHandler searchItemHandler;

    @GetMapping(path = "main/items")
    public String search(Model model) {
        var res = searchItemHandler.search();
        model.addAttribute("items", res.items());
        model.addAttribute("paging", res.pageInfo());
        return "main";
    }
}
