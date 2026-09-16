package duchess.tasks.exceptions;

import duchess.tasks.DateFormat;

/** Represents errors encountered while creating a task. */
public class TaskCreationException extends TaskException {
    private TaskCreationException(String message) {
        super(message);
    }

    /** Creates an exception for an empty task description. */
    public static TaskCreationException declareEmptyDescription(String type) {
        return new TaskCreationException(String.format(
                "The description of the %s cannot be empty.", type));
    }

    /** Creates an exception for invalid characters in a task command. */
    public static TaskCreationException declareInvalidCharacters(
            String command, String invalidChars) {
        return new TaskCreationException(String.format(
                "Your command contains invalid characters! \n"
                + "Invalid Characters: %s\n"
                + "Your command: %s", invalidChars, command));
    }

    /** Creates an exception for a missing field in a task command. */
    public static TaskCreationException declareMissingField(
            String command,
            String fieldName,
            String correctFormat,
            String exampleCommand) {
        return new TaskCreationException(String.format(
                "The %s command requires a %s field.\n"
                + "Please enter the command in the format: %s\n"
                + "Example: %s",
                command, fieldName, correctFormat, exampleCommand));
    }

    /** Creates an exception for an unrecognised task command. */
    public static TaskCreationException declareUnrecognisedCommand(String command) {
        return new TaskCreationException(String.format(
                "The duchess does not recognise the task: \n  %s", command));
    }

    /** Creates an exception for an invalid date format. */
    public static TaskCreationException declareInvalidDateFormat(String dateStr) {
        return new TaskCreationException(String.format(
                "The date \"%s\" is not in a valid format.\n"
                + "Please enter the date in the format: %s.\n"
                + "Example: %s",
                dateStr,
                DateFormat.PARSE_FORMAT_STRING,
                DateFormat.PARSE_FORMAT_EXAMPLE));
    }

    /** Creates an exception for an invalid event date range. */
    public static TaskCreationException declareInvalidDateRange(String from, String to) {
        return new TaskCreationException(String.format(
                "Your event starts from %s, \n"
                + "which is after its end date of %s.\n"
                + "Please enter a valid date range, as the duchess\n"
                + "is not a time traveller.", from, to));
    }
}
