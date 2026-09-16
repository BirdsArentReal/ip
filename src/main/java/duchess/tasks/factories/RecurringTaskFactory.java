package duchess.tasks.factories;

import java.time.LocalDate;
import java.time.Period;

import duchess.tasks.RecurringTask;
import duchess.tasks.exceptions.TaskException;

/** Creates {@link RecurringTask} tasks from user commands. */
class RecurringTaskFactory {
    private static final String TASK_TYPE = "recurring";
    private static final String BY_MARKER = "/by";
    private static final String REPEAT_MARKER = "/repeat";

    private RecurringTaskFactory() {
        // Utility class.
    }

    /**
     * Creates a recurring task from a command.
     *
     * @throws TaskException if required information, a valid date, or a valid
     *                       recurrence is missing
     */
    static RecurringTask create(String command) throws TaskException {
        RecurringTaskDetails details = parseDetails(command);
        validateDetails(details);

        LocalDate byDate = TaskFactory.parseDate(details.getByString());
        Period recurrence = parseRecurrence(details.getRepeatString());

        return new RecurringTask(details.getDescription(), byDate, recurrence);
    }

    private static RecurringTaskDetails parseDetails(String command)
            throws TaskException {
        int byIndex = TaskFactory.findMarker(command, BY_MARKER);
        int repeatIndex = TaskFactory.findMarker(command, REPEAT_MARKER);

        return byIndex < repeatIndex
                ? parseNormalMarkerOrder(command, byIndex, repeatIndex)
                : parseReversedMarkerOrder(command, byIndex, repeatIndex);
    }

    private static RecurringTaskDetails parseNormalMarkerOrder(
            String command, int byIndex, int repeatIndex) {
        return new RecurringTaskDetails(
                TaskFactory.readCommand(command, TASK_TYPE.length(), byIndex),
                TaskFactory.readCommand(
                        command, byIndex + BY_MARKER.length(), repeatIndex),
                TaskFactory.readCommand(
                        command, repeatIndex + REPEAT_MARKER.length(), command.length()));
    }

    private static RecurringTaskDetails parseReversedMarkerOrder(
            String command, int byIndex, int repeatIndex) {
        return new RecurringTaskDetails(
                TaskFactory.readCommand(command, TASK_TYPE.length(), repeatIndex),
                TaskFactory.readCommand(
                        command, byIndex + BY_MARKER.length(), command.length()),
                TaskFactory.readCommand(
                        command, repeatIndex + REPEAT_MARKER.length(), byIndex));
    }

    private static void validateDetails(RecurringTaskDetails details)
            throws TaskException {
        if (details.getDescription().isEmpty()) {
            throw TaskException.declareEmptyDescription(TASK_TYPE);
        }
        if (details.getByString().isEmpty()) {
            throw TaskException.declareMissingField(TASK_TYPE, BY_MARKER);
        }
        if (details.getRepeatString().isEmpty()) {
            throw TaskException.declareMissingField(TASK_TYPE, REPEAT_MARKER);
        }
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

    /** Holds the textual fields extracted from a recurring task command. */
    private static class RecurringTaskDetails {
        private final String description;
        private final String byString;
        private final String repeatString;

        RecurringTaskDetails(String description, String byString, String repeatString) {
            this.description = description;
            this.byString = byString;
            this.repeatString = repeatString;
        }

        String getDescription() {
            return description;
        }

        String getByString() {
            return byString;
        }

        String getRepeatString() {
            return repeatString;
        }
    }
}
