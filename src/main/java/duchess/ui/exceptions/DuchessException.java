package duchess.ui.exceptions;

/**
 * Represents an Exception due to the user command.
 */
public class DuchessException extends Exception {

    /**
     * Creates a {@code DuchessException} with the desired message.
     */
    public DuchessException(String message) {
        super("Ohoho! " + message);
    }
}
