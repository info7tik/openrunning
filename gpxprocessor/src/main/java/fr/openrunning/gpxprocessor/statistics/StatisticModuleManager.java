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
                StatisticModule<? extends DatabaseObject> statisticModule = null;
                switch (module) {
                    case FREQUENCY:
                        statisticModule = new FrequencyStatistic();
                        break;
                    case RECORD:
                        statisticModule = new RecordStatistic(2000);
                        break;
                    default:
                        throw new GpxProcessorException("the module " + module + "is not loaded or does not exist");
                }
                statisticModule.compute(track);
                statistics.add(statisticModule);
            } catch (Exception e) {
                logger.error(error, e);
            }
        });
    }
}
