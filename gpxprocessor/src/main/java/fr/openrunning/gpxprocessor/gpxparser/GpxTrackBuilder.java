package fr.openrunning.gpxprocessor.gpxparser;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.track.GpxTrack;

@Component
public class GpxTrackBuilder {
    public GpxTrack buildGpxTrack(File file) throws GpxProcessorException {
        GpxParser parser = new GpxParser();
        GpxTrack track = parser.parse(file);
        return track;
    }
}
