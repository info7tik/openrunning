package fr.openrunning.gpxprocessor.track;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.openrunning.gpxprocessor.gpxparser.GpxParser;

@SpringBootTest
public class GpxTrackTest {
    @Autowired
    private GpxParser parser;
    private GpxTrack track;

    @BeforeEach
    private void createTrack() {
        try {
            String filepath = "src/test/resources/activity_598628562.gpx";
            File gpxFile = new File(filepath);
            track = parser.parse(gpxFile);
        } catch (Exception e) {
            System.err.println("Can not parse the GPX file used by the test.");
        }
    }

    @Test
    public void computeTotalDistanceTest() {
        try {
            track.computeTotalDistance();
            Assertions.assertEquals(9415, track.getDistanceInMeters());
            Assertions.assertEquals(2772, track.getTimeInSeconds());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void computeSamplesTest() {
        try {
            track.computeSamples();
            Assertions.assertEquals(151, track.getSamples().size());
            int totalDistance = track.getSamples().stream()
                    .map(Sample::getDistanceInMeters).mapToInt(Integer::intValue).sum();
            Assertions.assertEquals(9415, totalDistance);
            Assertions.assertEquals(4.545, track.getBestSample().computeSpeed());
            Assertions.assertEquals(220, track.getBestSample().computePace());
            Assertions.assertEquals(72, track.getBestSampleIndex());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
