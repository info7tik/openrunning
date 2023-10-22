package fr.openrunning.orbackend.run.json;

import lombok.Getter;
import lombok.Setter;

public class JsonRun {
    @Getter
    @Setter
    private long timestampInSeconds;
    @Getter
    @Setter
    private long distanceMeters;
    @Getter
    @Setter
    private long timeSeconds;
    @Getter
    @Setter
    private long paceSeconds;
}
