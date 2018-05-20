package main;

import java.util.Map;

public interface FeatureExtractor {
    Map<String, Double> extractFeatures(ClassificationSubject classificationSubject);
}
