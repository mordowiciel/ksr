package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import main.distance.Distance;
import main.distance.metrics.ChebyshevMetric;
import main.distance.metrics.EuclideanMetric;
import main.distance.metrics.ManhattanMetric;
import main.distance.similarity.CosineAmplitude;
import main.distance.similarity.ModuleExponent;
import main.knn.ClassificationSubject;
import main.knn.KNNClassifier;

public class MainIris {

    private static InputArgs inputArgs;
    private static Distance distanceProvider;
    private static Logger LOG = Logger.getLogger(MainIris.class);

    public static void setInputArgs(InputArgs inputArgs) {
        MainIris.inputArgs = inputArgs;
    }

    public static void main(String[] args) throws IOException {

        List<ClassificationSubject> irisData = extractIrisData();
        Collections.shuffle(irisData, new Random(92813L));


        if (inputArgs.getDistance().equals("euclidean")) {
            distanceProvider = new EuclideanMetric();
        }
        if (inputArgs.getDistance().equals("chebyshev")) {
            distanceProvider = new ChebyshevMetric();
        }
        if (inputArgs.getDistance().equals("manhattan")) {
            distanceProvider = new ManhattanMetric();
        }
        if (inputArgs.getDistance().equals("cosine")) {
            distanceProvider = new CosineAmplitude();
        }
        if (inputArgs.getDistance().equals("exp")) {
            distanceProvider = new ModuleExponent();
        }

        int trainingSetSize = (int) Math.floor(0.60 * irisData.size());
        List<ClassificationSubject> trainingSet = irisData.subList(0, trainingSetSize);
        List<ClassificationSubject> testSet = irisData.subList(trainingSetSize, irisData.size());


        KNNClassifier knnClassifier = new KNNClassifier(inputArgs.getNeighbours(), trainingSet, distanceProvider);
        AtomicInteger properlyClassifiedIrisSetosa = new AtomicInteger(0);
        AtomicInteger properlyClassifiedIrisVersicolor = new AtomicInteger(0);
        AtomicInteger properlyClassifiedIrisVirginica = new AtomicInteger(0);
        AtomicInteger properlyClassified = new AtomicInteger(0);

        long testArticlesIrisSetosa = testSet.stream().filter(testElement -> testElement.getLabels().get(0).equals("Iris-setosa")).count();
        long testArticlesIrisVersicolor = testSet.stream().filter(testElement -> testElement.getLabels().get(0).equals("Iris-versicolor")).count();
        long testArticlesIrisVirginica = testSet.stream().filter(testElement -> testElement.getLabels().get(0).equals("Iris-virginica")).count();

        LOG.info("Iris-setosa: " + testArticlesIrisSetosa);
        LOG.info("Iris-versicolor: " + testArticlesIrisVersicolor);
        LOG.info("Iris-virginica: " + testArticlesIrisVirginica);

        testSet.forEach(testElement -> {

            String predictedLabel = knnClassifier.classifyObject(testElement);
            LOG.info("Expected label : " + testElement.getLabels());
            LOG.info("Predicted label : " + predictedLabel);

            if (testElement.getLabels().contains(predictedLabel)) {
                properlyClassified.getAndIncrement();
            }
            if (testElement.getLabels().contains(predictedLabel) && predictedLabel.equals("Iris-setosa")) {
                properlyClassifiedIrisSetosa.getAndIncrement();
            }
            if (testElement.getLabels().contains(predictedLabel) && predictedLabel.equals("Iris-versicolor")) {
                properlyClassifiedIrisVersicolor.getAndIncrement();
            }
            if (testElement.getLabels().contains(predictedLabel) && predictedLabel.equals("Iris-virginica")) {
                properlyClassifiedIrisVirginica.getAndIncrement();
            }
        });

        double percentIrisSetosa = (double) properlyClassifiedIrisSetosa.get() / (double) testArticlesIrisSetosa;
        double percentIrisVersicolor = (double) properlyClassifiedIrisVersicolor.get() / (double) testArticlesIrisVersicolor;
        double percentIrisVirginica = (double) properlyClassifiedIrisVirginica.get() / (double) testArticlesIrisVirginica;
        double totalPercent = ((double) properlyClassified.get() / (double) testSet.size()) * 100.0;

        long arithmeticAverage = properlyClassified.get() / 6;

        long weights = properlyClassifiedIrisSetosa.get() * testArticlesIrisSetosa
                + properlyClassifiedIrisVersicolor.get() * testArticlesIrisVersicolor
                + properlyClassifiedIrisVirginica.get() * testArticlesIrisVirginica;

        long sum = testArticlesIrisSetosa + testArticlesIrisVersicolor + testArticlesIrisVirginica;
        long weightedAverage = weights / sum;

        LOG.info("iris-setosa " +
                properlyClassifiedIrisSetosa.get() + " " + testArticlesIrisSetosa + " " + percentIrisSetosa);

        LOG.info("iris-versicolor " +
                properlyClassifiedIrisVersicolor.get() + " " + testArticlesIrisVersicolor + " " + percentIrisVersicolor);

        LOG.info("iris-virginica "
                + properlyClassifiedIrisVirginica.get() + " " + testArticlesIrisVirginica + " " + percentIrisVirginica);

        LOG.info("total percent " +
                properlyClassified.get() + " " + testSet.size() + " " + String.format("%.2f", totalPercent));

        LOG.info("arithmetic average " +
                arithmeticAverage + " " + testSet.size() + " " +
                String.format("%.2f", (double) arithmeticAverage / testSet.size()));

        LOG.info("weighted average " + weightedAverage + " " + testSet.size() + " " +
                String.format("%.2f", (double) weightedAverage / testSet.size()));
    }

    private static List<ClassificationSubject> extractIrisData() throws IOException {

        List<String> lines = FileUtils.readLines(new File(inputArgs.getDatasetPath()),
                "utf-8");

        List<ClassificationSubject> classificationSubjects = new ArrayList<>();

        for (String line : lines) {

            String[] elements = line.split(",");

            Map<String, Double> features = new HashMap<>();
            features.put("sepal_length", Double.valueOf(elements[0]));
            features.put("sepal_width", Double.valueOf(elements[1]));
            features.put("petal_length", Double.valueOf(elements[2]));
            features.put("petal_width", Double.valueOf(elements[3]));

            List<String> labels = new ArrayList<>();
            labels.add(elements[4]);

            ClassificationSubject classificationSubject = new ClassificationSubject();
            classificationSubject.setFeatures(features);
            classificationSubject.setLabels(labels);

            classificationSubjects.add(classificationSubject);
        }

        return classificationSubjects;
    }
}
