package duchess.tasks.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import duchess.tasks.Task;
import duchess.util.Pair;

/**
 * Handles the list of tasks and any requests related to them.
 */
public class TaskList {
    private static final String INVALID_TASK_NUMBER_MESSAGE =
            "Invalid task number.";
    private final ArrayList<Task> tasks;

    /**
     * Creates a new TaskList containing the specified tasks.
     *
     * @param tasks The list of tasks to be stored in the TaskList.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Creates a new TaskList containing  the specified tasks.
     *
     * @param tasks The tasks to be stored in the TaskList.
     */
    public TaskList(Task... tasks) {
        this.tasks = new ArrayList<>();

        this.tasks.addAll(Arrays.asList(tasks));
    }

    /**
     * Given an index and a task, display them
     * in the specified form.
     */
    private static String displayTaskWithIndex(int index, Task task) {
        return String.format(" %d. %s", index, task);
    }

    /**
     * Returns "s" if {@code tasks.size()} is 0, or is 2 or more.
     * Otherwise, if {@code tasks.size()} is exactly 1, return "".
     */
    private String displayTaskPluralOrSingular() {
        if (this.tasks.size() == 1) {
            return "";
        } else {
            return "s";
        }
    }

    /**
     * Checks if index is a valid index, in a 1-indexing of {@code tasks}.
     */
    private boolean isValidIndex(int index) {
        return ((index >= 1) && (index <= this.tasks.size()));
    }

    /**
     * Checks if there are any tasks being stored.
     *
     * @return true, if there is at least one task being stored. <br>
     *          false, otherwise.
     */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    /**
     * Adds a new task to the list of tasks.
     *
     * @param newTask The task to be added.
     * @return A string representing the changes to the task list.
     */
    public String addTask(Task newTask) {
        assert (newTask != null) : "A task added to tasklist cannot be null.";

        if (this.tasks.stream()
                .map(Task::toString)
                .anyMatch(str -> str.equals(newTask.toString()))) {
            return "An indistinguishable task already exists!";
        }

        if (this.tasks.stream()
                .map(Task::toString)
                .anyMatch(str -> str.equals(newTask.toString()))) {
            return "An indistinguishable task already exists!";
        }

        this.tasks.add(newTask);

        return String.format(
                "Got it. I've added this task:\n"
                        + "  %s\n"
                        + "Now you have %s task%s in the list.",
                newTask,
                this.tasks.size(),
                this.displayTaskPluralOrSingular()
        );
    }

    /**
     * Deletes a task from the task list.
     *
     * @param idx The index of the task to be deleted.
     * @return On success, a string representing the new state of the task list. <br>
     *          On failure, a string representing the failed operation.
     */
    public String deleteTaskFromIndex(int idx) {
        if (!isValidIndex(idx)) {
            return INVALID_TASK_NUMBER_MESSAGE;
        }
        Task t = this.tasks.remove(idx - 1);

        assert (!this.tasks.contains(t)) : "Task not removed from tasklist";

        return String.format(
                "Noted. I've removed this task:\n"
                        + "%s\n"
                        + "Now you have %d task%s in the list.",
                t,
                this.tasks.size(),
                this.displayTaskPluralOrSingular()
        );
    }

    /**
     * Marks a task on the task list as complete.
     *
     * @param idx The index of the task to be marked complete.
     * @return On success, a string representing the changed task. <br>
     *          On failure, a string representing the failed operation.
     */
    public String markTaskAt(int idx) {
        if (!isValidIndex(idx)) {
            return INVALID_TASK_NUMBER_MESSAGE;
        }

        Task t = this.tasks.get(idx - 1);
        t.mark();

        /*
         check that t has been marked using the toString,
         because we cannot directly access the state.
        */
        assert (t.toString().contains("[X] ")) : "Marked task should be complete";

        return "Nice! I've marked this task as done:\n  " + t;
    }

    /**
     * Marks a task on the task list as incomplete.
     *
     * @param idx The index of the task to be marked incomplete.
     * @return On success, a string representing the changed task. <br>
     *          On failure, a string representing the failed operation.
     */
    public String unmarkTaskAt(int idx) {
        if (!isValidIndex(idx)) {
            return INVALID_TASK_NUMBER_MESSAGE;
        }

        Task t = this.tasks.get(idx - 1);
        t.unmark();

        /*
         check that t is not marked using the toString,
         because we cannot directly access the state.
        */
        assert (t.toString().contains("[ ] ")) : "Unmarked task should be incomplete";

        return "OK, I've marked this task as not done yet:\n  " + t;
    }

    /**
     * Returns a list representing the tasks in storage format.
     *
     * @return The list of tasks in storage format.
     */
    public List<String> getStorageFormat() {
        // convert tasks to strings for storage
        List<String> storageFormat = this.tasks.stream()
                .map(Task::getStorageFormat)
                .toList();

        assert (storageFormat.size() == this.tasks.size())
                : "Each task should produce exactly one storage record";

        return storageFormat;
    }

    /**
     * Returns a numbered display list of tasks whose descriptions contain the supplied keyword.
     *
     * @param keywords the case-insensitive search terms
     * @return the matching tasks, or a message when no tasks match
     */
    public String getTasksMatching(String... keywords) {
        String results = IntStream.range(0, this.tasks.size())
                .mapToObj(index ->
                        new Pair<>(index, this.tasks.get(index)))
                .filter(pair ->
                        Arrays.stream(keywords)
                        .allMatch(keyword ->
                                pair.getSecond().containsKeyword(keyword)))
                .map(pair ->
                        TaskList.displayTaskWithIndex(
                                pair.getFirst() + 1,
                                pair.getSecond()))
                .collect(Collectors.joining("\n"));

        if (results.isEmpty()) {
            return "There are no matching tasks in your list.";
        }

        return results;
    }

    /**
     * Returns a list representing the tasks in user-readable format.
     * @return The list of tasks in user-readable format.
     */
    public String getTasksToPrint() {
        if (this.tasks.isEmpty()) {
            return "You have no tasks pending.";
        }

        return IntStream.range(0, this.tasks.size())
                .<String>mapToObj(index ->
                        TaskList.displayTaskWithIndex(
                                index + 1,
                                this.tasks.get(index)))
                .collect(Collectors.joining("\n"));
    }

}
