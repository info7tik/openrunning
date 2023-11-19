package fr.openrunning.orbackend.run.json;

import lombok.Getter;
import lombok.Setter;

public class JsonRecord {
    @Getter
    @Setter
    private long timestamp;
    @Getter
    @Setter
    private int distance;
    @Getter
    @Setter
    private long time;
    @Getter
    @Setter
    private int firstSampleIndex;
    @Getter
    @Setter
    private int lastSampleIndex;
}
