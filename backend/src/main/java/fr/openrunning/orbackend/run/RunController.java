package fr.openrunning.orbackend.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.openrunning.model.type.FrequencyType;
import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.common.json.JsonMessage;
import fr.openrunning.orbackend.common.json.JsonResponse;
import fr.openrunning.orbackend.user.UserService;

@Controller
@RequestMapping("/run")
public class RunController {
    private final Logger logger = LoggerFactory.getLogger(RunController.class);
    private final UserService userService;
    private final RunService runService;

    @Autowired
    public RunController(UserService userService, RunService runService) {
        this.userService = userService;
        this.runService = runService;
    }

    @GetMapping("/last/{frequency}/{startTime}")
    public ResponseEntity<Object> getLastRuns(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String headerWithToken,
            @PathVariable String frequency,
            @PathVariable long startTime) {
        try {
            int userId = userService.getUserId(extractToken(headerWithToken));
            JsonResponse response = new JsonResponse(runService.getLastRuns(userId, FrequencyType.DAILY, startTime));
            return response.buildSuccessResponse();
        } catch (OpenRunningException e) {
            String errorMessage = "error while retrieving last runs";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }

    @GetMapping("/sample/{timestamp}")
    public ResponseEntity<Object> getRunSamples(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String headerWithToken,
            @PathVariable long timestamp) {
        try {
            int userId = userService.getUserId(extractToken(headerWithToken));
            JsonResponse response = new JsonResponse(runService.getRunSamples(userId, timestamp));
            return response.buildSuccessResponse();
        } catch (OpenRunningException e) {
            String errorMessage = "error while retrieving all runs";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }

    @GetMapping("/records")
    public ResponseEntity<Object> getPersonalRecords(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String headerWithToken) {
        try {
            int userId = userService.getUserId(extractToken(headerWithToken));
            JsonResponse response = new JsonResponse(runService.getPersonalRecords(userId));
            return response.buildSuccessResponse();
        } catch (OpenRunningException e) {
            String errorMessage = "error while retrieving personal records";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }

    @GetMapping("/records/{timestamp}")
    public ResponseEntity<Object> getTrackRecords(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String headerWithToken,
            @PathVariable long timestamp) {
        int userId = -1;
        try {
            userId = userService.getUserId(extractToken(headerWithToken));
            JsonResponse response = new JsonResponse(runService.getTrackRecords(userId, timestamp));
            return response.buildSuccessResponse();
        } catch (OpenRunningException e) {
            String errorMessage = "error while retrieving track records from <" + userId + "," + timestamp + ">";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }

    private String extractToken(String authorizationHeader) throws OpenRunningException {
        String[] information = authorizationHeader.split(" ");
        if (information.length == 2) {
            return information[1];
        } else {
            throw new OpenRunningException("error while extracting token");
        }
    }
}
