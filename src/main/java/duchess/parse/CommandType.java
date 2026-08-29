package duchess.parse;

/**
 * Defines the commands that the Duchess is able to understand. <br>
 * Additionally, parses the input string to determine which command is being called.
 */
public enum CommandType {
    LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, FINDEXACT, BYE, UNKNOWN;

    /**
     * Parses the input string to determine which command is being called. <br>
     * Returns UNKNOWN if the command is not one of the predefined types.
     *
     * @param input The command entered by the user.
     * @return The CommandType corresponding to the command.
     */
    public static CommandType parse(String input) {
        if (input == null) return UNKNOWN;

        String s = input.stripLeading().toLowerCase();
        if (s.startsWith("mark ")) return MARK;
        if (s.startsWith("unmark ")) return UNMARK;
        if (s.startsWith("delete ")) return DELETE;
        if (s.startsWith("todo ")) return TODO;
        if (s.startsWith("deadline ")) return DEADLINE;
        if (s.startsWith("event ")) return EVENT;
        if (s.startsWith("find -e ")) return FINDEXACT;
        if (s.startsWith("find ")) return FIND;

        s = s.trim();
        if (s.equals("list")) return LIST;
        if (s.equals("bye")) return BYE;
        return UNKNOWN;
    }

    /**
     * Checks whether the command is one of those which will change
     * the tasks being stored.
     *
     * @param type The type of command.
     * @return true, if the command will make changes to the tasks. <br>
     *          false, otherwise.
     */
    public static boolean isMutator(CommandType type) {
        return switch (type) {
            // mutators
            case MARK, UNMARK, DELETE,
                 TODO, DEADLINE, EVENT -> true;

            // not mutators
            case LIST, FIND, FINDEXACT, UNKNOWN, BYE -> false;
        };
    }
}
