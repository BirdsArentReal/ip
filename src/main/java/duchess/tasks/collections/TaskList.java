package duchess.tasks.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import duchess.tasks.Task;

/**
 * Handles the list of tasks and any requests related to them.
 */
public class TaskList {
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
     * Checks if there are any tasks being stored.
     *
     * @return true, if there is at least one task being stored. <br>
     *          false, otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Adds a new task to the list of tasks.
     *
     * @param newTask The task to be added.
     * @return A string representing the changes to the task list.
     */
    public String addTask(Task newTask) {
        tasks.add(newTask);

        return String.format(
                "Got it. I've added this task:\n"
                        + "  %s\n"
                        + "Now you have %s task%s in the list.",
                newTask,
                tasks.size(),
                tasks.size() == 1 ? "" : "s"
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
        if (idx < 1 || idx > tasks.size()) {
            return "Invalid task number.";
        }
        Task t = tasks.remove(idx - 1);
        return String.format(
                "Noted. I've removed this task:\n"
                        + "%s\n"
                        + "Now you have %d task%s in the list.",
                t,
                tasks.size(),
                tasks.size() == 1 ? "" : "s"
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
        if (idx < 1 || idx > tasks.size()) {
            return "Invalid task number.";
        }

        Task t = tasks.get(idx - 1);
        t.mark();
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
        if (idx < 1 || idx > tasks.size()) {
            return "Invalid task number.";
        }

        Task t = tasks.get(idx - 1);
        t.unmark();
        return ("OK, I've marked this task as not done yet:\n  " + t);
    }

    /**
     * Returns a list representing the tasks in storage format.
     *
     * @return The list of tasks in storage format.
     */
    public List<String> getStorageFormat() {
        // convert tasks to strings for storage
        return tasks.stream().map(Task::getStorageFormat).toList();
    }

    /**
     * Returns a numbered display list of tasks whose descriptions contain the supplied keyword.
     *
     * @param keywords the case-insensitive search terms
     * @return the matching tasks, or a message when no tasks match
     */
    public String getTasksMatching(String... keywords) {
        Stream<Task> matchingTasks = tasks.stream();

        for (String keyword: keywords) {
            System.out.println(keyword);
            matchingTasks = matchingTasks.filter(task ->
                    task.containsKeyword(keyword));
        }

        List<String> results = matchingTasks.map(Task::toString).toList();

        if (results.isEmpty()) {
            return "There are no matching tasks in your list.";
        }

        StringBuilder result = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < results.size(); i++) {
            result.append(" ").append(i + 1).append(". ").append(results.get(i));
            if (i < results.size() - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    /**
     * Returns a list representing the tasks in user-readable format.
     * @return The list of tasks in user-readable format.
     */
    public String getTasksToPrint() {
        if (tasks.isEmpty()) {
            return "You have no tasks pending.";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(". ").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

}
