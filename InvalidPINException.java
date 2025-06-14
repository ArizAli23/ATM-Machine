package ATMSimulation;

public class InvalidPINException extends RuntimeException {
    public InvalidPINException(String message) {
        super(message);
    }
}
