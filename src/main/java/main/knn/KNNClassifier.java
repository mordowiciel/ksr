package main.knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import main.NGram;
import main.dataset.Article;
import main.metrics.Metric;
import main.similarity.CosineAmplitude;

public class KNNClassifier {

    private final int kNeighboursCount;
    private final List<Article> trainingSet;
    private final Metric metric;
    private final NGram nGram;

    public KNNClassifier(int kNeighboursCount, List<Article> trainingSet, Metric metric) {
        this.kNeighboursCount = kNeighboursCount;
        this.trainingSet = trainingSet;
        this.metric = metric;
        this.nGram = new NGram();
    }

    public String classifyObject(Article classificationObject) {

        CosineAmplitude cosineAmplitude = new CosineAmplitude();

        Map<Article, Double> trainingSetDistances = new HashMap<>();
//        Map<String, Double> classificationObjectTF = tfIdf.calculateInverseTermDocumentFrequency(classificationObject);
        Map<String, Double> classificationObjectNGrams = nGram.calculateNGram(classificationObject);
        ;

        // Calculate distances of training objects
        for (Article trainingObject : trainingSet) {
//            Map<String, Double> trainingObjectTF = tfIdf.calculateInverseTermDocumentFrequency(trainingObject);
//            double distance = metric.calculateDistance(classificationObjectTF, trainingObjectTF);
            Map<String, Double> trainingObjectNGrams = nGram.calculateNGram(trainingObject);
            double distance = cosineAmplitude.calculateSimilarity(classificationObjectNGrams, trainingObjectNGrams);
            trainingSetDistances.put(trainingObject, distance);
        }

        // Sort distances

//        List<Double> distances = trainingSetDistances.entrySet().stream()
//                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//                .map(Map.Entry::getValue)
//                .limit(kNeighboursCount)
//                .collect(Collectors.toList());

        List<Article> kNeighbours = trainingSetDistances.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(kNeighboursCount)
                .collect(Collectors.toList());

        // Return the most popular label
        Map<String, Integer> labelCount = new HashMap<>();
        for (Article neighbour : kNeighbours) {
            Integer currentLabelCount = labelCount.get(neighbour.getPlaces().get(0));
            if (currentLabelCount == null) {
                labelCount.put(neighbour.getPlaces().get(0), 1);
            } else {
                labelCount.put(neighbour.getPlaces().get(0), ++currentLabelCount);
            }
        }

        Integer highestCount = Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getValue();
        List<String> labelsWithHighestCount = getKeysByValue(labelCount, highestCount);

        // DUEL!
        if (labelsWithHighestCount.size() > 1) {

            Map<String, Double> duelMap = new HashMap<>();
            for (String label : labelsWithHighestCount) {

                Map<Article, Double> articlesWithLabel = trainingSetDistances.entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().getPlaces().get(0).equals(label))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                Double closestArticle = Collections.min(articlesWithLabel.entrySet(), Map.Entry.comparingByValue()).getValue();
                duelMap.put(label, closestArticle);
            }

            String winnerLabel = Collections.min(duelMap.entrySet(), Map.Entry.comparingByValue()).getKey();
            return winnerLabel;
        } else {
            String classifiedLabel = Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            return classifiedLabel;
        }
    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        List<T> keys = new ArrayList<>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
