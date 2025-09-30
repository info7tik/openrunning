package fr.openrunning.gpxprocessor.statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.statistics.modules.FrequencyStatistic;
import fr.openrunning.gpxprocessor.statistics.modules.RecordStatistic;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.database.DatabaseObject;
import fr.openrunning.model.exception.OpenRunningException;
import lombok.Getter;

@Component
public class StatisticModuleManager {
    private final Logger logger = LoggerFactory.getLogger(StatisticModuleManager.class);
    private List<StatisticModuleName> enabledModules = new LinkedList<>();
    @Getter
    private List<Integer> recordDistancesInMeters = Arrays.asList(1000, 5000, 10000, 15000, 21100, 42195);
    @Getter
    private List<StatisticModule<? extends DatabaseObject>> statistics = new ArrayList<>();

    public void enableStatisticModule(StatisticModuleName moduleName) {
        enabledModules.add(moduleName);
    }

    public boolean isEnabledStatisticModule(StatisticModuleName moduleName) {
        return enabledModules.contains(moduleName);
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
                        recordDistancesInMeters.forEach((target) -> statisticModules.add(new RecordStatistic(target)));
                        break;
                    default:
                        throw new OpenRunningException("the module " + module + "is not loaded or does not exist");
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
