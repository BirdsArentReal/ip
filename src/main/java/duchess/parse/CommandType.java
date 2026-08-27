package duchess.parse;

public enum CommandType {
    LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, BYE, UNKNOWN;

    public static CommandType parse(String input) {
        if (input == null) return UNKNOWN;

        String s = input.stripLeading().toLowerCase();
        if (s.startsWith("mark ")) return MARK;
        if (s.startsWith("unmark ")) return UNMARK;
        if (s.startsWith("delete ")) return DELETE;
        if (s.startsWith("todo ")) return TODO;
        if (s.startsWith("deadline ")) return DEADLINE;
        if (s.startsWith("event ")) return EVENT;

        s = s.trim();
        if (s.equals("list")) return LIST;
        if (s.equals("bye")) return BYE;
        return UNKNOWN;
    }

    public static boolean isMutator(CommandType type) {
        return switch (type) {
            // mutators
            case MARK, UNMARK, DELETE,
                 TODO, DEADLINE, EVENT -> true;

            // not mutators
            case LIST, UNKNOWN, BYE -> false;
        };
    }
}
