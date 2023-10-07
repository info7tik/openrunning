package fr.openrunning.model.database.frequency;

import fr.openrunning.model.database.DatabaseObject;
import fr.openrunning.model.database.TimestampUserPrimaryKey;
import fr.openrunning.model.type.FrequencyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(TimestampUserPrimaryKey.class)
@Table(name = "frequencies")
public class Frequency extends DatabaseObject {
    @Setter
    @Getter
    private FrequencyType frequency;
    @Getter
    @Setter
    @Column(name = "distance_m")
    private int totalDistanceInMeters;
    @Getter
    @Setter
    @Column(name = "time_s")
    private long totalTimeInSeconds;

    public void aggregate(Frequency frequency) {
        totalDistanceInMeters += frequency.totalDistanceInMeters;
        totalTimeInSeconds += frequency.totalTimeInSeconds;
    }
}
