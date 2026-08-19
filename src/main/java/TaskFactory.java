import Tasks.Deadline;
import Tasks.Event;
import Tasks.Exceptions.TaskException;
import Tasks.Task;
import Tasks.ToDo;

public class TaskFactory {

    /**
     * Create a Tasks.ToDo from a description string.
     */
    private static ToDo createToDo(String description) throws TaskException {
        description = description.trim();
        if (description.isEmpty()) {
            throw TaskException.emptyDescription("todo");
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
            throw TaskException.emptyDescription("deadline");
        }

        int byIndex = rest.trim().indexOf("/by");

        if (byIndex < 0) {
            throw TaskException.missingField("deadline", "by");
        }

        String desc = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + 3).trim();

        if (desc.isEmpty()) {
            throw TaskException.emptyDescription("deadline");
        } else if (by.isEmpty()) {
            throw TaskException.missingField("deadline", "by");
        }

        return new Deadline(desc, by);
    }

    /**
     * Create a Tasks.Event from a string of the form:
     *   "<description> /from <fromText> /to <toText>"
     * /from or /to may be omitted; parsing is tolerant.
     */
    private static Event createEvent(String rest) {
        if (rest == null) {
            return new Event("", "", "");
        }

        String trimmed = rest.trim();
        int fromIndex = trimmed.indexOf("/from");
        int toIndex = trimmed.indexOf("/to");

        String desc = trimmed;
        String from = "";
        String to = "";

        if (fromIndex >= 0 && toIndex >= 0 && fromIndex < toIndex) {
            desc = trimmed.substring(0, fromIndex).trim();
            from = trimmed.substring(fromIndex + 5, toIndex).trim();
            to = trimmed.substring(toIndex + 3).trim();
        } else if (fromIndex >= 0) {
            desc = trimmed.substring(0, fromIndex).trim();
            from = trimmed.substring(fromIndex + 5).trim();
        } else if (toIndex >= 0) {
            desc = trimmed.substring(0, toIndex).trim();
            to = trimmed.substring(toIndex + 3).trim();
        }

        return new Event(desc, from, to);
    }

    public static boolean isTaskCommand(String commandLine) {
        String lower = commandLine.stripLeading().toLowerCase();
        return lower.startsWith("todo ")
                || lower.startsWith("deadline ")
                || lower.startsWith("event ");
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
            // fallback: return null
            throw TaskException.emptyDescription(commandLower);
        }
    }
}
