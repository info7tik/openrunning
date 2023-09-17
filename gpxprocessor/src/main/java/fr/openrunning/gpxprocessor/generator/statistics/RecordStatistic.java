package fr.openrunning.gpxprocessor.generator.statistics;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openrunning.gpxprocessor.exception.GpxProcessorException;
import fr.openrunning.gpxprocessor.generator.StatisticModuleName;
import fr.openrunning.gpxprocessor.generator.Statistics;
import fr.openrunning.gpxprocessor.track.GpxPoint;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.gpxprocessor.track.Utils;
import lombok.Getter;

public class RecordStatistic extends Statistics {
    private final Logger logger = LoggerFactory.getLogger(RecordStatistic.class);
    private final int IMPROVE_PRECISION_MULTIPLIER = 1000000;
    @Getter
    private List<GpxPoint> gpxPoints = new ArrayList<>();
    private int startPointIndex = 0;
    private int endPointIndex = 0;
    private int distanceInMeters = 0;
    private long timeInSeconds = 0;
    @Getter
    private int bestStartIndex = 0;
    @Getter
    private int bestEndIndex = 0;
    @Getter
    private long firstTimeInSeconds = 0;
    @Getter
    private final int targetInMeters;
    @Getter
    private int bestDistanceInMeters = 0;
    @Getter
    private long bestTimeInSeconds = 0;

    public RecordStatistic(int targetInMeters) {
        super(StatisticModuleName.RECORD);
        this.targetInMeters = targetInMeters;
    }

    @Override
    public void compute(GpxTrack track) throws GpxProcessorException {
        if (!gpxPoints.isEmpty()) {
            logger.error("error while computing data with " + getModuleName()
                    + ": the statistic can be computed only one time");
            throw new GpxProcessorException("statistics can only be computed one time");
        }
        firstTimeInSeconds = track.getFirstTime();
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
    public boolean isAvailable() {
        return bestDistanceInMeters >= targetInMeters;
    }
}