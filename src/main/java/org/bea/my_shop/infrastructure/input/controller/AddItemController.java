package org.bea.my_shop.infrastructure.input.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.FileStorageService;
import org.bea.my_shop.application.handler.item.AddItemHandler;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AddItemController {

    private final FileStorageService fileStorageService;
    private final AddItemHandler addItemHandler;

    @GetMapping(path = "/item/add")
    public Mono<Rendering> addItemView() {
        return Mono.just(Rendering.redirectTo("add-item").build());
    }

    @PostMapping(value = "/item/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Rendering> addItem(@ModelAttribute Mono<AddItemRequest> addEditPostRequest) {
        return addEditPostRequest
                .flatMap(it -> fileStorageService.copyImageToResources(it.image()))
                .then(addItemHandler.add(addEditPostRequest))
                .thenReturn(Rendering.redirectTo("/item/add").build());
    }
}
