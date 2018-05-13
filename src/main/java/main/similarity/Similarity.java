package main.similarity;

import java.util.Map;

public interface Similarity {

    double calculateSimilarity(Map<String, Double> mapA, Map<String, Double> mapB);
}
