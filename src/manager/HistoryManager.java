package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    int add(Task task);

    List<Task> getHistory();
}
