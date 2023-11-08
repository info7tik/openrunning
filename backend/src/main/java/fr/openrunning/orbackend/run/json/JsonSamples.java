package fr.openrunning.orbackend.run.json;

import java.util.ArrayList;
import java.util.List;

import fr.openrunning.model.type.DistanceUnit;
import lombok.Getter;
import lombok.Setter;

public class JsonSamples {
    private long currentTimestamp;
    @Getter
    @Setter
    private DistanceUnit distanceUnit;
    @Getter
    private List<Long> timestamps = new ArrayList<>();
    @Getter
    private List<Integer> distances = new ArrayList<>();
    @Getter
    private List<Integer> times = new ArrayList<>();
    @Getter
    private List<Integer> paces = new ArrayList<>();

    public JsonSamples(long beginningTimestamp) {
        this.currentTimestamp = beginningTimestamp;
    }

    public void addSample(int distanceInMeters, int timeInSeconds) {
        timestamps.add(currentTimestamp);
        distances.add(distanceInMeters);
        times.add(timeInSeconds);
        paces.add(timeInSeconds * 1000 / distanceInMeters);
        currentTimestamp += timeInSeconds;
    }
}
