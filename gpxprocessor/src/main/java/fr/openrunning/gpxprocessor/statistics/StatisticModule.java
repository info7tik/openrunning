package fr.openrunning.gpxprocessor.statistics;

import java.util.List;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.database.DatabaseObject;
import lombok.Getter;

public abstract class StatisticModule<T extends DatabaseObject> {
    @Getter
    private final StatisticModuleName moduleName;

    public StatisticModule(StatisticModuleName moduleName) {
        this.moduleName = moduleName;
    }

    abstract public void compute(GpxTrack track) throws GpxProcessorException;

    abstract public List<T> toDatabaseObject(int userId);
}
