package fr.openrunning.gpxprocessor.generator.statistics;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.openrunning.gpxprocessor.generator.StatisticResult;
import fr.openrunning.gpxprocessor.parser.GpxParser;
import fr.openrunning.gpxprocessor.track.GpxTrack;

@SpringBootTest
public class RecordTest {
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
    public void computeTest() {
        Record record = new Record(4000);
        record.initialize();
        record.compute(track);
        StatisticResult result = record.getResult();
        Assertions.assertEquals(1192, record.getBestTimeInSeconds());
        Assertions.assertEquals(4022, record.getBestDistanceInMeters());
        Assertions.assertTrue(result.hasValue());
        Assertions.assertEquals(1185, result.getTimeInSeconds());
        Assertions.assertEquals(4000, result.getDistanceInMeters());
        record.initialize();
        record.compute(track);
        result = record.getResult();
        Assertions.assertEquals(1192, record.getBestTimeInSeconds());
        Assertions.assertEquals(4022, record.getBestDistanceInMeters());
        Assertions.assertTrue(result.hasValue());
        Assertions.assertEquals(1185, result.getTimeInSeconds());
        Assertions.assertEquals(4000, result.getDistanceInMeters());
    }
}
