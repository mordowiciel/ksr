package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TfIdf {

    private TermFrequency termFrequency = new TermFrequency();
    private Map<String, Double> termsCount;

    public TfIdf(List<ClassificationSubject> docs) {
        this.termsCount = countTermExistenseInDocument(docs);
    }

    public Map<String, Double> calculateInverseTermDocumentFrequency(ClassificationSubject classificationSubject) {

        Map<String, Double> articleTermFrequency = termFrequency.extractFeatures(classificationSubject);
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

    private Map<String, Double> countTermExistenseInDocument(List<ClassificationSubject> articleList) {

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

    private List<Map<String, Double>> calculateTermFrequencyForAllDocuments(List<ClassificationSubject> articleList) {
        List<Map<String, Double>> termFrequencyForAllDocuments = new ArrayList<>();
        articleList.forEach(article -> {
            Map<String, Double> articleTermFrequency = termFrequency.extractFeatures(article);
            termFrequencyForAllDocuments.add(articleTermFrequency);
        });
        return termFrequencyForAllDocuments;
    }

    private Set<String> getTermSet(List<ClassificationSubject> articleList) {
        Set<String> termSet = new HashSet<>();
        articleList.forEach(article -> termSet.addAll(article.getRawData()));
        return termSet;
    }
}
