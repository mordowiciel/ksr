import java.util.List;

public class EuclideanMetric implements Metric {
    @Override
    public double calculateDistance(List<Double> a, List<Double> b) {

        if (a.size() != b.size()) {
            throw new IllegalStateException("Vectors must have same size for calculating the distance using this metric.");
        }

        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += Math.pow(a.get(i) - b.get(i), 2);
        }

        return Math.sqrt(sum);
    }
}
