package fr.openrunning.gpxprocessor.database;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import fr.openrunning.gpxprocessor.statistics.modules.FrequencyStatistic;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;
import fr.openrunning.model.database.DatabaseObject;
import fr.openrunning.model.database.TimestampUserPrimaryKey;
import fr.openrunning.model.database.frequency.Frequency;
import fr.openrunning.model.database.frequency.FrequencyRepository;
import fr.openrunning.model.database.record.Record;
import fr.openrunning.model.database.record.RecordsRepository;
import fr.openrunning.model.database.samples.Sample;
import fr.openrunning.model.database.samples.SamplesRepository;
import fr.openrunning.model.database.track.Track;
import fr.openrunning.model.database.track.TracksRepository;
import fr.openrunning.model.database.user.User;
import fr.openrunning.model.database.user.UserRepository;

@Service
public class DatabaseService {
    private final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private final UserRepository userRepository;
    private final TracksRepository tracksRepository;
    private final SamplesRepository samplesRepository;
    private final RecordsRepository recordsRepository;
    private final FrequencyRepository frequencyRepository;

    @Autowired
    public DatabaseService(UserRepository userRepository, TracksRepository tracksRepository,
            SamplesRepository samplesRepository, RecordsRepository recordsRepository,
            FrequencyRepository frequencyRepository) {
        this.userRepository = userRepository;
        this.tracksRepository = tracksRepository;
        this.samplesRepository = samplesRepository;
        this.recordsRepository = recordsRepository;
        this.frequencyRepository = frequencyRepository;
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

    public void save(Record record) {
        try {
            save(recordsRepository, record);
        } catch (Exception e) {
            logger.info("error while saving to database", e);
        }
    }

    public void save(Track track) {
        try {
            save(tracksRepository, track);
        } catch (Exception e) {
            logger.info("error while saving to database", e);
        }
    }

    public void save(Sample sample) {
        try {
            save(samplesRepository, sample);
        } catch (Exception e) {
            logger.info("error while saving to database", e);
        }
    }

    public void save(int userId, RecordStatistic recordStats) {
        recordStats.toDatabaseObject(userId).forEach(dto -> {
            try {
                save(recordsRepository, dto);
            } catch (Exception e) {
                logger.info("error while saving to database", e);
            }
        });
    }

    public void save(int userId, FrequencyStatistic frequencyStats) {
        frequencyStats.toDatabaseObject(userId).forEach(dto -> {
            try {
                Frequency frequency = getFrequencyOrNull(userId, dto.getTimestamp());
                if (frequency == null) {
                    save(frequencyRepository, dto);
                } else {
                    frequency.aggregate(dto);
                    save(frequencyRepository, frequency);
                }
            } catch (Exception e) {
                logger.info("error while saving to database", e);
            }
        });
    }

    private Frequency getFrequencyOrNull(int userId, long timestamp) {
        TimestampUserPrimaryKey primaryKey = new TimestampUserPrimaryKey(timestamp, userId);
        Optional<Frequency> frequency = frequencyRepository.findById(primaryKey);
        if (frequency.isPresent()) {
            return frequency.get();
        } else {
            return null;
        }
    }

    private <T extends DatabaseObject> void save(
            CrudRepository<T, TimestampUserPrimaryKey> repository, T objectToSave) {
        repository.save(objectToSave);
    }
}
