import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main (String[] args) {

        TaskManager taskManager = Managers.getDefault();
        //добавляем задачи
        taskManager.add(new Task(InMemoryTaskManager.getIdCounter(),"Продать авто", "продать", Status.NEW));
        taskManager.add(new Task(InMemoryTaskManager.getIdCounter(),"Потратиться на себя", "Сделать себе приятно", Status.NEW));
        taskManager.add(new Epic(InMemoryTaskManager.getIdCounter(), "Пройти курс Java-разработчик", "пройти все спринты", Status.NEW));
        taskManager.add(new Subtask(InMemoryTaskManager.getIdCounter(), "Пройти Java Core", "База", Status.NEW, 3));
        taskManager.add(new Subtask(InMemoryTaskManager.getIdCounter(), "Стать гуру Spring", "Важная задача", Status.NEW, 3));
        taskManager.add(new Subtask(InMemoryTaskManager.getIdCounter(), "Сдать тесты", "Важная задача", Status.NEW, 3));
        taskManager.add(new Epic(InMemoryTaskManager.getIdCounter(), "Английский", "дойти до уровня Native", Status.NEW));
        System.out.println("--------------------------------------");
        System.out.println("проверяем работу истории");
        taskManager.getEpicById(3);
        taskManager.getEpicById(7);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(5);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(6);
        taskManager.getTaskById(2);
        System.out.println(taskManager.getHistory());
        System.out.println("--------------------------------------");
        System.out.println("удаляем задачу");
        taskManager.removeTaskById(1);
        System.out.println(taskManager.getHistory());
        System.out.println("--------------------------------------");
        System.out.println("удаляем эпик и его позадачи");
        taskManager.removeEpicById(3);
        System.out.println(taskManager.getHistory());
    }
}
