package duchess.ui;

import duchess.io.Storage;
import duchess.tasks.TaskFactory;
import duchess.tasks.exceptions.TaskException;
import duchess.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Scanner;

import duchess.parse.CommandType;
import duchess.ui.exceptions.DuchessException;

public class Duchess {
    public static void main(String[] args) {
        Duchess duchess;
        try {
            duchess = new Duchess();
        } catch (IOException i) {
            System.out.println("Oh no! The duchess was unable to find, nor create,\n"
                    + "the file /data/duchess.txt\n\n"
                    + "It appears she is unwelcome in the premises. :-(");
            return;
        }

        duchess.greet();

        while (true) {
            String command = duchess.ui.listen();
            CommandType type = CommandType.parse(command);
            if (type == CommandType.BYE) {
                break;
            }

            try {
                String response = switch (type) {
                    case LIST -> duchess.displayList();
                    case MARK -> duchess.handleMark(command);
                    case UNMARK -> duchess.handleUnmark(command);
                    case TODO, DEADLINE, EVENT -> duchess.handleAddTask(command);
                    case DELETE -> duchess.handleDeleteTask(command);
                    default -> throw new DuchessException(String.format(
                            "The duchess does not understand what you mean by %s.\n"
                                    + "Please enter valid commands only.",
                            command));
                };

                duchess.ui.say(response);

                if (CommandType.isMutator(type)) {
                    duchess.saveState();
                }

            } catch (DuchessException | TaskException e) {
                duchess.ui.say(e.getMessage());
            } catch (NumberFormatException n) {
                duchess.ui.say("Error: The command " + command +
                        " only works with valid integers!");
            } catch (IOException e) {
                duchess.ui.say("Oh no! The duchess has caught the goldfish syndrome! \n"
                        + "She is unable to remember your current list. \n"
                        + "Please check out /data/duchess.txt as soon as possible!"
                );
            }


        }

        duchess.exit();
    }

    private static final String banner =
            " ____  _   _  _____    _ ____  ___  ___\n"
            + "|  _ \\| | | |/ __/ |  | | ___|/   \\/   \\\n"
            + "| | | | | | | |  | |__| | |__|  | |  | |\n"
            + "| | | | | | | |  |  __  |  __|\\  \\/\\  \\/\n"
            + "| |_| | \\_/ | |__| |  | | |__ /\\  \\/\\  \\\n"
            + "|____/ \\___/ \\___\\_|  |_|____|\\___/\\___/\n";
    private static final String name = "Duchess";

    // 40 underscores
    private static final String separator = "*________________________________________*";


    private final ArrayList<Task> tasks;
    private final Storage db;
    private final Ui ui;

    Duchess() throws IOException {
        this.db = new Storage();
        this.tasks = db.load();
        this.ui = new Ui(Duchess.name, Duchess.banner, Duchess.separator);
    }

    private void greet() {
        this.ui.greet();
    }

    private void exit() {
        this.ui.exit();
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


}