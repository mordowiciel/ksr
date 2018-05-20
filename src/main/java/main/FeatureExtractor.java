package main;

import java.util.Map;

import main.dataset.Article;

public interface FeatureExtractor {
    public Map<String, Double> extractFeatures(Article article);
}
