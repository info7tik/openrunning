package fr.openrunning.model.database.track;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.openrunning.model.database.TimestampUserPrimaryKey;

@Repository
public interface TracksRepository extends CrudRepository<Track, TimestampUserPrimaryKey> {
}
