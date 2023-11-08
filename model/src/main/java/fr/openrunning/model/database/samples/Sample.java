package fr.openrunning.model.database.samples;

import fr.openrunning.model.database.DatabaseObject;
import fr.openrunning.model.database.TimestampUserIndexPrimaryKey;
import fr.openrunning.model.type.DistanceUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(TimestampUserIndexPrimaryKey.class)
@Table(name = "samples")
public class Sample extends DatabaseObject {
    @Setter
    @Getter
    @Column(name = "number")
    private int sampleNumber;
    @Setter
    @Getter
    @Column(name = "distance_m")
    private int distanceInMeters;
    @Setter
    @Getter
    @Column(name = "time_s")
    private int timeInSeconds;
    @Setter
    @Getter
    @Column(name = "nb_points")
    private int numberOfPoints;
    @Setter
    @Getter
    @Column(name = "unit")
    private DistanceUnit unit;
}
