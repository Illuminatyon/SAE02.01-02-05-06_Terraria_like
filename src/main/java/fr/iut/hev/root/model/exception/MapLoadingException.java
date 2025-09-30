package fr.iut.hev.root.model.exception;

public class MapLoadingException extends RuntimeException {

    public MapLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapLoadingException(String message) {
        super(message);
    }
}
