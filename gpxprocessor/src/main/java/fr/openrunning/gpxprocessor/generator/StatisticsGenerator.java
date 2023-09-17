package fr.openrunning.gpxprocessor.generator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.generator.statistics.RecordStatistic;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import lombok.Getter;

@Component
public class StatisticsGenerator {
    private final Logger logger = LoggerFactory.getLogger(StatisticsGenerator.class);
    private List<StatisticModuleName> enabledModules = new LinkedList<>();
    @Getter
    private List<Statistics> statistics = new ArrayList<>();

    public void enabledStatisticModule(StatisticModuleName moduleName) {
        enabledModules.add(moduleName);
    }

    public void generateStatistics(GpxTrack track) {
        enabledModules.forEach(module -> {
            String error = "error while generating statistic '" + module + "'";
            try {
                switch (module) {
                    case PERIODIC:
                        break;
                    case RECORD:
                        RecordStatistic recordStats = new RecordStatistic(2000);
                        recordStats.compute(track);
                        statistics.add(recordStats);
                        break;
                    default:
                        logger.error(error + ": module is not configured");
                }
            } catch (Exception e) {
                logger.error(error, e);
            }
        });
    }
}
