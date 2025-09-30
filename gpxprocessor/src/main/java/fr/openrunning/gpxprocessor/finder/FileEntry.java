package fr.openrunning.gpxprocessor.finder;

import lombok.Data;

@Data
public class FileEntry {
    private final String filename;
    private final String email;
    private final int userId;
}
