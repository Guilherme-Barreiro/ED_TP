package Colecoes.Estruturas;

import interfaces.NetworkADT;

import java.util.Iterator;

public class Network<T> implements NetworkADT<T> {

    protected final int DEFAULT_CAPACITY = 10;

    protected int numVertices;
    protected double[][] adjMatrix;
    protected T[] vertices;

    public Network() {
        numVertices = 0;
        adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        vertices = (T[]) new Object[DEFAULT_CAPACITY];

        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            for (int j = 0; j < DEFAULT_CAPACITY; j++) {
                adjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
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
        double[][] newAdj = new double[adjMatrix.length * 2][adjMatrix.length * 2];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                newAdj[i][j] = adjMatrix[i][j];
            }
        }
        for (int i = numVertices; i < newAdj.length; i++) {
            for (int j = 0; j < newAdj.length; j++) {
                newAdj[i][j] = Double.POSITIVE_INFINITY;
                newAdj[j][i] = Double.POSITIVE_INFINITY;
            }
        }
        adjMatrix = newAdj;

        T[] newVerts = (T[]) new Object[vertices.length * 2];
        for (int i = 0; i < numVertices; i++) {
            newVerts[i] = vertices[i];
        }
        vertices = newVerts;
    }

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        int i = getIndex(vertex1);
        int j = getIndex(vertex2);
        if (!indexIsValid(i) || !indexIsValid(j)) return;

        adjMatrix[i][j] = weight;
        adjMatrix[j][i] = weight;
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

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && adjMatrix[u][v] < Double.POSITIVE_INFINITY) {
                    double newDist = dist[u] + adjMatrix[u][v];
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
        if (numVertices == vertices.length) {
            expandCapacity();
        }
        vertices[numVertices] = vertex;

        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            adjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
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

    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(vertex1, vertex2, 1.0);
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        int i = getIndex(vertex1);
        int j = getIndex(vertex2);
        if (!indexIsValid(i) || !indexIsValid(j)) return;

        adjMatrix[i][j] = Double.POSITIVE_INFINITY;
        adjMatrix[j][i] = Double.POSITIVE_INFINITY;
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
            result.addToRear(vertices[v]);

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[v][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                    visited[i] = true;
                    queue.enqueue(i);
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

            for (int i = 0; i < numVertices && !found; i++) {
                if (adjMatrix[v][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                    stack.push(i);
                    visited[i] = true;
                    result.addToRear(vertices[i]);
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
                for (int i = 0; i < numVertices; i++) {
                    if (adjMatrix[v][i] < Double.POSITIVE_INFINITY && !visited[i]) {
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
        StringBuilder sb = new StringBuilder("   ");
        for (int i = 0; i < numVertices; i++) {
            sb.append(vertices[i]).append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < numVertices; i++) {
            sb.append(vertices[i]).append(" ");
            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] == Double.POSITIVE_INFINITY) {
                    sb.append("∞ ");
                } else {
                    sb.append(adjMatrix[i][j]).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    protected int[] getEdgeWithWeightOf(double weight, boolean[] visited) {
        int[] edge = {-1, -1};

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] == weight) {
                    if ((visited[i] && !visited[j]) || (!visited[i] && visited[j])) {
                        edge[0] = i;
                        edge[1] = j;
                        return edge;
                    }
                }
            }
        }

        return edge;
    }

    public Network<T> mstNetwork() {
        int x, y;
        int index;
        double weight;
        int[] edge = new int[2];

        LinkedHeap<Double> minHeap = new LinkedHeap<Double>();
        Network<T> resultGraph = new Network<T>();

        if (isEmpty() || !isConnected())
            return resultGraph;

        resultGraph.adjMatrix = new double[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                resultGraph.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        resultGraph.vertices = (T[]) (new Object[numVertices]);

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        edge[0] = 0;
        resultGraph.vertices[0] = this.vertices[0];
        resultGraph.numVertices++;
        visited[0] = true;

        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[0][i] < Double.POSITIVE_INFINITY) {
                minHeap.addElement(adjMatrix[0][i]);
            }
        }

        while ((resultGraph.size() < this.size()) && !minHeap.isEmpty()) {
            do {
                weight = minHeap.removeMin();
                edge = getEdgeWithWeightOf(weight, visited);
            } while ((!indexIsValid(edge[0]) || !indexIsValid(edge[1])) && !minHeap.isEmpty());

            if (!indexIsValid(edge[0]) || !indexIsValid(edge[1])) {
                break;
            }

            x = edge[0];
            y = edge[1];

            if (!visited[x]) {
                index = x;
            } else {
                index = y;
            }

            resultGraph.vertices[index] = this.vertices[index];
            visited[index] = true;
            resultGraph.numVertices++;
            resultGraph.adjMatrix[x][y] = this.adjMatrix[x][y];
            resultGraph.adjMatrix[y][x] = this.adjMatrix[y][x];

            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && this.adjMatrix[i][index] < Double.POSITIVE_INFINITY) {
                    minHeap.addElement(this.adjMatrix[index][i]);
                }
            }
        }

        return resultGraph;
    }

}
