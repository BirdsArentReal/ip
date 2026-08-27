package tasks;
import java.time.LocalDate;

public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("[D]%s%s",
                super.toString(),
                this.by);
    }

    @Override
    public String getStorageFormat() {
        return String.format("%s | D | /by %s", super.getStorageFormat(), this.by);
    }
}
