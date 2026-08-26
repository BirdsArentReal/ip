import Tasks.Exceptions.TaskException;
import Tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Scanner;

public class Duchess {
    public static void main(String[] args) {

        say(getGreeting());

        Duchess duchess;
        try {
            duchess = new Duchess();
        } catch (IOException i) {
            say("Oh no! The duchess was unable to find, nor create,\n"
                    + "the file /data/duchess.txt\n\n"
                    + "It appears she is unwelcome in the premises. :-(");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine().toLowerCase();
            CommandType type = CommandType.parse(command);
            if (type == CommandType.BYE) {
                break;
            }

            try {
                switch (type) {
                    case LIST -> {say(duchess.displayList());}
                    case MARK -> {say(duchess.handleMark(command));}
                    case UNMARK -> {say(duchess.handleUnmark(command));}
                    case TODO, DEADLINE, EVENT -> {say(duchess.handleAddTask(command));}
                    case DELETE -> {say(duchess.handleDeleteTask(command));}
                    default -> {
                        throw new DuchessException(String.format(
                                "The duchess does not understand what you mean by %s.\n"
                                        + "Please enter valid commands only.",
                                command
                    ));}
                }

                duchess.saveState();

            } catch (DuchessException | TaskException e) {
                say(e.getMessage());
            } catch (NumberFormatException n) {
                say("Error: The command " + command +
                        " only works with valid integers!");
            } catch (IOException e) {
                say("Oh no! The duchess has caught the goldfish syndrome! \n"
                        + "She is unable to remember your current list. \n"
                        + "Please check out /data/duchess.txt as soon as possible!"
                );
            }


        }

        say(getExitMessage());
        scanner.close();
    }


    final ArrayList<Task> tasks;
    final Storage db;

    Duchess() throws IOException {
        this.db = new Storage();
        this.tasks = db.load();
    }

    private static void say(String message) {
        // 40 underscores
        final String separator = "*________________________________________*";

        System.out.println(separator);
        System.out.println(message);
        System.out.println(separator);
    }

    private void saveState() throws IOException {
        this.db.save(this.tasks);
    }

    private String displayList() {
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

    private String handleAddTask(String command) throws TaskException {
        Task newTask = TaskFactory.createFromCommand(command);
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

    private String handleDeleteTask(String command) {
        String arg = command.substring(7).trim(); // after "delete "
        int idx = Integer.parseInt(arg);
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

    private String handleMark(String command) {
        String arg = command.substring(5).trim(); // after "mark "
        int idx = Integer.parseInt(arg);
        if (idx < 1 || idx > tasks.size()) {
            return "Invalid task number.";

        }
        Task t = tasks.get(idx - 1);
        t.mark();
        return "Nice! I've marked this task as done:\n  " + t;
    }

    private String handleUnmark(String command) {
        String arg = command.substring(7).trim(); // after "unmark "
        int idx = Integer.parseInt(arg);
        if (idx < 1 || idx > tasks.size()) {
            return "Invalid task number.";

        }
        Task t = tasks.get(idx - 1);
        t.unmark();
        return ("OK, I've marked this task as not done yet:\n  " + t);
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

    private static String getGreeting() {
        final String name = "Duchess";
        return getBanner()
                + "Hello! I am " + name + ". \n"
                + "What can I do for you?";
    }
}