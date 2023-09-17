package fr.openrunning.gpxprocessor.database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;
import fr.openrunning.model.DatabaseObject;
import fr.openrunning.model.Record;
import fr.openrunning.model.TimestampUserPrimaryKey;
import fr.openrunning.model.Track;
import fr.openrunning.model.User;

@Service
public class DatabaseService {
    private final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private final UserRepository userRepository;
    private final TracksRepository tracksRepository;
    private final RecordsRepository recordsRepository;

    @Autowired
    public DatabaseService(
            UserRepository userRepository, TracksRepository tracksRepository, RecordsRepository recordsRepository) {
        this.userRepository = userRepository;
        this.tracksRepository = tracksRepository;
        this.recordsRepository = recordsRepository;
    }

    public int getUserIdFromEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        if (users.isEmpty()) {
            return -1;
        } else {
            if (users.size() > 1) {
                logger.error("more than one user found from " + email);
            }
            return users.get(0).getId();
        }
    }

    public void save(Track track) throws GpxProcessorException {
        try {
            save(tracksRepository, track);
        } catch (Exception e) {
            throw new GpxProcessorException("error while saving to database", e);
        }
    }

    public Record save(int userId, RecordStatistic recordStats) throws GpxProcessorException {
        try {
            Record recordDTO = convert(userId, recordStats);
            save(recordsRepository, recordDTO);
            return recordDTO;
        } catch (Exception e) {
            throw new GpxProcessorException("error while saving to database", e);
        }
    }

    private Record convert(int userId, RecordStatistic recordStats) throws GpxProcessorException {
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

    private <T extends DatabaseObject> void save(CrudRepository<T, TimestampUserPrimaryKey> repository,
            T objectToSave) {
        if (exists(repository, objectToSave.getUserId(), objectToSave.getTimestamp())) {
            logger.warn("timestamp '" + objectToSave.getTimestamp() + "' already exists for user '"
                    + objectToSave.getUserId() + "'. We delete it.");
            delete(repository, objectToSave.getUserId(), objectToSave.getTimestamp());
        }
        repository.save(objectToSave);
    }

    private <T extends DatabaseObject> boolean exists(
            CrudRepository<T, TimestampUserPrimaryKey> repository, int userId, long timestamp) {
        TimestampUserPrimaryKey primaryKey = new TimestampUserPrimaryKey(timestamp, userId);
        return repository.existsById(primaryKey);
    }

    private <T extends DatabaseObject> void delete(
            CrudRepository<T, TimestampUserPrimaryKey> repository, int userId, long timestamp) {
        TimestampUserPrimaryKey primaryKey = new TimestampUserPrimaryKey(timestamp, userId);
        repository.deleteById(primaryKey);
    }
}
