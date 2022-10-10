package httpserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerGETTest {
    //TODO В тесты сервера треюуется запускать каждый класс по отдельности. Не удалось сделать тесты, чтобы
    // GET, POST и DELETE работали все вместе. Подозреваю, что они хаотично запускаются и обновляют
    // один и тот же файл, перепробовал многое. Но как вижу, единственный вариант
    // это переписывать логику в FileBackedTasksManager, чтобы можно было передавать разные файлы. Но не решился,
    // чтобы не сломать логику программы.

    public HttpTaskServer server;
    public HttpClient client;
    public String path = "http://localhost:8080";
    Gson gson;

    public HttpTaskServerGETTest() throws IOException {
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
    void getAllTasksAndEpicsAndSubtasksTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> tasksList = gson.fromJson(json, type);
            List<Task> expectedList = new ArrayList<>(manager.getAllTasks().values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), tasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getTasksTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> tasksList = gson.fromJson(json, type);
            List<Task> expectedList = new ArrayList<>(manager.getTasks().values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), tasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getEpicsTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Epic>>(){}.getType();
            List<Epic> epicsList = gson.fromJson(json, type);
            List<Task> expectedList = new ArrayList<>(manager.getEpics().values());
            for (int i = 0; i < epicsList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), epicsList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getSubtasksTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Subtask>>(){}.getType();
            List<Task> subtasksList = gson.fromJson(json, type);
            List<Task> expectedList = new ArrayList<>(manager.getSubtasks().values());
            for (int i = 0; i < subtasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), subtasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getTaskByIdTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            String json = response.body();
            Task task = gson.fromJson(json, Task.class);
            Task expectedTask = manager.getTaskById(1);

            assertEquals(expectedTask.getTitle(), task.getTitle());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getEpicByIdTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/epic?id=7");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            String json = response.body();
            Epic epic = gson.fromJson(json, Epic.class);
            Task expectedEpic = manager.getEpicById(7);

            assertEquals(expectedEpic.getTitle(), epic.getTitle());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getSubtaskByIdTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/subtask?id=5");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            String json = response.body();
            Subtask subtask = gson.fromJson(json, Subtask.class);
            Subtask expectedSubtask = manager.getSubtaskById(5);

            assertEquals(expectedSubtask.getTitle(), subtask.getTitle());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getAllSubtasksByEpicIdTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/subtask/epic?id=7");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            String json = response.body();
            Type type = new TypeToken<ArrayList<Subtask>>(){}.getType();
            List<Subtask> subtasksList = gson.fromJson(json, type);
            List<Subtask> expectedList = new ArrayList<>(manager.getAllSubtasksByEpic(7));
            for (int i = 0; i < subtasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), subtasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getHistoryTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> historyList = gson.fromJson(json, type);
            List<Task> expectedHistoryList = new ArrayList<>(manager.getHistory());
            for (int i = 0; i < historyList.size(); i++) {
                assertEquals(expectedHistoryList.get(i).getTitle(), historyList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getPrioritizedListTest() {
        HTTPTaskManager manager = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/priority");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            String json = response.body();
            Type type = new TypeToken<List<Task>>(){}.getType();
            List<Task> prioritizedList = gson.fromJson(json, type);
            List<Task> expectedPrioritizedList = new ArrayList<>(manager.getPrioritizedTasks());
            for (int i = 0; i < prioritizedList.size(); i++) {
                assertEquals(expectedPrioritizedList.get(i).getTitle(), prioritizedList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}
