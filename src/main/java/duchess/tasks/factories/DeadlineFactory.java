package duchess.tasks.factories;

import java.time.LocalDate;

import duchess.tasks.Deadline;
import duchess.tasks.exceptions.TaskException;

/** Creates {@link Deadline} tasks from user commands. */
class DeadlineFactory {
    private static final String TASK_TYPE = "deadline";
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
        DeadlineDetails details = parseDetails(command);
        validateDetails(details);

        LocalDate byDate = TaskFactory.parseDate(details.getByString());
        return new Deadline(details.getDescription(), byDate);
    }

    private static DeadlineDetails parseDetails(String command)
            throws TaskException {
        int byIndex = TaskFactory.findMarker(command, BY_MARKER);

        return new DeadlineDetails(
                command.substring(TASK_TYPE.length(), byIndex).trim(),
                command.substring(byIndex + BY_MARKER.length()).trim());
    }

    private static void validateDetails(DeadlineDetails details)
            throws TaskException {
        if (details.getDescription().isEmpty()) {
            throw TaskException.declareEmptyDescription(TASK_TYPE);
        }
        if (details.getByString().isEmpty()) {
            throw TaskException.declareMissingField(TASK_TYPE, BY_MARKER);
        }
    }

    /** Holds the textual fields extracted from a deadline command. */
    private static class DeadlineDetails {
        private final String description;
        private final String byString;

        DeadlineDetails(String description, String byString) {
            this.description = description;
            this.byString = byString;
        }

        String getDescription() {
            return description;
        }

        String getByString() {
            return byString;
        }
    }
}
