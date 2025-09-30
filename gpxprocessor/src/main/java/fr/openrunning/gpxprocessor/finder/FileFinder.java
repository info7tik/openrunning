package fr.openrunning.gpxprocessor.finder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openrunning.model.exception.OpenRunningException;
import fr.openrunning.model.services.Filesystem;

@Service
public class FileFinder {
    private final Logger logger = LoggerFactory.getLogger(FileFinder.class);
    private final Filesystem filesystem;

    @Autowired
    public FileFinder(Filesystem filesystem) {
        this.filesystem = filesystem;
    }

    public List<ReadyFile> getReadyToParse(List<FileEntry> entries) {
        List<ReadyFile> results = new ArrayList<>();
        for (FileEntry file : entries) {
            try {
                ReadyFile readyFile = this.buildReadyFile(file);
                if (this.filesystem.exists(readyFile.getFile())) {
                    results.add(readyFile);
                } else {
                    logger.error("GPX file does not exist: " + readyFile.getFile().getAbsolutePath());
                }
            } catch (Exception e) {
                logger.error("GPX file has errors: " + file.getFilename(), e);
            }
        }
        return results;
    }

    private ReadyFile buildReadyFile(FileEntry file) throws OpenRunningException {
        File userDirectory = this.filesystem.userUploadDirectory(file.getEmail());
        return new ReadyFile(new File(userDirectory, file.getFilename()), file.getUserId());

    }
}
