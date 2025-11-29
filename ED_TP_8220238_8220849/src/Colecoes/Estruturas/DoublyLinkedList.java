package Colecoes.Estruturas;

public class DoublyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    public DoublyLinkedList() {
        head = null;
        tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void addFirst(T value) {
        Node<T> newnode = new Node<>(value);
        if (head == null) {
            head = tail = newnode;
        } else {
            newnode.setNext(head);
            head.setPrev(newnode);
            head = newnode;
        }
    }

    public void addLast(T value) {
        Node<T> n = new Node<>(value);
        if (tail == null) {
            head = tail = n;
        } else {
            tail.setNext(n);
            n.setPrev(tail);
            tail = n;
        }
    }

    public void removerFirst() {
        if (head == null) {
            return;
        }
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.getNext();
            head.setPrev(null);
        }
    }

    public void removerLast() {
        if (tail == null) {
            return;
        }
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.getPrev();
            tail.setNext(null);
        }
    }

    public void print() {
        Node<T> temp = head;
        while (temp != null) {
            System.out.print(temp.getData() + " ");
            temp = temp.getNext();
        }
        System.out.println();
    }

    public Object[] toarray() {
        int length = 0;
        Node<T> temp = head;
        while (temp != null) {
            length++;
            temp = temp.getNext();
        }

        Object[] arr = new Object[length];
        temp = head;
        int i = 0;
        while (temp != null) {
            arr[i++] = temp.getData();
            temp = temp.getNext();
        }

        return arr;
    }

    public Object[] toArrayUntil(int pos) {
        if (pos < 0) {
            return new Object[0];
        }

        int length = 0;
        int i = 0;
        Node<T> temp = head;
        while (temp != null && i <= pos) {
            length++;
            temp = temp.getNext();
            i++;
        }

        Object[] arr = new Object[length];
        temp = head;
        i = 0;
        int j = 0;
        while (temp != null && i < length) {
            arr[j++] = temp.getData();
            temp = temp.getNext();
            i++;
        }

        return arr;
    }

    public Object[] toArrayAfter(int pos) {
        int length = 0;
        Node<T> temp = head;
        while (temp != null) {
            length++;
            temp = temp.getNext();
        }

        int startIndex = pos + 1;
        if (startIndex >= length) {
            return new Object[0];
        }

        int resultLen = length - startIndex;
        Object[] arr = new Object[resultLen];

        temp = head;
        int index = 0;
        int j = 0;
        while (temp != null) {
            if (index > pos) {
                arr[j++] = temp.getData();
            }
            temp = temp.getNext();
            index++;
        }

        return arr;
    }

    public Object[] toArrayBetween(int start, int end) {
        if (start < 0 || end < start) {
            return new Object[0];
        }

        int length = 0;
        Node<T> temp = head;
        while (temp != null) {
            length++;
            temp = temp.getNext();
        }

        if (start >= length) {
            return new Object[0];
        }

        if (end >= length) {
            end = length - 1;
        }

        int resultLen = end - start + 1;
        Object[] arr = new Object[resultLen];

        temp = head;
        int index = 0;
        int j = 0;
        while (temp != null && index <= end) {
            if (index >= start) {
                arr[j++] = temp.getData();
            }
            temp = temp.getNext();
            index++;
        }

        return arr;
    }

    public DoublyLinkedList<T> getPares() {
        DoublyLinkedList<T> temp = new DoublyLinkedList<T>();

        Node<T> node = head;

        while (node != null) {
            if (node.getData() instanceof Integer) {
                Integer value = (Integer) node.getData();
                if (value % 2 == 0) {
                    temp.addLast(node.getData());
                }
            }
            node = node.getNext();
        }
        return temp;
    }

    public int count(T num) {
        Node<T> temp = head;
        int count = 0;
        while (temp != null) {
            if (temp.getData().equals(num)) {
                count++;
            }
            temp = temp.getNext();
        }
        return count;
    }

    public void remove(T num) {
        Node<T> temp = head;

        while (temp != null) {
            if (temp.getData().equals(num)) {
                if (temp == head) {
                    head = temp.getNext();
                    if (head != null) {
                        head.setPrev(null);
                    } else {
                        tail = null;
                    }
                } else if (temp == tail) {
                    tail = tail.getPrev();
                    if (tail != null) {
                        tail.setNext(null);
                    } else {
                        head = null;
                    }
                } else {
                    temp.getPrev().setNext(temp.getNext());
                    temp.getNext().setPrev(temp.getPrev());
                }
            }
            temp = temp.getNext();
        }
    }

    public void printRecursiveForward() {
        printRecFor(head);
        System.out.println("null");
    }

    private void printRecFor(Node<T> node) {
        if (node == null) {
            return;
        }
        System.out.print(node.getData() + " <-> ");
        printRecFor(node.getNext());

    }

    public void printRecursiveBackward() {
        printRecback(tail);
        System.out.println("null");
    }

    private void printRecback(Node<T> node) {
        if (node == null) {
            return;
        }
        System.out.print(node.getData() + " <-> ");
        printRecback(node.getPrev());
    }

    public DoublyLinkedList<T> reverseList(){
        DoublyLinkedList<T> out = new DoublyLinkedList<>();
        buildReverse(tail, out);
        return out;
    }

    private void buildReverse(Node<T> node, DoublyLinkedList<T> out) {
        if (node == null) {
            return;
        }
        out.addLast(node.getData());
        buildReverse(node.getPrev(), out);
    }
}
