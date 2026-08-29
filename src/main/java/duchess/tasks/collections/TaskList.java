package duchess.tasks.collections;

import duchess.io.Storage;
import duchess.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public String addTask(Task newTask) {
        tasks.add(newTask);

        return String.format(
                "Got it. I've added this task:\n" +
                        "  %s\n" +
                        "Now you have %s task%s in the list.",
                newTask,
                tasks.size(),
                tasks.size() == 1 ? "" : "s"
        );
    }

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

    public String markTaskAt(int idx) {
        if (idx < 1 || idx > tasks.size()) {
            return "Invalid task number.";
        }

        Task t = tasks.get(idx - 1);
        t.mark();
        return "Nice! I've marked this task as done:\n  " + t;
    }

    public String unmarkTaskAt(int idx) {
        if (idx < 1 || idx > tasks.size()) {
            return "Invalid task number.";
        }

        Task t = tasks.get(idx - 1);
        t.unmark();
        return ("OK, I've marked this task as not done yet:\n  " + t);
    }

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

        for (String keyword: keywords){
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
