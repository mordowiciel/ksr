package main;

import java.util.List;
import java.util.Map;

public interface Distance {
    double calculateDistance(Map<String, Double> mapA, Map<String, Double> mapB);
    List<Double> getClosestDistances(List<Double> distances, int count);
}
