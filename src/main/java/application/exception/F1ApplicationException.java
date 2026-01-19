package application.exception;

public class F1ApplicationException extends RuntimeException {

    public F1ApplicationException(String message) {
        super(message);
    }

    public F1ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
