package main.metrics;

import java.util.Map;

public interface Metric {
    double calculateDistance(Map<String, Double> a, Map<String, Double> b);
}
