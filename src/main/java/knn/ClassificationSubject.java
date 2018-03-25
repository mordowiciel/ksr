package knn;

import java.util.List;

public class ClassificationSubject {

    private final String label;
    private final List<Double> vector;

    public ClassificationSubject(String label, List<Double> vector) {
        this.label = label;
        this.vector = vector;
    }

    public String getLabel() {
        return label;
    }

    public List<Double> getVector() {
        return vector;
    }
}
