package Colecoes.Estruturas;

import Excecoes.EmptyCollectionException;
import interfaces.StackADT;

public class ArrayStack<T> implements StackADT<T> {
    private static final int maxSize = 100;

    private T[] array;
    private int top;

    public ArrayStack() {
        this(maxSize);
    }

    public ArrayStack(int initialCapacity) {
        array = (T[]) new Object[initialCapacity];
        top = 0;
    }

    @Override
    public void push(T t) {
        if (size() == array.length) {
            expandCapacity();
        }

        array[top++] = t;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        top--;
        T t = array[top];
        array[top] = null;

        return t;
    }


    @Override
    public T peek() {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        return array[top - 1];
    }

    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    @Override
    public int size() {
        return top;
    }

    private void expandCapacity() {
        T[] newStack = (T[]) new Object[array.length * 2];
        System.arraycopy(array, 0, newStack, 0, array.length);
        array = newStack;
    }
}
