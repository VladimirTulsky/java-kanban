package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    Task getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    void add(Task task);
    void add(Epic epic);
    void add(Subtask subtask);

    void update(Task task);
    void update(Epic epic);
    void update(Subtask subtask);

    void removeAllTasks();
    void removeAllEpicsAndSubtasks();
    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(Integer id);

    ArrayList<Subtask> getAllSubtasksByEpic(int id);

    List<Task> getHistory();

}
