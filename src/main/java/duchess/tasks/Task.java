package duchess.tasks;

public abstract class Task {

    private final String description;
    private boolean isDone;

    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    /**
     * Default display format for a task. Subclasses can override this to change how tasks are shown.
     * Example output: "[X] read book" or "[ ] return book"
     */
    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }

    public String getStorageFormat(){
        return (isDone ? "1 | " : "0 | ") + description;
    }
}
