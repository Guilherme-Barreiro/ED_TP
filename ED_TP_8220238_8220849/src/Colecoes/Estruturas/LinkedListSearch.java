package Colecoes.Estruturas;

public class LinkedListSearch<T extends Comparable<? super T>> {
    private Node<T> head;

    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    public void remover(T data) {
        if (head == null) return;

        if (head.getData().equals(data)) {
            head = head.getNext();
            return;
        }

        Node<T> current = head;
        while (current.getNext() != null && !current.getNext().getData().equals(data)) {
            current = current.getNext();
        }

        if (current.getNext() != null) {
            current.setNext(current.getNext().getNext());
        }
    }

    public void print() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.getData() + " -> ");
            current = current.getNext();
        }
        System.out.println("null");
    }

    public void printRecursive() {
        printRec(head);
        System.out.println("null");
    }

    private void printRec(Node<T> node) {
        if (node == null) {
            System.out.println("null");
        } else {
            System.out.print(node.getData() + " -> ");
            printRec(node.getNext());
        }
    }

    public void replace(T existingElement, T newElement) {
        head = rep(head, existingElement, newElement);
    }

    private Node<T> rep(Node<T> node, T old, T newE) {
        if (node == null) {
            return null;
        }
        if (node.getData().equals(old)) {
            node.setData(newE);
        }
        node.setNext(rep(node.getNext(), old, newE));
        return node;
    }

    public LinkedListSearch<T> invert() {
        LinkedListSearch<T> out = new LinkedListSearch<>();
        buildReverse(head, out);
        return out;
    }

    private void buildReverse(Node<T> node, LinkedListSearch<T> out) {
        if (node == null) {
            return;
        }
        buildReverse(node.getNext(), out);
        out.add(node.getData());
    }

    public boolean linearSearch(T target) {
        Node<T> cur = head;
        while (cur != null) {
            if (cur.getData().compareTo(target) == 0) return true;
            cur = cur.getNext();
        }
        return false;
    }

    public boolean binarySearch(T target) {
        Node<T> start = head;
        Node<T> end = null;

        while (start != end) {
            Node<T> mid = middle(start, end);
            int cmp = mid.getData().compareTo(target);
            if (cmp == 0) return true;
            if (cmp < 0) {
                start = mid.getNext();
            } else {
                end = mid;
            }
        }
        return false;
    }

    private Node<T> middle(Node<T> start, Node<T> end) {
        Node<T> slow = start, fast = start;
        while (fast != end && fast.getNext() != end) {
            fast = fast.getNext().getNext();
            slow = slow.getNext();
        }
        return slow;
    }

}
