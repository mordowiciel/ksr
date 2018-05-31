package main.featureExtraction;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NGram implements FeatureExtractor {

    private final int n;

    public NGram(int n) {
        this.n = n;
    }

    @Override
    public Map<String, Double> extractFeatures(List<String> rawData, double percentThreshold) {

        Map<String, Double> nGramCounts = new LinkedHashMap<>();

        for (String word : rawData) {
            NGramIterator nGramIterator = new NGramIterator(n, word);
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
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() / rawData.size(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

    }
}
