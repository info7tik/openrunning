package fr.openrunning.gpxprocessor.generator;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import lombok.Getter;

public abstract class Statistics {
    @Getter
    private final StatisticModuleName moduleName;

    public Statistics(StatisticModuleName moduleName) {
        this.moduleName = moduleName;
    }

    abstract public void compute(GpxTrack track) throws GpxProcessorException;

    abstract public boolean isAvailable();
}
