package Colecoes.Estruturas;

public class LinkedListSort<T extends Comparable<? super T>> {
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

    public LinkedList<T> invert() {
        LinkedList<T> out = new LinkedList<>();
        buildReverse(head, out);
        return out;
    }

    private void buildReverse(Node<T> node, LinkedList<T> out) {
        if (node == null) {
            return;
        }
        buildReverse(node.getNext(), out);
        out.add(node.getData());
    }

    private int cmp(T a, T b) {
        return a.compareTo(b);
    }

    public void insertionSort() {
        if (head == null || head.getNext() == null) return;
        Node<T> sorted = null;
        Node<T> cur = head;
        while (cur != null) {
            Node<T> next = cur.getNext();
            if (sorted == null || cmp(cur.getData(), sorted.getData()) <= 0) {
                cur.setNext(sorted);
                sorted = cur;
            } else {
                Node<T> s = sorted;
                while (s.getNext() != null && cmp(s.getNext().getData(), cur.getData()) < 0) {
                    s = s.getNext();
                }
                cur.setNext(s.getNext());
                s.setNext(cur);
            }
            cur = next;
        }
        head = sorted;
    }

    public void selectionSort() {
        Node<T> sortedHead = null, sortedTail = null;
        while (head != null) {
            Node<T> minPrev = null, min = head;
            Node<T> prev = head, cur = head.getNext();
            while (cur != null) {
                if (cmp(cur.getData(), min.getData()) < 0) {
                    minPrev = prev;
                    min = cur;
                }
                prev = cur;
                cur = cur.getNext();
            }
            if (minPrev == null) head = head.getNext();
            else minPrev.setNext(min.getNext());
            min.setNext(null);
            if (sortedHead == null) {
                sortedHead = sortedTail = min;
            } else {
                sortedTail.setNext(min);
                sortedTail = min;
            }
        }
        head = sortedHead;
    }

    public void bubbleSort() {
        if (head == null || head.getNext() == null) return;
        boolean swapped;
        do {
            swapped = false;
            Node<T> dummy = new Node<>(null);
            dummy.setNext(head);
            Node<T> prev = dummy;
            Node<T> a = head;
            while (a != null && a.getNext() != null) {
                Node<T> b = a.getNext();
                if (cmp(a.getData(), b.getData()) > 0) {
                    a.setNext(b.getNext());
                    b.setNext(a);
                    prev.setNext(b);
                    swapped = true;
                    prev = b;
                } else {
                    prev = a;
                    a = a.getNext();
                }
            }
            head = dummy.getNext();
        } while (swapped);
    }

    public void mergeSort() {
        head = mergeSortRec(head);
    }

    private Node<T> mergeSortRec(Node<T> h) {
        if (h == null || h.getNext() == null) return h;
        Node<T> mid = middle(h);
        Node<T> right = mid.getNext();
        mid.setNext(null);
        Node<T> leftSorted = mergeSortRec(h);
        Node<T> rightSorted = mergeSortRec(right);
        return mergeLists(leftSorted, rightSorted);
    }

    private Node<T> middle(Node<T> h) {
        Node<T> slow = h, fast = h.getNext();
        while (fast != null && fast.getNext() != null) {
            slow = slow.getNext();
            fast = fast.getNext().getNext();
        }
        return slow;
    }

    private Node<T> mergeLists(Node<T> a, Node<T> b) {
        Node<T> dummy = new Node<>(null);
        Node<T> tail = dummy;
        while (a != null && b != null) {
            if (cmp(a.getData(), b.getData()) <= 0) {
                tail.setNext(a);
                a = a.getNext();
            } else {
                tail.setNext(b);
                b = b.getNext();
            }
            tail = tail.getNext();
            tail.setNext(null);
        }
        tail.setNext(a != null ? a : b);
        return dummy.getNext();
    }

    public void quickSort() {
        head = quickSortRec(head);
    }

    private Node<T> quickSortRec(Node<T> h) {
        if (h == null || h.getNext() == null) return h;

        T pivot = middle(h).getData();

        Node<T> lessH = null, lessT = null;
        Node<T> equalH = null, equalT = null;
        Node<T> greaterH = null, greaterT = null;

        Node<T> cur = h;
        while (cur != null) {
            Node<T> nxt = cur.getNext();
            cur.setNext(null);
            int c = cur.getData().compareTo(pivot);
            if (c < 0) {
                if (lessH == null) lessH = lessT = cur;
                else {
                    lessT.setNext(cur);
                    lessT = cur;
                }
            } else if (c == 0) {
                if (equalH == null) equalH = equalT = cur;
                else {
                    equalT.setNext(cur);
                    equalT = cur;
                }
            } else {
                if (greaterH == null) greaterH = greaterT = cur;
                else {
                    greaterT.setNext(cur);
                    greaterT = cur;
                }
            }
            cur = nxt;
        }

        lessH = quickSortRec(lessH);
        greaterH = quickSortRec(greaterH);

        return concat(concat(lessH, equalH), greaterH);
    }

    private Node<T> concat(Node<T> a, Node<T> b) {
        if (a == null) return b;
        Node<T> cur = a;
        while (cur.getNext() != null) cur = cur.getNext();
        cur.setNext(b);
        return a;
    }
}

