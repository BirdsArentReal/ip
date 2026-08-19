import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Scanner;

public class Duchess {
    static final ArrayList<Task> task = new ArrayList<>();

    public static void main(String[] args) {

        say(getGreeting());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("bye")) {
                break;
            } else if (command.equalsIgnoreCase("list")) {
                say(displayList(Duchess.arr));
            } else {
                say("added: " + command);
                task.add(new Task(command));
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
        return
                " ____  _   _  _____    _ ____  ___  ___\n"
                        + "|  _ \\| | | |/ __/ |  | | ___|/   \\/   \\\n"
                        + "| | | | | | | |  | |__| | |__|  | |  | |\n"
                        + "| | | | | | | |  |  __  |  __|\\  \\/\\  \\/\n"
                        + "| |_| | \\_/ | |__| |  | | |__ /\\  \\/\\  \\\n"
                        + "|____/ \\___/ \\___\\_|  |_|____|\\___/\\___/\n";

    }
}