package fr.openrunning.orbackend.common.exception;

public class OpenRunningException extends Exception {
    public OpenRunningException(String message) {
        super(message);
    }

    public OpenRunningException(String message, Throwable cause) {
        super(message, cause);
    }
}
