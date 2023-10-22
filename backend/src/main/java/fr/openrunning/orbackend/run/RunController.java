package fr.openrunning.orbackend.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.common.json.JsonMessage;
import fr.openrunning.orbackend.common.json.JsonResponse;

@Controller
@RequestMapping("/run")
public class RunController {
    private final Logger logger = LoggerFactory.getLogger(RunController.class);
    private final RunService service;

    @Autowired
    public RunController(RunService runService) {
        this.service = runService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRunById(String runIdentifier) {
        try {
            JsonResponse response = new JsonResponse(service.getAllRuns());
            return response.buildSuccessResponse();
        } catch (OpenRunningException e) {
            String errorMessage = "error while retrieving all runs";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }
}
