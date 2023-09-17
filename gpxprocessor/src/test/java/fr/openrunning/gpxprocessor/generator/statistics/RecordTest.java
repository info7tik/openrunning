package fr.openrunning.gpxprocessor.generator.statistics;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.openrunning.gpxprocessor.database.DatabaseSerializer;
import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.gpxparser.GpxParser;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.Record;

@SpringBootTest
public class RecordTest {
    @Autowired
    private DatabaseSerializer databaseSerializer;
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
        try {
            RecordStatistic record = new RecordStatistic(4000);
            record.compute(track);
            Assertions.assertEquals(1192, record.getBestTimeInSeconds());
            Assertions.assertEquals(4022, record.getBestDistanceInMeters());
            int userId = 10;
            Record result = databaseSerializer.convert(userId, record);
            Assertions.assertEquals(userId, result.getUserId());
            Assertions.assertEquals(1185, result.getTimeInSeconds());
            Assertions.assertEquals(4000, result.getDistanceInMeters());
            try {
                record.compute(track);
                Assertions.fail("The statistic can only be computed one time");
            } catch (GpxProcessorException e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
