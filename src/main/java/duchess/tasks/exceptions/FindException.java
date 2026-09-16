package duchess.tasks.exceptions;

/** Represents errors related to the find command. */
public class FindException extends TaskException {
    private FindException(String message) {
        super(message);
    }

    /** Creates an exception for a find command without a search keyword. */
    public static FindException declareEmptySearchKeyword() {
        return new FindException("Please provide a keyword to find.");
    }
}
