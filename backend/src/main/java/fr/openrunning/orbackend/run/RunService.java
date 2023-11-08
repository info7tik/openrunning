package fr.openrunning.orbackend.run;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.openrunning.model.database.samples.SamplesRepository;
import fr.openrunning.model.database.track.TracksRepository;
import fr.openrunning.model.type.DistanceUnit;
import fr.openrunning.model.type.FrequencyType;
import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.run.json.JsonRun;
import fr.openrunning.orbackend.run.json.JsonSamples;

@Service
public class RunService {
    private final Logger logger = LoggerFactory.getLogger(RunService.class);
    private final int NUMBER_OF_LAST_RUNS = 15;
    private final TracksRepository tracksRepository;
    private final SamplesRepository samplesRepository;

    @Autowired
    public RunService(TracksRepository tracksRepository, SamplesRepository samplesRepository) {
        this.tracksRepository = tracksRepository;
        this.samplesRepository = samplesRepository;
    }

    public List<JsonRun> getLastRuns(int userId, FrequencyType frequency, long startTime) throws OpenRunningException {
        try {
            List<JsonRun> runs = new ArrayList<>();
            switch (frequency) {
                case DAILY:
                    tracksRepository.findLastRuns(userId, startTime, PageRequest.of(0, NUMBER_OF_LAST_RUNS))
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

    public JsonSamples getRunSamples(int userId, long runTimestamp) throws OpenRunningException {
        JsonSamples samples = new JsonSamples(runTimestamp);
        samples.setDistanceUnit(DistanceUnit.METER);
        samplesRepository.findByUserIdAndTimestamp(userId, runTimestamp).forEach((sample) -> {
            samples.addSample(sample.getDistanceInMeters(), sample.getTimeInSeconds());
        });
        return samples;
    }

    // TODO: API to get the records
    // TODO: API to get the frequencies
}
