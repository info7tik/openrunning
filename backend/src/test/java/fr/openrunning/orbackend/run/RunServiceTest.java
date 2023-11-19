package fr.openrunning.orbackend.run;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import fr.openrunning.orbackend.run.json.JsonRecord;

@SpringBootTest
@Sql("/personal-record.sql")
public class RunServiceTest {
    private int USER_ID_WITH_MULTIPLE_RECORDS = 5;
    private int USER_ID_WITH_SINGLE_RECORD = 6;
    @Autowired
    private RunService service;

    @Test
    public void personalRecordTest() {
        try {
            List<JsonRecord> records = service.getPersonalRecords(USER_ID_WITH_MULTIPLE_RECORDS);
            Assertions.assertEquals(2, records.size());
            records.forEach((record) -> {
                if (record.getDistance() == 4000) {
                    Assertions.assertEquals(300, record.getTime());
                } else if (record.getDistance() == 1000) {
                    Assertions.assertEquals(100, record.getTime());
                } else {
                    Assertions.fail("Distance does not exist in the database");
                }
            });
            records = service.getPersonalRecords(USER_ID_WITH_SINGLE_RECORD);
            Assertions.assertEquals(1, records.size());
            Assertions.assertEquals(50, records.get(0).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void trackRecordTest() {
        try {
            List<JsonRecord> records = service.getTrackRecords(USER_ID_WITH_MULTIPLE_RECORDS, 1461945688);
            Assertions.assertEquals(2, records.size());
            records.forEach((record) -> {
                if (record.getDistance() == 4000) {
                    Assertions.assertEquals(700, record.getTime());
                } else if (record.getDistance() == 1000) {
                    Assertions.assertEquals(700, record.getTime());
                } else {
                    Assertions.fail("Distance does not exist in the database");
                }
            });
            records = service.getTrackRecords(USER_ID_WITH_MULTIPLE_RECORDS, 1461945686);
            Assertions.assertEquals(1, records.size());
            Assertions.assertEquals(100, records.get(0).getTime());
            records = service.getTrackRecords(USER_ID_WITH_MULTIPLE_RECORDS, 0);
            Assertions.assertEquals(0, records.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

}
