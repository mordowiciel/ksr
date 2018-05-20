package main;

import java.util.List;
import java.util.Map;

public class ClassificationSubject {

    private List<String> labels;
    //    private List<String> rawData;
    private Map<String, Double> features;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

//    public List<String> getRawData() {
//        return rawData;
//    }
//
//    public void setRawData(List<String> rawData) {
//        this.rawData = rawData;
//    }

    public Map<String, Double> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Double> features) {
        this.features = features;
    }
}
