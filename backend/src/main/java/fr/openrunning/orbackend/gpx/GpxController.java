package fr.openrunning.orbackend.gpx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fr.openrunning.model.exception.OpenRunningException;
import fr.openrunning.orbackend.common.json.JsonMessage;
import fr.openrunning.orbackend.common.json.JsonResponse;
import fr.openrunning.orbackend.gpx.json.JsonUpload;
import fr.openrunning.orbackend.user.UserService;

@Controller
@RequestMapping("/gpx")
public class GpxController {
    private final Logger logger = LoggerFactory.getLogger(GpxController.class);
    private final GpxService gpxService;
    private final UserService userService;

    @Autowired
    public GpxController(GpxService gpxService, UserService userService) {
        this.gpxService = gpxService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> handleFileUpload(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String headerWithToken,
            @RequestParam("file") MultipartFile file) {
        try {
            int userId = userService.getUserId(headerWithToken);
            String userEmail = userService.getUserEmail(userId);
            gpxService.store(file, userId, userEmail);
            JsonResponse response = new JsonResponse(new JsonUpload(file.getOriginalFilename()));
            return response.buildSuccessResponse();
        } catch (OpenRunningException e) {
            String errorMessage = "error while storing the file '" + file.getOriginalFilename() + "'";
            logger.error(errorMessage, e);
            JsonResponse response = new JsonResponse(new JsonMessage(errorMessage));
            return response.buildInternalErrorResponse();
        }
    }
}
