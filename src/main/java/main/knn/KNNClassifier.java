package main.knn;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import main.TextUtils;
import main.dataset.Article;
import main.metrics.Metric;
import main.similarity.SimilarityUtils;

public class KNNClassifier {

    private final int kNeighboursCount;
    private final List<Article> trainingSet;
    private final List<String> keywords;
    private final Metric metric;

    public KNNClassifier(int kNeighboursCount, List<Article> trainingSet,
                         List<String> keywords, Metric metric) {
        this.kNeighboursCount = kNeighboursCount;
        this.trainingSet = trainingSet;
        this.keywords = keywords;
        this.metric = metric;
    }

    public String classifyObject(Article articleToClassify) {

        long startTime = System.nanoTime();
        System.out.println("Starting classification of object...");

        Map<Article, Double> trainingSetDistances = new HashMap<>();

        // Calculate distances
        System.out.println("Calculating distances for objects in training set...");
        for (Article trainingObject : trainingSet) {

            Map<String, Long> allWordsTraining = TextUtils.getAllWordsCounts(trainingObject);
            Map<String, Long> allWordsToClassify = TextUtils.getAllWordsCounts(articleToClassify);

            keywords.forEach(keyword -> {
                if (allWordsTraining.containsKey(keyword)) {
                    allWordsTraining.put(keyword, 0L);
                }

                if (!allWordsToClassify.containsKey(keyword)) {
                    allWordsToClassify.put(keyword, 0L);
                }
            });

            double distance = SimilarityUtils.calculateCosineAmplitude(allWordsTraining, allWordsToClassify);
            trainingSetDistances.put(trainingObject, distance);
        }

        // Sort distances
        List<Article> kNeighbours = trainingSetDistances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
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

        String classifiedLabel = Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        long stopTime = System.nanoTime();
        long totalTime = stopTime - startTime;

        System.out.println("Finished classification task for given object. : " + TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS));
        return classifiedLabel;
    }
}
