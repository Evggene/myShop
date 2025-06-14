package org.bea.my_shop.application.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ResourceRootPathConfiguration {

    public ResourceRootPathConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private final ResourceLoader resourceLoader;

    public static final String IMAGES = "resources/static";

    public Mono<Path> getRootPathTo(String folderName) {
        return Mono.fromCallable(() -> Paths.get(folderName))
                .flatMap(targetPath ->
                        Mono.fromCallable(() -> Files.exists(targetPath))
                                .flatMap(exists -> {
                                    if (!exists) {
                                        return Mono.fromCallable(() -> Files.createDirectories(targetPath))
                                                .thenReturn(targetPath);
                                    }
                                    return Mono.just(targetPath);
                                })
                                .map(Path::toAbsolutePath)
                                .onErrorResume(e -> Mono.error(new RuntimeException(
                                        "Failed to create directory: " + folderName, e))));
    }
}
