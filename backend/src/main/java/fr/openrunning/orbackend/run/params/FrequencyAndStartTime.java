package fr.openrunning.orbackend.run.params;

import fr.openrunning.model.type.FrequencyType;
import lombok.Getter;
import lombok.Setter;

public class FrequencyAndStartTime {
    @Getter
    @Setter
    private FrequencyType frequency;
    @Getter
    @Setter
    private long startTime;
}
