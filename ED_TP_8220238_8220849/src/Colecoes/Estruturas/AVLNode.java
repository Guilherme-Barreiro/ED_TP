package Colecoes.Estruturas;

public class AVLNode<T> extends BinaryTreeNode<T> {
    protected int height;

    AVLNode(T e) {
        super(e);
        height = 1;
    }
}

