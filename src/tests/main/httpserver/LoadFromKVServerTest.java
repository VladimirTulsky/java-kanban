package httpserver;

import httpclient.HTTPTaskManager;
import kvserver.KVServer;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoadFromKVServerTest {
    HTTPTaskManager httpTaskManager;
    KVServer kvServer;

    @BeforeEach
    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterEach
    void stop() {
        kvServer.stop();
    }

    @Test
    void saveAndLoadTasksKVServerTest() throws IOException {
        httpTaskManager = Managers.loadedHTTPTasksManager();
        httpTaskManager.getToken();
        httpTaskManager.saveTasks();
        HTTPTaskManager loadedFromServerManager = new HTTPTaskManager("http://localhost:8078");
        loadedFromServerManager.getToken();
        loadedFromServerManager.loadTasks();
        Assertions.assertEquals(httpTaskManager.getAllTasks().size(), loadedFromServerManager.getAllTasks().size());
    }
}
