package duchess.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import duchess.io.exceptions.StorageException;
import duchess.tasks.Task;
import duchess.tasks.collections.TaskList;
import duchess.tasks.exceptions.TaskException;
import duchess.tasks.factories.TaskFactory;

/**
 * Handles loading tasks from and saving tasks to a text file.
 *
 * <p>Each task occupies one line in the storage file, using one of these formats:</p>
 * <pre>
 * &lt;isDone&gt; | &lt;description&gt; | T |
 * &lt;isDone&gt; | &lt;description&gt; | D | /by &lt;by&gt;
 * &lt;isDone&gt; | &lt;description&gt; | E | /from &lt;from&gt; /to &lt;to&gt;
 * &lt;isDone&gt; | &lt;description&gt; | R | /by &lt;by&gt; /repeat &lt;days&gt;
 * </pre>
 *
 * <p>{@code isDone} is {@code 0} for an incomplete task and {@code 1} for a completed task.</p>
 */
public class Storage {
    /* Class-level constants */
    private static final Map<String, String> STORED_CHAR_TO_TYPE = Map.of(
            "T", "todo",
            "D", "deadline",
            "E", "event",
            "R", "recurring"
    );

    /** Uses "\\|" to represent "|", as this string is for regex */
    private static final String SPLIT_CHARACTER = "\\|";

    private static final String COMPLETED_STATUS = "1";
    private static final String INCOMPLETE_STATUS = "0";

    private static final int EXPECTED_STORE_FORMAT_LENGTH = 4;

    private static final int TASK_STATUS_INDEX = 0;
    private static final int TASK_DESCRIPTION_INDEX = 1;
    private static final int TASK_TYPE_INDEX = 2;
    private static final int TASK_ADDITIONAL_INFORMATION_INDEX = 3;

    /* Instance-level variables */
    private final Path filePath;

    /**
     * Creates a storage handler that saves data in {@code filePath}.
     * If the file or directory doesn't exist, create it.
     *
     * @param filePath The location of the file to save to.
     * @throws IOException If the file doesn't exist and couldn't
     *                      be created.
     */
    public Storage(Path filePath) throws IOException {
        assert (filePath != null) : "Cannot create storage without a file";
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
     * Splits the stored task into individual components.
     * @param line The line representing a stored task.
     * @return An array representing the individual components
     * @throws StorageException If the line cannot be interpreted
     *                          as a task.
     */
    private static String[] splitStored(String line) throws StorageException {
        assert (line != null) : "Stored task should not be null";

        // Set limit to -1 so that all instances of "|" are split.
        String[] items = line.split(SPLIT_CHARACTER, -1);
        for (int i = 0; i < items.length; i++) {
            assert (items[i] != null) : "Component of a task cannot be null";
            items[i] = items[i].trim();
        }

        if (!isValidSplit(items)) {
            throw new StorageException("Split failed. \n" + line);
        }

        return items;
    }

    /**
     * Conducts a preliminary check for whether the string array can
     * describe a {@code Task}.
     * @param components The components of the storage line after the split.
     * @return {@code true}, if the array is of correct length, and the task type is valid.
     *          <p> {@code false}, otherwise.
     */
    private static boolean isValidSplit(String[] components) {
        assert (components != null) : "Components of a task cannot be null";

        if (components.length != EXPECTED_STORE_FORMAT_LENGTH) {
            return false;
        }

        return STORED_CHAR_TO_TYPE.containsKey(components[TASK_TYPE_INDEX]);
    }

    /**
     * Creates a task from its components.
     * @param components The string array representing the split
     *                   components of the storage line.
     * @return A {@code Task}, as described by the components.
     * @throws TaskException If the components cannot be recognized as a {@code Task}.
     */
    private static Task makeTask(String[] components) throws TaskException {
        String type = STORED_CHAR_TO_TYPE.get(components[TASK_TYPE_INDEX]);
        String desc = components[TASK_DESCRIPTION_INDEX];
        String additional = components[TASK_ADDITIONAL_INFORMATION_INDEX];
        Task task = TaskFactory.createFromCommand(String.format(
                "%s %s %s",
                type,
                desc,
                additional
        ));
        return task;
    }

    /**
     * Marks the {@code task} as complete if the {@code taskStatus} says so.
     */
    private static void markIfComplete(Task task, String taskStatus) throws StorageException {
        if (isTaskComplete(taskStatus)) {
            task.mark();
        }
    }

    /**
     * Determines if the {@code taskStatus} describes a completed task.
     * @throws StorageException If the {@code taskStatus} could not be recognized.
     */
    private static boolean isTaskComplete(String taskStatus) throws StorageException {
        return switch (taskStatus) {
            case INCOMPLETE_STATUS -> false;
            case COMPLETED_STATUS -> true;
            default -> throw new StorageException("IsDone has incorrect format.");
        };
    }

    /**
     * Loads previously saved tasks.
     *
     * @return The saved tasks, or an empty list when there is no data file.
     */
    public ArrayList<Task> load() throws IOException {
        List<String> lines = Files.readAllLines(this.filePath);

        // Add all tasks in storage to an arraylist.
        ArrayList<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            try {
                tasks.add(Storage.deserialize(line));
            } catch (TaskException t) {
                // do nothing, since we are expected to
                // skip unrecognised lines.
            }
        }

        return tasks;
    }

    /**
     * Reconstructs one task from a line in the data file.
     *
     * @param line One stored task record.
     * @return A new task with the same data as recorded.
     */
    private static Task deserialize(String line) throws TaskException {
        try {
            String[] components = Storage.splitStored(line);
            Task task = makeTask(components);
            markIfComplete(task, components[TASK_STATUS_INDEX]);
            return task;
        } catch (StorageException e) {
            throw TaskException.declareUnrecognisedCommand(line);
        }
    }

    /**
     * Saves the supplied tasks, replacing the previous contents of the data file.
     *
     * @param tasks The current task list.
     */
    public void save(TaskList tasks) throws IOException {
        Files.write(this.filePath, tasks.getStorageFormat());
    }
}
