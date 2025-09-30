package fr.openrunning.gpxprocessor.track;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.openrunning.model.database.track.Track;
import lombok.Getter;
import lombok.Setter;

public class GpxTrack {
    private final int SAMPLE_SIZE_METERS = 50;
    @Getter
    @Setter
    private String name = "anonymous";
    @Getter
    @Setter
    private String filename = "unknown";
    @Getter
    private int distanceInMeters = 0;
    @Getter
    private int timeInSeconds = 0;
    @Getter
    private final List<GpxPoint> gpxPoints = new ArrayList<>();
    @Getter
    private final List<GpxSample> samples = new LinkedList<>();
    @Getter
    private GpxSample bestSample;

    public void addGpxPoint(GpxPoint point) {
        gpxPoints.add(point);
    }

    public void computeTotalDistanceAndTime() {
        distanceInMeters = 0;
        timeInSeconds = 0;
        for (int pointIndex = 1; pointIndex < gpxPoints.size(); pointIndex++) {
            GpxPoint l1 = gpxPoints.get(pointIndex - 1);
            GpxPoint l2 = gpxPoints.get(pointIndex);
            distanceInMeters += Utils.distanceInMeters(l2, l1);
            timeInSeconds += Duration.between(l1.getTime(), l2.getTime()).getSeconds();
        }
    }

    public void computeSamples() {
        int distance = 0;
        long time = 0;
        int number = 0;
        double bestSampleSpeed = 0.0;
        for (int pointIndex = 1; pointIndex < gpxPoints.size(); pointIndex++) {
            distance += Utils.distanceInMeters(gpxPoints.get(pointIndex - 1), gpxPoints.get(pointIndex));
            time += Duration.between(
                    gpxPoints.get(pointIndex - 1).getTime(), gpxPoints.get(pointIndex).getTime()).getSeconds();
            number++;
            if (distance >= SAMPLE_SIZE_METERS) {
                GpxSample sample = new GpxSample(getFirstTime(), distance, (int) time, number);
                samples.add(sample);
                if (sample.computeSpeed() > bestSampleSpeed) {
                    bestSample = sample;
                    bestSampleSpeed = sample.computeSpeed();
                }
                distance = 0;
                time = 0;
                number = 0;
            }
        }
        if (distance > 0) {
            samples.add(new GpxSample(getFirstTime(), distance, (int) time, number));
        }
    }

    public long getFirstTime() {
        return gpxPoints.get(0).getTime().getEpochSecond();
    }

    public int getBestSampleIndex() {
        return samples.indexOf(bestSample);
    }

    public String buildTrackInformation() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: " + name + "\n");
        builder.append("Date: " + gpxPoints.get(0).getTime() + "\n");
        builder.append("Total Distance: " + Utils.formatDistanceInMeters(distanceInMeters) + "\n");
        builder.append("Total Time: " + Utils.formatTimeInSeconds(timeInSeconds) + "\n");
        builder.append("Pace: " + buildPaceInformation() + "\n");
        return builder.toString();
    }

    public String buildPaceInformation() {
        float paceSeconds = timeInSeconds / ((float) distanceInMeters / 1000);
        return Utils.formatTimeInSeconds((long) paceSeconds);
    }

    public String buildSampleInformation() {
        StringBuilder builder = new StringBuilder();
        int totalDistance = 0;
        for (GpxSample s : samples) {
            totalDistance += s.getDistanceInMeters();
            builder.append(Utils.formatDistanceInMeters(totalDistance) + ": " + s.toString() + "\n");
        }
        return builder.toString();
    }

    public Track toDatabaseObject(int userId) {
        Track newTrack = new Track();
        newTrack.setUserId(userId);
        newTrack.setTimestamp(getFirstTime());
        newTrack.setDistanceInMeters(distanceInMeters);
        newTrack.setTimeInSeconds(timeInSeconds);
        newTrack.setName(name);
        newTrack.setFilename(filename);
        return newTrack;
    }

    @Override
    public String toString() {
        if (gpxPoints.isEmpty()) {
            return "[" + name + "][0]";
        } else {
            return "[" + name + "@" + gpxPoints.get(0).getTime() + "][" + gpxPoints.size() + "]";
        }
    }
}
