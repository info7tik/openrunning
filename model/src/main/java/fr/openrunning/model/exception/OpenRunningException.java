package fr.openrunning.model.exception;

public class OpenRunningException extends Exception {
    public OpenRunningException(String message) {
        super(message);
    }

    public OpenRunningException(String message, Throwable cause) {
        super(message, cause);
    }
}
