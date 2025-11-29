package Colecoes.Estruturas;

import interfaces.BinarySearchTreeADT;

import java.util.NoSuchElementException;

public class AVLTree<T extends Comparable<? super T>> extends LinkedBinaryTree<T> implements BinarySearchTreeADT<T> {

    private AVLNode<T> asAvl(BinaryTreeNode<T> root) {
        return (AVLNode<T>) root;
    }

    private int h(BinaryTreeNode<T> n) {
        return (n == null) ? 0 : asAvl(n).height;
    }

    private int bf(BinaryTreeNode<T> n) {
        return (n == null) ? 0 : h(n.left) - h(n.right);
    }

    private void upd(AVLNode<T> n) {
        int hl = h(n.left);
        int hr = h(n.right);
        if (hl >= hr) n.height = hl + 1;
        else n.height = hr + 1;
    }

    private AVLNode<T> rotR(AVLNode<T> y) {
        AVLNode<T> x = asAvl(y.left);
        BinaryTreeNode<T> T2 = x.right;
        x.right = y;
        y.left = T2;
        upd(y);
        upd(x);
        return x;
    }

    private AVLNode<T> rotL(AVLNode<T> x) {
        AVLNode<T> y = asAvl(x.right);
        BinaryTreeNode<T> T2 = y.left;
        y.left = x;
        x.right = T2;
        upd(x);
        upd(y);
        return y;
    }

    private AVLNode<T> rebalance(AVLNode<T> n) {
        int b = bf(n);
        if (b > 1) {
            if (bf(n.left) < 0) n.left = rotL(asAvl(n.left));
            return rotR(n);
        }
        if (b < -1) {
            if (bf(n.right) > 0) n.right = rotR(asAvl(n.right));
            return rotL(n);
        }
        return n;
    }

    @Override
    public void addElement(T element) {
        root = insert(asAvl(root), element);
        count++;
    }

    private AVLNode<T> insert(AVLNode<T> n, T e) {
        if (n == null) return new AVLNode<>(e);
        int cmp = ((Comparable<T>) e).compareTo(n.element);
        if (cmp < 0) n.left = insert(asAvl(n.left), e);
        else n.right = insert(asAvl(n.right), e);
        upd(n);
        return rebalance(n);
    }

    @Override
    public T removeElement(T targetElement) {
        T result = find(targetElement);

        root = removeRec(asAvl(root), targetElement);

        count--;
        return result;
    }

    private AVLNode<T> removeRec(AVLNode<T> n, T x) {
        if (n == null) return null;

        int cmp = ((Comparable<T>) x).compareTo(n.element);
        if (cmp < 0) {
            n.left = removeRec(asAvl(n.left), x);
        } else if (cmp > 0) {
            n.right = removeRec(asAvl(n.right), x);
        } else {
            if (n.left == null) return asAvl(n.right);
            if (n.right == null) return asAvl(n.left);
            BinaryTreeNode<T> s = n.right;
            while (s.left != null) s = s.left;
            n.element = s.element;
            n.right = removeMinNode(asAvl(n.right));
        }

        upd(n);
        return rebalance(n);
    }

    @Override
    public void removeAllOccurrences(T targetElement) {
        while (contains(targetElement)) {
            removeElement(targetElement);
        }
    }

    @Override
    public T removeMin() {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");

        T res = findMin();

        root = removeMinRec(asAvl(root));

        count--;
        return res;
    }

    private AVLNode<T> removeMinRec(AVLNode<T> n) {
        if (n.left == null) return asAvl(n.right);
        n.left = removeMinRec(asAvl(n.left));
        upd(n);
        return rebalance(n);
    }

    private AVLNode<T> removeMinNode(AVLNode<T> n) {
        if (n.left == null) return asAvl(n.right);
        n.left = removeMinNode(asAvl(n.left));
        upd(n);
        return rebalance(n);
    }

    @Override
    public T removeMax() {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");
        T res = findMax();
        root = removeMaxRec(asAvl(root));
        count--;
        return res;
    }

    private AVLNode<T> removeMaxRec(AVLNode<T> n) {
        if (n.right == null) return asAvl(n.left);
        n.right = removeMaxRec(asAvl(n.right));
        upd(n);
        return rebalance(n);
    }

    @Override
    public T findMin() {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");
        BinaryTreeNode<T> n = root;
        while (n.left != null) n = n.left;
        return n.element;
    }

    @Override
    public T findMax() {
        if (isEmpty()) throw new NoSuchElementException("binary search tree");
        BinaryTreeNode<T> n = root;
        while (n.right != null) n = n.right;
        return n.element;
    }

    @Override
    public boolean contains(T targetElement) {
        BinaryTreeNode<T> n = root;
        while (n != null) {
            int c = ((Comparable<T>) targetElement).compareTo(n.element);
            if (c == 0) return true;
            n = (c < 0) ? n.left : n.right;
        }
        return false;
    }

    @Override
    public T find(T targetElement) {
        BinaryTreeNode<T> n = root;
        while (n != null) {
            int c = ((Comparable<T>) targetElement).compareTo(n.element);
            if (c == 0) return n.element;
            n = (c < 0) ? n.left : n.right;
        }
        throw new NoSuchElementException("binary tree");
    }
}
