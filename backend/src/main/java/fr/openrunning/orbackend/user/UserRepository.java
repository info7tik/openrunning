package fr.openrunning.orbackend.user;

import org.springframework.data.repository.CrudRepository;

import fr.openrunning.orbackend.user.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

}