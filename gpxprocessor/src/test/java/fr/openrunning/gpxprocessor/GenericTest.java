package fr.openrunning.gpxprocessor;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openrunning.gpxprocessor.gpxparser.GpxParser;
import fr.openrunning.gpxprocessor.track.GpxTrack;

public class GenericTest {
    @Autowired
    private GpxParser parser;
    protected GpxTrack track1;
    protected GpxTrack track2;

    @BeforeEach
    private void createTrack() {
        try {
            String filepath = "src/test/resources/activity_598628562.gpx";
            File gpxFile = new File(filepath);
            track1 = parser.parse(gpxFile);
            track1.computeTotalDistanceAndTime();
            filepath = "src/test/resources/activity_678295375.gpx";
            gpxFile = new File(filepath);
            track2 = parser.parse(gpxFile);
            track2.computeTotalDistanceAndTime();
        } catch (Exception e) {
            System.err.println("Can not parse the GPX file used by the test.");
        }
    }
}
