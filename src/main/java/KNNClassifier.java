import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KNNClassifier {

    private final int kNeighboursCount;
    private final List<ClassificationSubject> trainingSet;
    private final Metric metric;

    public KNNClassifier(int kNeighboursCount, List<ClassificationSubject> trainingSet, Metric metric) {
        this.kNeighboursCount = kNeighboursCount;
        this.trainingSet = trainingSet;
        this.metric = metric;
    }

    public String classifyObject(ClassificationSubject subjectToClassify) {

        Map<ClassificationSubject, Double> trainingSetDistances = new HashMap<>();

        // Calculate distances
        for(ClassificationSubject trainingObject : trainingSet) {
            double distance = metric.calculateDistance(subjectToClassify.getVector(), trainingObject.getVector());
            trainingSetDistances.put(trainingObject, distance);
        }

        // Sort distances
//        Map<ClassificationSubject, Double> kNeighbours = trainingSetDistances.entrySet().stream()
//                .sorted(Map.Entry.comparingByValue())
//                .limit(kNeighboursCount)
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Sort distances
        List<ClassificationSubject> kNeighbours = trainingSetDistances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .limit(kNeighboursCount)
                .collect(Collectors.toList());

        // Return the most popular label
        Map<String, Integer> labelCount = new HashMap<>();
        for(ClassificationSubject neighbour : kNeighbours) {
            Integer currentLabelCount = labelCount.get(neighbour.getLabel());
            if(currentLabelCount == null) {
                labelCount.put(neighbour.getLabel(), 1);
            }
            else {
                labelCount.put(neighbour.getLabel(), ++currentLabelCount);
            }
        }

        String classifiedLabel = Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getKey();
        return classifiedLabel;
    }
}
