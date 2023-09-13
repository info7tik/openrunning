package fr.openrunning.gpxprocessor.generator;

import fr.openrunning.gpxprocessor.track.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StatisticResultValue implements StatisticResult {
    @Getter
    private int distanceInMeters;
    @Getter
    private long timeInSeconds;
    @Getter
    private int firstGpxPointIndex;
    @Getter
    private int lastGpxPointIndex;

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Distance: " + Utils.formatDistanceInMeters(distanceInMeters) + "\n");
        builder.append("Time: " + Utils.formatTimeInSeconds(timeInSeconds) + "\n");
        builder.append("Start point at: " + firstGpxPointIndex + "\n");
        builder.append("End point at: " + lastGpxPointIndex);
        return builder.toString();
    }
}
