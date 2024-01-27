package fr.openrunning.orbackend.gpx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.openrunning.model.database.gpxfiles.GpxFile;
import fr.openrunning.model.database.gpxfiles.GpxFilesRepository;
import fr.openrunning.model.type.FileStatus;
import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.user.SecurityEncoder;

@Service
public class GpxService {
    private final Logger logger = LoggerFactory.getLogger(GpxService.class);
    private final SecurityEncoder securityEncoder;
    private final File uploadDirectory;
    private final GpxFilesRepository filesRepository;

    @Autowired
    public GpxService(SecurityEncoder securityEncoder, GpxFilesRepository repository) {
        String allDirectoriesLocation = "gpxfiles";
        this.uploadDirectory = new File(allDirectoriesLocation);
        this.securityEncoder = securityEncoder;
        this.filesRepository = repository;
    }

    public void createUserDirectory(String email) throws OpenRunningException {
        try {
            File userDirectory = buildUserUploadedDirectory(email);
            if (userDirectory.exists()) {
                String errorMessage = "error while creating the directory '" + userDirectory.getName()
                        + "': directory already exists";
                logger.error(errorMessage);
                throw new OpenRunningException(errorMessage);
            } else {
                userDirectory.mkdirs();
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
            GpxFile gpxFile = createDatabaseEntry(file);
            File destinationFile = new File(buildUserUploadedDirectory(email), file.getOriginalFilename());
            saveFile(file, destinationFile);
            setCheckingStatus(gpxFile);
        } catch (OpenRunningException ore) {
            throw ore;
        } catch (Exception e) {
            throw new OpenRunningException("can not store the file", e);
        }
    }

    private File buildUserUploadedDirectory(String email) throws OpenRunningException {
        return new File(uploadDirectory, securityEncoder.hashWithSHA256(email));
    }

    private GpxFile createDatabaseEntry(MultipartFile gpxFile) {
        GpxFile file = new GpxFile();
        file.setFilename(gpxFile.getOriginalFilename());
        file.setStatus(FileStatus.UPLOADING);
        filesRepository.save(file);
        return file;
    }

    private void saveFile(MultipartFile file, File destinationFile) throws IOException {
        logger.info("Storing " + destinationFile.getAbsolutePath());
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void setCheckingStatus(GpxFile gpxFile) {
        gpxFile.setStatus(FileStatus.CHECKING);
        filesRepository.save(gpxFile);
    }
}
