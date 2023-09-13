package fr.openrunning.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(TrackId.class)
@Table(name = "tracks")
public class Track {
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
    private long timeInSeconds;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String filename;
}
