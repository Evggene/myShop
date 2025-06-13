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

//    @GetMapping("/images/{id}")
//    public ResponseEntity<Resource> getImage(@PathVariable("id") UUID id) throws IOException {
//        var post = itemRepository.findById(id);
//        if (post.isEmpty()) {
//            throw new RuntimeException("image not found");
//        }
//        var rootPath = resourceRootPathConfiguration.getRootPathTo(ResourceRootPathConfiguration.IMAGES);
//        Path imagePath = Paths.get(rootPath + File.separator + post.get().getImagePath());
//        Resource resource = new UrlResource(imagePath.toUri());
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(resource);
//    }
}
