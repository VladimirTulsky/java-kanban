package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private  int idCounter = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        return task;
    }

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        return epic;
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        return subtask;
    }

    public void add(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
    }

    public void add(Epic epic) {
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
        for (Subtask subtask : subtasks.values()) {
            if(epic.getId() == subtask.getEpicID()) {
                epic.getSubtaskIDs().add(subtask.getId());
            }
        }
    }

    public void add(Subtask subtask) {
        subtask.setId(idCounter++);
        subtasks.put(subtask.getId(), subtask);
        for (Epic epic : epics.values()) {
            if(epic.getId() == subtask.getEpicID()) {
                epic.getSubtaskIDs().add(subtask.getId());
            }
        }
    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(Epic epic) {
        epic.setStatus("NEW");
        epics.put(epic.getId(), epic);
        ArrayList<Integer> epicList = epic.getSubtaskIDs();
        if (epic.getSubtaskIDs().isEmpty()) {
            return;
        }
        for (int id : epicList) {
            if (subtasks.get(id).getStatus().equals("DONE")) {
                epic.setStatus("DONE");
                break;
            } else if (subtasks.get(id).getStatus().equals("IN_PROGRESS")) {
                epic.setStatus("IN_PROGRESS");
                return;
            } else if (epic.getStatus().equals("DONE") && subtasks.get(id).getStatus().equals("NEW")) {
                epic.setStatus("IN_PROGRESS");
                return;
            }
        }
    }

    public void update(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void removeAllTasks() {
        tasks.clear();
    }
    public void removeAllEpicsAndSubtasks() {
        epics.clear();
        subtasks.clear();
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }
    public void removeEpicById(int id) {
        ArrayList<Integer> subtasksInEpic = epics.get(id).getSubtaskIDs();
        for (int subtaskId : subtasksInEpic) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }
    public void removeSubtaskById(Integer id) {
        int epicID = subtasks.get(id).getEpicID();
        epics.get(epicID).getSubtaskIDs().remove(id);
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getAllSubtasksFromEpic(int id) {
        ArrayList<Integer> numbers = epics.get(id).getSubtaskIDs();
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        for (int item : numbers) {
            subtaskArrayList.add(subtasks.get(item));
        }
        return subtaskArrayList;
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
