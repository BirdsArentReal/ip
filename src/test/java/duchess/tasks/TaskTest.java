package duchess.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Tests the completion-state behaviour shared by all tasks. */
class TaskTest {
    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static LocalDate dummyDate = LocalDate.parse("1000-12-13", pattern);

    @Test
    public void todoCreation_InitiallyUnmarked() {
        Task task = new ToDo("test");
        assertTrue(task.toString().startsWith("[T][ ]"));
    }

    @Test
    public void deadlineCreation_InitiallyUnmarked() {
        Deadline task = new Deadline("test", dummyDate);
        assertTrue(task.toString().startsWith("[D][ ]"));
    }

    @Test
    public void eventCreation_InitiallyUnmarked() {
        Event task = new Event("test", dummyDate, dummyDate);
        assertTrue(task.toString().startsWith("[E][ ]"));
    }

    @Test
    public void markAndUnmark_updatesDisplayedCompletionState() {
        Task task = new ToDo("read book");

        assertEquals("[T][ ] read book", task.toString());

        task.mark();
        assertEquals("[T][X] read book", task.toString());

        task.unmark();
        assertEquals("[T][ ] read book", task.toString());
    }


}
