package duchess.tasks;

import java.time.LocalDate;

/**
 * Represents a task occurring within a specific range of time.
 */
public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates a task occurring within a specific range of time.
     *
     * @param description The description of the task.
     * @param from The starting date of the task.
     * @param to The ending date of the task.
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the string representation of a task occurring
     * in a specific range of time, in user-readable format.
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)",
                super.toString(),
                this.from.format(DateFormat.DISPLAY_FORMAT),
                this.to.format(DateFormat.DISPLAY_FORMAT));
    }

    /**
     * Returns the string representation of a task occurring
     * in a specific range of time, in storage format.
     */
    @Override
    public String getStorageFormat() {
        return String.format("%s | E | /from %s /to %s",
                super.getStorageFormat(),
                this.from.format(DateFormat.PARSE_FORMAT),
                this.to.format(DateFormat.PARSE_FORMAT));
    }
}
