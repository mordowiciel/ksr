package main.knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import main.ClassificationSubject;
import main.Distance;
import main.FeatureExtractor;

public class KNNClassifier {

    private final int kNeighboursCount;
    private final List<ClassificationSubject> trainingSet;
    private final Distance distanceProvider;
    private final FeatureExtractor featureExtractor;

    public KNNClassifier(int kNeighboursCount, List<ClassificationSubject> trainingSet, Distance distanceProvider,
                         FeatureExtractor featureExtractor) {
        this.kNeighboursCount = kNeighboursCount;
        this.trainingSet = trainingSet;
        this.distanceProvider = distanceProvider;
        this.featureExtractor = featureExtractor;
    }

    public String classifyObject(ClassificationSubject classificationObject) {

        Map<ClassificationSubject, Double> trainingSetDistances = new HashMap<>();
        Map<String, Double> classificationObjectNGrams = featureExtractor.extractFeatures(classificationObject);

        // Calculate distances of training objects
        for (ClassificationSubject trainingObject : trainingSet) {
            Map<String, Double> trainingObjectNGrams = featureExtractor.extractFeatures(trainingObject);
            double distance = distanceProvider.calculateDistance(classificationObjectNGrams, trainingObjectNGrams);
            trainingSetDistances.put(trainingObject, distance);
        }

        List<Double> distances = distanceProvider.getClosestDistances(new ArrayList<>(trainingSetDistances.values()), kNeighboursCount);
        List<ClassificationSubject> kNeighbours = new ArrayList<>();

        for (Double distance : distances) {
            ClassificationSubject article = getKeysByValue(trainingSetDistances, distance).get(0);
            kNeighbours.add(article);
        }


        // Return the most popular label
        Map<String, Integer> labelCount = new HashMap<>();
        for (ClassificationSubject neighbour : kNeighbours) {

            List<String> neighbourLabels = neighbour.getLabels();

            for (String label : neighbourLabels) {
                Integer currentLabelCount = labelCount.get(label);
                if (currentLabelCount == null) {
                    labelCount.put(label, 1);
                } else {
                    labelCount.put(label, ++currentLabelCount);
                }
            }

        }

        Integer highestCount = Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getValue();
        List<String> labelsWithHighestCount = getKeysByValue(labelCount, highestCount);

        // DUEL!
        if (labelsWithHighestCount.size() > 1) {

            Map<String, Double> duelMap = new HashMap<>();
            for (String label : labelsWithHighestCount) {

                Map<ClassificationSubject, Double> articlesWithLabel = trainingSetDistances.entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().getLabels().contains(label))
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
