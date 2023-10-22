package fr.openrunning.orbackend.run;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openrunning.model.database.track.TracksRepository;
import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.run.json.JsonRun;

@Service
public class RunService {
    private final Logger logger = LoggerFactory.getLogger(RunService.class);
    private final TracksRepository repository;

    @Autowired
    public RunService(TracksRepository tracksRepository) {
        this.repository = tracksRepository;
    }

    public List<JsonRun> getAllRuns() throws OpenRunningException {
        try {
            List<JsonRun> runs = new ArrayList<>();
            repository.findAll().forEach((track) -> {
                JsonRun newRun = new JsonRun();
                newRun.setTimestampInSeconds(track.getTimestamp());
                newRun.setDistanceMeters(track.getDistanceInMeters());
                newRun.setTimeSeconds(track.getTimeInSeconds());
                newRun.setPaceSeconds(track.getTimeInSeconds() * 1000 / track.getDistanceInMeters());
                runs.add(newRun);
            });
            return runs;
        } catch (Exception e) {
            String error = "error while retrieving all runs";
            logger.error(error, e);
            throw new OpenRunningException(error, e);
        }
    }
}
