package fr.openrunning.gpxprocessor.generator;

import fr.openrunning.gpxprocessor.track.GpxTrack;

public interface Statistics {
    void initialize();

    void compute(GpxTrack track);

    StatisticResult getResult();
}
