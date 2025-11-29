package Colecoes.Estruturas;

import interfaces.UnorderedListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedUnorderedList<T> implements UnorderedListADT<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    public DoubleLinkedUnorderedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> novo = new Node<>(element);
        if (isEmpty()) {
            head = novo;
            tail = novo;
        } else {
            novo.setNext(head);
            head.setPrev(novo);
            head = novo;
        }
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> novo = new Node<>(element);
        if (isEmpty()) {
            head = tail = novo;
        } else {
            novo.setPrev(tail);
            tail.setNext(novo);
            tail = novo;
        }
        size++;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> current = head;
        while (current != null && !current.getData().equals(target)) {
            current = current.getNext();
        }
        if (current == null) {
            throw new NoSuchElementException("Elemento alvo não encontrado: " + target);
        }
        Node<T> novo = new Node<>(element);
        novo.setPrev(current);
        novo.setNext(current.getNext());
        if (current.getNext() != null) {
            current.getNext().setPrev(novo);
        } else {
            tail = novo;
        }
        current.setNext(novo);
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
            throw new NoSuchElementException();
        }
        Node<T> current = head;
        while (current != null && !current.getData().equals(element)) {
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
        return head.getData();
    }

    @Override
    public T last() {
        return tail.getData();
    }

    @Override
    public boolean contains(T target) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> current = head;
        while (current != null) {
            if (current.getData().equals(target)) {
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
