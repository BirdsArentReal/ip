package duchess.tasks.exceptions;

/**
 * Represents all exceptions related to creating a task.
 */
public class TaskException extends Exception {
    private TaskException(String message) {
        super("OOPS!!! " + message);
    }

    /**
     * Creates a TaskException representing a failed creation operation due to
     * an empty description.
     *
     * @param type The type of Task being created.
     * @return The TaskException instance representing the reason for
     *          the failed creation.
     */
    public static TaskException declareEmptyDescription(String type) {
        return new TaskException(String.format(
                "The description of the %s cannot be empty.",
                type
        ));
    }
    
    /** Creates an exception for a find command without a search keyword. */
    public static TaskException declareEmptySearchKeyword() {
        return new TaskException("Please provide a keyword to find.");
    }

    /**
     * Creates a TaskException representing a failed creation operation due to
     * invalid characters in the input.
     *
     * @param command The input string containing an invalid character.
     * @param invalidChars The list of invalid characters.
     * @return The TaskException instance representing the reason for
     *          the failed creation.
     */
    public static TaskException declareInvalidCharacters(String command, String invalidChars) {
        return new TaskException(String.format(
                "Your command contains invalid characters! \n"
                + "Invalid Characters: %s\n"
                + "Your command: %s",
                invalidChars,
                command
        ));
    }

    /**
     * Creates a TaskException representing a failed creation operation due to
     * missing fields in the input command.
     *
     * @param command The input string with a missing field.
     * @param fieldName The missing field.
     * @return The TaskException instance representing the reason for
     *          the failed creation.
     */
    public static TaskException declareMissingField(String command, String fieldName) {
        return new TaskException(String.format(
                "The %s command requires a %s field.\n"
                + "Please re-enter your command with /%s [date].",
                command,
                fieldName,
                fieldName
        ));
    }

    /**
     * Creates a TaskException representing a failed creation operation due to
     * an unrecognized command.
     *
     * @param command The input string containing an unrecognized command.
     * @return The TaskException instance representing the reason for
     *          the failed creation.
     */
    public static TaskException declareUnrecognisedCommand(String command) {
        return new TaskException(String.format(
                "The duchess does not recognise the task: \n"
                + "  %s",
                command
        ));
    }

    /**
     * Creates a TaskException representing a failed creation operation due to
     * an invalid date format.
     *
     * @param dateStr The date string that is in the invalid format.
     * @return The TaskException instance representing the reason for
     *          the failed creation, and the correct date format.
     */
    public static TaskException declareInvalidDateFormat(String dateStr) {
        return new TaskException(String.format(
                "The date \"%s\" is not in a valid format. \n"
                + " Please enter your date in yyyy-MM-dd format.",
                dateStr
        ));
    }

    /**
     * Creates a TaskException representing a failed creation operation due to
     * an invalid date range.
     *
     * @param from The string representing the starting date.
     * @param to The string representing the ending date.
     * @return The TaskException instance representing the reason for
     *          the failed creation.
     */
    public static TaskException declareInvalidDateRange(String from, String to) {
        return new TaskException(String.format(
                "Your event starts from %s, \n"
                + "which is after its end date of %s.\n"
                + "Please enter a valid date range, as the duchess\n"
                + "is not a time traveller.",
                from,
                to
        ));
    }


}
