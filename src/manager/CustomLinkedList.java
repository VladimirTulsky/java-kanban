package manager;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;

    public void addLast(T t) {
        if (head == null) {
            Node<T> currentNode = new Node<>(t, null, tail);
            head = currentNode;
            tail = new Node<>(null, currentNode, null);
            return;
        }
        Node<T> currentNode = tail;
        currentNode.value = t;
        tail = new Node<>(null, currentNode, null);
        currentNode.prev.next = currentNode;
        currentNode.next = tail;
    }

    public void removeNode(Node<T> node) {
        if (node == head) {
            head = node.next;
            if (node.next != null) {
                node.next.prev = null;
            }
        } else {
            node.prev.next = node.next;
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
    }

    public List getTasks() {
        List<T> historyList = new ArrayList<>();
        Node<T> item = head;
        while(item.value != null) {
            historyList.add(item.value);
            item = item.next;
        }
        return historyList;
    }
}
