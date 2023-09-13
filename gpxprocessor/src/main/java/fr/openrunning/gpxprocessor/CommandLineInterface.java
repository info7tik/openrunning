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
import fr.openrunning.gpxprocessor.generator.StatisticsGenerator;
import fr.openrunning.gpxprocessor.parser.GpxParser;
import fr.openrunning.gpxprocessor.track.GpxTrack;

@Component
public class CommandLineInterface implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(CommandLineInterface.class);
    private final CommandLineOptionManager optionManager;
    private final StatisticsGenerator statisticsGenerator;
    private final DatabaseService databaseService;

    // TODO Save the record to the database
    // TODO Use statistics to fill database with weekly, monthly, yearly data
    private List<File> gpxFiles;
    private List<GpxTrack> gpxTracks;

    @Autowired
    public CommandLineInterface(CommandLineOptionManager optionManager, StatisticsGenerator generator,
            DatabaseService service) {
        this.databaseService = service;
        this.statisticsGenerator = generator;
        this.optionManager = optionManager;
        this.gpxFiles = new LinkedList<>();
        this.gpxTracks = new LinkedList<>();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            logger.info("Starting the CLI...");
            optionManager.addStatistics(statisticsGenerator, args);
            this.gpxFiles = optionManager.getGpxFiles(args);
            parseGpxFiles();
            buildGpxTracks();
            if (optionManager.mustSaveToDatabase(args)) {
                int userId = optionManager.getUserIdFromEmail(args);
                saveToDatabase(args, userId);
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

    private void saveToDatabase(ApplicationArguments args, final int userId) {
        gpxTracks.forEach((gpxTrack) -> {
            try {
                if (databaseService.existsTrack(userId, gpxTrack.getFirstTime())) {
                    logger.warn(
                            "track associated to " + gpxTrack.getFilename()
                                    + " already exists. We will delete this track and the associated statistics.");
                    databaseService.deleteTrack(userId, gpxTrack.getFirstTime());
                }
                databaseService.insertTrack(gpxTrack.toTrack(userId));
            } catch (Exception e) {
                logger.error("can not save data from " + gpxTrack.getFilename(), e);
            }
        });
    }
}
