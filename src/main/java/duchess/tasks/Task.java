package duchess.tasks;

/**
 * Represents a task that can be completed.
 */
public abstract class Task {

    private final String description;
    private boolean done;

    /**
     * Creates a new task with some description.
     *
     * @param description The description of the task.
     */
    protected Task(String description) {
        this.description = description;
        this.done = false;
    }

    /**
     * Marks this task as complete.
     */
    public void mark() {
        this.done = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void unmark() {
        this.done = false;
    }

    /**
     * Returns the string representation of a task, in
     * user-readable format.
     */
    @Override
    public String toString() {
        return (done ? "[X] " : "[ ] ") + description;
    }

    /**
     * Returns the string representation of a task, in
     * storage format.
     */
    public String getStorageFormat(){
        return (done ? "1 | " : "0 | ") + description;
    }
}
