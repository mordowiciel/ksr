package main.featureExtraction;

import java.util.List;
import java.util.Map;

public interface FeatureExtractor {
    Map<String, Double> extractFeatures(List<String> rawData);
}
