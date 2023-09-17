package fr.openrunning.gpxprocessor.database;

import org.springframework.data.repository.CrudRepository;

import fr.openrunning.model.Record;
import fr.openrunning.model.TimestampUserPrimaryKey;

public interface RecordsRepository extends CrudRepository<Record, TimestampUserPrimaryKey> {
}
