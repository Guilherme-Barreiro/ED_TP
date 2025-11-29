package Colecoes.Estruturas;

import Excecoes.EmptyCollectionException;
import interfaces.ArrayOrderedListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class DoubleLinkedOrderedList<T extends Comparable<T>> implements ArrayOrderedListADT<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    public DoubleLinkedOrderedList() {
        head = tail = null;
        size = modCount = 0;
    }

    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else if (element.compareTo(head.getData()) <= 0) {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        } else if (element.compareTo(tail.getData()) >= 0) {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null && current.getNext().getData().compareTo(element) < 0) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            newNode.setPrev(current);
            current.getNext().setPrev(newNode);
            current.setNext(newNode);

        }
        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T data = head.getData();
        head = head.getNext();
        if (head == null) {
            tail = null;
        } else {
            head.setPrev(null);
        }
        size--;
        modCount++;
        return data;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T data = tail.getData();
        tail = tail.getPrev();
        if (tail == null) {
            head = null;
        } else {
            tail.setNext(null);
        }
        size--;
        modCount++;
        return data;
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new EmptyCollectionException("List");
        }
        Node<T> current = head;
        while (current != null && current.getData().compareTo(element) != 0) {
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException();
        }
        if (current == head) {
            removeFirst();
        }
        if (current == tail) {
            removeLast();
        }
        current.getPrev().setNext(current.getNext());
        current.getNext().setPrev(current.getPrev());
        size--;
        modCount++;
        return current.getData();
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new EmptyCollectionException("List");
        }
        return head.getData();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new EmptyCollectionException("List");
        }
        return tail.getData();
    }

    @Override
    public boolean contains(T target) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> current = head;
        while (current != null) {
            if (current.getData().compareTo(target) == 0) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public DoubleLinkedUnorderedList<T> inverter() {
        DoubleLinkedUnorderedList<T> invertida = new DoubleLinkedUnorderedList<>();

        Node<T> current = tail;
        while (current != null) {
            invertida.addToRear(current.getData());
            current = current.getPrev();
        }

        return invertida;
    }

    private class MyIterator implements Iterator<T> {
        private Node<T> current = head;
        private int expectedMod = modCount;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (expectedMod != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.getData();
            current = current.getNext();
            return data;
        }
    }
}
