package org.bea.my_shop.application.configuration;

import lombok.RequiredArgsConstructor;
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

    public void copyImageToResourcesReactive(FilePart imagePart) throws IOException {
        if (imagePart.filename().isBlank()) {
            return;
        }
        Path path = Path.of(
                rootPath.getRootPathTo(ResourceRootPathConfiguration.IMAGES)
                        + File.separator
                        + imagePart.filename()
        );
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            Mono.error(e);
            return;
        }
        DataBufferUtils.write(
                        imagePart.content(),
                        path,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                )
                .then();
    }
}
