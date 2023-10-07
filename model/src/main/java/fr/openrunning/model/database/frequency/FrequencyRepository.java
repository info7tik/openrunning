package fr.openrunning.model.database.frequency;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.openrunning.model.database.TimestampUserPrimaryKey;

@Repository
public interface FrequencyRepository extends CrudRepository<Frequency, TimestampUserPrimaryKey> {
}
