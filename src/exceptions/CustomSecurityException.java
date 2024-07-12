package exceptions;

public class CustomSecurityException extends RuntimeException {
    
    public CustomSecurityException(String message) {
        super(message);
    }
}
