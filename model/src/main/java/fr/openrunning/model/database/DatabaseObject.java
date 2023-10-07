package fr.openrunning.model.database;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class DatabaseObject {
    @Id
    @Setter
    @Getter
    protected long timestamp;
    @Id
    @Column(name = "user_id")
    @Setter
    @Getter
    protected int userId;
}
