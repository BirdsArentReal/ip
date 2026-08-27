package tasks;

import java.time.LocalDate;

public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)",
                super.toString(),
                this.from.format(DateFormat.DISPLAY_FORMAT),
                this.to.format(DateFormat.DISPLAY_FORMAT));
    }

    @Override
    public String getStorageFormat() {
        return String.format("%s | E | /from %s /to %s",
                super.getStorageFormat(),
                this.from.format(DateFormat.PARSE_FORMAT),
                this.to.format(DateFormat.PARSE_FORMAT));
    }
}
