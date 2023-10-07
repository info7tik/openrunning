package fr.openrunning.gpxprocessor.generator.statistics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import fr.openrunning.gpxprocessor.GenericTest;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;

@SpringBootTest
public class RecordTest extends GenericTest {

    @Test
    public void computeTest() {
        try {
            RecordStatistic record = new RecordStatistic(4000);
            record.compute(track);
            Assertions.assertEquals(1192, record.getBestTimeInSeconds());
            Assertions.assertEquals(4022, record.getBestDistanceInMeters());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
