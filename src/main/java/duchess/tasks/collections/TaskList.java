package duchess.tasks.collections;

import duchess.io.Storage;
import duchess.tasks.Task;

import java.util.ArrayList;
import java.util.List;

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
