package duchess.tasks;

import java.time.format.DateTimeFormatter;

/**
 * Stores the DateTimeFormatter used for parsing the displaying
 * dates.
 */
public final class DateFormat {

    /** Represents the date in creation and storage. */
    public static final DateTimeFormatter PARSE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** Represents the date when displayed to the user. */
    public static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");


    // DateFormat should not be initializable.
    private DateFormat() {
    }



}
