package Tasks;

public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    private String getPeriod() {
        return String.format("(from: %s to: %s)", this.from, this.to);
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)",
                super.toString(),
                this.from,
                this.to);
    }

    @Override
    public String getStorageFormat() {
        return String.format("%s | E | %s | %s",
                super.getStorageFormat(),
                this.from,
                this.to);
    }
}
