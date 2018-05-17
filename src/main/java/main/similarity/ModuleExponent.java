package main.similarity;

import java.util.Map;
import java.util.Set;

public class ModuleExponent implements Similarity {

    @Override
    public double calculateSimilarity(Map<String, Double> mapA, Map<String, Double> mapB) {
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
}
