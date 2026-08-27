import tasks.exceptions.TaskException;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles loading tasks from and saving tasks to a text file.
 *
 * <p>Each task occupies one line in the storage file, using one of these formats:</p>
 * <pre>
 * &lt;isDone&gt; | &lt;description&gt; | T |
 * &lt;isDone&gt; | &lt;description&gt; | D | /by &lt;by&gt;
 * &lt;isDone&gt; | &lt;description&gt; | E | /from &lt;from&gt; /to &lt;to&gt;
 * </pre>
 *
 * <p>{@code isDone} is {@code 0} for an incomplete task and {@code 1} for a completed task.</p>
 */
public class Storage {
    private final Path filePath;

    public Storage() throws IOException {
        this(Path.of("data", "duchess.txt"));
    }

    public Storage(Path filePath) throws IOException {
        this.filePath = filePath;

        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Loads previously saved tasks.
     *
     * @return the saved tasks, or an empty list when there is no data file
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (Files.notExists(this.filePath)) {
            return tasks;
        }

        // Read every line from FILE_PATH.
        // Convert each line back into its corresponding Task object.
        // Add each reconstructed task to tasks.
        List<String> lines;
        try {
            lines = Files.readAllLines(this.filePath);
        } catch (IOException i) {
            // explicitly throw IOException,
            // to make code easier to read,
            // and edit in the future.
            throw i;
        }

        // Add all tasks stored,
        // skip unrecognised lines
        for (String line: lines) {
            try {
                tasks.add(Storage.deserialize(line));
            } catch (TaskException t) {
                // do nothing
            }
        }

        return tasks;
    }

    /**
     * Saves the supplied tasks, replacing the previous contents of the data file.
     *
     * @param tasks the current task list
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        // convert tasks to strings for storage
        List<String> lines = tasks.stream().map(Storage::serialize).toList();

        // Write lines to FILE_PATH, replacing the previous contents.
        Files.write(this.filePath, lines);
    }

    /**
     * Converts one task to a line that can be stored in the data file.
     *
     * @param task the task to convert
     * @return a storage line, such as "0 | read book | T"
     */
    private static String serialize(Task task) {
        return task.getStorageFormat();
    }

    /**
     * Reconstructs one task from a line in the data file.
     *
     * @param line one stored task record
     * @return the corresponding task
     */
    private static Task deserialize(String line) throws TaskException {
        final Map<String, String> charToType = Map.of(
                "T", "task",
                "D", "deadline",
                "E", "event"
        );

        // Split the line using " | ".
        // Set limit to -1 so that task splits properly.
        String[] items = line.split("\\|", -1);

        // Throws error immediately if fewer items than expected
        if (items.length < 4) {
            throw TaskException.unrecognisedCommand(line);
        }

        // Use the first part to restore whether it is marked done.
        boolean isDone = items[0].trim().equals("1");

        // Use the third part to determine whether this is a
        // ToDo, Deadline, or Event.
        String type = charToType.getOrDefault(items[2].trim(), null);

        // Get the description and any additional information
        // of the stored line.
        String desc = items[1].trim();
        String additional = items[3].trim();


        if (type == null) {
            throw TaskException.unrecognisedCommand(line);
        }

        Task task = TaskFactory.createFromCommand(String.format(
                "%s %s %s",
                type,
                desc,
                additional
        ));

        if (isDone) {
            task.mark();
        }

        return task;
    }
}
