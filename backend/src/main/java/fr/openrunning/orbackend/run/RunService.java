package fr.openrunning.orbackend.run;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.openrunning.model.database.record.Record;
import fr.openrunning.model.database.record.RecordsRepository;
import fr.openrunning.model.database.samples.SamplesRepository;
import fr.openrunning.model.database.track.TracksRepository;
import fr.openrunning.model.exception.OpenRunningException;
import fr.openrunning.model.type.DistanceUnit;
import fr.openrunning.model.type.FrequencyType;
import fr.openrunning.orbackend.run.json.JsonRecord;
import fr.openrunning.orbackend.run.json.JsonRun;
import fr.openrunning.orbackend.run.json.JsonSamples;

@Service
public class RunService {
    private final Logger logger = LoggerFactory.getLogger(RunService.class);
    private final int NUMBER_OF_LAST_RUNS = 15;
    private final TracksRepository tracksRepository;
    private final SamplesRepository samplesRepository;
    private final RecordsRepository recordsRepository;

    @Autowired
    public RunService(TracksRepository tracksRepository, SamplesRepository samplesRepository,
            RecordsRepository recordsRepository) {
        this.tracksRepository = tracksRepository;
        this.samplesRepository = samplesRepository;
        this.recordsRepository = recordsRepository;

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

    public List<JsonRecord> getPersonalRecords(int userId) {
        List<JsonRecord> records = new ArrayList<>();
        recordsRepository.getRecordDistances().forEach((distance) -> {
            Optional<Record> record = recordsRepository.getBestRecordFromDistanceInMeters(userId, distance);
            record.ifPresent((rec) -> records.add(buildJsonRecord(rec)));
        });
        return records;
    }

    public List<JsonRecord> getTrackRecords(int userId, long timestamp) {
        List<JsonRecord> json = new ArrayList<>();
        List<Record> records = recordsRepository.getTrackRecords(userId, timestamp);
        records.forEach((record) -> json.add(buildJsonRecord(record)));
        return json;
    }

    private JsonRecord buildJsonRecord(Record record) {
        JsonRecord json = new JsonRecord();
        json.setTimestamp(record.getTimestamp());
        json.setDistance(record.getDistanceInMeters());
        json.setFirstSampleIndex(record.getFirstPointIndex());
        json.setLastSampleIndex(record.getLastPointIndex());
        json.setTime(record.getTimeInSeconds());
        return json;
    }

    // TODO: API to get the frequencies
}
