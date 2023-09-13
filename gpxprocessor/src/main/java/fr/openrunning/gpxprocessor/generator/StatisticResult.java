package fr.openrunning.gpxprocessor.generator;

public interface StatisticResult {
    boolean hasValue();

    int getDistanceInMeters();

    long getTimeInSeconds();

    int getFirstGpxPointIndex();

    int getLastGpxPointIndex();
}
