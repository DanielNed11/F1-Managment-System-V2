package application.exception;

/**
 * Custom exception for F1 application business logic errors.
 * Extends RuntimeException to be an unchecked exception.
 */
public class F1ApplicationException extends RuntimeException {

    public F1ApplicationException(String message) {
        super(message);
    }

    public F1ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
