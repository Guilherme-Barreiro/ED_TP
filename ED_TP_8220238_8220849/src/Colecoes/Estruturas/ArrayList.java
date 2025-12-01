package Colecoes.Estruturas;

import Colecoes.interfaces.ListADT;

import java.util.Iterator;

public abstract class ArrayList<T> implements ListADT<T>, Iterable<T> {

    protected T[] list;
    protected int size;
    protected int modCount;
    protected static final int CAPACITY = 100;

    public ArrayList() {
        this(CAPACITY);
    }

    public ArrayList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        list = (T[]) new Object[capacity];
        size = 0;
        modCount = 0;
    }

    protected void expandCapacity() {
        final int newCapacity = 2;

        T[] newList = (T[]) new Object[list.length * newCapacity];
        System.arraycopy(list, 0, newList, 0, list.length);
        list = newList;

        modCount++;
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
            throw new IllegalStateException("List is empty");
        }
        return list[0];
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return list[size - 1];
    }

    @Override
    public boolean contains(T target) {
        for (int i = 0; i < size; i++) {
            if (list[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<T> {
        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements");
            }
            return list[current++];
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(list[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}