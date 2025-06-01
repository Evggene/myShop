package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.AddGoodsHandler;
import org.bea.my_shop.application.configuration.FileStorageService;
import org.bea.my_shop.infrastructure.input.dto.AddGoodsRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AddGoodsController {

    private final FileStorageService fileStorageService;
    private final AddGoodsHandler addGoodsHandler;

    @GetMapping(path = "/goods/add")
    public String addGoodsView() {
        return "add-goods";
    }

    @PostMapping(value = "/goods/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addGoods(@ModelAttribute AddGoodsRequest addEditPostRequest) {
        fileStorageService.copyImageToResources(addEditPostRequest.image());
        addGoodsHandler.add(addEditPostRequest);
        return "redirect:/goods/add";
    }
}
