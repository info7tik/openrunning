package fr.openrunning.orbackend.run;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.openrunning.model.database.track.TracksRepository;
import fr.openrunning.model.type.FrequencyType;
import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.run.json.JsonRun;

@Service
public class RunService {
    private final Logger logger = LoggerFactory.getLogger(RunService.class);
    private final int NUMBER_OF_LAST_RUNS = 3;
    private final TracksRepository repository;

    @Autowired
    public RunService(TracksRepository tracksRepository) {
        this.repository = tracksRepository;
    }

    public List<JsonRun> getLastRuns(int userId, FrequencyType frequency, long startTime) throws OpenRunningException {
        try {
            List<JsonRun> runs = new ArrayList<>();
            switch (frequency) {
                case DAILY:
                    repository.findLastRuns(userId, startTime, PageRequest.of(0, NUMBER_OF_LAST_RUNS))
                            .forEach((track) -> {
                                JsonRun newRun = new JsonRun();
                                newRun.setTimestampInSeconds(track.getTimestamp());
                                newRun.setDistanceMeters(track.getDistanceInMeters());
                                newRun.setTimeSeconds(track.getTimeInSeconds());
                                newRun.setPaceSeconds(track.getTimeInSeconds() * 1000 / track.getDistanceInMeters());
                                runs.add(newRun);
                            });
                    break;
                default:
                    // Use another repository
                    logger.info("Not implemented yet");
                    break;
            }
            return runs;
        } catch (Exception e) {
            String error = "error while retrieving all runs";
            logger.error(error, e);
            throw new OpenRunningException(error, e);
        }
    }

    // public JsonRunData getRunDataById() throws OpenRunningException {
    // }
}
