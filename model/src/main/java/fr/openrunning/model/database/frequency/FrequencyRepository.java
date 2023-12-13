package fr.openrunning.model.database.frequency;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.openrunning.model.database.TimestampUserFrequencyPrimaryKey;

@Repository
public interface FrequencyRepository extends CrudRepository<Frequency, TimestampUserFrequencyPrimaryKey> {
}
