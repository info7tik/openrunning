package fr.openrunning.gpxprocessor.database;

import org.springframework.data.repository.CrudRepository;

import fr.openrunning.model.TimestampUserPrimaryKey;
import fr.openrunning.model.Track;

public interface TracksRepository extends CrudRepository<Track, TimestampUserPrimaryKey> {
}
