package fr.openrunning.gpxprocessor.statistics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.statistics.modules.FrequencyStatistic;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.database.DatabaseObject;
import lombok.Getter;

@Component
public class StatisticModuleManager {
    private final Logger logger = LoggerFactory.getLogger(StatisticModuleManager.class);
    private List<StatisticModuleName> enabledModules = new LinkedList<>();
    @Getter
    private List<StatisticModule<? extends DatabaseObject>> statistics = new ArrayList<>();

    public void enabledStatisticModule(StatisticModuleName moduleName) {
        enabledModules.add(moduleName);
    }

    public void generateStatistics(GpxTrack track) {
        enabledModules.forEach(module -> {
            String error = "error while generating statistic '" + module + "'";
            try {
                List<StatisticModule<? extends DatabaseObject>> statisticModules = new ArrayList<>();
                switch (module) {
                    case FREQUENCY:
                        statisticModules.add(new FrequencyStatistic());
                        break;
                    case RECORD:
                        statisticModules.add(new RecordStatistic(1000));
                        statisticModules.add(new RecordStatistic(5000));
                        statisticModules.add(new RecordStatistic(10000));
                        statisticModules.add(new RecordStatistic(15000));
                        statisticModules.add(new RecordStatistic(21000));
                        statisticModules.add(new RecordStatistic(42000));
                        break;
                    default:
                        throw new GpxProcessorException("the module " + module + "is not loaded or does not exist");
                }
                statisticModules.forEach((statsModule) -> {
                    try {
                        statsModule.compute(track);
                        statistics.add(statsModule);
                    } catch (Exception e) {
                        logger.error(error, e);
                    }
                });
            } catch (Exception e) {
                logger.error(error, e);
            }
        });
    }
}
