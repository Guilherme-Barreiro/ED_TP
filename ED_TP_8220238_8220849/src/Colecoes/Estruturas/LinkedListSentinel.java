package Colecoes.Estruturas;

public class LinkedListSentinel<T> {
    private Node<T> head;

    public LinkedListSentinel() {
        head = new Node<T>(null);
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        Node<T> current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        current.setNext(newNode);
    }

    public void remover(T data) {
        Node<T> current = head;
        while (current.getNext() != null && !current.getNext().getData().equals(data)) {
            current = current.getNext();
        }
        if (current.getNext() != null) {
            current.setNext(current.getNext().getNext());
        }
    }

    public void printList() {
        Node<T> current = head.getNext();
        while (current != null) {
            System.out.println(current.getData());
            current = current.getNext();
        }
        System.out.println();
    }
}
