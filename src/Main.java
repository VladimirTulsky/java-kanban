import filemanager.FileBackedTasksManager;
import manager.Managers;
import java.io.IOException;

public class Main {
    public static void main (String[] args) throws IOException {

        FileBackedTasksManager fileManager = Managers.getDefaultFileManager();
        //загружаем данные из файла
        fileManager.loadFromFile();
        //выводим задачи в консоль
        System.out.println(fileManager.getTasks());
        System.out.println(fileManager.getEpics());
        System.out.println(fileManager.getSubtasks());
        //выводим историю в консоль
        System.out.println("Get history---------------------");
        System.out.println(fileManager.getHistory());
        //выводим обновленную историю в консоль
        System.out.println("Update history------------------");
        fileManager.getEpicById(7);
        fileManager.removeTaskById(1);
        System.out.println(fileManager.getHistory());
        //проверяем в консоли и файле
    }
}
