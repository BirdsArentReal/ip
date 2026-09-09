package duchess.parse;

/**
 * Defines the commands that the Duchess is able to understand.
 * <p> Additionally, parses the input string to determine which command is being called.
 *
 */
public enum CommandType {
    LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, FIND_EXACT, BYE, UNKNOWN;

    /**
     * Parses the input string to determine which command is being called.
     * <p> Returns {@code UNKNOWN} if the command is not one of the predefined types.
     *
     * @param input The command entered by the user.
     * @return The CommandType corresponding to the command.
     */
    public static CommandType parse(String input) {
        input = input.stripLeading().toLowerCase();
        if (input == null) {
            return UNKNOWN;
        }

        // Commands with additional fields
        if (input.startsWith("mark ")) {
            return MARK;
        }
        if (input.startsWith("unmark ")) {
            return UNMARK;
        }
        if (input.startsWith("delete ")) {
            return DELETE;
        }
        if (input.startsWith("todo ")) {
            return TODO;
        }
        if (input.startsWith("deadline ")) {
            return DEADLINE;
        }
        if (input.startsWith("event ")) {
            return EVENT;
        }
        if (input.startsWith("find -e ")) {
            return FIND_EXACT;
        }
        if (input.startsWith("find ")) {
            return FIND;
        }

        // Commands without additional fields
        if (input.equals("list")) {
            return LIST;
        }
        if (input.equals("bye")) {
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
                 TODO, DEADLINE, EVENT -> true;

            case LIST, FIND, FIND_EXACT, UNKNOWN, BYE -> false;
        };
    }
}
