package fr.openrunning.gpxprocessor.generator;

public class StatisticNoResult implements StatisticResult {

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public int getDistanceInMeters() {
        return -1;
    }

    @Override
    public long getTimeInSeconds() {
        return -1L;
    }

    @Override
    public int getFirstGpxPointIndex() {
        return -1;
    }

    @Override
    public int getLastGpxPointIndex() {
        return -1;
    }

    @Override
    public String toString() {
        return "N/A";
    }
}
