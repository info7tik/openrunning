package fr.openrunning.gpxprocessor.exception;

public class GpxProcessorException extends Exception {
    public GpxProcessorException(String message) {
        super(message);
    }

    public GpxProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

}
