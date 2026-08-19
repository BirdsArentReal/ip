import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Scanner;

public class Duchess {
    static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {

        say(getGreeting());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine().toLowerCase();

            if (command.equals("bye")) {
                break;
            }

            if (command.equals("list")) {
                say(displayList(Duchess.tasks));
            } else if (command.startsWith("mark ")) {
                handleMark(command, Duchess.tasks);
            } else if (command.startsWith("unmark ")) {
                handleUnmark(command, Duchess.tasks);
            } else if (TaskFactory.isTaskCommand(command)) {
                tasks.add(TaskFactory.createFromCommand(command));
            } else {
                say("Error: Unrecognised input");
            }

        }

        say(getExitMessage());

        scanner.close();
    }

    private static void say(String message) {
        // 40 underscores
        final String separator = "*________________________________________*";

        System.out.println(separator);
        System.out.println(message);
        System.out.println(separator);
    }

    private static String displayList(ArrayList<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(". ").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private static void handleMark(String command, ArrayList<Task> tasks) {
        String arg = command.substring(5).trim(); // after "mark "
        if (idx < 1 || idx > tasks.size()) {
            say("Invalid task number.");
            return;
        }
        Task t = tasks.get(idx - 1);
        t.mark();
        say("Nice! I've marked this task as done:\n  " + t);
    }

    private static void handleUnmark(String command, ArrayList<Task> tasks) {
        String arg = command.substring(7).trim(); // after "unmark "
        if (idx < 1 || idx > tasks.size()) {
            say("Invalid task number.");
            return;
        }
        Task t = tasks.get(idx - 1);
        t.unmark();
        say("OK, I've marked this task as not done yet:\n  " + t);
    }

    private static String getGreeting() {
        final String name = "Duchess";
        return getBanner()
                + "Hello! I am " + name + ". \n"
                + "What can I do for you?";
    }

    private static String getExitMessage() {
        return "Bye! Hope to see you again soon!";
    }

    private static String getBanner() {
        return    " ____  _   _  _____    _ ____  ___  ___\n"
                + "|  _ \\| | | |/ __/ |  | | ___|/   \\/   \\\n"
                + "| | | | | | | |  | |__| | |__|  | |  | |\n"
                + "| | | | | | | |  |  __  |  __|\\  \\/\\  \\/\n"
                + "| |_| | \\_/ | |__| |  | | |__ /\\  \\/\\  \\\n"
                + "|____/ \\___/ \\___\\_|  |_|____|\\___/\\___/\n";

    }
}