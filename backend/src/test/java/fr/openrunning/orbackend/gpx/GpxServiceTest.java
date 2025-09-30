package fr.openrunning.orbackend.gpx;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import fr.openrunning.model.Filesystem;
import fr.openrunning.model.database.gpxfiles.GpxFile;
import fr.openrunning.model.type.FileStatus;

public class GpxServiceTest {
    @Test
    public void store() {
        try {
            Filesystem filesystem = new Filesystem();
            String email = "testing@openrunning";
            File userDirectory = filesystem.userUploadDirectory(email);
            Assertions.assertTrue(userDirectory.mkdirs());
            MockGpxFilesRepository repository = new MockGpxFilesRepository();
            GpxService service = new GpxService(filesystem, repository);
            String fileContent = "testing content";
            String filename = "gpxfile.txt";
            MultipartFile file = creaMultipartFile(filename, fileContent);
            service.store(file, 3, email);
            File uploadedFile = new File(userDirectory, file.getOriginalFilename());
            Assertions.assertTrue(uploadedFile.exists());
            Assertions.assertEquals(fileContent.getBytes().length, uploadedFile.length());
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
