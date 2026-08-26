import Tasks.Deadline;
import Tasks.Event;
import Tasks.Task;
import Tasks.ToDo;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading tasks from and saving tasks to a text file.
 *
 * <p>Each task occupies one line in the storage file, using one of these formats:</p>
 * <pre>
 * &lt;isDone&gt; | &lt;description&gt; | T
 * &lt;isDone&gt; | &lt;description&gt; | D | /by &lt;by&gt;
 * &lt;isDone&gt; | &lt;description&gt; | E | /from &lt;from&gt; /to &lt;to&gt;
 * </pre>
 *
 * <p>{@code isDone} is {@code 0} for an incomplete task and {@code 1} for a completed task.</p>
 */
public class Storage {
    private final Path filePath;

    public Storage() {
        this(Path.of("data", "duchess.txt"));
    }

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads previously saved tasks.
     *
     * @return the saved tasks, or an empty list when there is no data file
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (Files.notExists(this.filePath)) {
            return tasks;
        }

        // Read every line from FILE_PATH.
        // Convert each line back into its corresponding Task object.
        // Add each reconstructed task to tasks.

        return tasks;
    }

    /**
     * Saves the supplied tasks, replacing the previous contents of the data file.
     *
     * @param tasks the current task list
     */
    public void save(List<Task> tasks) {
        // Create the data directory if it does not exist.

        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(serialize(task));
        }

        // Write lines to FILE_PATH, replacing the previous contents.
    }

    /**
     * Converts one task to a line that can be stored in the data file.
     *
     * @param task the task to convert
     * @return a storage line, such as "T | 0 | read book"
     */
    private String serialize(Task task) {
        // Example formats:
        // T | 0 | read book
        // D | 1 | submit assignment | Friday
        // E | 0 | team meeting | Monday 2pm | Monday 4pm
        return "";
    }

    /**
     * Reconstructs one task from a line in the data file.
     *
     * @param line one stored task record
     * @return the corresponding task
     */
    private Task deserialize(String line) {
        // Split the line using " | ".
        // Use the first part to determine whether this is a ToDo,
        // Deadline, or Event.
        // Use the second part to restore whether it is marked done.
        return null;
    }
}
