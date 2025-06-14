package org.bea.my_shop.infrastructure.input.controller;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.configuration.ResourceRootPathConfiguration;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceRootPathConfiguration resourceRootPathConfiguration;
    private final ItemRepository itemRepository;

    @GetMapping("/images/{id}")
    public Mono<ResponseEntity<Resource>> getImage(@PathVariable("id") UUID id) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Image not found")))
                .flatMap(post -> resourceRootPathConfiguration.getRootPathTo(ResourceRootPathConfiguration.IMAGES)
                        .map(rootPath -> Paths.get(rootPath + File.separator + post.getImagePath()))
                        .flatMap(imagePath ->
                                Mono.fromCallable(() -> {
                                            Resource resource = new UrlResource(imagePath.toUri());
                                            if (!resource.exists() || !resource.isReadable()) {
                                                throw new IOException("Image not accessible");
                                            }
                                            return resource;
                                        })
                                        .map(resource -> ResponseEntity.ok()
                                                .contentType(MediaType.IMAGE_JPEG)
                                                .body(resource))
                                        .onErrorMap(IOException.class, e -> new RuntimeException("Failed to load image: " + e.getMessage()))
                        )
                );
    }
}
