package Colecoes.Estruturas;

import Colecoes.interfaces.StackADT;

import java.util.EmptyStackException;

public class LinkedStack<T> implements StackADT<T> {
    private Node<T> top;
    private int size;

    public LinkedStack() {
        top = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void push(T value) {
        Node<T> node = new Node<>(value);

        node.setNext(top);
        top = node;

        size++;
    }
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        T result = top.getData();
        top = top.getNext();
        size--;

        return result;
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        return top.getData();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LinkedStack{");

        sb.append("top=").append(top);
        sb.append(", size=").append(size);
        sb.append('}');

        return sb.toString();
    }
}
