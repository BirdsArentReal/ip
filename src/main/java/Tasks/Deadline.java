package Tasks;

public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        String byPart = (by != null && !by.isEmpty()) ? String.format(" (by: %s)", by) : "";

        return String.format("[D]%s%s",
                super.toString(),
                byPart);
    }

    @Override
    public String getStorageFormat() {
        return String.format("%s | D | %s", super.getStorageFormat(), this.by);
    }
}
