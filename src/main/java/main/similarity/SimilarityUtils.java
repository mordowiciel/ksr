package main.similarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimilarityUtils {


    public static double calculateCosineAmplitude(Map<String, Double> m1, Map<String, Double> m2) {

        double sumUpper = 0L;
        Set<String> m1Keys = m1.keySet();
        for (String m1Key : m1Keys) {
            Double wordCount1 = m1.get(m1Key);
            Double wordCount2 = m2.get(m1Key);
            if (wordCount2 == null) {
                wordCount2 = 0.0;
            }
            sumUpper += wordCount1 * wordCount2;
        }

        double firstLower = 0L;
        double secondLower = 0L;
        List<Double> m1Values = new ArrayList<>(m1.values());
        List<Double> m2Values = new ArrayList<>(m2.values());

        for (Double m1Value : m1Values) {
            firstLower += Math.pow(m1Value, 2);
        }

        for (Double m2Value : m2Values) {
            secondLower += Math.pow(m2Value, 2);
        }

        double sqrtLower = Math.sqrt(firstLower * secondLower);

        return sumUpper / sqrtLower;
    }


    public static double calculateCosineAmplitude(List<Double> vector1, List<Double> vector2) {

        double numerator = 0;
        double denominator = 0;

        double firstHigher = 0;

        double firstLower = 0;
        double secondLower = 0;

        if (vector1.size() > vector2.size()) {
            int sizeDifference = vector1.size() - vector2.size();
            for (int i = 0; i < sizeDifference; i++) {
                vector2.add(0.0);
            }
        }

        if (vector2.size() < vector1.size()) {
            int sizeDifference = vector2.size() - vector1.size();
            for (int i = 0; i < sizeDifference; i++) {
                vector1.add(0.0);
            }
        }

        for (int i = 0; i < vector1.size(); i++) {
            firstHigher += vector1.get(i) * vector2.get(i);
        }

        for (int i = 0; i < vector1.size(); i++) {
            firstLower += vector1.get(i) * vector1.get(i);
            secondLower += vector2.get(i) * vector2.get(i);
        }

        numerator = Math.abs(firstHigher);
        denominator = Math.sqrt(firstLower * secondLower);

        return numerator / denominator;
    }
}
