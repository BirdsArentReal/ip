package duchess.tasks.factories;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import duchess.tasks.DateFormat;
import duchess.tasks.Task;
import duchess.tasks.exceptions.TaskCreationException;
import duchess.tasks.exceptions.TaskException;

/**
 * Handles the creation of tasks.
 */
public class TaskFactory {
    private static final char[] INVALID_CHARACTERS = new char[]{'|'};

    static LocalDate parseDate(String dateStr) throws TaskCreationException {
        try {
            return LocalDate.parse(dateStr, DateFormat.PARSE_FORMAT);
        } catch (DateTimeParseException e) {
            throw TaskCreationException.declareInvalidDateFormat(dateStr);
        }
    }

    private static String getCommandName(String command) {
        return command.split(" ")[0];
    }

    static int findMarker(String command, String marker) throws TaskCreationException {
        int location = command.indexOf(marker);
        if (location == -1) {
            // not in command
            throw TaskCreationException.declareMissingField(getCommandName(command), marker);
        }

        return location;
    }

    static String readCommand(String command, int start, int end) {
        return command.substring(start, end).trim();
    }

    /**
     * Checks if a command contains invalid characters.
     */
    private static boolean containsInvalidCharacters(String command) {
        for (char c : TaskFactory.INVALID_CHARACTERS) {
            if (command.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a task from the user command.
     *
     * @param commandLower The command to create a task, in lower case.
     * @throws TaskCreationException If the command is unrecognized, or otherwise
     *                          contains invalid characters,
     *                          insufficient information,
     *                          invalid date format,
     *                          or invalid date range.
     */
    public static Task createFromCommand(String commandLower) throws TaskCreationException {
        assert (commandLower != null) : "Command to create Task cannot be null!";
        assert (commandLower.equals(commandLower.toLowerCase()))
                : "commandLower must be in lower case!";

        if (TaskFactory.containsInvalidCharacters(commandLower)) {
            throw TaskCreationException.declareInvalidCharacters(
                    commandLower,
                    Arrays.toString(TaskFactory.INVALID_CHARACTERS));
        }

        commandLower = commandLower.stripLeading();
        if (commandLower.startsWith("todo ")) {
            return TodoFactory.create(commandLower);
        } else if (commandLower.startsWith("deadline ")) {
            return DeadlineFactory.create(commandLower);
        } else if (commandLower.startsWith("event ")) {
            return EventFactory.create(commandLower);
        } else if (commandLower.startsWith("recurring ")) {
            return RecurringTaskFactory.create(commandLower);
        } else {
            // Unrecognised command type
            throw TaskCreationException.declareUnrecognisedCommand(commandLower);
        }
    }
}
