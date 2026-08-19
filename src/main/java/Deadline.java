public class Deadline extends Task {
    private final String by;

    Deadline(String description, String by) {
        super(description);
        this.by = by == null ? "" : by;
    }

    @Override
    public String toString() {
        String base = "[D]" + super.toString();
        if (by != null && !by.isEmpty()) {
            base += " (by: " + by + ")";
        }
        return base;
    }
}
