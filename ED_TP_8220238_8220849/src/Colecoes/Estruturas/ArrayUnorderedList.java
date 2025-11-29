package Colecoes.Estruturas;

import interfaces.UnorderedListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayUnorderedList<T> implements UnorderedListADT<T> {
    private final static int MAX = 10;

    private T[] array;
    private int size;
    private int modCount;

    public ArrayUnorderedList() {
        array = (T[]) new Object[MAX];
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        if (size == array.length) {
            expand();
        }
        for (int i = size; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = element;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        if (size == array.length) {
            expand();
        }
        array[size++] = element;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) {
        if (size == array.length) {
            expand();
        }
        if (isEmpty()) {
            throw new ConcurrentModificationException();
        }
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (array[i].equals(target)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new NoSuchElementException();
        }
        for (int i = size; i > index; i++) {
            array[i] = array[i - 1];
        }
        array[index + 1] = element;
        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T result = array[0];
        for (int i = 0; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        size--;
        modCount++;
        return result;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T result = array[size - 1];
        array[--size] = null;
        modCount++;
        return result;
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                T temp = array[i];
                for (int j = i; j < size - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[--size] = null;
                modCount++;
                return temp;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[0];
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[size - 1];
    }

    @Override
    public boolean contains(T target) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(target)) {
                return true;
            }
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

    private void expand() {
        T[] newArray = (T[]) new Object[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    private class MyIterator implements Iterator<T> {
        private int current = 0;
        private int expectedMod = modCount;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public T next() {
            if (expectedMod != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return array[current++];
        }
    }
}
