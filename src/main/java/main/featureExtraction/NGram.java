package main.featureExtraction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NGram implements FeatureExtractor {

    public Map<String, Double> extractFeatures(List<String> rawData) {

        Map<String, Double> nGramCounts = new LinkedHashMap<>();

        for (String word : rawData) {

            NGramIterator nGramIterator = new NGramIterator(3, word);
            nGramIterator.forEachRemaining(nGram -> {
                Double count = nGramCounts.get(nGram);
                if (count == null) {
                    count = 1.0;
                    nGramCounts.put(nGram, count);
                } else {
                    count += 1;
                    nGramCounts.put(nGram, count);
                }
            });
        }

        return nGramCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() / (double) nGramCounts.size()));
    }
}
