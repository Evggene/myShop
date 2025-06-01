package org.bea.my_shop.application.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final ResourceRootPathConfiguration rootPath;

    public void copyImageToResources(MultipartFile image) {
        if (image.getOriginalFilename().isBlank()) {
            return;
        }
        try {
            var path = Path.of(
                    rootPath.getRootPathTo(ResourceRootPathConfiguration.IMAGES)
                            + File.separator
                            + image.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
