package tests;

import filemanager.TaskType;
import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest extends InMemoryHistoryManager {

//    HistoryManager manager = createHistoryManager();
//
//    @BeforeEach
//    protected HistoryManager createHistoryManager() {
//        return new InMemoryHistoryManager();
//    }

    @Test
    void addHistoryAndRemoveHistoryAndGetHistoryTest() {
        HistoryManager manager = new InMemoryHistoryManager();
        Epic epic = new Epic(1, TaskType.EPIC, "new epic", "test description", Status.NEW);
        Subtask s1 = new Subtask(2, TaskType.SUBTASK, "subtask 1", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 20, 0), Duration.ofMinutes(30));
        Subtask s2 = new Subtask(3, TaskType.SUBTASK, "subtask 2", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        Subtask s3 = new Subtask(4, TaskType.SUBTASK, "subtask 3", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 19, 0), Duration.ofMinutes(45));
        manager.add(epic);
        manager.add(s1);
        manager.add(s2);
        manager.add(s3);
        assertEquals(4, manager.getHistory().size());

        manager.removeFromHistory(epic);

        assertEquals(3, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(epic));

        manager.removeFromHistory(s3);

        assertEquals(2, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(s3));

        manager.add(epic);
        manager.add(s3);
        manager.removeFromHistory(s2);

        assertEquals(3, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(s2));

    }

    @Test
    void emptyHistoryTest() {
        HistoryManager manager = new InMemoryHistoryManager();
        assertNull(manager.getHistory());
    }

    @Test
    void duplicateInHistoryTest() {
        HistoryManager manager = new InMemoryHistoryManager();
        Epic epic = new Epic(1, TaskType.EPIC, "new epic", "test description", Status.NEW);

        manager.add(epic);
        manager.add(epic);
        manager.add(epic);
        manager.add(epic);

        assertEquals(1, manager.getHistory().size());
    }

}
