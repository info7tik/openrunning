package fr.openrunning.gpxprocessor.track;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class GpxPoint {
    @Getter
    private double latitude;
    @Getter
    private double longitude;
    @Getter
    private double elevation;
    @Getter
    private Instant time;

    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + "@" + time + ")";
    }

}
