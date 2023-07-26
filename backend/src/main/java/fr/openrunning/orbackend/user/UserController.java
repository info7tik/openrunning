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

import fr.openrunning.orbackend.common.ServerConfiguration;
import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.common.json.JsonMessage;
import fr.openrunning.orbackend.common.json.JsonResponse;
import fr.openrunning.orbackend.user.json.JsonLoginInformation;

@Controller
@RequestMapping("user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService service;
    private final String frontendUrl;

    @Autowired
    public UserController(UserService userService, ServerConfiguration serverConfiguration) {
        this.service = userService;
        this.frontendUrl = serverConfiguration.getFrontendUrl();
    }

    @GetMapping("/test")
    public ResponseEntity<Object> testing() {
        JsonResponse response = new JsonResponse(frontendUrl, "That works!");
        return response.buildSuccessResponse();
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody JsonLoginInformation loginInformation) {
        try {
            service.signup(loginInformation);
            JsonResponse response = new JsonResponse(frontendUrl, new JsonMessage("successful user signup"));
            return response.buildSuccessResponse();
        } catch (OpenRunningException ore) {
            JsonResponse response = new JsonResponse(frontendUrl, ore);
            return response.buildBadRequestResponse();
        } catch (Exception e) {
            String errorMessage = "signup failure with email '" + loginInformation.getEmail() + "'";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(frontendUrl, new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody JsonLoginInformation loginInformation) {
        try {
            service.signin(loginInformation);
            JsonResponse response = new JsonResponse(frontendUrl, new JsonMessage("successful user signin"));
            return response.buildSuccessResponse();
        } catch (OpenRunningException ore) {
            JsonResponse response = new JsonResponse(frontendUrl, ore);
            return response.buildBadRequestResponse();
        } catch (Exception e) {
            String errorMessage = "signin failure with email '" + loginInformation.getEmail() + "'";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(frontendUrl, new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }
}
