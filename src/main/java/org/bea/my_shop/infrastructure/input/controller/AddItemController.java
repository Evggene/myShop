package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.AddItemHandler;
import org.bea.my_shop.application.configuration.FileStorageService;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AddItemController {

    private final FileStorageService fileStorageService;
    private final AddItemHandler addItemHandler;

    @GetMapping(path = "/item/add")
    public String addItemView() {
        return "add-item";
    }

    @PostMapping(value = "/item/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addItem(@ModelAttribute AddItemRequest addEditPostRequest) {
        fileStorageService.copyImageToResources(addEditPostRequest.image());
        addItemHandler.add(addEditPostRequest);
        return "redirect:/item/add";
    }
}
