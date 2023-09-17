package fr.openrunning.gpxprocessor.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.generator.statistics.RecordStatistic;
import fr.openrunning.model.Record;

@Component
public class DatabaseSerializer {
    private final Logger logger = LoggerFactory.getLogger(DatabaseSerializer.class);

    public Record convert(int userId, RecordStatistic recordStats) throws GpxProcessorException {
        if (recordStats.isAvailable()) {
            long bestTimeForTarget = recordStats.getBestTimeInSeconds() * recordStats.getTargetInMeters()
                    / recordStats.getBestDistanceInMeters();
            Record recordDatabase = new Record();
            recordDatabase.setTimestamp(recordStats.getFirstTimeInSeconds());
            recordDatabase.setUserId(userId);
            recordDatabase.setDistanceInMeters(recordStats.getTargetInMeters());
            recordDatabase.setTimeInSeconds(bestTimeForTarget);
            recordDatabase.setFirstPointIndex(recordStats.getBestStartIndex());
            recordDatabase.setLastPointIndex(recordStats.getBestEndIndex());
            return recordDatabase;
        } else {
            String error = "No record available for " + recordStats.getTargetInMeters() + " meters";
            logger.error(error);
            throw new GpxProcessorException(error);
        }
    }
}
