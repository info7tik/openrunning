package fr.openrunning.model.database.gpxfiles;

import fr.openrunning.model.type.FileStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gpxfiles")
public class GpxFile {
    @Id
    @Getter
    @Setter
    private String filename;
    @Getter
    @Setter
    @Column(name = "user_id")
    private int usedId;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private FileStatus status;
}
