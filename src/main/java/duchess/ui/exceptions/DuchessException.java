package duchess.ui.exceptions;

public class DuchessException extends Exception {
    public DuchessException(String message) {
        super("Ohoho! " + message);
    }
}
