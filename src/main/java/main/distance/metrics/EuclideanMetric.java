package main.distance.metrics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.distance.Distance;

public class EuclideanMetric implements Distance {

    @Override
    public double calculateDistance(Map<String, Double> mapA, Map<String, Double> mapB) {

        double sum = 0;
        for (String keyMapA : mapA.keySet()) {

            Double aValue = mapA.get(keyMapA);
            Double bValue = mapB.get(keyMapA);
            if (bValue == null) {
                bValue = 0.0;
            }

            sum += Math.pow(aValue - bValue, 2);
        }

        return Math.sqrt(sum);
    }

    @Override
    public List<Double> getClosestDistances(List<Double> distances, int count) {
        return distances.stream()
                .sorted(Double::compareTo)
                .limit(count)
                .collect(Collectors.toList());
    }
}
