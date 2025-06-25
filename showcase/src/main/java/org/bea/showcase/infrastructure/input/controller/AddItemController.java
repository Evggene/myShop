package org.bea.showcase.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.FileStorageService;
import org.bea.showcase.application.service.item.AddItemService;
import org.bea.showcase.infrastructure.input.dto.AddItemRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class AddItemController {

    private final FileStorageService fileStorageService;
    private final AddItemService addItemService;

    @GetMapping(path = "/item/add")
    public Mono<Rendering> addItemView() {
        return Mono.just(Rendering.view("add-item").build());
    }

    @PostMapping(value = "/item/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Rendering> addItem(@ModelAttribute Mono<AddItemRequest> addEditPostRequest) {
        return addEditPostRequest
                .flatMap(it -> fileStorageService.copyImageToResources(it.image()))
                .then(addItemService.add(addEditPostRequest))
                .thenReturn(Rendering.redirectTo("/item/add").build());
    }
}
