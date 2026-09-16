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
        int byIndex = TaskFactory.findMarker(command, BY_MARKER);
        int repeatIndex = TaskFactory.findMarker(command, REPEAT_MARKER);
        String description;
        String byString;
        String repeatString;

        if (byIndex < repeatIndex) {
            description = command.substring(TASK_TYPE.length(), byIndex).trim();
            byString = command.substring(byIndex + BY_MARKER.length(), repeatIndex).trim();
            repeatString = command.substring(repeatIndex + REPEAT_MARKER.length()).trim();
        } else {
            description = command.substring(TASK_TYPE.length(), repeatIndex).trim();
            repeatString = command.substring(repeatIndex + REPEAT_MARKER.length(), byIndex).trim();
            byString = command.substring(byIndex + BY_MARKER.length()).trim();
        }

        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription(TASK_TYPE);
        }
        if (byString.isEmpty()) {
            throw TaskException.declareMissingField(TASK_TYPE, BY_MARKER);
        }
        if (repeatString.isEmpty()) {
            throw TaskException.declareMissingField(TASK_TYPE, REPEAT_MARKER);
        }

        return new RecurringTask(description, TaskFactory.parseDate(byString),
                parseRecurrence(repeatString));
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
}
