package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    final static private int HISTORY_SIZE = 10;

    private List<Task> historyList = new ArrayList<>();

    @Override
    public int add(Task task) {
        historyList.add(task);
        if (historyList.size() >= HISTORY_SIZE) {
            historyList.remove(0);
            historyList.add(task);
        }
        return task.getId();
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
