package fr.openrunning.orbackend.gpx;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import fr.openrunning.model.database.gpxfiles.GpxFile;
import fr.openrunning.model.type.FileStatus;
import fr.openrunning.orbackend.user.SecurityEncoder;

public class GpxServiceTest {
    @Test
    public void store() {
        try {
            SecurityEncoder encoder = new SecurityEncoder("signingkey", "passwordsalt");
            String email = "testing@openrunning";
            String userDirectoryName = encoder.hashWithSHA256(email);
            Path userDirectory = Paths.get("gpxfiles/" + userDirectoryName);
            Assertions.assertTrue(userDirectory.toFile().mkdirs());
            MockGpxFilesRepository repository = new MockGpxFilesRepository();
            GpxService service = new GpxService(encoder, repository);
            String fileContent = "testing content";
            String filename = "gpxfile.txt";
            MultipartFile file = creaMultipartFile(filename, fileContent);
            service.store(file, 3, email);
            Path uploadedFile = userDirectory.resolve(file.getOriginalFilename());
            Assertions.assertTrue(uploadedFile.toFile().exists());
            Assertions.assertEquals(fileContent.getBytes().length, uploadedFile.toFile().length());
            Assertions.assertTrue(FileSystemUtils.deleteRecursively(userDirectory));
            Assertions.assertEquals(1, repository.getFiles().size());
            GpxFile storedFile = repository.getFiles().iterator().next();
            Assertions.assertEquals(FileStatus.CHECKING, storedFile.getStatus());
            Assertions.assertEquals(filename, storedFile.getFilename());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    private MockMultipartFile creaMultipartFile(String name, String content) {
        String staticName = "multipartName.txt";
        String contentType = "text/plain";
        return new MockMultipartFile(staticName, name, contentType, content.getBytes());
    }
}
