package httpclient;

import kvserver.KVServer;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.Task;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest {
    public KVServer kvServer = new KVServer();
    public HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078");

    public HTTPTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void serverStart() {
        kvServer.start();
    }

    @AfterEach
    public void stopStart() {
        kvServer.stop();
    }

    @Test
    void saveTasksAndLoadTasksTest() throws IOException {
        taskManager.getToken();
        taskManager.saveTasks();
        HTTPTaskManager httpTaskManager1 = new HTTPTaskManager("http://localhost:8078");
        httpTaskManager1.getToken();
        httpTaskManager1.loadTasks();

        assertEquals(taskManager.getAllTasks().size(), httpTaskManager1.getAllTasks().size());
        for (Task task : taskManager.getAllTasks().values()) {
            int id = task.getId();
            assertEquals(task.getTitle(), httpTaskManager1.getAllTasks().get(id).getTitle());
        }
        assertEquals(taskManager.getHistory().size(), httpTaskManager1.getHistory().size());
    }
}