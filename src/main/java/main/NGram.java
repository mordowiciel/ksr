package main;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import main.dataset.Article;

public class NGram {

    public Map<String, Double> calculateNGram(Article article) {

        Map<String, Double> nGramCounts = new LinkedHashMap<>();

        for (String word : article.getBodyWords()) {

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
