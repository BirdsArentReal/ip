package duchess.parse;

/**
 * Defines the commands that the Duchess is able to understand.
 * <p> Additionally, parses the input string to determine which command is being called.
 *
 */
public enum CommandType {
    LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, RECURRING,
            FIND, FIND_EXACT, BYE, UNKNOWN;

    /**
     * Parses the input string to determine which command is being called.
     * <p> Returns {@code UNKNOWN} if the command is not one of the predefined types.
     *
     * @param input The command entered by the user.
     * @return The CommandType corresponding to the command.
     */
    public static CommandType parse(String input) {
        if (input == null) {
            return UNKNOWN;
        }
        String commandLower = input.stripLeading().toLowerCase();

        // Commands with additional fields
        if (commandLower.startsWith("mark ")) {
            return MARK;
        }
        if (commandLower.startsWith("unmark ")) {
            return UNMARK;
        }
        if (commandLower.startsWith("delete ")) {
            return DELETE;
        }
        if (commandLower.startsWith("todo ")) {
            return TODO;
        }
        if (commandLower.startsWith("deadline ")) {
            return DEADLINE;
        }
        if (commandLower.startsWith("event ")) {
            return EVENT;
        }
        if (commandLower.startsWith("recurring ")) {
            return RECURRING;
        }
        if (commandLower.startsWith("find -e ")) {
            return FIND_EXACT;
        }
        if (commandLower.startsWith("find ")) {
            return FIND;
        }

        // Commands without additional fields
        if (commandLower.equals("list")) {
            return LIST;
        }
        if (commandLower.equals("bye")) {
            return BYE;
        }

        // Unrecognized commands.
        return UNKNOWN;
    }

    /**
     * Checks whether the command is one of those which will change
     * the tasks being stored.
     *
     * @param commandType The type of command.
     * @return {@code true}, if the command will make changes to the tasks.
     *          <p> {@code false}, otherwise.
     */
    public static boolean isMutator(CommandType commandType) {
        assert (commandType != null) : "Command type cannot be null";

        return switch (commandType) {
            case MARK, UNMARK, DELETE,
                 TODO, DEADLINE, EVENT, RECURRING -> true;

            case LIST, FIND, FIND_EXACT, UNKNOWN, BYE -> false;
        };
    }
}
