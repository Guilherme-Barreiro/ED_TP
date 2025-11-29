package Colecoes.Estruturas;

import interfaces.QueueADT;

public class LinkedQueue<T> implements QueueADT<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void enqueue(T obj) {
        Node<T> node = new Node<>(obj);

        if (isEmpty()) {
            head = node;
        } else {
            tail.setNext(node);
        }

        tail = node;
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }

        T obj = head.getData();
        head = head.getNext();
        size--;

        if (isEmpty()) {
            tail = null;
        }

        return obj;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }

        return head.getData();
    }

}
