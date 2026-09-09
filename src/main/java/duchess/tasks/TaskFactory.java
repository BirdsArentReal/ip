package duchess.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import duchess.parse.CommandType;
import duchess.tasks.exceptions.TaskException;

/**
 * Handles the creation of tasks.
 */
public class TaskFactory {
    private static final char[] INVALID_CHARACTERS = new char[]{'|'};

    private static final String DEADLINE_BY_MARKER = "/by";

    private static final String EVENT_FROM_MARKER = "/from";
    private static final String EVENT_TO_MARKER = "/to";

    private static LocalDate parseDate(String dateStr) throws TaskException {
        try {
            return LocalDate.parse(dateStr, DateFormat.PARSE_FORMAT);
        } catch (DateTimeParseException e) {
            throw TaskException.declareInvalidDateFormat(dateStr);
        }
    }

    private static String getCommandName(String command) {
        return command.split(" ")[0];
    }

    private static int findMarker(String command, String marker) throws TaskException {
        int location = command.indexOf(marker);
        if (location == -1) {
            // not in command
            throw TaskException.declareMissingField(getCommandName(command), marker);
        }

        return location;
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

    private static String[] parseTodoCommand(String command)
            throws TaskException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription("todo");
        }
        return new String[]{description};
    }

    private static String[] parseDeadlineCommand(String command, int byIndex)
            throws TaskException {
        String description = command.substring("deadline".length(), byIndex).trim();
        String byString = command
                .substring(byIndex + DEADLINE_BY_MARKER.length())
                .trim();

        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription("deadline");
        } else if (byString.isEmpty()) {
            throw TaskException.declareMissingField("deadline", DEADLINE_BY_MARKER);
        }

        return new String[]{description, byString};
    }

    private static String[] parseEventCommand(String command, int fromIndex, int toIndex)
            throws TaskException {
        String desc;
        String fromStr;
        String toStr;

        if (toIndex < fromIndex) {
            desc = command.substring("event".length(), toIndex).trim();
            fromStr = command.substring(fromIndex + EVENT_FROM_MARKER.length()).trim();
            toStr = command.substring(toIndex + EVENT_TO_MARKER.length(), fromIndex).trim();
        } else {
            desc = command.substring("event".length(), fromIndex).trim();
            fromStr = command.substring(fromIndex + EVENT_FROM_MARKER.length(), toIndex).trim();
            toStr = command.substring(toIndex + EVENT_TO_MARKER.length()).trim();
        }

        if (desc.isEmpty()) {
            throw TaskException.declareEmptyDescription("event");
        }
        if (fromStr.isEmpty()) {
            throw TaskException.declareMissingField("event", EVENT_FROM_MARKER);
        }
        if (toStr.isEmpty()) {
            throw TaskException.declareMissingField("event", EVENT_TO_MARKER);
        }

        return new String[]{desc, fromStr, toStr};
    }

    private static LocalDate[] parseEventDates(String fromDate, String toDate)
            throws TaskException {

        LocalDate from = TaskFactory.parseDate(fromDate);
        LocalDate to = TaskFactory.parseDate(toDate);

        if (from.isAfter(to)) {
            throw TaskException.declareInvalidDateRange(fromDate, toDate);
        }

        return new LocalDate[]{from, to};
    }


    /**
     * Create a task with no specific deadline nor date.
     *
     * @throws TaskException If the description is empty.
     */
    private static ToDo createToDo(String command) throws TaskException {
        String description = parseTodoCommand(command)[0];
        return new ToDo(description);
    }

    /**
     * Create a task with a deadline.
     *
     * @throws TaskException If the command contains insufficient information,
     *                          or invalid date format.
     */
    private static Deadline createDeadline(String command) throws TaskException {
        int byIndex = TaskFactory.findMarker(command, DEADLINE_BY_MARKER);

        String[] deadlineComponents = TaskFactory.parseDeadlineCommand(command, byIndex);
        String desc = deadlineComponents[0];

        LocalDate byDate = TaskFactory.parseDate(deadlineComponents[1]);

        return new Deadline(desc, byDate);
    }

    /**
     * Create a task occurring in a specific range of time.
     *
     * @throws TaskException If the command contains insufficient information,
     *                          an invalid date format, or invalid date range.
     */
    private static Event createEvent(String command) throws TaskException {
        int fromIndex = TaskFactory.findMarker(command, EVENT_FROM_MARKER);
        int toIndex = TaskFactory.findMarker(command, EVENT_TO_MARKER);

        String[] eventComponents = TaskFactory.parseEventCommand(command, fromIndex, toIndex);
        String description = eventComponents[0];
        String fromString = eventComponents[1];
        String toString = eventComponents[2];

        LocalDate[] dateRange = TaskFactory.parseEventDates(fromString, toString);
        LocalDate fromDate = dateRange[0];
        LocalDate toDate = dateRange[1];

        return new Event(description, fromDate, toDate);
    }

    /**
     * Creates a task from the user command.
     *
     * @param commandLower The command to create a task, in lower case.
     * @throws TaskException If the command is unrecognized, or otherwise
     *                          contains invalid characters,
     *                          insufficient information,
     *                          invalid date format,
     *                          or invalid date range.
     */
    public static Task createFromCommand(String commandLower) throws TaskException {
        assert (commandLower != null) : "Command to create Task cannot be null!";
        assert (commandLower.equals(commandLower.toLowerCase()))
                : "commandLower must be in lower case!";

        if (TaskFactory.containsInvalidCharacters(commandLower)) {
            throw TaskException.declareInvalidCharacters(
                    commandLower,
                    Arrays.toString(TaskFactory.INVALID_CHARACTERS));
        }

        commandLower = commandLower.stripLeading();
        if (commandLower.startsWith("todo ")) {
            return createToDo(commandLower);
        } else if (commandLower.startsWith("deadline ")) {
            return createDeadline(commandLower);
        } else if (commandLower.startsWith("event ")) {
            return createEvent(commandLower);
        } else {
            // Unrecognised command type
            throw TaskException.declareUnrecognisedCommand(commandLower);
        }
    }
}
