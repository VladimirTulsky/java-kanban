package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 1;

    private HistoryManager historyManager = Managers.getDefaultHistory();
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public int add(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
        for (Subtask subtask : subtasks.values()) {
            if(epic.getId() == subtask.getEpicID()) {
                epic.getSubtaskIDs().add(subtask.getId());
            }
        }
        return epic.getId();
    }

    @Override
    public int add(Subtask subtask) {
        subtask.setId(idCounter++);
        subtasks.put(subtask.getId(), subtask);
        for (Epic epic : epics.values()) {
            if(epic.getId() == subtask.getEpicID()) {
                epic.getSubtaskIDs().add(subtask.getId());
            }
        }
        if (epics.containsKey(subtask.getEpicID())) {
            update(epics.get(subtask.getEpicID()));
        }
        return subtask.getId();
    }
    @Override
    public int update(Task task) {
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int update(Epic epic) {
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
        List<Integer> epicList = epic.getSubtaskIDs();
        if (epic.getSubtaskIDs().isEmpty()) {
            return epic.getId();
        }
        for (int id : epicList) {
            if (subtasks.get(id).getStatus() == Status.DONE) {
                epic.setStatus(Status.DONE);
                break;
            } else if (subtasks.get(id).getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return epic.getId();
            } else if (epic.getStatus() == Status.DONE && subtasks.get(id).getStatus() == Status.NEW) {
                epic.setStatus(Status.IN_PROGRESS);
                return epic.getId();
            }
        }
        return epic.getId();
    }
    @Override
    public int update(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        update(epics.get(subtask.getEpicID()));
        return subtask.getId();
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpicsAndSubtasks() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        List<Integer> subtasksInEpic = epics.get(id).getSubtaskIDs();
        for (int subtaskId : subtasksInEpic) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        int epicID = subtasks.get(id).getEpicID();
        epics.get(epicID).getSubtaskIDs().remove(id);
        update(epics.get(epicID));
        subtasks.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtasksByEpic(int id) {
        List<Integer> numbers = epics.get(id).getSubtaskIDs();
        List<Subtask> subtaskArrayList = new ArrayList<>();
        for (int item : numbers) {
            subtaskArrayList.add(subtasks.get(item));
        }
        return subtaskArrayList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                ", subtasks=" + subtasks +
                '}';
    }
}
