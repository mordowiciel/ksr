package main;

public class InputArgs {

    private int neighbours;
    private String distance;
    private String featureExtractor;
    private String labels;
    private String dataset;

    public int getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(int neighbours) {
        this.neighbours = neighbours;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFeatureExtractor() {
        return featureExtractor;
    }

    public void setFeatureExtractor(String featureExtractor) {
        this.featureExtractor = featureExtractor;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }
}
