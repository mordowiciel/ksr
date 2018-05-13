package main.metrics;

import java.util.Map;

public class ManhattanMetric implements Metric {
    @Override
    public double calculateDistance(Map<String, Double> mapA, Map<String, Double> mapB) {
        double sum = 0;

        for (String keyMapA : mapA.keySet()) {

            Double aValue = mapA.get(keyMapA);
            Double bValue = mapB.get(keyMapA);
            if (bValue == null) {
                bValue = 0.0;
            }

            sum += Math.abs(aValue - bValue);
        }

        return sum;
    }
}
