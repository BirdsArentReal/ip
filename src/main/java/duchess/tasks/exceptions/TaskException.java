package duchess.tasks.exceptions;

/** Represents task-related command errors. */
public abstract class TaskException extends Exception {
    protected TaskException(String message) {
        super("OOPS!!! " + message);
    }
}
