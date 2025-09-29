package fr.openrunning.gpxprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.database.DatabaseService;
import fr.openrunning.gpxprocessor.gpxparser.GpxTrackBuilder;
import fr.openrunning.gpxprocessor.statistics.StatisticModuleManager;
import fr.openrunning.gpxprocessor.statistics.StatisticModuleName;
import fr.openrunning.gpxprocessor.statistics.modules.FrequencyStatistic;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;
import fr.openrunning.gpxprocessor.track.GpxTrack;

@Component
public class CommandLineInterface implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(CommandLineInterface.class);
    private final CommandLineOptionManager optionManager;
    private final StatisticModuleManager moduleManager;
    private final GpxTrackBuilder trackBuilder;
    private final DatabaseService databaseService;
    private final int waitBeetweenLoopSeconds = 2;

    @Autowired
    public CommandLineInterface(CommandLineOptionManager optionManager, StatisticModuleManager moduleManager,
            GpxTrackBuilder trackBuilder, DatabaseService service) {
        this.optionManager = optionManager;
        this.moduleManager = moduleManager;
        this.trackBuilder = trackBuilder;
        this.databaseService = service;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            logger.info("Starting the CLI...");
            moduleManager.enableStatisticModule(StatisticModuleName.RECORD);
            moduleManager.enableStatisticModule(StatisticModuleName.FREQUENCY);
            while (true) {
                try {
                    List<File> gpxFiles = optionManager.getGpxFiles(args);
                    List<GpxTrack> gpxTracks = parseGpxFiles(gpxFiles);
                    buildStatistics(gpxTracks);
                    saveTrackAndStatistics(1, gpxTracks);
                    logger.debug("Wait " + waitBeetweenLoopSeconds + " seconds before the next loop");
                    Thread.sleep(waitBeetweenLoopSeconds * 1000);
                } catch (Exception e) {
                    logger.error("can not parse the GPX files", e);
                }
            }
        } catch (Exception e) {
            logger.error("error while parsing GPX files", e);
            System.out.println("ERROR: " + e);
        }
    }

    private List<GpxTrack> parseGpxFiles(List<File> gpxFiles) {
        List<GpxTrack> gpxTracks = new ArrayList<>();
        gpxFiles.forEach((file) -> {
            logger.info("Parsing " + file.getAbsolutePath());
            try {
                if (databaseService.isFileAlreadyParsed(file.getName())) {
                    logger.error("File " + file.getName() + " have already been parsed! "
                            + "Ignore this file for the rest of the workflow.");
                } else {
                    GpxTrack track = trackBuilder.buildGpxTrack(file);
                    track.computeTotalDistanceAndTime();
                    track.computeSamples();
                    gpxTracks.add(track);
                    logger.info(track.buildTrackInformation());
                }
            } catch (Exception e) {
                logger.error("error while parsing '" + file.getAbsolutePath() + "'", e);
            }
        });
        return gpxTracks;
    }

    private void buildStatistics(List<GpxTrack> gpxTracks) {
        gpxTracks.forEach((track) -> {
            logger.info("Statistics for " + track.getFilename());
            try {
                moduleManager.generateStatistics(track);
            } catch (Exception e) {
                logger.error("error while generating stats from '" + track.getName() + "'", e);
            }
        });
    }

    private void saveTrackAndStatistics(final int userId, List<GpxTrack> gpxTracks) {
        gpxTracks.forEach((gpxTrack) -> {
            try {
                databaseService.saveTrackWithSamples(userId, gpxTrack);
            } catch (Exception e) {
                logger.error("can not save data from " + gpxTrack.getFilename(), e);
            }
        });
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
}
