package main;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TermFrequency implements FeatureExtractor {

    public Map<String, Double> extractFeatures(ClassificationSubject classificationSubject) {

        Map<String, Double> termFrequency = new LinkedHashMap<>();

        for (String word : classificationSubject.getRawData()) {
            Double count = termFrequency.get(word);
            if (count == null) {
                termFrequency.put(word, 0.0);
            } else {
                termFrequency.put(word, ++count);
            }
        }

        return termFrequency.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue() / classificationSubject.getRawData().size()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));


    }
}
