package fr.openrunning.gpxprocessor;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class FileSystem {
    public boolean exists(File file) {
        return file.exists();
    }
}
