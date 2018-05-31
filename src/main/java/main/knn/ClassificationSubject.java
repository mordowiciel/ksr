package main.knn;

import java.util.List;
import java.util.Map;

public class ClassificationSubject {

    private List<String> labels;
    private Map<String, Double> features;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Map<String, Double> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Double> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "ClassificationSubject{" +
                "labels=" + labels +
                ", features=" + features +
                '}';
    }
}
