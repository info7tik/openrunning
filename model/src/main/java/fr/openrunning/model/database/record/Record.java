package fr.openrunning.model.database.record;

import fr.openrunning.model.database.DatabaseObject;
import fr.openrunning.model.database.TimestampUserDistancePrimaryKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(TimestampUserDistancePrimaryKey.class)
@Table(name = "records")
public class Record extends DatabaseObject {
    @Setter
    @Getter
    @Column(name = "distance_m")
    private int distanceInMeters;
    @Setter
    @Getter
    @Column(name = "time_s")
    private long timeInSeconds;
    @Setter
    @Getter
    @Column(name = "first_point_index")
    private int firstPointIndex;
    @Setter
    @Getter
    @Column(name = "last_point_index")
    private int lastPointIndex;
}
