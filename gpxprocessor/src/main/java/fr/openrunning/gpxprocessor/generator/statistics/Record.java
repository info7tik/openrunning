package fr.openrunning.gpxprocessor.generator.statistics;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import fr.openrunning.gpxprocessor.generator.StatisticNoResult;
import fr.openrunning.gpxprocessor.generator.StatisticResult;
import fr.openrunning.gpxprocessor.generator.StatisticResultValue;
import fr.openrunning.gpxprocessor.generator.Statistics;
import fr.openrunning.gpxprocessor.track.GpxPoint;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.gpxprocessor.track.Utils;
import lombok.Getter;

public class Record implements Statistics {
    private final int IMPROVE_PRECISION_MULTIPLIER = 1000000;
    private final int targetInMeters;
    private List<GpxPoint> gpxPoints = new ArrayList<>();
    private int startPointIndex = 0;
    private int endPointIndex = 0;
    private int distanceInMeters = 0;
    private long timeInSeconds = 0;
    private int bestStartIndex = 0;
    private int bestEndIndex = 0;
    @Getter
    private int bestDistanceInMeters = 0;
    @Getter
    private long bestTimeInSeconds = 0;

    public Record(int targetInMeters) {
        this.targetInMeters = targetInMeters;
    }

    @Override
    public StatisticResult getResult() {
        if (bestDistanceInMeters < targetInMeters) {
            return new StatisticNoResult();
        } else {
            long bestTimeForTarget = bestTimeInSeconds * targetInMeters / bestDistanceInMeters;
            return new StatisticResultValue(targetInMeters, bestTimeForTarget, bestStartIndex, bestEndIndex);
        }
    }

    public void initialize() {
        gpxPoints.clear();
        startPointIndex = 0;
        endPointIndex = 0;
        distanceInMeters = 0;
        timeInSeconds = 0;
        bestStartIndex = 0;
        bestEndIndex = 0;
        bestDistanceInMeters = 0;
        bestTimeInSeconds = 0;
    }

    @Override
    public void compute(GpxTrack track) {
        gpxPoints.addAll(track.getGpxPoints());
        for (endPointIndex = 1; endPointIndex < gpxPoints.size();) {
            if (distanceInMeters < targetInMeters) {
                addLastPoint();
            } else {
                while (distanceInMeters >= targetInMeters) {
                    removeFirstPoint();
                }
                addLastPoint();
            }
            if (distanceInMeters >= targetInMeters) {
                if (bestTimeInSeconds == 0) {
                    saveRecord();
                } else if (currentSpeedIsBestSpeed()) {
                    saveRecord();
                }
            }
        }
    }

    private void removeFirstPoint() {
        GpxPoint origin = gpxPoints.get(startPointIndex);
        GpxPoint destination = gpxPoints.get(startPointIndex + 1);
        distanceInMeters -= Utils.distanceInMeters(origin, destination);
        timeInSeconds -= Duration.between(origin.getTime(), destination.getTime()).getSeconds();
        startPointIndex++;
    }

    private void addLastPoint() {
        GpxPoint origin = gpxPoints.get(endPointIndex - 1);
        GpxPoint destination = gpxPoints.get(endPointIndex);
        distanceInMeters += Utils.distanceInMeters(origin, destination);
        timeInSeconds += Duration.between(origin.getTime(), destination.getTime()).getSeconds();
        endPointIndex++;
    }

    private void saveRecord() {
        bestTimeInSeconds = timeInSeconds;
        bestDistanceInMeters = distanceInMeters;
        bestStartIndex = startPointIndex;
        bestEndIndex = endPointIndex;
    }

    private boolean currentSpeedIsBestSpeed() {
        long bestSpeed = bestDistanceInMeters * IMPROVE_PRECISION_MULTIPLIER / bestTimeInSeconds;
        long currentSpeed = distanceInMeters * IMPROVE_PRECISION_MULTIPLIER / timeInSeconds;
        return bestSpeed < currentSpeed;
    }

    @Override
    public String toString() {
        return getResult().toString();
    }
}