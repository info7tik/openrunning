package fr.openrunning.model.database.frequency;

import fr.openrunning.model.database.DatabaseObject;
import fr.openrunning.model.database.TimestampUserFrequencyPrimaryKey;
import fr.openrunning.model.type.FrequencyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(TimestampUserFrequencyPrimaryKey.class)
@Table(name = "frequencies")
public class Frequency extends DatabaseObject {
    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private FrequencyType frequency;
    @Getter
    @Setter
    @Column(name = "distance_m")
    private int totalDistanceInMeters;
    @Getter
    @Setter
    @Column(name = "time_s")
    private int totalTimeInSeconds;

    public Frequency aggregate(Frequency frequency) {
        Frequency aggregated = new Frequency();
        aggregated.setTimestamp(timestamp);
        aggregated.setUserId(userId);
        aggregated.setFrequency(frequency.getFrequency());
        aggregated.setTotalTimeInSeconds(totalTimeInSeconds + frequency.totalTimeInSeconds);
        aggregated.setTotalDistanceInMeters(totalDistanceInMeters + frequency.totalDistanceInMeters);
        return aggregated;
    }
}
