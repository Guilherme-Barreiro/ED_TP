package Colecoes.Estruturas;

import Colecoes.interfaces.BinarySearchTreeADT;

import java.util.NoSuchElementException;

public class ArrayBinarySearchTree<T> extends ArrayBinaryTree<T> implements BinarySearchTreeADT<T> {
    protected int height;
    protected int maxIndex;

    /**
     * Creates an empty binary search tree.
     */
    public ArrayBinarySearchTree() {
        super();
        height = 0;
        maxIndex = 0;
    }

    /**
     * Creates a binary search with the specified element as its root
     *
     * @param element the element that will become the root of
     *                the new tree
     */
    public ArrayBinarySearchTree(T element) {
        super(element);
        height = 1;
        maxIndex = 0;
    }

    /**
     * Adds the specified object to this binary search tree in the
     * appropriate position according to its key value. Note that
     * equal elements are added to the right. Also note that the
     * index of the left child of the current index can be found by
     * doubling the current index and adding 1. Finding the index
     * of the right child can be calculated by doubling the current
     * index and adding 2.
     *
     * @param element the element to be added to the search tree
     */
    public void addElement(T element) {
        if (tree.length < maxIndex * 2 + 3)
            expandCapacity();
        Comparable<T> tempelement = (Comparable<T>) element;
        if (isEmpty()) {
            tree[0] = element;
            maxIndex = 0;
        } else {
            boolean added = false;
            int currentIndex = 0;
            while (!added) {
                if (tempelement.compareTo((tree[currentIndex])) < 0) {
                    /** go left */
                    if (tree[currentIndex * 2 + 1] == null) {
                        tree[currentIndex * 2 + 1] = element;
                        added = true;
                        if (currentIndex * 2 + 1 > maxIndex)
                            maxIndex = currentIndex * 2 + 1;
                    } else
                        currentIndex = currentIndex * 2 + 1;
                } else {
                    /** go right */
                    if (tree[currentIndex * 2 + 2] == null) {
                        tree[currentIndex * 2 + 2] = element;
                        added = true;
                        if (currentIndex * 2 + 2 > maxIndex)
                            maxIndex = currentIndex * 2 + 2;
                    } else
                        currentIndex = currentIndex * 2 + 2;
                }
            }
        }
        height = (int) (Math.log(maxIndex + 1) / Math.log(2)) + 1;
        count++;
    }

    private void expandCapacity() {
        int oldLen = (tree == null) ? 0 : tree.length;
        int newLen = (oldLen == 0) ? 1 : oldLen * 2;
        T[] newTree = (T[]) new Object[newLen];
        if (tree != null) {
            System.arraycopy(tree, 0, newTree, 0, oldLen);
        }
        tree = newTree;
    }

    @Override
    public T removeElement(T targetElement) {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");

        Comparable<T> key = (Comparable<T>) targetElement;

        int i = 0;
        while (i < tree.length && tree[i] != null) {
            int cmp = key.compareTo(tree[i]);
            if (cmp == 0) break;
            i = (cmp < 0) ? (2 * i + 1) : (2 * i + 2);
        }
        if (i >= tree.length || tree[i] == null)
            throw new NoSuchElementException("binary search tree");

        T result = tree[i];

        int li = 2 * i + 1, ri = 2 * i + 2;
        boolean hasL = li < tree.length && tree[li] != null;
        boolean hasR = ri < tree.length && tree[ri] != null;

        if (hasL && hasR) {
            int s = ri;
            while (true) {
                int ls = 2 * s + 1;
                if (ls >= tree.length || tree[ls] == null) break;
                s = ls;
            }
            tree[i] = tree[s];
            i = s;
        }

        while (true) {
            li = 2 * i + 1;
            ri = 2 * i + 2;
            boolean hL = li < tree.length && tree[li] != null;
            boolean hR = ri < tree.length && tree[ri] != null;

            if (!hL && !hR) {
                tree[i] = null;
                break;
            } else if (hR) {
                tree[i] = tree[ri];
                i = ri;
            } else {
                tree[i] = tree[li];
                i = li;
            }
        }

        count--;
        return result;
    }

    @Override
    public void removeAllOccurrences(T targetElement) {
        while (true) {
            try {
                removeElement(targetElement);
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }

    @Override
    public T removeMin() {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");

        int i = 0;
        int li = 2 * i + 1;
        while (li < tree.length && tree[li] != null) {
            i = li;
            li = 2 * i + 1;
        }

        T res = tree[i];

        while (true) {
            int left = 2 * i + 1, right = 2 * i + 2;
            boolean hL = left < tree.length && tree[left] != null;
            boolean hR = right < tree.length && tree[right] != null;

            if (!hL && !hR) {
                tree[i] = null;
                break;
            } else if (hR) {
                tree[i] = tree[right];
                i = right;
            }else {
                tree[i] = tree[left];
                i = left;
            }
        }

        count--;
        return res;
    }

    @Override
    public T removeMax() {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");

        int i = 0;
        int ri = 2 * i + 2;
        while (ri < tree.length && tree[ri] != null) {
            i = ri;
            ri = 2 * i + 2;
        }

        T res = tree[i];

        while (true) {
            int left = 2 * i + 1, right = 2 * i + 2;
            boolean hL = left < tree.length && tree[left] != null;
            boolean hR = right < tree.length && tree[right] != null;

            if (!hL && !hR) {
                tree[i] = null;
                break;
            } else if (hL) {
                tree[i] = tree[left];
                i = left;
            }else {
                tree[i] = tree[right];
                i = right;
            }

        }

        count--;
        return res;
    }

    @Override
    public T findMin() {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");
        int i = 0;
        int li = 2*i + 1;
        while (li < tree.length && tree[li] != null) {
            i = li;
            li = 2*i + 1;
        }
        return tree[i];
    }

    @Override
    public T findMax() {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");
        int i = 0;
        int ri = 2*i + 2;
        while (ri < tree.length && tree[ri] != null) {
            i = ri;
            ri = 2*i + 2;
        }
        return tree[i];

    }

}

