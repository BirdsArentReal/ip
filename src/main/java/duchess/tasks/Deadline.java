package duchess.tasks;
import java.time.LocalDate;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private final LocalDate by;

    /**
     * Creates a task with a deadline.
     *
     * @param description The description of the task.
     * @param by The deadline of the task.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the string representation of a task with a deadline,
     * in user-readable format.
     */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)",
                super.toString(),
                this.by.format(DateFormat.DISPLAY_FORMAT));
    }

    /**
     * Returns the string representation of a task with a deadline,
     * in storage format.
     */
    @Override
    public String getStorageFormat() {
        return String.format("%s | D | /by %s",
                super.getStorageFormat(),
                this.by.format(DateFormat.PARSE_FORMAT));
    }
}
