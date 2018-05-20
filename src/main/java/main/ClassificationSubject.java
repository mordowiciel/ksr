package main;

import java.util.List;

public class ClassificationSubject {

    private List<String> labels;
    private List<String> rawData;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getRawData() {
        return rawData;
    }

    public void setRawData(List<String> rawData) {
        this.rawData = rawData;
    }
}
