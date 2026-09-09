package duchess.io.exceptions;

/**
 * Represents the exceptions that may be encountered
 * when loading or saving tasks.
 */
public class StorageException extends Exception {
    public StorageException(String message) {
        super(message);
    }
}
