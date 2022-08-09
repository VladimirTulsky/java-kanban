package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtaskIDs;

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        subtaskIDs = new ArrayList<>();
    }

    public List<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }

    public void setSubtaskIDs(List<Integer> subtaskIDs) {
        this.subtaskIDs = subtaskIDs;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIDs=" + subtaskIDs +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
