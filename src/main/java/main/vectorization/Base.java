package main.vectorization;

public class Base<T> {

    private String label;
    private T content;

    public Base(String label, T content) {
        this.label = label;
        this.content = content;
    }
}
