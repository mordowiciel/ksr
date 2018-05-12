package main.knn;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import main.dataset.Article;
import main.metrics.Metric;
import main.similarity.SimilarityUtils;
import main.vectorization.TermFrequency;

public class KNNClassifier {

    private final int kNeighboursCount;
    private final Set<Article> trainingSet;
    private final Set<String> keywords;
    private final Set<String> stopwords;
    private final Metric metric;

    public KNNClassifier(int kNeighboursCount, Set<Article> trainingSet,
                         Set<String> keywords, Set<String> stopwords, Metric metric) {
        this.kNeighboursCount = kNeighboursCount;
        this.trainingSet = trainingSet;
        this.keywords = keywords;
        this.stopwords = stopwords;
        this.metric = metric;
    }

    public String classifyObject(Article articleToClassify) {

        long startTime = System.nanoTime();
        System.out.println("Starting classification of object...");

        Map<Article, Double> trainingSetDistances = new HashMap<>();

//        Map<String, Double> allWordsToClassify = TextUtils.getAllWordsCounts(articleToClassify, stopwords, keywords);
        Map<String, Double> allWordsToClassify = TermFrequency.getTDMap(trainingSet, articleToClassify, keywords, stopwords);

        System.out.println(allWordsToClassify);

        // Calculate distances
        System.out.println("Calculating distances for objects in training set...");
        for (Article trainingObject : trainingSet) {

//            Map<String, Double> allWordsTraining = TextUtils.getAllWordsCounts(trainingObject, stopwords, keywords);
            Map<String, Double> allWordsTraining = TermFrequency.getTDMap(trainingSet, trainingObject, keywords, stopwords);

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
