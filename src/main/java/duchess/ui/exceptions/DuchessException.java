package duchess.ui.exceptions;

/**
 * Represents an Exception due to the user command.
 */
public class DuchessException extends Exception {
    public DuchessException(String message) {
        super("Ohoho! " + message);
    }
}
