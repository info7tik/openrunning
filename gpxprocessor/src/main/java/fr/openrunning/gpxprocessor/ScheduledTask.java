package fr.openrunning.gpxprocessor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.database.DatabaseService;
import fr.openrunning.gpxprocessor.database.UserService;
import fr.openrunning.gpxprocessor.finder.FileFinder;
import fr.openrunning.gpxprocessor.finder.ReadyFile;
import fr.openrunning.gpxprocessor.gpxparser.GpxTrackBuilder;
import fr.openrunning.gpxprocessor.statistics.StatisticModuleManager;
import fr.openrunning.gpxprocessor.statistics.StatisticModuleName;
import fr.openrunning.gpxprocessor.statistics.modules.FrequencyStatistic;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.exception.OpenRunningException;

@Component
public class ScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    private final StatisticModuleManager moduleManager;
    private final GpxTrackBuilder trackBuilder;
    private final DatabaseService databaseService;
    private final FileFinder fileFinder;
    private final UserService userService;

    @Autowired
    public ScheduledTask(
            FileFinder fileFinder, StatisticModuleManager moduleManager,
            UserService userService, GpxTrackBuilder trackBuilder, DatabaseService service) {
        this.moduleManager = moduleManager;
        this.trackBuilder = trackBuilder;
        this.databaseService = service;
        this.fileFinder = fileFinder;
        this.userService = userService;
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void run() throws Exception {
        try {
            moduleManager.enableStatisticModule(StatisticModuleName.RECORD);
            moduleManager.enableStatisticModule(StatisticModuleName.FREQUENCY);
            for (ReadyFile file : this.fileFinder.getReadyToParse(this.userService.buildFileEntries())) {
                logger.info("processing the file " + file.getFile().getAbsolutePath());
                GpxTrack track = parse(file.getFile());
                buildStatistics(track);
                saveTrackAndStatistics(file.getUserId(), track);
            }
        } catch (Exception e) {
            logger.error("error while parsing GPX files", e);
        }
    }

    private GpxTrack parse(File gpxFile) throws OpenRunningException {
        logger.info("Parsing " + gpxFile.getAbsolutePath());
        try {
            if (databaseService.isFileAlreadyParsed(gpxFile.getName())) {
                throw logThenRaise("File " + gpxFile.getName() + " has already been parsed");
            } else {
                GpxTrack track = trackBuilder.buildGpxTrack(gpxFile);
                track.computeTotalDistanceAndTime();
                track.computeSamples();
                logger.info(track.buildTrackInformation());
                return track;
            }
        } catch (OpenRunningException gpe) {
            throw gpe;
        } catch (Exception e) {
            throw logThenRaise("error while parsing '" + gpxFile.getAbsolutePath() + "'", e);
        }
    }

    private void buildStatistics(GpxTrack track) {
        logger.info("Statistics for " + track.getFilename());
        moduleManager.generateStatistics(track);
    }

    private void saveTrackAndStatistics(final int userId, GpxTrack gpxTrack) {
        databaseService.saveTrackWithSamples(userId, gpxTrack);
        moduleManager.getStatistics().forEach((stats) -> {
            String error = "error while saving '" + stats.getModuleName() + "' to the database";
            try {
                switch (stats.getModuleName()) {
                    case FREQUENCY:
                        databaseService.save(userId, (FrequencyStatistic) stats);
                        break;
                    case RECORD:
                        databaseService.save(userId, (RecordStatistic) stats);
                        break;
                    default:
                        logger.error(error + ": unknown module");
                }
            } catch (Exception e) {
                logger.error(error, e);
            }
        });
    }

    private OpenRunningException logThenRaise(String message) {
        logger.error(message);
        return new OpenRunningException(message);
    }

    private OpenRunningException logThenRaise(String message, Exception e) {
        logger.error(message, e);
        return new OpenRunningException(message, e);
    }
}
