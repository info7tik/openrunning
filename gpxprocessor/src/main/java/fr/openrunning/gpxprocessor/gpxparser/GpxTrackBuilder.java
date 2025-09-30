package fr.openrunning.gpxprocessor.gpxparser;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.exception.OpenRunningException;

@Component
public class GpxTrackBuilder {
    public GpxTrack buildGpxTrack(File file) throws OpenRunningException {
        GpxParser parser = new GpxParser();
        GpxTrack track = parser.parse(file);
        return track;
    }
}
