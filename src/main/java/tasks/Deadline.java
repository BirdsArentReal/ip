package tasks;
import java.time.LocalDate;

public class Deadline extends Task {
    private final LocalDate by;

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)",
                super.toString(),
                this.by.format(DateFormat.DISPLAY_FORMAT));
    }

    @Override
    public String getStorageFormat() {
        return String.format("%s | D | /by %s",
                super.getStorageFormat(),
                this.by.format(DateFormat.PARSE_FORMAT));
    }
}
