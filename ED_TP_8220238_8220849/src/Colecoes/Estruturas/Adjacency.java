package Colecoes.Estruturas;

public class Adjacency<T> {
    private T vertex;
    private double weight;

    public Adjacency(T vertex, double weight) {
        this.vertex = vertex;
        this.weight = weight;
    }

    public T getVertex() {
        return vertex;
    }

    public double getWeight() {
        return weight;
    }

    public void setVertex(T vertex) {
        this.vertex = vertex;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" + vertex + ", " + weight + ")";
    }
}
