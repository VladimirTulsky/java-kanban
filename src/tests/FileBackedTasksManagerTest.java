package tests;

import filemanager.FileBackedTasksManager;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    protected FileBackedTasksManager createManager() {
        FileBackedTasksManager.setIdCounter(1);
        return new FileBackedTasksManager();
    }
}
