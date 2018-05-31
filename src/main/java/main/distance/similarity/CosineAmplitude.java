package main.distance.similarity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import main.distance.Distance;

public class CosineAmplitude implements Distance {

    @Override
    public double calculateDistance(Map<String, Double> mapA, Map<String, Double> mapB) {
        double sumUpper = 0.0;
        Set<String> m1Keys = mapA.keySet();
        for (String m1Key : m1Keys) {
            Double wordCount1 = mapA.get(m1Key);
            Double wordCount2 = mapB.get(m1Key);
            if (wordCount2 == null) {
                wordCount2 = 0.0;
            }
            sumUpper += wordCount1 * wordCount2;
        }

        double firstLower = 0.0;
        double secondLower = 0.0;
        List<Double> m1Values = new ArrayList<>(mapA.values());
        List<Double> m2Values = new ArrayList<>(mapB.values());

        for (Double m1Value : m1Values) {
            firstLower += Math.pow(m1Value, 2);
        }

        for (Double m2Value : m2Values) {
            secondLower += Math.pow(m2Value, 2);
        }

        if (firstLower == 0.0 || secondLower == 0.0) {
            return 0.0;
        }

        double sqrtLower = Math.sqrt(firstLower * secondLower);
        double result = sumUpper / sqrtLower;

        return result;
    }

    @Override
    public List<Double> getClosestDistances(List<Double> distances, int count) {
        return distances.stream()
                .sorted(Comparator.reverseOrder())
                .limit(count)
                .collect(Collectors.toList());
    }
}
