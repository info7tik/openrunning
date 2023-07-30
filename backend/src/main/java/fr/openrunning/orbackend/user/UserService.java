package fr.openrunning.orbackend.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.user.json.JsonLoginInformation;
import fr.openrunning.orbackend.user.model.User;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final SecurityEncoder securityEncoder;
    private Set<String> tokens = new HashSet<>();

    public UserService(UserRepository userRepository, SecurityEncoder securityEncoder) {
        this.repository = userRepository;
        this.securityEncoder = securityEncoder;
    }

    public void signup(JsonLoginInformation loginInformation) throws OpenRunningException {
        User existingUser = repository.findByEmail(loginInformation.getEmail());
        if (existingUser == null) {
            User newUser = new User();
            newUser.setEmail(loginInformation.getEmail());
            String encodedPassword = securityEncoder.generateEncodedPassword(loginInformation.getPassword());
            newUser.setPassword(encodedPassword);
            repository.save(newUser);
        } else {
            String errorMessage = "signup failure: email '" + loginInformation.getEmail() + "' already exists";
            logger.error(errorMessage);
            throw new OpenRunningException(errorMessage);
        }
    }

    public String signin(JsonLoginInformation loginInformation) throws OpenRunningException {
        User existingUser = repository.findByEmail(loginInformation.getEmail());
        if (existingUser == null) {
            logger.error("signin failure: email '" + loginInformation.getEmail() + "' does not exist");
            throw new OpenRunningException("wrong password or email");
        } else {
            String encodedPassword = securityEncoder.generateEncodedPassword(loginInformation.getPassword());
            if (existingUser.getPassword().equals(encodedPassword)) {
                String token = securityEncoder.generateToken(loginInformation);
                tokens.add(token);
                return token;
            } else {
                throw new OpenRunningException("wrong password or email");
            }
        }
    }

    public void checkToken(String userToken) throws OpenRunningException {
        try {
            logger.info("all tokens: " + tokens);
            Optional<String> existingToken = tokens.stream()
                    .filter(token -> (token.equals(userToken))).findFirst();
            if (existingToken.isEmpty()) {
                logger.error("error while checking token: token '" + userToken + "' does not exist");
                throw securityEncoder.buildOpenRunningExceptionForInvalidToken();
            } else {
                securityEncoder.checkToken(userToken);
            }
        } catch (OpenRunningException pe) {
            throw pe;
        } catch (Exception e) {
            throw securityEncoder.buildOpenRunningExceptionForInvalidToken();
        }
    }

}
