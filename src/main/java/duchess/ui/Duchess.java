package duchess.ui;

import java.io.IOException;
import java.nio.file.Path;

import duchess.io.Storage;
import duchess.parse.CommandType;
import duchess.tasks.Task;
import duchess.tasks.TaskFactory;
import duchess.tasks.collections.TaskList;
import duchess.tasks.exceptions.TaskException;
import duchess.ui.exceptions.DuchessException;

/**
 * Represents the chatbot.
 */
public class Duchess {
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
    private final IUi ui;

    /**
     * Creates a new chatbot, with the location
     * of the storage file.
     *
     * @param directory The subdirectory containing the storage file.
     * @param filepath The remaining address to the storage file.
     * @throws IOException If the storage file could not be found nor created.
     */
    Duchess(String directory, String filepath) throws IOException {
        this.db = new Storage(Path.of(directory, filepath));
        this.tasks = new TaskList(db.load());
        this.ui = new Cli(Duchess.name, Duchess.banner, Duchess.separator);
        // this.ui = new Gui(Duchess.name);
    }

    /**
     * Runs the duchess chatbot.
     */
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
                    case FIND -> this.handleFind(command);
                    case FINDEXACT -> this.handleFindExact(command);
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
                this.ui.say("Error: The command " + command
                        + " only works with valid integers!");
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

    /**
     * Saves the list of tasks to the storage file.
     * @throws IOException If unable to save to the file.
     */
    private void saveState() throws IOException {
        this.db.save(this.tasks);
    }

    /**
     * Returns a string representing the list of tasks
     * in user-readable format.
     */
    private String displayList() {
        return this.tasks.getTasksToPrint();
    }

    /**
     * Creates a new task from the user command, and
     * adds it to the list of tasks.
     *
     * @param command The user input.
     * @return A string representing the
     *          changes to the list of tasks.
     * @throws TaskException If the task could not be
     *                          created from the command.
     */
    private String handleAddTask(String command) throws TaskException {
        Task newTask = TaskFactory.createFromCommand(command);
        return this.tasks.addTask(newTask);
    }

    /**
     * Deletes a task from the list of tasks.
     *
     * @param command The user input.
     * @return A string representing the result of the deletion.
     */
    private String handleDeleteTask(String command) {
        String arg = command.substring(7).trim(); // after "delete "
        int idx = Integer.parseInt(arg);
        return this.tasks.deleteTaskFromIndex(idx);
    }

    /**
     * Marks a task as complete.
     *
     * @param command The user input.
     * @return A string representing
     *          the task marked as complete.
     */
    private String handleMark(String command) {
        String arg = command.substring(5).trim(); // after "mark "
        int idx = Integer.parseInt(arg);
        return this.tasks.markTaskAt(idx);
    }

    /**
     * Marks a task as incomplete.
     *
     * @param command The user input.
     * @return A string representing the task
     *          marked as incomplete.
     */
    private String handleUnmark(String command) {
        String arg = command.substring(7).trim(); // after "unmark "
        int idx = Integer.parseInt(arg);
        return this.tasks.unmarkTaskAt(idx);
    }

    /** Finds tasks whose descriptions contain the keyword supplied after the find command. */
    private String handleFind(String command) throws TaskException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw TaskException.declareEmptySearchKeyword();
        }

        return this.tasks.getTasksMatching(keyword.split(" ", -1));
    }

    private String handleFindExact(String command) throws TaskException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw TaskException.declareEmptySearchKeyword();
        }

        return this.tasks.getTasksMatching(keyword);

    }


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


}
