package duchess.tasks.factories;

import duchess.tasks.ToDo;
import duchess.tasks.exceptions.TaskException;

/** Creates {@link ToDo} tasks from user commands. */
class TodoFactory {
    private TodoFactory() {
        // Utility class.
    }

    /**
     * Creates a to-do task from a command.
     *
     * @throws TaskException if the description is empty
     */
    static ToDo create(String command) throws TaskException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw TaskException.declareEmptyDescription("todo");
        }
        return new ToDo(description);
    }
}
