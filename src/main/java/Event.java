public class Event extends Task {
    private final String from;
    private final String to;

    Event(String description, String from, String to) {
        super(description);
        this.from = from == null ? "" : from;
        this.to = to == null ? "" : to;
    }

    private String getPeriod() {
        if (from.isEmpty() && to.isEmpty()) {
            return "";
        } else if (from.isEmpty()) {
            return String.format("(to: %s)", this.to);
        } else if (to.isEmpty()) {
            return String.format("(from: %s)", this.from);
        } else {
            return String.format("(from: %s to: %s)", this.from, this.to);
        }
    }

    @Override
    public String toString() {
        return String.format("[E]%s %s",
                super.toString(),
                this.getPeriod());
    }
}
