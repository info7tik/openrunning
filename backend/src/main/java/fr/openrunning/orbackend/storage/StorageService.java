package fr.openrunning.orbackend.storage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.openrunning.orbackend.common.exception.OpenRunningException;

@Service
public class StorageService {
    private final Logger logger = LoggerFactory.getLogger(StorageService.class);
    private final Path uploadDirectory;

    @Autowired
    public StorageService(StorageConfiguration storageConfiguration) {
        this.uploadDirectory = Paths.get(storageConfiguration.getLocation());
    }

    public void store(MultipartFile file) throws OpenRunningException {
        try {
            if (file.isEmpty()) {
                throw new OpenRunningException("can not store empty files");
            }
            Path destinationFile = this.uploadDirectory.resolve(
                    Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
            logger.info("Storing " + destinationFile.toAbsolutePath());
            if (!destinationFile.getParent().equals(this.uploadDirectory.toAbsolutePath())) {
                throw new OpenRunningException("can not store files outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (OpenRunningException ore) {
            throw ore;
        } catch (Exception e) {
            throw new OpenRunningException("can not store the file", e);
        }
    }
}
