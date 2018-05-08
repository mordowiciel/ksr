package similarity;

import java.util.List;

public class SimilarityUtils {

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
