package fr.openrunning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TrackId {
    @Getter
    private long timestamp;
    @Getter
    private int userId;
}
