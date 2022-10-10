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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpTaskServerDELETETest {
    public HttpTaskServer server;
    public HttpClient client;
    public String path = "http://localhost:8080";
    Gson gson;

    public HttpTaskServerDELETETest() throws IOException {
        server = new HttpTaskServer();
        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    @BeforeEach
    void serverStart() {
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
        server.start();
    }

    @AfterEach
    void stopStart() {
        server.stop();
    }

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
    void deleteAllTasksFromServerTest() {
        URI url = URI.create(path + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            HTTPTaskManager taskManager = Managers.loadedHTTPTasksManager();
            assertEquals(0, taskManager.getTasks().size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deleteAllEpicsAndSubtasksFromServerTest() {
        URI url = URI.create(path + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            HTTPTaskManager taskServer = Managers.loadedHTTPTasksManager();
            assertEquals(0, taskServer.getEpics().size());
            assertEquals(0, taskServer.getSubtasks().size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deleteTaskByIdFromServerTest() {
        URI url = URI.create(path + "/tasks/task?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            HTTPTaskManager taskManager = Managers.loadedHTTPTasksManager();
            assertNull(taskManager.getTaskById(2));
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deleteEpicByIdAndAllSubtasksByThisEpicFromServerTest() {
        URI url = URI.create(path + "/tasks/epic?id=7");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            HTTPTaskManager taskManager = Managers.loadedHTTPTasksManager();
            assertNull(taskManager.getEpicById(7));
            assertEquals(0, taskManager.getSubtasks().size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deleteSubtaskByIdFromServerTest() {
        URI url = URI.create(path + "/tasks/subtask?id=5");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            HTTPTaskManager taskManager = Managers.loadedHTTPTasksManager();
            assertNull(taskManager.getSubtaskById(5));
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}