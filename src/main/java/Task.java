public class Task {
    private final String description;
    private boolean done;

    Task(String description) {
        this.description = description;
        this.done = false;
    }

    public void mark() {
        this.done = true;
    }

    public void unmark() {
        this.done = false;
    }

    /**
     * Default display format for a task. Subclasses can override this to change how tasks are shown.
     * Example output: "[X] read book" or "[ ] return book"
     */
    @Override
    public String toString() {
        return (done ? "[X] " : "[ ] ") + description;
    }
}
