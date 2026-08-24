/**
 * InsufficientBalanceException.java
 * Custom checked exception thrown when a withdrawal or transfer
 * exceeds the available account balance.
 *
 * This is a nice talking point in interviews/assessments — shows
 * you understand custom exceptions, not just try-catch on built-ins.
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
