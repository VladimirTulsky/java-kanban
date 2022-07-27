import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;

public class Main {
    public static void main (String[] args) {
        Epic e1 = new Epic("Пройти курс Java-разработчик", "пройти все спринты и выполнить задания", "NEW");
        Subtask s1 = new Subtask("Пройти Java Core", "База", "IN_PROGRESS", 1);
        Subtask s2 = new Subtask("Стать гуру Spring", "Важная задача", "NEW", 1);
        Epic e2 = new Epic("Английский", "дойти до уровня Native", "NEW");
        Subtask s3 = new Subtask("Учить", "узнавать что-то новое каждый день", "IN_PROGRESS", 4);

        TaskManager taskManager = new TaskManager();
        taskManager.add(e1);
        taskManager.add(s1);
        taskManager.add(s2);
        taskManager.add(e2);
        taskManager.add(s3);
        System.out.println(taskManager);
        taskManager.update(e1);
        taskManager.update(e2);
        System.out.println("--------------------------------------");
        System.out.println(taskManager);
        taskManager.removeEpicById(4);
        taskManager.removeSubtaskById(3);
        System.out.println("--------------------------------------");
        System.out.println(taskManager);
        System.out.println("--------------------------------------");
        System.out.println(taskManager.getAllSubtasksFromEpic(1));

    }
}
