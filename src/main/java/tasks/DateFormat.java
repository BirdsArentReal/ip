package tasks;

import java.time.format.DateTimeFormatter;

public final class DateFormat {

    public static final DateTimeFormatter PARSE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");


    // DateFormat should not be initializable.
    private DateFormat() {
    }



}
