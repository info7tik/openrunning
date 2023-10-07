package fr.openrunning.gpxprocessor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.database.DatabaseService;
import fr.openrunning.gpxprocessor.gpxparser.GpxParser;
import fr.openrunning.gpxprocessor.statistics.StatisticModuleManager;
import fr.openrunning.gpxprocessor.statistics.modules.FrequencyStatistic;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;
import fr.openrunning.gpxprocessor.track.GpxTrack;

@Component
public class CommandLineInterface implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(CommandLineInterface.class);
    private final CommandLineOptionManager optionManager;
    private final StatisticModuleManager moduleManager;
    private final DatabaseService databaseService;
    private List<File> gpxFiles;
    private List<GpxTrack> gpxTracks;

    @Autowired
    public CommandLineInterface(
            CommandLineOptionManager optionManager, StatisticModuleManager moduleManager, DatabaseService service) {
        this.optionManager = optionManager;
        this.moduleManager = moduleManager;
        this.databaseService = service;
        this.gpxFiles = new LinkedList<>();
        this.gpxTracks = new LinkedList<>();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            logger.info("Starting the CLI...");
            optionManager.getEnabledStatisticModule(args)
                    .forEach(moduleName -> moduleManager.enabledStatisticModule(moduleName));
            this.gpxFiles = optionManager.getGpxFiles(args);
            parseGpxFiles();
            buildGpxTracks();
            if (optionManager.mustSaveToDatabase(args)) {
                int userId = optionManager.getUserIdFromEmail(args);
                saveToDatabase(userId);
            }
        } catch (Exception e) {
            logger.error("error while parsing GPX files", e);
            System.out.println("ERROR: " + e);
        }
    }

    private void parseGpxFiles() {
        gpxFiles.forEach((file) -> {
            logger.info("Parsing " + file.getAbsolutePath());
            try {
                GpxParser parser = new GpxParser();
                GpxTrack track = parser.parse(file);
                track.computeTotalDistanceAndTime();
                track.computeSamples();
                gpxTracks.add(track);
            } catch (Exception e) {
                logger.error("error while parsing '" + file.getAbsolutePath() + "'", e);
            }
        });
    }

    private void buildGpxTracks() {
        gpxTracks.forEach((track) -> {
            logger.info("Statistics for " + track.getFilename());
            try {
                logger.info(track.buildTrackInformation());
                moduleManager.generateStatistics(track);
            } catch (Exception e) {
                logger.error("error while generating stats from '" + track.getName() + "'", e);
            }
        });
    }

    private void saveToDatabase(final int userId) {
        gpxTracks.forEach((gpxTrack) -> {
            try {
                databaseService.save(gpxTrack.toDatabaseObject(userId));
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
