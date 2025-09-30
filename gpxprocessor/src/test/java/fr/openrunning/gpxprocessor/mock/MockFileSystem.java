package fr.openrunning.gpxprocessor.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.openrunning.gpxprocessor.FileSystem;

public class MockFileSystem extends FileSystem {
    private List<File> files = new ArrayList<>();

    public void addFile(File file) {
        files.add(file);
    }

    @Override
    public boolean exists(File file) {
        return files.contains(file);
    }
}
