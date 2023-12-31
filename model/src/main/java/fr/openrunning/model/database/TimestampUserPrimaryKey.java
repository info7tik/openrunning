package fr.openrunning.model.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TimestampUserPrimaryKey {
    @Getter
    protected long timestamp;
    @Getter
    protected int userId;
}
