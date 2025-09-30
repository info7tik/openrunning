package fr.openrunning.gpxprocessor.database;

import org.springframework.stereotype.Service;

import fr.openrunning.model.database.gpxfiles.GpxFile;
import fr.openrunning.model.database.gpxfiles.GpxFilesRepository;
import fr.openrunning.model.exception.OpenRunningException;
import fr.openrunning.model.type.FileStatus;

@Service
public class StatusService {
    private final GpxFilesRepository filesRepository;

    public StatusService(GpxFilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public GpxFile get(String filename) throws OpenRunningException {
        return filesRepository.findById(filename)
                .orElseThrow(() -> new OpenRunningException("GPX file '" + filename + "' does not exist"));
    }

    public void markAsProcessing(GpxFile file) throws OpenRunningException {
        markAs(file, FileStatus.PROCESSING);
    }

    public void markAsCompleted(GpxFile file) throws OpenRunningException {
        markAs(file, FileStatus.COMPLETED);
    }

    private void markAs(GpxFile file, FileStatus status) {
        file.setStatus(status);
        filesRepository.save(file);
    }
}
