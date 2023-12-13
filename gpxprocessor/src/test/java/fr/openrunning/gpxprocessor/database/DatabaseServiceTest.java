package fr.openrunning.gpxprocessor.database;

import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.openrunning.gpxprocessor.GenericTest;
import fr.openrunning.gpxprocessor.statistics.modules.FrequencyStatistic;
import fr.openrunning.model.database.frequency.Frequency;
import fr.openrunning.model.database.frequency.FrequencyRepository;

@SpringBootTest
public class DatabaseServiceTest extends GenericTest {
    @Autowired
    private DatabaseService service;
    @Autowired
    private FrequencyRepository frequencyRepository;

    @Test
    public void saveFrequencyStatsTest() {
        try {
            frequencyRepository.deleteAll();
            FrequencyStatistic frequencyStatistic = new FrequencyStatistic();
            frequencyStatistic.compute(track);
            service.save(11, frequencyStatistic);
            Iterator<Frequency> iterator = frequencyRepository.findAll().iterator();
            Assertions.assertTrue(iterator.hasNext());
            while (iterator.hasNext()) {
                Frequency frequency = iterator.next();
                Assertions.assertEquals(frequencyStatistic.getDistanceInMeters(), frequency.getTotalDistanceInMeters());
            }
            service.save(11, frequencyStatistic);
            int dailyCounter = 0;
            int weeklyCounter = 0;
            int monthlyCounter = 0;
            int yearlyCounter = 0;
            iterator = frequencyRepository.findAll().iterator();
            while (iterator.hasNext()) {
                Frequency frequency = iterator.next();
                Assertions.assertEquals(
                        frequencyStatistic.getDistanceInMeters() * 2, frequency.getTotalDistanceInMeters());
                switch (frequency.getFrequency()) {
                    case DAILY:
                        dailyCounter++;
                        break;
                    case WEEKLY:
                        weeklyCounter++;
                        break;
                    case MONTHLY:
                        monthlyCounter++;
                        break;
                    case YEARLY:
                        yearlyCounter++;
                        break;
                }
            }
            Assertions.assertEquals(1, dailyCounter);
            Assertions.assertEquals(1, weeklyCounter);
            Assertions.assertEquals(1, monthlyCounter);
            Assertions.assertEquals(1, yearlyCounter);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
