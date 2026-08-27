package tasks.exceptions;

public class TaskException extends Exception {
    private TaskException(String message) {
        super("OOPS!!! " + message);
    }

    public static TaskException declareEmptyDescription(String name) {
        return new TaskException(String.format(
                "The description of the %s cannot be empty.",
                name
        ));
    }

    public static TaskException declareInvalidCharacters(String cmd, String invalidChars) {
        return new TaskException(String.format(
                "Your command contains invalid characters! \n"
                + "Invalid Characters: %s\n"
                + "Your command:",
                invalidChars,
                cmd
        ));
    }

    public static TaskException declareMissingField(String command, String fieldName) {
        return new TaskException(String.format(
                "The %s command requires a %s field.\n"
                + "Please re-enter your command with /%s [date].",
                command,
                fieldName,
                fieldName
        ));
    }

    public static TaskException declareUnrecognisedCommand(String cmd) {
        return new TaskException(String.format(
                "The duchess does not recognise the task: \n"
                + "  %s",
                cmd
        ));
    }

    public static TaskException declareInvalidDateFormat(String dateStr) {
        return new TaskException(String.format(
                "The date \"%s\" is not in a valid format. \n"
                + " Please enter your date in yyyy-MM-dd format.",
                dateStr
        ));
    }

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
