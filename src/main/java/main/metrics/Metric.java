package main.metrics;

import java.util.List;

public interface Metric {
    double calculateDistance(List<Double> a, List<Double> b);
}
