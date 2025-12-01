package Colecoes.Estruturas;

import Colecoes.interfaces.NetworkADT;

import java.util.Iterator;

public class NetworkList<T> implements NetworkADT<T> {
    protected final int DEFAULT_CAPACITY = 10;

    protected int numVertices;
    protected T[] vertices;
    protected ArrayUnorderedList<Adjacency<T>>[] adjList;

    public NetworkList() {
        numVertices = 0;
        vertices = (T[]) new Object[DEFAULT_CAPACITY];
        adjList = (ArrayUnorderedList<Adjacency<T>>[])
                new ArrayUnorderedList[DEFAULT_CAPACITY];

        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            adjList[i] = new ArrayUnorderedList<>();
        }
    }

    protected boolean indexIsValid(int index) {
        return index >= 0 && index < numVertices;
    }

    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) return i;
        }
        return -1;
    }

    protected void expandCapacity() {
        T[] newVerts = (T[]) new Object[vertices.length * 2];
        ArrayUnorderedList<Adjacency<T>>[] newAdj =
                (ArrayUnorderedList<Adjacency<T>>[])
                        new ArrayUnorderedList[adjList.length * 2];

        for (int i = 0; i < numVertices; i++) {
            newVerts[i] = vertices[i];
            newAdj[i] = adjList[i];
        }
        for (int i = numVertices; i < newAdj.length; i++) {
            newAdj[i] = new ArrayUnorderedList<>();
        }

        vertices = newVerts;
        adjList = newAdj;
    }

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        int i = getIndex(vertex1);
        int j = getIndex(vertex2);
        if (!indexIsValid(i) || !indexIsValid(j)) return;

        adjList[i].addToRear(new Adjacency<>(vertex2, weight));
        adjList[j].addToRear(new Adjacency<>(vertex1, weight));
    }

    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        int start = getIndex(vertex1);
        int target = getIndex(vertex2);

        if (!indexIsValid(start) || !indexIsValid(target)) {
            return Double.POSITIVE_INFINITY;
        }

        double[] dist = new double[numVertices];
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }
        dist[start] = 0.0;

        for (int k = 0; k < numVertices; k++) {
            int u = -1;
            double best = Double.POSITIVE_INFINITY;
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && dist[i] < best) {
                    best = dist[i];
                    u = i;
                }
            }

            if (u == -1 || dist[u] == Double.POSITIVE_INFINITY) {
                break;
            }

            visited[u] = true;
            if (u == target) {
                break;
            }

            Iterator<Adjacency<T>> itAdj = adjList[u].iterator();
            while (itAdj.hasNext()) {
                Adjacency<T> neigh = itAdj.next();
                int v = getIndex(neigh.getVertex());
                if (!visited[v]) {
                    double w = neigh.getWeight();
                    double newDist = dist[u] + w;
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                    }
                }
            }
        }

        return dist[target];
    }

    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length) expandCapacity();
        vertices[numVertices] = vertex;
        numVertices++;
    }

    @Override
    public void removeVertex(T vertex) {
        int index = getIndex(vertex);
        if (!indexIsValid(index)) return;

        for (int i = 0; i < numVertices; i++) {
            removeNeighborFromList(i, vertex);
        }

        for (int i = index; i < numVertices - 1; i++) {
            vertices[i] = vertices[i + 1];
            adjList[i] = adjList[i + 1];
        }

        vertices[numVertices - 1] = null;
        adjList[numVertices - 1] = new ArrayUnorderedList<>();
        numVertices--;
    }

    private void removeNeighborFromList(int index, T targetVertex) {
        ArrayUnorderedList<Adjacency<T>> oldList = adjList[index];
        ArrayUnorderedList<Adjacency<T>> newList = new ArrayUnorderedList<>();

        Iterator<Adjacency<T>> it = oldList.iterator();
        while (it.hasNext()) {
            Adjacency<T> node = it.next();
            if (!node.getVertex().equals(targetVertex)) {
                newList.addToRear(node);
            }
        }

        adjList[index] = newList;
    }

    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(vertex1, vertex2, 1.0);
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        int i = getIndex(vertex1);
        int j = getIndex(vertex2);
        if (!indexIsValid(i) || !indexIsValid(j)) return;

        removeNeighborFromList(i, vertex2);
        removeNeighborFromList(j, vertex1);
    }

    @Override
    public Iterator iteratorBFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        ArrayUnorderedList<T> result = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex)) return result.iterator();

        boolean[] visited = new boolean[numVertices];
        LinkedQueue<Integer> queue = new LinkedQueue<>();

        visited[startIndex] = true;
        queue.enqueue(startIndex);

        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            T vert = vertices[v];
            result.addToRear(vert);

            Iterator<Adjacency<T>> itAdj = adjList[v].iterator();
            while (itAdj.hasNext()) {
                Adjacency<T> neigh = itAdj.next();
                int idx = getIndex(neigh.getVertex());
                if (!visited[idx]) {
                    visited[idx] = true;
                    queue.enqueue(idx);
                }
            }
        }

        return result.iterator();
    }

    @Override
    public Iterator iteratorDFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        ArrayUnorderedList<T> result = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex)) return result.iterator();

        boolean[] visited = new boolean[numVertices];
        LinkedStack<Integer> stack = new LinkedStack<>();

        stack.push(startIndex);
        visited[startIndex] = true;
        result.addToRear(vertices[startIndex]);

        while (!stack.isEmpty()) {
            int v = stack.peek();
            boolean found = false;

            Iterator<Adjacency<T>> itAdj = adjList[v].iterator();
            while (itAdj.hasNext() && !found) {
                Adjacency<T> neigh = itAdj.next();
                int idx = getIndex(neigh.getVertex());
                if (!visited[idx]) {
                    stack.push(idx);
                    visited[idx] = true;
                    result.addToRear(vertices[idx]);
                    found = true;
                }
            }

            if (!found) {
                stack.pop();
            }
        }

        return result.iterator();
    }

    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) {
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
                Iterator<Adjacency<T>> itAdj = adjList[v].iterator();
                while (itAdj.hasNext()) {
                    Adjacency<T> neigh = itAdj.next();
                    int idx = getIndex(neigh.getVertex());
                    if (!visited[idx]) {
                        visited[idx] = true;
                        dist[idx] = dist[v] + 1;
                        pred[idx] = v;
                        q.enqueue(idx);
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
        Iterator it = iteratorBFS(vertices[0]);
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            sb.append(vertices[i]).append(": ");
            Iterator<Adjacency<T>> itAdj = adjList[i].iterator();
            while (itAdj.hasNext()) {
                sb.append(itAdj.next()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
