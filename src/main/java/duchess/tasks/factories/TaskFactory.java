package duchess.tasks.factories;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import duchess.tasks.DateFormat;
import duchess.tasks.Deadline;
import duchess.tasks.Event;
import duchess.tasks.RecurringTask;
import duchess.tasks.Task;
import duchess.tasks.ToDo;
import duchess.tasks.exceptions.TaskException;

/**
 * Handles the creation of tasks.
 */
public class TaskFactory {
    private static final char[] INVALID_CHARACTERS = new char[]{'|'};

    private static final String DEADLINE_BY_MARKER = "/by";

    private static final String RECURRING_BY_MARKER = "/by";
    private static final String RECURRING_REPEAT_MARKER = "/repeat";

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

    private static String[] parseRecurringCommand(String command, int byIndex,
            int repeatIndex) throws TaskException {
        String description;
        String byString;
        String repeatString;

        if (byIndex < repeatIndex) {
            description = command.substring("recurring".length(), byIndex).trim();
            byString = command.substring(
                    byIndex + RECURRING_BY_MARKER.length(), repeatIndex).trim();
            repeatString = command.substring(
                    repeatIndex + RECURRING_REPEAT_MARKER.length()).trim();
        } else {
            description = command.substring("recurring".length(), repeatIndex).trim();
            repeatString = command.substring(
                    repeatIndex + RECURRING_REPEAT_MARKER.length(), byIndex).trim();
            byString = command.substring(
                    byIndex + RECURRING_BY_MARKER.length()).trim();
        }

        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription("recurring");
        }
        if (byString.isEmpty()) {
            throw TaskException.declareMissingField("recurring", RECURRING_BY_MARKER);
        }
        if (repeatString.isEmpty()) {
            throw TaskException.declareMissingField("recurring", RECURRING_REPEAT_MARKER);
        }

        return new String[]{description, byString, repeatString};
    }

    private static Period parseRecurrence(String recurrence) throws TaskException {
        String[] components = recurrence.trim().split("\\s+");
        boolean isDayUnit = components.length == 2
                && (components[1].equals("day") || components[1].equals("days"));
        if (!isDayUnit) {
            throw TaskException.declareInvalidDateFormat(recurrence);
        }

        int days;
        try {
            days = Integer.parseInt(components[0]);
        } catch (NumberFormatException e) {
            throw TaskException.declareInvalidDateFormat(recurrence);
        }

        if (days <= 0) {
            throw TaskException.declareInvalidDateFormat(recurrence);
        }
        return Period.ofDays(days);
    }

    private static RecurringTask createRecurring(String command)
            throws TaskException {
        int byIndex = TaskFactory.findMarker(command, RECURRING_BY_MARKER);
        int repeatIndex = TaskFactory.findMarker(command, RECURRING_REPEAT_MARKER);

        String[] components = TaskFactory
                .parseRecurringCommand(command, byIndex, repeatIndex);
        String description = components[0];
        LocalDate byDate = TaskFactory.parseDate(components[1]);
        Period recurrence = TaskFactory.parseRecurrence(components[2]);

        return new RecurringTask(description, byDate, recurrence);
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
        } else if (commandLower.startsWith("recurring ")) {
            return createRecurring(commandLower);
        } else {
            // Unrecognised command type
            throw TaskException.declareUnrecognisedCommand(commandLower);
        }
    }
}
