package fr.openrunning.model.database;

import fr.openrunning.model.type.FrequencyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TimestampUserFrequencyPrimaryKey extends TimestampUserPrimaryKey {
    public TimestampUserFrequencyPrimaryKey(long timestamp, int userId, FrequencyType frequencyType) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.frequency = frequencyType;
    }

    @Getter
    @Enumerated(EnumType.STRING)
    private FrequencyType frequency;
}
