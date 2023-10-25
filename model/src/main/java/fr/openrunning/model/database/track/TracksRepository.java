package fr.openrunning.model.database.track;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.openrunning.model.database.TimestampUserPrimaryKey;

@Repository
public interface TracksRepository extends CrudRepository<Track, TimestampUserPrimaryKey> {
    @Query(value = "SELECT t FROM Track t WHERE t.userId = ?1 AND t.timestamp >= ?2")
    List<Track> findLastRuns(int userId, long startTimestamp, Pageable pageable);
}
