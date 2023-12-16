package fr.openrunning.gpxprocessor.generator.statistics;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import fr.openrunning.gpxprocessor.GenericTest;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;
import fr.openrunning.model.database.record.Record;
import fr.openrunning.model.database.record.RecordsRepository;

@SpringBootTest
public class RecordTest extends GenericTest {
    @Autowired
    private RecordsRepository recordsRepository;

    @Test
    public void computeTest() {
        try {
            RecordStatistic record = new RecordStatistic(4000);
            record.compute(track1);
            Assertions.assertEquals(1192, record.getBestTimeInSeconds());
            Assertions.assertEquals(4022, record.getBestDistanceInMeters());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    @Sql({ "/personal-record.sql" })
    public void personalRecordTest() {
        try {
            Optional<Record> record = recordsRepository.getBestRecordFromDistanceInMeters(5, 1000);
            Assertions.assertEquals(100, record.get().getTimeInSeconds());
            record = recordsRepository.getBestRecordFromDistanceInMeters(5, 4000);
            Assertions.assertEquals(300, record.get().getTimeInSeconds());
            record = recordsRepository.getBestRecordFromDistanceInMeters(5, 2000);
            Assertions.assertFalse(record.isPresent());
            List<Record> records = recordsRepository.getTrackRecords(5, 1461945685);
            Assertions.assertEquals(2, records.size());
            records.forEach((trackRecord) -> {
                if (trackRecord.getDistanceInMeters() == 1000) {
                    Assertions.assertEquals(500, trackRecord.getTimeInSeconds());
                } else if (trackRecord.getDistanceInMeters() == 4000) {
                    Assertions.assertEquals(800, trackRecord.getTimeInSeconds());
                } else {
                    Assertions.fail("No more distance for this user");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

}
