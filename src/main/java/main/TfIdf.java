package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.dataset.Article;

public class TfIdf {

    private TermFrequency termFrequency = new TermFrequency();
    private Map<String, Double> termsCount;

    public TfIdf(List<Article> docs) {
        this.termsCount = countTermExistenseInDocument(docs);
    }

    public Map<String, Double> calculateInverseTermDocumentFrequency(Article article) {

        Map<String, Double> articleTermFrequency = termFrequency.calculateTermFrequency(article);
        Map<String, Double> inverseDocumentFrequency = new HashMap<>();

        for (Map.Entry<String, Double> termFrequency : articleTermFrequency.entrySet()) {

            String term = termFrequency.getKey();
            double tf = articleTermFrequency.get(term);

            Double count = termsCount.get(term);
            if (count == null) {
                count = 1.0;
            }

            Double idf = termsCount.size() / (count + 1.0);
            idf = Math.log(idf);

            double tfidf = tf * idf;
            inverseDocumentFrequency.put(term, tfidf);
        }

        return inverseDocumentFrequency;
    }

    private Map<String, Double> countTermExistenseInDocument(List<Article> articleList) {

        Set<String> termsSet = getTermSet(articleList);
        List<Map<String, Double>> termFrequencyOfAllDocuments = calculateTermFrequencyForAllDocuments(articleList);

        Map<String, Double> termCountInAllDocuments = new HashMap<>();
        termsSet.forEach(term -> {
            double count = 0.0;
            for (Map<String, Double> documentTermFrequency : termFrequencyOfAllDocuments) {
                if (documentTermFrequency.get(term) != null) {
                    count++;
                }
            }
            termCountInAllDocuments.put(term, count);
        });

        return termCountInAllDocuments;

    }

    private List<Map<String, Double>> calculateTermFrequencyForAllDocuments(List<Article> articleList) {
        List<Map<String, Double>> termFrequencyForAllDocuments = new ArrayList<>();
        articleList.forEach(article -> {
            Map<String, Double> articleTermFrequency = termFrequency.calculateTermFrequency(article);
            termFrequencyForAllDocuments.add(articleTermFrequency);
        });
        return termFrequencyForAllDocuments;
    }

    private Set<String> getTermSet(List<Article> articleList) {
        Set<String> termSet = new HashSet<>();
        articleList.forEach(article -> termSet.addAll(article.getBodyWords()));
        return termSet;
    }
}
