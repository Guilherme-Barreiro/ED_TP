package Colecoes.Estruturas;

import Colecoes.interfaces.ArrayOrderedListADT;
import Colecoes.interfaces.BinarySearchTreeADT;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedListBinarySearchTree<T extends Comparable<? super T>> implements ArrayOrderedListADT<T> {

    private final BinarySearchTreeADT<T> tree = new LinkedBinarySearchTree<>();

    @Override
    public void add(T element) {
        tree.addElement(element);
    }

    @Override
    public T removeFirst() {
        if (tree.isEmpty()) throw new NoSuchElementException("list");
        return tree.removeMin();
    }

    @Override
    public T removeLast() {
        if (tree.isEmpty()) throw new NoSuchElementException("list");
        return tree.removeMax();
    }

    @Override
    public T remove(T element) {
        return tree.removeElement(element);
    }

    @Override
    public T first() {
        return tree.findMin();
    }

    @Override
    public T last() {
        return tree.findMax();
    }

    @Override
    public boolean contains(T target) {
        return tree.contains(target);
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public Iterator<T> iterator() {
        return tree.iteratorInOrder();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Iterator<T> it = iterator();
        boolean first = true;
        while (it.hasNext()) {
            if (!first) sb.append(", ");
            sb.append(it.next());
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    public void removeAllOccurrences(T element) {
        tree.removeAllOccurrences(element);
    }

}
