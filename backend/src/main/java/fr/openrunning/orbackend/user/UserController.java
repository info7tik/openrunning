package fr.openrunning.orbackend.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.common.json.JsonMessage;
import fr.openrunning.orbackend.common.json.JsonResponse;
import fr.openrunning.orbackend.user.json.JsonApiToken;
import fr.openrunning.orbackend.user.json.JsonLoginInformation;

@Controller
@RequestMapping("user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService service;

    @Autowired
    public UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping("/test")
    public ResponseEntity<Object> testing() {
        JsonResponse response = new JsonResponse("That works!");
        return response.buildSuccessResponse();
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody JsonLoginInformation loginInformation) {
        try {
            service.signup(loginInformation);
            JsonResponse response = new JsonResponse(new JsonMessage("successful user signup"));
            return response.buildSuccessResponse();
        } catch (OpenRunningException ore) {
            JsonResponse response = new JsonResponse(ore);
            return response.buildBadRequestResponse();
        } catch (Exception e) {
            String errorMessage = "signup failure with email '" + loginInformation.getEmail() + "'";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody JsonLoginInformation loginInformation) {
        try {
            String token = service.signin(loginInformation);
            JsonApiToken jsonToken = new JsonApiToken();
            jsonToken.setToken(token);
            JsonResponse response = new JsonResponse(jsonToken);
            return response.buildSuccessResponse();
        } catch (OpenRunningException ore) {
            JsonResponse response = new JsonResponse(ore);
            return response.buildBadRequestResponse();
        } catch (Exception e) {
            String errorMessage = "signin failure with email '" + loginInformation.getEmail() + "'";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }
}
