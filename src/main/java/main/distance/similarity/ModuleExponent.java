package main.distance.similarity;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import main.distance.Distance;

public class ModuleExponent implements Distance {

    @Override
    public double calculateDistance(Map<String, Double> mapA, Map<String, Double> mapB) {
        double sum = 0.0;
        Set<String> m1Keys = mapA.keySet();
        for (String m1Key : m1Keys) {
            Double wordCount1 = mapA.get(m1Key);
            Double wordCount2 = mapB.get(m1Key);
            if (wordCount2 == null) {
                wordCount2 = 0.0;
            }
            sum += Math.abs(wordCount1 - wordCount2);
        }

        return Math.exp(-sum);

    }

    @Override
    public List<Double> getClosestDistances(List<Double> distances, int count) {
        return distances.stream()
                .sorted(Comparator.reverseOrder())
                .limit(count)
                .collect(Collectors.toList());
    }
}
