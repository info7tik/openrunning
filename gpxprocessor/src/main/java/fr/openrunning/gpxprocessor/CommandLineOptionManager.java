package fr.openrunning.gpxprocessor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.database.DatabaseService;
import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.generator.StatisticsGenerator;
import fr.openrunning.gpxprocessor.generator.statistics.Record;

@Component
public class CommandLineOptionManager {
    private final Logger logger = LoggerFactory.getLogger(CommandLineOptionManager.class);
    private final DatabaseService databaseService;

    @Autowired
    public CommandLineOptionManager(DatabaseService service) {
        this.databaseService = service;
    }

    public void addStatistics(StatisticsGenerator generator, ApplicationArguments args) {
        String statsOptionName = "stats";
        if (args.containsOption(statsOptionName)) {
            List<String> statisticsNames = args.getOptionValues(statsOptionName);
            statisticsNames.forEach((statsName) -> {
                logger.info("Statistics " + statsName + " added");
                if (statsName.equals("records")) {
                    generator.addStatistics(new Record(2000));
                }
            });
        }
    }

    public List<File> getGpxFiles(ApplicationArguments args) {
        List<File> gpxFiles = new LinkedList<>();
        args.getNonOptionArgs().forEach((filePath) -> {
            File gpxFile = new File(filePath);
            if (gpxFile.exists()) {
                logger.info("add the GPX file " + filePath);
                gpxFiles.add(gpxFile);
            }
        });
        return gpxFiles;
    }

    public int getUserIdFromEmail(ApplicationArguments args) throws GpxProcessorException {
        String userOptionName = "user";
        if (args.containsOption(userOptionName)) {
            List<String> userOptionValues = args.getOptionValues(userOptionName);
            if (userOptionValues.isEmpty()) {
                String error = "the '" + userOptionName + "' option requires a value, for example, --"
                        + userOptionName + "=myuser@mydomain.com";
                logger.error(error);
                throw new GpxProcessorException(error);
            } else {
                String userEmail = userOptionValues.get(0);
                int userId = databaseService.getUserIdFromEmail(userEmail);
                if (userId == -1) {
                    String error = "failed to find the user id from the database";
                    logger.error(error);
                    throw new GpxProcessorException(error);
                } else {
                    return userId;
                }
            }
        } else {
            String error = "the '" + userOptionName + "' option is required, for example, --" + userOptionName
                    + "=myuser@mydomain.com";
            throw new GpxProcessorException(error);
        }
    }

    public boolean mustSaveToDatabase(ApplicationArguments args){
        return args.containsOption("save");
    }
}
