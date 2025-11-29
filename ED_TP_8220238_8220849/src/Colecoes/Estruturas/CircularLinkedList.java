package Colecoes.Estruturas;

import java.util.NoSuchElementException;

public class CircularLinkedList<T> {
    private Node<T> tail;
    private int size;

    public CircularLinkedList() {
        tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            tail = newNode;
            tail.setNext(tail);
        }
        else {
            newNode.setNext(tail.getNext());
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> node = tail.getNext();
        T data = node.getData();
        if(node == tail) {
            tail = null;
        }else{
            tail.setNext(node.getNext());
        }
        size--;
        return data;
    }

    public T first(){
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getNext().getData();
    }

}
