package fr.openrunning.gpxprocessor.generator.statistics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import fr.openrunning.gpxprocessor.GenericTest;
import fr.openrunning.gpxprocessor.statistics.modules.FrequencyStatistic;

@SpringBootTest
public class FrequencyTest extends GenericTest {

    @Test
    public void computeTest() {
        try {
            FrequencyStatistic frequencyStatistic = new FrequencyStatistic();
            frequencyStatistic.compute(track);
            Assertions.assertEquals(9415, frequencyStatistic.getDistanceInMeters());
            Assertions.assertEquals(2772L, frequencyStatistic.getTimeInSeconds());
            Assertions.assertEquals(1411423200L, frequencyStatistic.getDayTimestamp());
            Assertions.assertEquals(1411336800L, frequencyStatistic.getWeekTimestamp());
            Assertions.assertEquals(1409522400L, frequencyStatistic.getMonthTimestamp());
            Assertions.assertEquals(1388530800L, frequencyStatistic.getYearTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
