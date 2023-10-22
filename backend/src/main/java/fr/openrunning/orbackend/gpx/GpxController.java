package fr.openrunning.orbackend.gpx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fr.openrunning.orbackend.common.exception.OpenRunningException;
import fr.openrunning.orbackend.common.json.JsonMessage;
import fr.openrunning.orbackend.common.json.JsonResponse;
import fr.openrunning.orbackend.gpx.json.JsonUpload;

@Controller
@RequestMapping("/gpx")
public class GpxController {
    private final Logger logger = LoggerFactory.getLogger(GpxController.class);
    private final GpxService service;

    @Autowired
    public GpxController(GpxService gpxService) {
        this.service = gpxService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> handleFileUpload(
            @RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
        try {
            service.store(file, email);
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
