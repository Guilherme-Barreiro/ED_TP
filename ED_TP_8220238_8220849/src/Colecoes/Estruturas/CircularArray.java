package Colecoes.Estruturas;

import Colecoes.interfaces.QueueADT;

import java.util.NoSuchElementException;

public class CircularArray<T> implements QueueADT<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private T[] array;
    private int front;
    private int rear;
    private int count;

    public CircularArray() {
        this(DEFAULT_CAPACITY);
    }

    public CircularArray(int capacity) {
        array = (T[]) new Object[capacity];
        count = front = rear = 0;
    }

    @Override
    public void enqueue(T obj) {
        if (count == array.length) {
            expand();
        }

        array[rear] = obj;
        rear = (rear + 1) % array.length;

        count++;
    }

    @Override
    public T dequeue() {
        if (count == 0) {
            throw new NoSuchElementException();
        }
        T result = array[front];

        array[front] = null;
        front = (front + 1) % array.length;

        count--;

        return result;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return array[front];
    }

    public void expand() {
        int newCapacity = array.length * 2;
        T[] newQueue = (T[]) new Object[newCapacity];
        for (int i = 0; i < count; i++) {
            newQueue[i] = array[(front + i) % array.length];
        }
        array = newQueue;
        front = 0;
        rear = count;
    }
}

