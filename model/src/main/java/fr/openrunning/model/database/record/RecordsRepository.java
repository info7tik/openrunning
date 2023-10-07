package fr.openrunning.model.database.record;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.openrunning.model.database.TimestampUserPrimaryKey;

@Repository
public interface RecordsRepository extends CrudRepository<Record, TimestampUserPrimaryKey> {
}
