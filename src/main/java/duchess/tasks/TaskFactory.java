package duchess.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import duchess.tasks.exceptions.TaskException;

/**
 * Handles the creation of tasks.
 */
public class TaskFactory {
    private static final char[] INVALID_CHARACTERS = new char[]{'|'};

    private static LocalDate parseDate(String dateStr) throws TaskException {
        try {
            return LocalDate.parse(dateStr, DateFormat.PARSE_FORMAT);
        } catch (DateTimeParseException e) {
            throw TaskException.declareInvalidDateFormat(dateStr);
        }
    }


    /**
     * Create a task with no specific deadline nor date.
     *
     * @throws TaskException If the description is empty.
     */
    private static ToDo createToDo(String description) throws TaskException {
        description = description.trim();
        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription("todo");
        }
        return new ToDo(description);
    }

    /**
     * Create a task with a deadline.
     *
     * @throws TaskException If the command contains insufficient information,
     *                          or invalid date format.
     */
    private static Deadline createDeadline(String rest) throws TaskException {
        if (rest.isEmpty()) {
            throw TaskException.declareEmptyDescription("deadline");
        }

        int byIndex = rest.trim().indexOf("/by");

        if (byIndex < 0) {
            throw TaskException.declareMissingField("deadline", "by");
        }

        String desc = rest.substring(0, byIndex).trim();
        String byString = rest.substring(byIndex + 3).trim();

        if (desc.isEmpty()) {
            throw TaskException.declareEmptyDescription("deadline");
        } else if (byString.isEmpty()) {
            throw TaskException.declareMissingField("deadline", "by");
        }

        // this might throw TaskException.declareInvalidDateFormat()
        return new Deadline(desc, TaskFactory.parseDate(byString));
    }

    /**
     * Create a task occurring in a specific range of time.
     *
     * @throws TaskException If the command contains insufficient information,
     *                          an invalid date format, or invalid date range.
     */
    private static Event createEvent(String rest) throws TaskException {
        if (rest.isEmpty()) {
            // empty description
            throw TaskException.declareEmptyDescription("event");
        }

        String trimmed = rest.trim();
        int fromIndex = trimmed.indexOf("/from");
        int toIndex = trimmed.indexOf("/to");

        if (fromIndex < 0) {
            throw TaskException.declareMissingField("event", "from");
        } else if (toIndex < 0) {
            throw TaskException.declareMissingField("event", "to");
        }

        String desc;
        String fromStr;
        String toStr;

        if (toIndex < fromIndex) {
            desc = trimmed.substring(0, toIndex).trim();
            fromStr = trimmed.substring(fromIndex + 5).trim();
            toStr = trimmed.substring(toIndex + 3, fromIndex).trim();
        } else {
            desc = trimmed.substring(0, fromIndex).trim();
            fromStr = trimmed.substring(fromIndex + 5, toIndex).trim();
            toStr = trimmed.substring(toIndex + 3).trim();
        }

        if (desc.isEmpty()) {
            throw TaskException.declareEmptyDescription("event");
        } else if (fromStr.isEmpty()) {
            throw TaskException.declareMissingField("event", "from");
        } else if (toStr.isEmpty()) {
            throw TaskException.declareMissingField("event", "to");
        }

        // these are separated from return because they might
        // throw TaskException.declareInvalidDateFormat
        LocalDate from = TaskFactory.parseDate(fromStr);
        LocalDate to = TaskFactory.parseDate(toStr);

        if (from.isAfter(to)) {
            throw TaskException.declareInvalidDateRange(fromStr, toStr);
        }

        return new Event(desc, from, to);
    }

    /**
     * Checks if a command contains invalid characters.
     */
    private static boolean containsInvalidCharacters(String command) {
        for (char c : TaskFactory.INVALID_CHARACTERS) {
            if (command.indexOf(c) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a task from the user command.
     *
     * @throws TaskException If the command is unrecognized, or otherwise
     *                          contains invalid characters,
     *                          insufficient information,
     *                          invalid date format,
     *                          or invalid date range.
     */
    public static Task createFromCommand(String commandLower) throws TaskException {
        if (TaskFactory.containsInvalidCharacters(commandLower)) {
            throw TaskException.declareInvalidCharacters(
                    commandLower,
                    Arrays.toString(TaskFactory.INVALID_CHARACTERS));
        }

        if (commandLower.startsWith("todo ")) {
            return createToDo(commandLower.substring(5));
        } else if (commandLower.startsWith("deadline ")) {
            return createDeadline(commandLower.substring(9));
        } else if (commandLower.startsWith("event ")) {
            return createEvent(commandLower.substring(6));
        } else {
            // Unrecognised command type
            throw TaskException.declareUnrecognisedCommand(commandLower);
        }
    }
}
