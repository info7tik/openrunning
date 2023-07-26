package fr.openrunning.orbackend.common.json;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.openrunning.orbackend.common.exception.OpenRunningException;

public class JsonResponse {
    private final String frontendUrl;
    private final Object body;

    public JsonResponse(String frontendUrl, Object body) {
        this.frontendUrl = frontendUrl;
        this.body = body;
    }

    public JsonResponse(String frontendUrl, String message) {
        this.frontendUrl = frontendUrl;
        this.body = new JsonMessage(message);
    }

    public JsonResponse(String frontendUrl, OpenRunningException exception) {
        this(frontendUrl, exception.getMessage());
    }

    public ResponseEntity<Object> buildSuccessResponse() {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", frontendUrl).body(body);
    }

    public ResponseEntity<Object> buildBadRequestResponse() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Access-Control-Allow-Origin", frontendUrl).body(body);
    }

    public ResponseEntity<Object> buildInternalErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Access-Control-Allow-Origin", frontendUrl).body(body);
    }
}
