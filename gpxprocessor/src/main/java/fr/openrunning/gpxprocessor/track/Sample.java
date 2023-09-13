package fr.openrunning.gpxprocessor.track;

import lombok.Getter;

public class Sample {
    private final int SPEED_ACCURACY_FACTOR = 1000;
    @Getter
    private final int distanceInMeters;
    @Getter
    private final int timeInSeconds;
    private final int numberOfGPXPoints;

    public Sample(int distance, int time, int numberOfPoints) {
        distanceInMeters = distance;
        timeInSeconds = time;
        numberOfGPXPoints = numberOfPoints;
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
}
