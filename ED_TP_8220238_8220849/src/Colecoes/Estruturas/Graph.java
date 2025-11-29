package Colecoes.Estruturas;

import interfaces.GraphADT;

import java.util.Iterator;

/**
 * Graph represents an adjacency matrix implementation of a graph.
 */
public class Graph<T> implements GraphADT<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected boolean[][] adjMatrix;
    protected T[] vertices;

    /**
     * Creates an empty graph.
     */
    public Graph() {
        numVertices = 0;
        adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        vertices = (T[]) new Object[DEFAULT_CAPACITY];
    }

    protected boolean indexIsValid(int index) {
        return (index >= 0 && index < numVertices);
    }

    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) return i;
        }
        return -1;
    }

    protected void expandCapacity() {
        boolean[][] newAdj = new boolean[adjMatrix.length * 2][adjMatrix.length * 2];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                newAdj[i][j] = adjMatrix[i][j];
            }
        }
        adjMatrix = newAdj;

        T[] newVerts = (T[]) (new Object[vertices.length * 2]);
        for (int i = 0; i < numVertices; i++) {
            newVerts[i] = vertices[i];
        }
        vertices = newVerts;
    }

    /**
     * Adds a vertex to the graph, expanding the capacity of the graph
     * if necessary. It also associates an object with the vertex.
     *
     * @param vertex the vertex to add to the graph
     */
    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();
        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }
        numVertices++;

    }

    @Override
    public void removeVertex(T vertex) {
        int index = getIndex(vertex);
        if (!indexIsValid(index)) return;

        for (int i = index; i < numVertices - 1; i++) {
            vertices[i] = vertices[i + 1];
        }
        for (int i = index; i < numVertices - 1; i++) {
            for (int j = 0; j < numVertices; j++) {
                adjMatrix[i][j] = adjMatrix[i + 1][j];
            }
        }
        for (int j = index; j < numVertices - 1; j++) {
            for (int i = 0; i < numVertices; i++) {
                adjMatrix[i][j] = adjMatrix[i][j + 1];
            }
        }
        numVertices--;
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param index1 the first index
     * @param index2 the second index
     */
    public void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true;
            adjMatrix[index2][index1] = true;
        }
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        int i = getIndex(vertex1);
        int j = getIndex(vertex2);
        if (indexIsValid(i) && indexIsValid(j)) {
            adjMatrix[i][j] = false;
            adjMatrix[j][i] = false;
        }
    }

    @Override
    public Iterator iteratorBFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        return iteratorBFS(startIndex);
    }

    /**
     * Returns an iterator that performs a breadth first search
     * traversal starting at the given index.
     *
     * @param startIndex the index to begin the search from
     * @return an iterator that performs a breadth first traversal
     */
    private Iterator<T> iteratorBFS(int startIndex) {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();

        if (!indexIsValid(startIndex))
            return resultList.iterator();

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x.intValue()]);
            /** Find all vertices adjacent to x that have
             not been visited and queue them up */
            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x.intValue()][i] && !visited[i]) {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    @Override
    public Iterator iteratorDFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        return iteratorDFS(startIndex);
    }

    /**
     * Returns an iterator that performs a depth first search
     * traversal starting at the given index.
     *
     * @param startIndex the index to begin the search traversal from
     * @return an iterator that performs a depth first traversal
     */
    private Iterator<T> iteratorDFS(int startIndex) {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        boolean[] visited = new boolean[numVertices];
        if (!indexIsValid(startIndex))
            return resultList.iterator();
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;
        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;
            /** Find a vertex adjacent to x that has not been visited
             and push it on the stack */
            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x.intValue()][i] && !visited[i]) {
                    traversalStack.push(i);
                    resultList.addToRear(vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty())
                traversalStack.pop();
        }
        return resultList.iterator();
    }

    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        int start = getIndex(startVertex);
        int target = getIndex(targetVertex);

        ArrayUnorderedList<T> empty = new ArrayUnorderedList<>();
        if (!indexIsValid(start) || !indexIsValid(target)) return empty.iterator();

        int[] pred = new int[numVertices];
        int[] dist = new int[numVertices];
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            pred[i] = -1;
            dist[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        LinkedQueue<Integer> q = new LinkedQueue<>();
        visited[start] = true;
        dist[start] = 0;
        q.enqueue(start);

        boolean found = false;

        while (!q.isEmpty() && !found) {
            int v = q.dequeue();
            if (v == target) {
                found = true;
            } else {
                for (int i = 0; i < numVertices; i++) {
                    if (adjMatrix[v][i] && !visited[i]) {
                        visited[i] = true;
                        dist[i] = dist[v] + 1;
                        pred[i] = v;
                        q.enqueue(i);
                    }
                }
            }
        }

        if (!found) return empty.iterator();

        LinkedStack<T> stack = new LinkedStack<>();
        int current = target;
        while (current != -1) {
            stack.push(vertices[current]);
            current = pred[current];
        }

        ArrayUnorderedList<T> result = new ArrayUnorderedList<>();
        while (!stack.isEmpty()) {
            result.addToRear(stack.pop());
        }

        return result.iterator();
    }


    @Override
    public boolean isEmpty() {
        return numVertices == 0;
    }

    @Override
    public boolean isConnected() {
        if (isEmpty()) return true;
        Iterator<T> it = iteratorBFS(vertices[0]);
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count == numVertices;
    }

    @Override
    public int size() {
        return numVertices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("   ");
        for (int i = 0; i < numVertices; i++) {
            sb.append(vertices[i]).append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < numVertices; i++) {
            sb.append(vertices[i]).append(" ");
            for (int j = 0; j < numVertices; j++) {
                sb.append(adjMatrix[i][j] ? "1 " : "0 ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
