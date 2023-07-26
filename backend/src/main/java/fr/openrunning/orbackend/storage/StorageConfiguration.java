package fr.openrunning.orbackend.storage;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {
    private final String location = "upload-dir";

    public String getLocation() {
        return location;
    }
}
