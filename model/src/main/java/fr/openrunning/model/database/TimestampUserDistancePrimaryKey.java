package fr.openrunning.model.database;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class TimestampUserDistancePrimaryKey extends TimestampUserPrimaryKey{
    @Setter
    @Getter
    @Column(name = "distance_m")
    private int distanceInMeters;
}
