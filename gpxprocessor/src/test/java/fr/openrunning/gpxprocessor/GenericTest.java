package fr.openrunning.gpxprocessor;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openrunning.gpxprocessor.gpxparser.GpxParser;
import fr.openrunning.gpxprocessor.track.GpxTrack;

public class GenericTest {
    @Autowired
    private GpxParser parser;
    protected GpxTrack track;

    @BeforeEach
    private void createTrack() {
        try {
            String filepath = "src/test/resources/activity_598628562.gpx";
            File gpxFile = new File(filepath);
            track = parser.parse(gpxFile);
            track.computeTotalDistanceAndTime();
        } catch (Exception e) {
            System.err.println("Can not parse the GPX file used by the test.");
        }
    }
}
