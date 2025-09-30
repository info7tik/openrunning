package fr.openrunning.gpxprocessor.finder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openrunning.model.database.gpxfiles.GpxFilesRepository;

@Service
public class FileFinder {
    @Autowired
    private final GpxFilesRepository filesRepository;

    public FileFinder(GpxFilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public List<ReadyFile> getReadyToParse() {
        List<ReadyFile> results = new ArrayList<>();
        return results;
    }

}
