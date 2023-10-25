package fr.openrunning.orbackend.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openrunning.model.database.user.User;
import fr.openrunning.model.database.user.UserRepository;
import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.gpx.GpxService;
import fr.openrunning.orbackend.user.json.JsonLoginInformation;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final SecurityEncoder securityEncoder;
    private final GpxService storageService;
    //TODO: signout method
    private Map<String, Integer> tokens = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository, SecurityEncoder securityEncoder, GpxService storageService) {
        this.repository = userRepository;
        this.securityEncoder = securityEncoder;
        this.storageService = storageService;
    }

    public void signup(JsonLoginInformation loginInformation) throws OpenRunningException {
        List<User> existingUsers = repository.findByEmail(loginInformation.getEmail());
        if (existingUsers.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(loginInformation.getEmail());
            String encodedPassword = securityEncoder.generateEncodedPassword(loginInformation.getPassword());
            newUser.setPassword(encodedPassword);
            repository.save(newUser);
            storageService.createUserDirectory(loginInformation.getEmail());
            logger.info("New account for user '" + loginInformation.getEmail() + "'");
        } else {
            String errorMessage = "signup failure: email '" + loginInformation.getEmail() + "' already exists";
            logger.error(errorMessage);
            throw new OpenRunningException(errorMessage);
        }
    }

    public String signin(JsonLoginInformation loginInformation) throws OpenRunningException {
        List<User> existingUsers = repository.findByEmail(loginInformation.getEmail());
        if (existingUsers.isEmpty()) {
            logger.error("signin failure: email '" + loginInformation.getEmail() + "' does not exist");
            throw new OpenRunningException("wrong password or email");
        } else {
            String encodedPassword = securityEncoder.generateEncodedPassword(loginInformation.getPassword());
            User incommingUser = existingUsers.get(0);
            if (incommingUser.getPassword().equals(encodedPassword)) {
                logger.info("Successful signin with the user '" + loginInformation.getEmail() + "'");
                String token = securityEncoder.generateToken(incommingUser);
                tokens.put(token, incommingUser.getId());
                // TODO: Remove tokens from the expiration date
                return token;
            } else {
                throw new OpenRunningException("wrong password or email");
            }
        }
    }

    public void checkToken(String userToken) throws OpenRunningException {
        try {
            Optional<String> existingToken = tokens.keySet().stream()
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
            logger.error("error while checking token", e);
            throw securityEncoder.buildOpenRunningExceptionForInvalidToken();
        }
    }

    public int getUserId(String token) throws OpenRunningException{
        if(tokens.containsKey(token)){
        return tokens.get(token);
        } else {
            throw new OpenRunningException("error while getting user id with unknown token");
        }
    }
}
