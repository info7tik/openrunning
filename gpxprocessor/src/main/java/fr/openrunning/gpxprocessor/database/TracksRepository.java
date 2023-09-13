package fr.openrunning.gpxprocessor.database;

import org.springframework.data.repository.CrudRepository;

import fr.openrunning.model.Track;
import fr.openrunning.model.TrackId;

public interface TracksRepository extends CrudRepository<Track, TrackId> {
}
