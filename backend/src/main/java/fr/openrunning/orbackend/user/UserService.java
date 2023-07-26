package fr.openrunning.orbackend.user;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.user.json.JsonLoginInformation;
import fr.openrunning.orbackend.user.model.User;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final int PASSWORD_STRENGTH = 10;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(PASSWORD_STRENGTH, new SecureRandom());
    }

    public void signup(JsonLoginInformation loginInformation) throws OpenRunningException {
        User existingUser = repository.findByEmail(loginInformation.getEmail());
        if (existingUser == null) {
            User newUser = new User();
            newUser.setEmail(loginInformation.getEmail());
            String encodedPassword = passwordEncoder.encode(loginInformation.getPassword());
            newUser.setPassword(encodedPassword);
            repository.save(newUser);
        } else {
            String errorMessage = "signup failure: email '" + loginInformation.getEmail() + "' already exists";
            logger.error(errorMessage);
            throw new OpenRunningException(errorMessage);
        }
    }

    public void signin(JsonLoginInformation loginInformation) throws OpenRunningException {
        User existingUser = repository.findByEmail(loginInformation.getEmail());
        if (existingUser == null) {
            String errorMessage = "signin failure: email '" + loginInformation.getEmail() + "' does not exist";
            logger.error(errorMessage);
            throw new OpenRunningException(errorMessage);
        } else {
            String encodedPassword = passwordEncoder.encode(loginInformation.getPassword());
            logger.info("pwd: " + encodedPassword);
            if(!passwordEncoder.matches(loginInformation.getPassword(), encodedPassword)){
                throw new OpenRunningException("wrong password or email");
            }
        }
    }
}
