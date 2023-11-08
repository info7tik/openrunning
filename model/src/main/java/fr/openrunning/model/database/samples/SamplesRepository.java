package fr.openrunning.model.database.samples;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.openrunning.model.database.TimestampUserPrimaryKey;

public interface SamplesRepository extends CrudRepository<Sample, TimestampUserPrimaryKey> {
    List<Sample> findByUserIdAndTimestamp(int userId, long timestamp);
}
