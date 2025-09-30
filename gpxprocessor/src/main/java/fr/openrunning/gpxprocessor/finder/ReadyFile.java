package fr.openrunning.gpxprocessor.finder;

import java.io.File;

import lombok.Data;

@Data
public class ReadyFile {
    private final File file;
    private final int userId;
}
