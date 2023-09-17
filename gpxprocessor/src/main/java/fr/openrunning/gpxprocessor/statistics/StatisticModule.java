package fr.openrunning.gpxprocessor.statistics;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import lombok.Getter;

public abstract class StatisticModule {
    @Getter
    private final StatisticModuleName moduleName;

    public StatisticModule(StatisticModuleName moduleName) {
        this.moduleName = moduleName;
    }

    abstract public void compute(GpxTrack track) throws GpxProcessorException;

    abstract public boolean isAvailable();
}
