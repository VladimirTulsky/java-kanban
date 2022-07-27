import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIDs;

    public Epic(String title, String description, String status) {
        super(title, description, status);
        subtaskIDs = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }

    public void setSubtaskIDs(ArrayList<Integer> subtaskIDs) {
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
