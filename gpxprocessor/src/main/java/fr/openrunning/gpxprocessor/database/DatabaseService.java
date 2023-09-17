package fr.openrunning.gpxprocessor.database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
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
    public DatabaseService(UserRepository userRepository, TracksRepository tracksRepository,
            RecordsRepository recordsRepository) {
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

    public void save(Record record) throws GpxProcessorException {
        try {
            save(recordsRepository, record);
        } catch (Exception e) {
            throw new GpxProcessorException("error while saving to database", e);
        }
    }

    private <T extends DatabaseObject> void save(CrudRepository<T, TimestampUserPrimaryKey> repository, T track) {
        if (exists(repository, track.getUserId(), track.getTimestamp())) {
            logger.warn(
                    "timestamp '" + track.getTimestamp() + "' already exists for user '" + track.getUserId()
                            + "'. We delete it.");
            delete(repository, track.getUserId(), track.getTimestamp());
        }
        repository.save(track);
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
