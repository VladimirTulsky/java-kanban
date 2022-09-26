package tests;

import manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createManager() {
        InMemoryTaskManager.setIdCounter(1);
        return new InMemoryTaskManager();
    }
}
