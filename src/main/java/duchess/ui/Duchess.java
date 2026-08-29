package duchess.ui;

import duchess.io.Storage;
import duchess.tasks.TaskFactory;
import duchess.tasks.collections.TaskList;
import duchess.tasks.exceptions.TaskException;
import duchess.tasks.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Scanner;

import duchess.parse.CommandType;
import duchess.ui.exceptions.DuchessException;

public class Duchess {
    public static void main(String[] args) {
        Duchess duchess;
        try {
            duchess = new Duchess("data", "duchess.txt");
        } catch (IOException i) {
            System.out.println("Oh no! The duchess was unable to find, nor create,\n"
                    + "the file /data/duchess.txt\n\n"
                    + "It appears she is unwelcome in the premises. :-(");
            return;
        }

        duchess.run();


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

    private final TaskList tasks;
    private final Storage db;
    private final Ui ui;

    Duchess() throws IOException {
        this("data", "duchess.txt");
    }

    Duchess(String directory, String filepath) throws IOException {
        this.db = new Storage(Path.of(directory, filepath));
        this.tasks = new TaskList(db.load());
        this.ui = new Ui(Duchess.name, Duchess.banner, Duchess.separator);
    }

    public void run() {
        this.greet();

        while (true) {
            String command = this.ui.listen();
            CommandType type = CommandType.parse(command);
            if (type == CommandType.BYE) {
                break;
            }

            try {
                String response = switch (type) {
                    case LIST -> this.displayList();
                    case MARK -> this.handleMark(command);
                    case UNMARK -> this.handleUnmark(command);
                    case TODO, DEADLINE, EVENT -> this.handleAddTask(command);
                    case DELETE -> this.handleDeleteTask(command);
                    default -> throw new DuchessException(String.format(
                            "The duchess does not understand what you mean by %s.\n"
                                    + "Please enter valid commands only.",
                            command));
                };

                this.ui.say(response);

                if (CommandType.isMutator(type)) {
                    this.saveState();
                }

            } catch (DuchessException | TaskException e) {
                this.ui.say(e.getMessage());
            } catch (NumberFormatException n) {
                this.ui.say("Error: The command " + command +
                        " only works with valid integers!");
            } catch (IOException e) {
                this.ui.say("Oh no! The duchess has caught the goldfish syndrome! \n"
                        + "She is unable to remember your current list. \n"
                        + "Please check out /data/duchess.txt as soon as possible!"
                );
            }
        }

        this.exit();
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
        return this.tasks.getTasksToPrint();
    }

    private String handleAddTask(String command) throws TaskException {
        Task newTask = TaskFactory.createFromCommand(command);
        return this.tasks.addTask(newTask);
    }

    private String handleDeleteTask(String command) {
        String arg = command.substring(7).trim(); // after "delete "
        int idx = Integer.parseInt(arg);
        return this.tasks.deleteTaskFromIndex(idx);
    }

    private String handleMark(String command) {
        String arg = command.substring(5).trim(); // after "mark "
        int idx = Integer.parseInt(arg);
        return this.tasks.markTaskAt(idx);
    }

    private String handleUnmark(String command) {
        String arg = command.substring(7).trim(); // after "unmark "
        int idx = Integer.parseInt(arg);
        return this.tasks.unmarkTaskAt(idx);
    }


}