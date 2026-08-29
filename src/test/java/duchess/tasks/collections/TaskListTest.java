package duchess.tasks.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import duchess.tasks.Task;
import duchess.tasks.ToDo;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/** Tests task-list operations and their user-visible results. */
class TaskListTest {

    @Test
    void emptyList_isEmptyAndPrintsNoTasksMessage() {
        TaskList taskList = new TaskList(new ArrayList<>());

        assertTrue(taskList.isEmpty());
        assertEquals("You have no tasks pending.", taskList.getTasksToPrint());
    }

    @Test
    void addTask_validTask_addsTaskAndReturnsConfirmation() {
        TaskList taskList = new TaskList(new ArrayList<>());

        String response = taskList.addTask(new ToDo("read book"));

        assertFalse(taskList.isEmpty());
        assertEquals("Got it. I've added this task:\n"
                        + "  [T][ ] read book\n"
                        + "Now you have 1 task in the list.",
                response);
        assertEquals(" 1. [T][ ] read book", taskList.getTasksToPrint());
    }

    @Test
    void deleteTaskFromIndex_validIndex_removesTask() {
        TaskList taskList = taskListWith(new ToDo("first"), new ToDo("second"));

        String response = taskList.deleteTaskFromIndex(1);

        assertEquals("Noted. I've removed this task:\n"
                        + "[T][ ] first\n"
                        + "Now you have 1 task in the list.",
                response);
        assertEquals(" 1. [T][ ] second", taskList.getTasksToPrint());
    }

    @Test
    void deleteTaskFromIndex_invalidIndex_leavesListUnchanged() {
        TaskList taskList = taskListWith(new ToDo("read book"));

        assertEquals("Invalid task number.", taskList.deleteTaskFromIndex(0));
        assertEquals("Invalid task number.", taskList.deleteTaskFromIndex(2));
        assertEquals(" 1. [T][ ] read book", taskList.getTasksToPrint());
    }

    @Test
    void markTaskAt_validIndex_marksTask() {
        TaskList taskList = taskListWith(new ToDo("read book"));

        String response = taskList.markTaskAt(1);

        assertEquals("Nice! I've marked this task as done:\n  [T][X] read book", response);
        assertEquals(" 1. [T][X] read book", taskList.getTasksToPrint());
    }

    @Test
    void unmarkTaskAt_validIndex_unmarksTask() {
        ToDo task = new ToDo("read book");
        task.mark();
        TaskList taskList = taskListWith(task);

        String response = taskList.unmarkTaskAt(1);

        assertEquals("OK, I've marked this task as not done yet:\n  [T][ ] read book", response);
        assertEquals(" 1. [T][ ] read book", taskList.getTasksToPrint());
    }

    @Test
    void markAndUnmarkTaskAt_invalidIndex_leaveTaskUnchanged() {
        TaskList taskList = taskListWith(new ToDo("read book"));

        assertEquals("Invalid task number.", taskList.markTaskAt(0));
        assertEquals("Invalid task number.", taskList.unmarkTaskAt(2));
        assertEquals(" 1. [T][ ] read book", taskList.getTasksToPrint());
    }

    private TaskList taskListWith(Task... tasks) {
        return new TaskList(new ArrayList<>(Arrays.asList(tasks)));
    }
}
