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

    public static TaskException declareMissingField(String command, String fieldName) {
        return new TaskException(String.format(
                "The %s command requires a %s field.\n"
                + "Please re-enter your command with /%s [date].",
                command,
                fieldName,
                fieldName
        ));
    }

    public static TaskException unrecognisedCommand(String cmd) {
        return new TaskException(String.format(
                "The duchess does not recognise the command: \n"
                + "  %s",
                cmd
        ));
    }


}
