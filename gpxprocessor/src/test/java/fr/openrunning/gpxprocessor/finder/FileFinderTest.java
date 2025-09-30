package fr.openrunning.gpxprocessor.finder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.openrunning.gpxprocessor.mock.MockFileSystem;
import fr.openrunning.model.database.gpxfiles.GpxFile;
import fr.openrunning.model.database.gpxfiles.GpxFilesRepository;
import fr.openrunning.model.type.FileStatus;

@ExtendWith(MockitoExtension.class)
public class FileFinderTest {

    @Test
    public void getReadyToParse(@Mock GpxFilesRepository filesRepository) {
        int firstUserId = 1;
        GpxFile checkingFile = new GpxFile();
        checkingFile.setFilename("first.gpx");
        checkingFile.setStatus(FileStatus.CHECKING);
        checkingFile.setUsedId(firstUserId);
        GpxFile checkingFileNotExist = new GpxFile();
        checkingFileNotExist.setFilename("second.gpx");
        checkingFileNotExist.setStatus(FileStatus.CHECKING);
        checkingFileNotExist.setUsedId(firstUserId);
        when(filesRepository.findByStatus(any())).thenReturn(List.of(checkingFile, checkingFileNotExist));
        MockFileSystem fileSystem = new MockFileSystem();
        fileSystem.addFile(new File(checkingFile.getFilename()));

        FileFinder finder = new FileFinder(fileSystem, filesRepository);
        List<ReadyFile> files = finder.getReadyToParse();
        assertEquals(1, files.size());
    }
}
