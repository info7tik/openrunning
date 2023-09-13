package fr.openrunning.gpxprocessor.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.openrunning.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByEmail(String email);
}
