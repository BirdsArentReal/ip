package duchess.tasks.factories;

import java.time.LocalDate;

import duchess.tasks.Event;
import duchess.tasks.exceptions.TaskException;

/** Creates {@link Event} tasks from user commands. */
class EventFactory {
    private static final String FROM_MARKER = "/from";
    private static final String TO_MARKER = "/to";

    private EventFactory() {
        // Utility class.
    }

    /**
     * Creates an event task from a command.
     *
     * @throws TaskException if required information, valid dates, or a valid
     *                       date range is missing
     */
    static Event create(String command) throws TaskException {
        int fromIndex = TaskFactory.findMarker(command, FROM_MARKER);
        int toIndex = TaskFactory.findMarker(command, TO_MARKER);
        String description;
        String fromString;
        String toString;

        if (toIndex < fromIndex) {
            description = command.substring("event".length(), toIndex).trim();
            fromString = command.substring(fromIndex + FROM_MARKER.length()).trim();
            toString = command.substring(toIndex + TO_MARKER.length(), fromIndex).trim();
        } else {
            description = command.substring("event".length(), fromIndex).trim();
            fromString = command.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
            toString = command.substring(toIndex + TO_MARKER.length()).trim();
        }

        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription("event");
        }
        if (fromString.isEmpty()) {
            throw TaskException.declareMissingField("event", FROM_MARKER);
        }
        if (toString.isEmpty()) {
            throw TaskException.declareMissingField("event", TO_MARKER);
        }

        LocalDate from = TaskFactory.parseDate(fromString);
        LocalDate to = TaskFactory.parseDate(toString);
        if (from.isAfter(to)) {
            throw TaskException.declareInvalidDateRange(fromString, toString);
        }
        return new Event(description, from, to);
    }
}
