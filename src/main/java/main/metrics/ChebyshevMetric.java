package main.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ChebyshevMetric implements Metric {

    @Override
    public double calculateDistance(Map<String, Double> mapA, Map<String, Double> mapB) {

        List<Double> vectorDistances = new ArrayList();
        for (String keyMapA : mapA.keySet()) {

            Double aValue = mapA.get(keyMapA);
            Double bValue = mapB.get(keyMapA);
            if (bValue == null) {
                bValue = 0.0;
            }
            vectorDistances.add(aValue - bValue);
        }

        return Collections.max(vectorDistances);
    }
}
