import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

public class Main {
    public static void main (String[] args) {
        Epic e1 = new Epic("Пройти курс Java-разработчик", "пройти все спринты и выполнить задания", Status.NEW);
        Subtask s1 = new Subtask("Пройти Java Core", "База", Status.NEW, 1);
        Subtask s2 = new Subtask("Стать гуру Spring", "Важная задача", Status.NEW, 1);
        Epic e2 = new Epic("Английский", "дойти до уровня Native", Status.NEW);
        Subtask s3 = new Subtask("Учить", "узнавать что-то новое каждый день", Status.IN_PROGRESS, 4);

        TaskManager taskManager = Managers.getDefault();
        taskManager.add(e1);
        taskManager.add(s1);
        taskManager.add(s2);
        taskManager.add(e2);
        taskManager.add(s3);
        System.out.println(taskManager);
        taskManager.update(e1);
        taskManager.update(e2);
        System.out.println("--------------------------------------");
        System.out.println(taskManager.getAllSubtasksByEpic(1));
        System.out.println("--------------------------------------");
        taskManager.getEpicById(1);
        taskManager.getSubtaskById(2);
        taskManager.getEpicById(4);
        taskManager.getEpicById(1);
        taskManager.getEpicById(1);
        System.out.println(taskManager.getHistory());

    }
}
