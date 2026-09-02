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
    public static final String NAME = "Duchess";

    private final TaskList tasks;
    private final Storage db;

    /**
     * Creates a new chatbot, with the location
     * of the storage file.
     *
     * @param directory The subdirectory containing the storage file.
     * @param filepath The remaining address to the storage file.
     * @throws IOException If the storage file could not be found nor created.
     */
    public Duchess(String directory, String filepath) throws IOException {
        this.db = new Storage(Path.of(directory, filepath));
        this.tasks = new TaskList(db.load());
    }

    /**
     * Returns the exit message for duchess.
     * For UI to compare the response strings to, to know
     * when to close the application.
     */
    public static String getExitMessage() {
        return "Bye! See you again! :^D";
    }

    /**
     * Runs the duchess chatbot.
     */
    public String respondTo(String userInput) {
        CommandType type = CommandType.parse(userInput);
        if (type == CommandType.BYE) {
            return Duchess.getExitMessage();
        }

        try {
            String response = switch (type) {
                case LIST -> this.displayList();
                case MARK -> this.handleMark(userInput);
                case UNMARK -> this.handleUnmark(userInput);
                case TODO, DEADLINE, EVENT -> this.handleAddTask(userInput);
                case DELETE -> this.handleDeleteTask(userInput);
                case FIND -> this.handleFind(userInput);
                case FINDEXACT -> this.handleFindExact(userInput);
                default -> throw new DuchessException(String.format(
                        "The duchess does not understand what you mean by %s.\n"
                                + "Please enter valid commands only.",
                        userInput));
            };

            if (CommandType.isMutator(type)) {
                this.saveState();
            }
            return response;

        } catch (DuchessException | TaskException e) {
            return e.getMessage();
        } catch (NumberFormatException n) {
            return "Error: The command " + userInput
                    + " only works with valid integers!";
        } catch (IOException e) {
            return "Oh no! The duchess has caught the goldfish syndrome! \n"
                    + "She is unable to remember your current list. \n"
                    + "Please check out /data/duchess.txt as soon as possible!";
        }
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

    /**
     * Finds tasks whose descriptions contain the keyword supplied after the find command.
     * Text separated by spaces are treated as different keywords, and the result
     * only contains tasks whose description match all keywords.
     */
    private String handleFind(String command) throws TaskException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw TaskException.declareEmptySearchKeyword();
        }

        return this.tasks.getTasksMatching(keyword.split(" ", -1));
    }

    /**
     * Finds tasks whose descriptions contain the keyword supplied after the find command.
     * The entire string after the -e flag is treated as one keyword, and
     * the entire keywords must be contained in the task description as
     * one continuous string, for the task to be displayed.
     */
    private String handleFindExact(String command) throws TaskException {
        String keyword = command.substring("find -e".length()).trim();
        if (keyword.isEmpty()) {
            throw TaskException.declareEmptySearchKeyword();
        }

        return this.tasks.getTasksMatching(keyword);

    }
}
