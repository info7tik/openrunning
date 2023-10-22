package fr.openrunning.orbackend.gpx;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.user.SecurityEncoder;

@Service
public class GpxService {
    private final Logger logger = LoggerFactory.getLogger(GpxService.class);
    private final SecurityEncoder securityEncoder;
    private final File uploadDirectory;

    @Autowired
    public GpxService(SecurityEncoder securityEncoder) {
        String allDirectoriesLocation = "upload-dir";
        this.uploadDirectory = new File(allDirectoriesLocation);
        this.securityEncoder = securityEncoder;
    }

    public void createUserDirectory(String email) throws OpenRunningException {
        try {
            String userDirectoryName = securityEncoder.hashWithSHA256(email);
            File userDirectory = new File(uploadDirectory, userDirectoryName);
            if (userDirectory.exists()) {
                String errorMessage = "error while creating the directory '" + userDirectoryName
                        + "': directory already exists";
                logger.error(errorMessage);
                throw new OpenRunningException(errorMessage);
            } else {
                userDirectory.mkdirs();
                getUserUploadedDirectory(email).mkdir();
                getUserProcessedDirectory(email).mkdir();
            }
        } catch (Exception e) {
            throw new OpenRunningException("can not create the directory '" + email + "'", e);
        }
    }

    public void store(MultipartFile file, String email) throws OpenRunningException {
        try {
            if (file.isEmpty()) {
                throw new OpenRunningException("can not store empty files");
            }
            File destinationFile = new File(getUserUploadedDirectory(email), file.getOriginalFilename());
            logger.info("Storing " + destinationFile.getAbsolutePath());
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (OpenRunningException ore) {
            throw ore;
        } catch (Exception e) {
            throw new OpenRunningException("can not store the file", e);
        }
    }

    private File getUserUploadedDirectory(String email) throws OpenRunningException {
        File userDirectory = new File(uploadDirectory, securityEncoder.hashWithSHA256(email));
        return new File(userDirectory, "uploaded");
    }

    private File getUserProcessedDirectory(String email) throws OpenRunningException {
        File userDirectory = new File(uploadDirectory, securityEncoder.hashWithSHA256(email));
        return new File(userDirectory, "processed");
    }
}
