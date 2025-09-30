package fr.openrunning.gpxprocessor.finder;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openrunning.model.FileSystem;
import fr.openrunning.model.database.gpxfiles.GpxFilesRepository;
import fr.openrunning.model.type.FileStatus;

@Service
public class FileFinder {
    private final FileSystem filesystem;
    @Autowired
    private final GpxFilesRepository filesRepository;

    public FileFinder(FileSystem filesystem, GpxFilesRepository filesRepository) {
        this.filesystem = filesystem;
        this.filesRepository = filesRepository;
    }

    public List<ReadyFile> getReadyToParse() {
        return filesRepository.findByStatus(FileStatus.CHECKING).stream()
                .map(file -> new ReadyFile(new File(file.getFilename()), file.getUsedId()))
                .filter(file -> this.filesystem.exists(file.getFile()))
                .toList();
    }
}
