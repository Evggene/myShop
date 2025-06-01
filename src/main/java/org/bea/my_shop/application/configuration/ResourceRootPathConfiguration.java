package org.bea.my_shop.application.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class ResourceRootPathConfiguration {

    public ResourceRootPathConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private final ResourceLoader resourceLoader;

    public static final String IMAGES = "resources/static";

    public Path getRootPathTo(String folderName) throws IOException {
        var resource = resourceLoader.getResource(folderName);
        return Path.of(resource.getFile().getAbsolutePath());
    }
}
