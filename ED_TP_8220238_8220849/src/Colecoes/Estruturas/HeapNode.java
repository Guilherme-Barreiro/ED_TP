package Colecoes.Estruturas;

public class HeapNode<T> extends BinaryTreeNode<T> {
    protected HeapNode<T> parent;

    /**
     * Creates a new heap node with the specified data.
     *
     * @param obj the data to be contained within
     * the new heap nodes
     */
    HeapNode (T obj) {
        super(obj);
        parent = null;
    }


}
