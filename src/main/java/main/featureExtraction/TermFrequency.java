package main.featureExtraction;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TermFrequency implements FeatureExtractor {

    public Map<String, Double> extractFeatures(List<String> rawData, int sizeThreshold) {

        Map<String, Double> termFrequency = new LinkedHashMap<>();

        for (String word : rawData) {
            Double count = termFrequency.get(word);
            if (count == null) {
                termFrequency.put(word, 0.0);
            } else {
                termFrequency.put(word, ++count);
            }
        }

        Map<String, Double> returnedMap = termFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(sizeThreshold)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return returnedMap;

    }
}
