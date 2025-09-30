package fr.openrunning.orbackend.common.json;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.openrunning.model.exception.OpenRunningException;

public class JsonResponse {
    private final Object body;

    public JsonResponse(Object body) {
        this.body = body;
    }

    public JsonResponse(String message) {
        this.body = new JsonMessage(message);
    }

    public JsonResponse(OpenRunningException exception) {
        this(exception.getMessage());
    }

    public ResponseEntity<Object> buildSuccessResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public ResponseEntity<Object> buildBadRequestResponse() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    public ResponseEntity<Object> buildInternalErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
