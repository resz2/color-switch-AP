package sample;

public class CannotContinueException extends Exception {
    public CannotContinueException(String errorMessage) {
        super(errorMessage);
    }
}
