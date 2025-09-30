package fr.openrunning.gpxprocessor.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.openrunning.model.services.Filesystem;

public class MockFilesystem extends Filesystem {
    public MockFilesystem() {
        super("not_defined");
    }

    private List<String> filenames = new ArrayList<>();

    public void addFile(String filename) {
        filenames.add(filename);
    }

    @Override
    public boolean exists(File file) {
        return filenames.contains(file.getName());
    }
}
