package Colecoes.Estruturas;

import interfaces.QueueADT;

import java.util.EmptyStackException;

public class TwoStack<T> implements QueueADT<T> {
    private Stack<T> first;
    private Stack<T> second;
    private int size;

    public TwoStack() {
        first = new Stack<>();
        second = new Stack<>();
        size = 0;
    }

    @Override
    public void enqueue(T obj) {
        first.push(obj);
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        if (second.isEmpty()) {
            while (!first.isEmpty()) {
                second.push(first.pop());
            }
        }
        size--;
        return second.pop();
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
    public T first() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        if (second.isEmpty()) {
            while (!first.isEmpty()) {
                second.push(first.pop());
            }
        }
        return second.peek();
    }
}
