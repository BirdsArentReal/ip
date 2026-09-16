package duchess.tasks.factories;

import java.time.LocalDate;

import duchess.tasks.Deadline;
import duchess.tasks.exceptions.TaskException;

/** Creates {@link Deadline} tasks from user commands. */
class DeadlineFactory {
    private static final String BY_MARKER = "/by";

    private DeadlineFactory() {
        // Utility class.
    }

    /**
     * Creates a deadline task from a command.
     *
     * @throws TaskException if required information or a valid date is missing
     */
    static Deadline create(String command) throws TaskException {
        int byIndex = TaskFactory.findMarker(command, BY_MARKER);
        String description = command.substring("deadline".length(), byIndex).trim();
        String byString = command.substring(byIndex + BY_MARKER.length()).trim();

        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription("deadline");
        }
        if (byString.isEmpty()) {
            throw TaskException.declareMissingField("deadline", BY_MARKER);
        }
        return new Deadline(description, TaskFactory.parseDate(byString));
    }
}
