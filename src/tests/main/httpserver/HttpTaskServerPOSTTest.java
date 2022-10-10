package httpserver;

import com.google.gson.Gson;
import filemanager.FileBackedTasksManager;
import filemanager.TaskType;
import httpclient.HTTPTaskManager;
import manager.Managers;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerPOSTTest {
    public HttpTaskServer server;
    public HttpClient client;
    public String path = "http://localhost:8080";
    Gson gson;

    public HttpTaskServerPOSTTest() throws IOException {
        server = new HttpTaskServer();
        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    @BeforeEach
    void serverStart() {
        server.start();
    }

    @AfterEach
    void stopStart() {
        server.stop();
    }

    @BeforeAll
    @AfterAll
    static void updateFile() {
        var fileManager = Managers.getDefaultFileManager();
        FileBackedTasksManager.setIdCounter(1);
        //создаем объекты и закидываем в файл
        fileManager.add(new Task(FileBackedTasksManager.getIdCounter(), TaskType.TASK, "Продать авто",
                "продать", Status.NEW, LocalDateTime.of(2022, 9, 25, 13, 30, 15), 30));
        fileManager.add(new Task(FileBackedTasksManager.getIdCounter(), TaskType.TASK, "Потратиться на себя",
                "Сделать себе приятно", Status.NEW, LocalDateTime.of(2022, 9, 26, 12, 0, 15), 30));
        fileManager.add(new Epic(FileBackedTasksManager.getIdCounter(), TaskType.EPIC, "Пройти курс Java-разработчик",
                "пройти все спринты", Status.NEW));
        fileManager.add(new Subtask(FileBackedTasksManager.getIdCounter(), TaskType.SUBTASK, "Пройти Java Core",
                "База", Status.NEW, 7, LocalDateTime.now(), 30));
        fileManager.add(new Subtask(FileBackedTasksManager.getIdCounter(), TaskType.SUBTASK, "Стать гуру Spring",
                "Важная задача", Status.NEW, 7, LocalDateTime.of(2022, 9, 24, 10, 0, 15), 45));
        fileManager.add(new Subtask(FileBackedTasksManager.getIdCounter(), TaskType.SUBTASK, "Сдать тесты",
                "Важная задача", Status.NEW, 7, LocalDateTime.of(2022, 9, 25, 11, 0, 15), 120));
        fileManager.add(new Epic(FileBackedTasksManager.getIdCounter(), TaskType.EPIC, "Английский",
                "дойти до уровня Native", Status.NEW));

        fileManager.getTaskById(1);
        fileManager.getEpicById(7);
        fileManager.getEpicById(3);
    }

    @Test
    void sendTaskToServerTest() {
        Task task = new Task(8, TaskType.TASK, "Test task",
                "test", Status.NEW, LocalDateTime.of(2022, 9, 29, 13, 30, 15), 30);
        task.setEndTime(LocalDateTime.of(2022, 9, 29, 14, 00, 15));
        URI url = URI.create(path + "/tasks/task");
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
            assertNotNull(manager.getTaskById(8));
            assertEquals(task.getTitle(), manager.getTaskById(8).getTitle());
            manager.removeTaskById(8);
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void sendEpicToServerTest() {
        Epic epic = new Epic(9, TaskType.EPIC, "Epic epic",
                "test", Status.NEW);
        epic.setEndTime(null);
        URI url = URI.create(path + "/tasks/epic");
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
            assertNotNull(manager.getEpicById(9));
            assertEquals(epic.getTitle(), manager.getEpicById(9).getTitle());

        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void sendSubtaskToServerTest() {
        Subtask subtask = new Subtask(10, TaskType.SUBTASK, "Test task",
                "test", Status.NEW, 3, LocalDateTime.of(2022, 9, 30, 13, 30, 15), 30);
        subtask.setEndTime(LocalDateTime.of(2022, 9, 30, 14, 00, 15));
        URI url = URI.create(path + "/tasks/subtask");
        String json = gson.toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
            assertNotNull(manager.getSubtaskById(10));
            assertEquals(subtask.getTitle(), manager.getSubtaskById(10).getTitle());

        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}
