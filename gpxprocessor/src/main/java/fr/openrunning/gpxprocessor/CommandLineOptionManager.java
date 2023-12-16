package fr.openrunning.gpxprocessor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;

@Component
public class CommandLineOptionManager {
    private final Logger logger = LoggerFactory.getLogger(CommandLineOptionManager.class);

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

    public String getUserEmail(ApplicationArguments args) throws GpxProcessorException {
        String userOptionName = "user";
        if (args.containsOption(userOptionName)) {
            List<String> userOptionValues = args.getOptionValues(userOptionName);
            if (userOptionValues.isEmpty()) {
                String error = "the '" + userOptionName + "' option requires a value, for example, --"
                        + userOptionName + "=myuser@mydomain.com";
                logger.error(error);
                throw new GpxProcessorException(error);
            } else {
                return userOptionValues.get(0);
            }
        } else {
            String error = "the '" + userOptionName + "' option is required, for example, --" + userOptionName
                    + "=myuser@mydomain.com";
            logger.error(error);
            throw new GpxProcessorException(error);
        }
    }
}
