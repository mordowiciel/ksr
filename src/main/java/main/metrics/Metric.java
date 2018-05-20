package main.metrics;

import java.util.Map;

public interface Metric {
    double calculateDistance(Map<String, Double> mapA, Map<String, Double> mapB);
}
