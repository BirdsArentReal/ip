package duchess.ui.exceptions;

import duchess.parse.CommandType;

/**
 * Represents an Exception due to the user command.
 */
public class DuchessException extends Exception {

    /**
     * Creates a {@code DuchessException} with the desired message.
     */
    private DuchessException(String message) {
        super("Ohoho! " + message);
    }

    /** Creates an exception for an unrecognised command. */
    public static DuchessException declareUnrecognizedCommand(String command) {
        return new DuchessException(String.format(
                "The duchess does not understand what you mean by %s.\n"
                + "Understood commands: %s",
                command,
                CommandType.getUnderstoodCommands()));
    }
}
