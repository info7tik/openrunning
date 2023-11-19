package fr.openrunning.model.database.record;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.openrunning.model.database.TimestampUserPrimaryKey;

@Repository
public interface RecordsRepository extends CrudRepository<Record, TimestampUserPrimaryKey> {
    @Query("SELECT DISTINCT r.distanceInMeters FROM Record r")
    List<Integer> getRecordDistances();

    Optional<Record> findFirstByUserIdAndDistanceInMetersOrderByTimeInSecondsAsc(int userId, int distanceInMeters);

    default Optional<Record> getBestRecordFromDistanceInMeters(int userId, int distanceInMeters) {
        return this.findFirstByUserIdAndDistanceInMetersOrderByTimeInSecondsAsc(userId, distanceInMeters);
    }

    List<Record> findByUserIdAndTimestamp(int userId, long timestamp);

    default List<Record> getTrackRecords(int userId, long timestamp) {
        return this.findByUserIdAndTimestamp(userId, timestamp);
    }
}
