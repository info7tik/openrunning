package fr.openrunning.gpxprocessor.track;

import fr.openrunning.model.database.samples.Sample;
import fr.openrunning.model.type.DistanceUnit;
import lombok.Getter;

public class GpxSample {
    private final int SPEED_ACCURACY_FACTOR = 1000;
    @Getter
    private final int distanceInMeters;
    @Getter
    private final int timeInSeconds;
    private final long runTimestamp;
    private final int numberOfGPXPoints;

    public GpxSample(long fileTimestamp, int distance, int time, int numberOfPoints) {
        this.runTimestamp = fileTimestamp;
        this.distanceInMeters = distance;
        this.timeInSeconds = time;
        this.numberOfGPXPoints = numberOfPoints;
    }

    public double computeSpeed() {
        int speed = distanceInMeters * SPEED_ACCURACY_FACTOR / timeInSeconds;
        return (double) speed / 1000;
    }

    public int computePace() {
        return 1000 * timeInSeconds / distanceInMeters;
    }

    @Override
    public String toString() {
        return Utils.formatTimeInSeconds(computePace()) + " | " + distanceInMeters + "m | "
                + timeInSeconds + "s | " + numberOfGPXPoints + " points";
    }

    public Sample toDatabaseObject(int userId, int sampleIndex) {
        Sample sample = new Sample();
        sample.setUserId(userId);
        sample.setTimestamp(runTimestamp);
        sample.setSampleNumber(sampleIndex);
        sample.setTimeInSeconds(timeInSeconds);
        sample.setDistanceInMeters(distanceInMeters);
        sample.setNumberOfPoints(numberOfGPXPoints);
        sample.setUnit(DistanceUnit.METER);
        return sample;
    }
}
