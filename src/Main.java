import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main (String[] args) {
        Task t1 = new Task(1,"Потратиться на себя", "Сделать себе приятно", Status.NEW);
        Epic e1 = new Epic(2, "Пройти курс Java-разработчик", "пройти все спринты и выполнить задания", Status.NEW);
        Subtask s1 = new Subtask(3, "Пройти Java Core", "База", Status.NEW, 2);
        Subtask s2 = new Subtask(4, "Стать гуру Spring", "Важная задача", Status.NEW, 2);
        Epic e2 = new Epic(5, "Английский", "дойти до уровня Native", Status.NEW);
        Subtask s3 = new Subtask(6, "Учить", "узнавать что-то новое каждый день", Status.IN_PROGRESS, 5);

        TaskManager taskManager = Managers.getDefault();
        //добавляем задачи
        taskManager.add(t1);
        taskManager.add(e1);
        taskManager.add(s1);
        taskManager.add(s2);
        taskManager.add(e2);
        taskManager.add(s3);
        //выводим задачи
        System.out.println(taskManager);
        System.out.println("--------------------------------------");
        //обновляем по id, epics обновляется автоматически при обновлении или добавлении объектов SubTask
        taskManager.update(new Task(1,"Потратиться на себя", "Сделать себе приятно", Status.IN_PROGRESS));
        taskManager.update(new Subtask(4, "Стать гуру Spring", "Важная задача", Status.IN_PROGRESS, 2));
        System.out.println(taskManager);
        System.out.println("--------------------------------------");
        System.out.println("тест вывода всех подзадач эпика");
        System.out.println(taskManager.getAllSubtasksByEpic(2));
        System.out.println("--------------------------------------");
        System.out.println("проверяем работу истории, добавив 11 элементов");
        taskManager.getEpicById(2);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(5);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(2);
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistory());
        System.out.println("Количество элементов в истории:");
        System.out.println(taskManager.getHistory().size());
        System.out.println("--------------------------------------");
        System.out.println("Удаляем эпик и все его подзадачи и проверяем список задач");
        taskManager.removeEpicById(2);
        System.out.println(taskManager);

    }
}
