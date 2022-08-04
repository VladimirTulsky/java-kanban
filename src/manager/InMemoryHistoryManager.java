package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    final static private int HISTORY_SIZE = 10;

    protected static List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() < HISTORY_SIZE) {
            historyList.add(task);
        } else {
            historyList.remove(0);
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
