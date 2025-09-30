package fr.openrunning.gpxprocessor.finder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.openrunning.gpxprocessor.mock.MockFilesystem;

public class FileFinderTest {

    @Test
    public void getReadyToParse() {
        int userId = 1;
        String existingFilename = "first.gpx";
        List<FileEntry> entries = List.of(new FileEntry(existingFilename, "first@mail.com", userId),
                new FileEntry("second.gpx", "second@mail.com", userId));
        MockFilesystem filesystem = new MockFilesystem();
        filesystem.addFile(existingFilename);

        FileFinder finder = new FileFinder(filesystem);
        List<ReadyFile> files = finder.getReadyToParse(entries);
        assertEquals(1, files.size());
    }
}
