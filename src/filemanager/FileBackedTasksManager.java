package filemanager;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    //проверочный main
    public static void main(String[] args) {
        var fileManager = Managers.getDefaultFileManager();
        //создаем объекты и закидываем в файл
        fileManager.add(new Task(FileBackedTasksManager.getIdCounter(), TaskType.TASK, "Продать авто", "продать", Status.NEW));
        fileManager.add(new Task(FileBackedTasksManager.getIdCounter(), TaskType.TASK, "Потратиться на себя", "Сделать себе приятно", Status.NEW));
        fileManager.add(new Epic(FileBackedTasksManager.getIdCounter(), TaskType.EPIC, "Пройти курс Java-разработчик", "пройти все спринты", Status.NEW));
        fileManager.add(new Subtask(FileBackedTasksManager.getIdCounter(), TaskType.SUBTASK, "Пройти Java Core", "База", Status.NEW, 3));
        fileManager.add(new Subtask(FileBackedTasksManager.getIdCounter(), TaskType.SUBTASK, "Стать гуру Spring", "Важная задача", Status.NEW, 3));
        fileManager.add(new Subtask(FileBackedTasksManager.getIdCounter(), TaskType.SUBTASK, "Сдать тесты", "Важная задача", Status.NEW, 3));
        fileManager.add(new Epic(FileBackedTasksManager.getIdCounter(), TaskType.EPIC, "Английский", "дойти до уровня Native", Status.NEW));
        //создаем историю
        fileManager.getEpicById(3);
        fileManager.getEpicById(7);
        fileManager.getTaskById(1);
        fileManager.getTaskById(2);
        fileManager.getEpicById(3);
        //тут все данные должны быть в файле

        //загружаем задачи и историю из файла
        var fileManager1 = FileBackedTasksManager.loadedFromFileTasksManager();
        //выводим задачи в консоль
        System.out.println("All tasks from file---------------------");
        System.out.println(fileManager1.getTasks());
        System.out.println(fileManager1.getEpics());
        System.out.println(fileManager1.getSubtasks());

        System.out.println("History---------------------");
        System.out.println(fileManager1.getHistory());
    }

    protected Map<Integer, Task> allTasks = new HashMap<>();
    private final static String HEAD = "id,type,title,description,status,epic\n";
    private final static String PATH = "resources\\data.csv";

    @Override
    public int add(Task task) {
        super.add(task);
        save();
        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        super.add(epic);
        save();
        return epic.getId();
    }

    @Override
    public int add(Subtask subtask) {
        super.add(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }

    //метод возвращает объект с историей из файла
    public static FileBackedTasksManager loadedFromFileTasksManager () {
        var fileManager = new FileBackedTasksManager();
        fileManager.loadFromFile();
        return fileManager;
    }

    //метод сохранения данных в файл
    public void save() {
        allTasks.clear();
        allTasks.putAll(getTasks());
        allTasks.putAll(getEpics());
        allTasks.putAll(getSubtasks());
        try (Writer writer = new FileWriter(PATH)) {
            String stringToFile = HEAD + tasksToString() + "\n" + historyToString();
            writer.write(stringToFile);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в работе менеджера");
        }
    }
    //метод загрузки данных из файла, вызывающий методы загрузки задач и истории
    public void loadFromFile() {
        tasksFromString();
        historyFromString();
    }

    public void tasksFromString() {
        String[] lines;
        try {
            lines = Files.readString(Path.of(PATH)).split("\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать файл");
        }
        if (lines.length < 2) return;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] lineContents = line.split(",");
            if (lineContents.length >= 5) {
                if (lineContents[1].equals("TASK")) {
                    int id = Integer.parseInt(lineContents[0]);
                    String title = lineContents[2];
                    String description = lineContents[3];
                    Status status = Enum.valueOf(Status.class, lineContents[4]);
                    this.tasks.put(id, new Task(id, TaskType.TASK, title, description, status));
                    if (getIdCounter() <= id) setIdCounter(++id);
                }
                if (lineContents[1].equals("EPIC")) {
                    int id = Integer.parseInt(lineContents[0]);
                    String title = lineContents[2];
                    String description = lineContents[3];
                    Status status = Enum.valueOf(Status.class, lineContents[4]);
                    this.epics.put(id, new Epic(id, TaskType.EPIC, title, description, status));
                    if (getIdCounter() <= id) setIdCounter(++id);
                }
                if (lineContents[1].equals("SUBTASK")) {
                    int id = Integer.parseInt(lineContents[0]);
                    String title = lineContents[2];
                    String description = lineContents[3];
                    Status status = Enum.valueOf(Status.class, lineContents[4]);
                    int epicId = Integer.parseInt(lineContents[5]);
                    this.subtasks.put(id, new Subtask(id, TaskType.SUBTASK, title, description, status, epicId));
                    if (getIdCounter() <= id) setIdCounter(++id);
                }
            }
        }
    }

    public void historyFromString() {
        String[] lines;
        try {
            lines = Files.readString(Path.of(PATH)).split("\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать файл");
        }
        allTasks.putAll(getTasks());
        allTasks.putAll(getEpics());
        allTasks.putAll(getSubtasks());
        if (lines[lines.length - 2].isBlank() && lines.length >= 4) {
            String historyLine = lines[lines.length - 1];
            String[] historyLineContents = historyLine.split(",");
            for (String s : historyLineContents) {
                historyManager.add(allTasks.get(Integer.parseInt(s)));
            }
        }
    }

    public String historyToString() throws IOException {
        if (historyManager.getHistory() == null) return "";
        List<Task> historyList = historyManager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task task : historyList) {
            sb.append(task.getId()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String tasksToString() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Task task : allTasks.values()) {
            if (task.getType() == TaskType.TASK || task.getType() == TaskType.EPIC) {
                sb.append(task.getId()).append(",").append(task.getType()).append(",").append(task.getTitle())
                        .append(",").append(task.getDescription()).append(",").append(task.getStatus()).append("\n");
            } else {
                Subtask subtask = (Subtask) task;
                sb.append(task.getId()).append(",").append(task.getType()).append(",").append(task.getTitle())
                        .append(",").append(task.getDescription()).append(",").append(task.getStatus()).append(",")
                        .append(subtask.getEpicID()).append("\n");
            }
        }
        return sb.toString();
    }
}