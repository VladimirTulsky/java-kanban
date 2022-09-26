package tests;

import filemanager.TaskType;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;

    protected abstract T createManager();

    @BeforeEach
    void getManager() {
        manager = createManager();
    }

    @Test
    void addTaskAndGetTaskByIdTest() {
        Task task = new Task(1, TaskType.TASK, "Test task", "test description", Status.NEW,
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        manager.add(task);
        assertFalse(manager.getTasks().isEmpty());
        assertEquals(1, manager.getTasks().size());
        assertEquals(task, manager.getTaskById(1));

    }

    @Test
    void addEpicAndGetEpicByIdTest() {
        Epic epic = new Epic(1, TaskType.EPIC, "Epic", "test description", Status.NEW);
        manager.add(epic);
        assertFalse(manager.getEpics().isEmpty());
        assertEquals(1, manager.getEpics().size());
        assertEquals(epic, manager.getEpicById(1));
    }

    @Test
    void addSubtaskAndGetSubtaskIdTest() {
        Subtask subtask = new Subtask(1, TaskType.SUBTASK, "Test task", "test description", Status.NEW,
                0, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        manager.add(subtask);
        assertFalse(manager.getSubtasks().isEmpty());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(subtask, manager.getSubtaskById(1));
    }

    @Test
    void getAllTasksEpicsSubtasksTest() {
        Task task = new Task(1, TaskType.TASK, "Test task", "test description", Status.NEW,
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        Epic epic = new Epic(2, TaskType.EPIC, "Epic", "test description", Status.NEW);
        Subtask subtask = new Subtask(3, TaskType.SUBTASK, "Test task", "test description", Status.NEW,
                0, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        manager.add(task);
        manager.add(epic);
        manager.add(subtask);
        assertEquals(1, manager.getTasks().size());
        assertEquals(1, manager.getEpics().size());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(task, manager.getTasks().get(1));
        assertEquals(epic, manager.getEpics().get(2));
        assertEquals(subtask, manager.getSubtasks().get(3));
    }

    @Test
    void updateTaskTest() {
        Task taskOld = new Task(1, TaskType.TASK, "TaskOld", "test description", Status.NEW,
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        Task taskNew = new Task(1, TaskType.TASK, "TaskNew", "test description", Status.NEW,
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        manager.add(taskOld);
        manager.update(taskNew);
        assertEquals("TaskNew", manager.getTaskById(1).getTitle());
    }

    @Test
    void updateEpicTest() {
        Epic epicOld = new Epic(1, TaskType.EPIC, "old epic", "test description", Status.NEW);
        Epic epicNew = new Epic(1, TaskType.EPIC, "new epic", "test description", Status.NEW);
        manager.add(epicOld);
        manager.update(epicNew);
        assertEquals("new epic", manager.getEpicById(1).getTitle());
    }

    @Test
    void updateSubtaskTest() {
        Epic epic = new Epic(1, TaskType.EPIC, "new epic", "test description", Status.NEW);
        Subtask subtaskOld = new Subtask(2, TaskType.SUBTASK, "old subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        Subtask subtaskNew = new Subtask(2, TaskType.SUBTASK, "new subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        manager.add(epic);
        manager.add(subtaskOld);
        manager.update(subtaskNew);
        assertEquals("new subtask", manager.getSubtaskById(2).getTitle());
    }

    @Test
    void updateEpicStatusBySubtasksTest() {
        Epic epic = new Epic(1, TaskType.EPIC, "new epic", "test description", Status.NEW);
        manager.add(epic);
        assertEquals(Status.NEW, manager.getEpics().get(1).getStatus());

        manager.add(new Subtask(2, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.add(new Subtask(3, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));

        assertEquals(Status.NEW, manager.getEpics().get(1).getStatus());

        manager.add(new Subtask(4, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.update(new Subtask(2, TaskType.SUBTASK, "new subtask", "test description", Status.IN_PROGRESS,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        assertEquals(Status.IN_PROGRESS, manager.getEpics().get(1).getStatus());

        manager.update(new Subtask(2, TaskType.SUBTASK, "new subtask", "test description", Status.DONE,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.update(new Subtask(3, TaskType.SUBTASK, "new subtask", "test description", Status.DONE,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        assertEquals(Status.IN_PROGRESS, manager.getEpics().get(1).getStatus());

        manager.update(new Subtask(4, TaskType.SUBTASK, "subtask", "test description", Status.DONE,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        assertEquals(Status.DONE, manager.getEpics().get(1).getStatus());
    }

    @Test
    void removeAllTasksTest() {
        manager.add(new Task(1, TaskType.TASK, "Task1", "test description", Status.NEW,
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.add(new Task(2, TaskType.TASK, "Task2", "test description", Status.NEW,
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.removeAllTasks();
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void removeAllEpicsAndSubtasksTest() {
        manager.add(new Epic(1, TaskType.EPIC, "new epic", "test description", Status.NEW));
        manager.add(new Subtask(2, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.add(new Subtask(3, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.add(new Subtask(4, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.removeAllEpicsAndSubtasks();
        assertTrue(manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty());
    }

    @Test
    void removeTaskByIdAndEpicByIdAndSubtaskById() {
        manager.add(new Task(1, TaskType.TASK, "Task1", "test description", Status.NEW,
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.add(new Epic(2, TaskType.EPIC, "new epic", "test description", Status.NEW));
        manager.add(new Subtask(3, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                2, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.removeTaskById(1);
        assertTrue(manager.getTasks().isEmpty());
        manager.removeSubtaskById(3);
        assertTrue(manager.getSubtasks().isEmpty());
        manager.removeEpicById(2);
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void getAllSubtasksByEpicTest() {
        manager.add(new Epic(1, TaskType.EPIC, "new epic", "test description", Status.NEW));
        Subtask s1 = new Subtask(2, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        Subtask s2 = new Subtask(3, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        Subtask s3 = new Subtask(4, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        manager.add(s1);
        manager.add(s2);
        manager.add(s3);
        List<Subtask> testList = List.of(s1, s2, s3);
        for (int i = 0; i < manager.getAllSubtasksByEpic(1).size(); i++) {
            assertEquals(testList.get(i), manager.getAllSubtasksByEpic(1).get(i));
        }
    }

    @Test
    void getHistoryAndDuplicatesInHistoryTest() {
        manager.add(new Task(1, TaskType.TASK, "Task1", "test description", Status.NEW,
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.add(new Epic(2, TaskType.EPIC, "new epic", "test description", Status.NEW));
        manager.add(new Subtask(3, TaskType.SUBTASK, "subtask", "test description", Status.NEW,
                2, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));

        manager.getTaskById(1);
        manager.getSubtaskById(3);
        manager.getEpicById(2);
        manager.getSubtaskById(3);
        assertEquals(3, manager.getHistory().size());
        for (int i = 0; i < manager.getHistory().size(); i++) {
            assertEquals(i + 1, manager.getHistory().get(i).getId());
        }
    }


}
