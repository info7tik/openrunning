package fr.openrunning.orbackend.run.json;

import java.util.List;

public class JsonTimestamp {
    private final long noTimestamp = 0L;
    private final long data;

    public JsonTimestamp(long timestamp) {
        this.data = timestamp;
    }

    public JsonTimestamp() {
        this.data = noTimestamp;
    }

    public List<Long> getData() {
        if (data == noTimestamp) {
            return List.of();
        } else {
            return List.of(data);
        }
    }
}
