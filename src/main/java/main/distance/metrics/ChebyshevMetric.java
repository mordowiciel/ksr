package main.distance.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.distance.Distance;

public class ChebyshevMetric implements Distance {

    @Override
    public double calculateDistance(Map<String, Double> mapA, Map<String, Double> mapB) {

        List<Double> vectorDistances = new ArrayList();
        for (String keyMapA : mapA.keySet()) {

            Double aValue = mapA.get(keyMapA);
            Double bValue = mapB.get(keyMapA);
            if (bValue == null) {
                bValue = 0.0;
            }
            vectorDistances.add(Math.abs(aValue - bValue));
        }

        return Collections.max(vectorDistances);
    }

    @Override
    public List<Double> getClosestDistances(List<Double> distances, int count) {
        return distances.stream()
                .sorted(Double::compareTo)
                .limit(count)
                .collect(Collectors.toList());
    }
}
