package fr.openrunning.model.database.track;

import fr.openrunning.model.database.DatabaseObject;
import fr.openrunning.model.database.TimestampUserPrimaryKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(TimestampUserPrimaryKey.class)
@Table(name = "tracks")
public class Track extends DatabaseObject {
    @Id
    @Setter
    @Getter
    private long timestamp;
    @Id
    @Column(name = "user_id")
    @Setter
    @Getter
    private int userId;
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
    private String name;
    @Setter
    @Getter
    private String filename;
}
