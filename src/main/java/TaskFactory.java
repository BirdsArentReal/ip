import tasks.DateFormat;
import tasks.Task;
import tasks.ToDo;
import tasks.Deadline;
import tasks.Event;
import tasks.exceptions.TaskException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TaskFactory {

    /**
     *
     */
    private static LocalDate parseDate(String dateStr) throws TaskException {
        try {
            return LocalDate.parse(dateStr, DateFormat.PARSE_FORMAT);
        } catch (DateTimeParseException e) {
            throw TaskException.declareInvalidDateFormat(dateStr);
        }
    }


    /**
     * Create a Tasks.ToDo from a description string.
     */
    private static ToDo createToDo(String description) throws TaskException {
        description = description.trim();
        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription("todo");
        }
        return new ToDo(description);
    }

    /**
     * Create a Tasks.Deadline from a string of the form:
     *   "<description> /by <byText>"
     * If "/by" is missing, the whole string is treated as the description.
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
     * Create a Tasks.Event from a string of the form:
     *   "<description> /from <fromText> /to <toText>"
     * /from or /to may be omitted; parsing is tolerant.
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

        String desc, fromStr, toStr;

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
     * Convenience: create a Tasks.Task from a full command line.
     * Recognizes leading keywords: "todo ", "deadline ", "event ".
     * If none match, returns a Tasks.ToDo with the whole command as description.
     */
    public static Task createFromCommand(String commandLower) throws TaskException {

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
