package org.bea.my_shop.application;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.configuration.ResourceRootPathConfiguration;
import org.bea.my_shop.domain.Money;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final ResourceRootPathConfiguration rootPath;

    public Mono<String> copyImageToResources(FilePart imagePart) {
        if (imagePart.filename().isBlank()) {
            return Mono.empty();
        }
        return getPath(imagePart)
                .flatMap(path ->
                        createDirectory(path)
                                .then(DataBufferUtils.write(
                                        imagePart.content(),
                                        path,
                                        StandardOpenOption.CREATE,
                                        StandardOpenOption.TRUNCATE_EXISTING
                                ))
                                .thenReturn(Mono.just(path.getFileName()).toString()));
    }

    private Mono<Path> getPath(FilePart imagePart) {
        return Mono.fromCallable(() ->
                Path.of(rootPath.getRootPathTo(ResourceRootPathConfiguration.IMAGES) + File.separator + imagePart.filename())
        ).onErrorResume(IOException.class, e -> Mono.error(new RuntimeException("Failed to create path", e)));
    }

    private Mono<Path> createDirectory(Path path) {
        return Mono.fromCallable(() -> Files.createDirectories(path.getParent()))
                .onErrorResume(IOException.class, e -> Mono.error(new RuntimeException("Failed to create directories", e)));
    }
}
