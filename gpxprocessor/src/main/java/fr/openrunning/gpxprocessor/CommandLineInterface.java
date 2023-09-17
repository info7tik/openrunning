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

import fr.openrunning.gpxprocessor.database.DatabaseSerializer;
import fr.openrunning.gpxprocessor.database.DatabaseService;
import fr.openrunning.gpxprocessor.generator.StatisticsGenerator;
import fr.openrunning.gpxprocessor.generator.statistics.RecordStatistic;
import fr.openrunning.gpxprocessor.gpxparser.GpxParser;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.Record;

@Component
public class CommandLineInterface implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(CommandLineInterface.class);
    private final CommandLineOptionManager optionManager;
    private final StatisticsGenerator statisticsGenerator;
    private final DatabaseService databaseService;
    private final DatabaseSerializer databaseSerializer;

    // TODO Use statistics to fill database with weekly, monthly, yearly data
    private List<File> gpxFiles;
    private List<GpxTrack> gpxTracks;

    @Autowired
    public CommandLineInterface(CommandLineOptionManager optionManager, StatisticsGenerator generator,
            DatabaseService service, DatabaseSerializer databaseSerializer) {
        this.optionManager = optionManager;
        this.statisticsGenerator = generator;
        this.databaseService = service;
        this.databaseSerializer = databaseSerializer;
        this.gpxFiles = new LinkedList<>();
        this.gpxTracks = new LinkedList<>();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            logger.info("Starting the CLI...");
            optionManager.getEnabledStatisticModule(args)
                    .forEach(moduleName -> statisticsGenerator.enabledStatisticModule(moduleName));
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
                track.computeTotalDistance();
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
                statisticsGenerator.generateStatistics(track);
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
        statisticsGenerator.getStatistics().forEach((stats) -> {
            String error = "error while saving '" + stats.getModuleName() + "' to the database";
            try {
                switch (stats.getModuleName()) {
                    case PERIODIC:
                        break;
                    case RECORD:
                        Record moduleResult = databaseSerializer.convert(userId, (RecordStatistic) stats);
                        databaseService.save(moduleResult);
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
