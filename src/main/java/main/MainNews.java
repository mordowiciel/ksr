package main;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import main.dataset.Article;
import main.dataset.Dataset;
import main.dataset.Preprocessing;
import main.dataset.ReutersDataset;
import main.distance.Distance;
import main.distance.metrics.ChebyshevMetric;
import main.distance.metrics.EuclideanMetric;
import main.distance.metrics.ManhattanMetric;
import main.distance.similarity.CosineAmplitude;
import main.distance.similarity.ModuleExponent;
import main.featureExtraction.FeatureExtractor;
import main.featureExtraction.NGram;
import main.featureExtraction.TermFrequency;
import main.knn.ClassificationSubject;
import main.knn.KNNClassifier;

public class MainNews {

    private static Logger LOG = Logger.getLogger("app");
    private static InputArgs inputArgs;
    private static FeatureExtractor featureExtractor;
    private static Distance distanceProvider;

    public static void setInputArgs(InputArgs inputArgs) {
        MainNews.inputArgs = inputArgs;
    }

    public static void main(String[] args) throws IOException {


        if (inputArgs.getFeatureExtractor().matches("^[0-9]grams")) {
            int n = Integer.valueOf(inputArgs.getFeatureExtractor().substring(0, 1));
            featureExtractor = new NGram(n);
        }

        if (inputArgs.getFeatureExtractor().equals("tf")) {
            featureExtractor = new TermFrequency();
        }

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

        List<Article> allArticles = loadData();
        performDataPreprocessing(allArticles);
        LOG.info("Successfully performed articles preprocessing");

        List<ClassificationSubject> classificationSubjects = allArticles.stream()
                .map(MainNews::mapArticleToClassificationSubject)
                .collect(Collectors.toList());

        int trainingSetSize = (int) Math.floor(0.60 * allArticles.size());

        List<ClassificationSubject> trainingArticles = classificationSubjects.subList(0, trainingSetSize);
        List<ClassificationSubject> testArticles = classificationSubjects.subList(trainingSetSize, allArticles.size());

        if (inputArgs.getLabels().equals("topics")) {
            topicsClassification(trainingArticles, testArticles);
        } else if (inputArgs.getLabels().equals("places")) {
            countryClassification(trainingArticles, testArticles);
        }
    }

    private static void performDataPreprocessing(List<Article> articleList) throws IOException {

        Set<String> stopwords = new HashSet<>(FileUtils.readLines(new File("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\src\\main\\resources\\stopwords.txt"), "utf-8"));
        articleList.forEach(article -> {
            Preprocessing.removeStopwords(article, stopwords);
            Preprocessing.removeSpecialCharacters(article);
        });
    }

    private static List<Article> loadData() {

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\news_data");
        List<Article> allArticles = reutersDataset.getAll();
        Collections.shuffle(allArticles, new Random(92831L));
        return allArticles;
    }

    private static ClassificationSubject mapArticleToClassificationSubject(Article article) {

        ClassificationSubject classificationSubject = new ClassificationSubject();

        if (inputArgs.getLabels().equals("topics")) {
            classificationSubject.setLabels(article.getTopics());
        } else if (inputArgs.getLabels().equals("places")) {
            classificationSubject.setLabels(article.getPlaces());
        }
        Map<String, Double> features = featureExtractor.extractFeatures(article.getBodyWords(), 0.5);
        LOG.info("Features : " + features);
        classificationSubject.setFeatures(features);

        return classificationSubject;
    }

    private static void countryClassification(List<ClassificationSubject> trainingArticles,
                                              List<ClassificationSubject> testArticles) {

        KNNClassifier knnClassifier = new KNNClassifier(inputArgs.getNeighbours(), trainingArticles,
                distanceProvider);

        AtomicInteger properlyClassifiedNorthAmerica = new AtomicInteger(0);
        AtomicInteger properlyClassifiedRussia = new AtomicInteger(0);
        AtomicInteger properlyClassifiedEurope = new AtomicInteger(0);
        AtomicInteger properlyClassifiedAsia = new AtomicInteger(0);

        long testArticlesNorthAmerica = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("north-america")).count();
        long testArticlesRussia = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("russia")).count();
        long testArticlesEurope = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("europe")).count();
        long testArticlesAsia = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("asia")).count();

        LOG.info("Performing classification task");
        testArticles.forEach(testElement -> {

            List<String> expectedLabel = testElement.getLabels();
            String returnedLabel = knnClassifier.classifyObject(testElement);

//            LOG.info("Article countries : " + testElement.getLabels());
//            LOG.info("Predicted article country : " + returnedLabel);

            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("north-america")) {
                properlyClassifiedNorthAmerica.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("russia")) {
                properlyClassifiedRussia.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("europe")) {
                properlyClassifiedEurope.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("asia")) {
                properlyClassifiedAsia.getAndIncrement();
            }
        });

        double percentNorthAmerica = (double) properlyClassifiedNorthAmerica.get() / (double) testArticlesNorthAmerica;
        double percentRussia = (double) properlyClassifiedRussia.get() / (double) testArticlesRussia;
        double percentEurope = (double) properlyClassifiedEurope.get() / (double) testArticlesEurope;
        double percentAsia = (double) properlyClassifiedAsia.get() / (double) testArticlesAsia;

        LOG.info("Properly classified (north-america): " +
                properlyClassifiedNorthAmerica.get() + "/" + testArticlesNorthAmerica + ": " + percentNorthAmerica);

        LOG.info("Properly classified (russia): " +
                properlyClassifiedRussia.get() + "/" + testArticlesRussia + ": " + percentRussia);

        LOG.info("Properly classified (europe): "
                + properlyClassifiedEurope.get() + "/" + testArticlesEurope + ": " + percentEurope);

        LOG.info("Properly classified (asia): "
                + properlyClassifiedAsia.get() + "/" + testArticlesAsia + ": " + percentAsia);

    }

    private static void topicsClassification(List<ClassificationSubject> trainingArticles,
                                             List<ClassificationSubject> testArticles) {

        KNNClassifier knnClassifier = new KNNClassifier(inputArgs.getNeighbours(), trainingArticles,
                distanceProvider);

        AtomicInteger properlyClassifiedCrime = new AtomicInteger(0);
        AtomicInteger properlyClassifiedBusiness = new AtomicInteger(0);
        AtomicInteger properlyClassifiedPolitics = new AtomicInteger(0);
        AtomicInteger properlyClassifiedSports = new AtomicInteger(0);
        AtomicInteger properlyClassifiedTechnologyScience = new AtomicInteger(0);

        long testArticlesCrime = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("crime")).count();
        long testArticlesBusiness = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("business")).count();
        long testArticlesPolitics = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("politics")).count();
        long testArticlesSports = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("sports")).count();
        long testArticlesTechnologyScience = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("technology-science")).count();

        LOG.info("crime : " + testArticlesCrime);
        LOG.info("business: " + testArticlesBusiness);
        LOG.info("politics : " + testArticlesPolitics);
        LOG.info("sports : " + testArticlesSports);
        LOG.info("technology-science : " + testArticlesTechnologyScience);

        LOG.info("Performing classification task");
        testArticles.forEach(testElement -> {

            List<String> expectedLabel = testElement.getLabels();
            String returnedLabel = knnClassifier.classifyObject(testElement);

//            LOG.info("Article topics : " + testElement.getLabels());
//            LOG.info("Predicted article topic : " + returnedLabel);


            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("crime")) {
                properlyClassifiedCrime.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("business")) {
                properlyClassifiedBusiness.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("politics")) {
                properlyClassifiedPolitics.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("sports")) {
                properlyClassifiedSports.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("technology-science")) {
                properlyClassifiedTechnologyScience.getAndIncrement();
            }
        });

        double percentCrime = (double) properlyClassifiedCrime.get() / (double) testArticlesCrime;
        double percentBusiness = (double) properlyClassifiedBusiness.get() / (double) testArticlesBusiness;
        double percentPolitics = (double) properlyClassifiedPolitics.get() / (double) testArticlesPolitics;
        double percentSports = (double) properlyClassifiedSports.get() / (double) testArticlesSports;
        double percentTechnologyScience = (double) properlyClassifiedTechnologyScience.get() / (double) testArticlesTechnologyScience;

        LOG.info("Properly classified (crime): " +
                properlyClassifiedCrime.get() + "/" + testArticlesCrime + ": " + percentCrime);

        LOG.info("Properly classified (business): " +
                properlyClassifiedBusiness.get() + "/" + testArticlesBusiness + ": " + percentBusiness);

        LOG.info("Properly classified (politics): "
                + properlyClassifiedPolitics.get() + "/" + testArticlesPolitics + ": " + percentPolitics);

        LOG.info("Properly classified (sports): "
                + properlyClassifiedSports.get() + "/" + testArticlesSports + ": " + percentSports);

        LOG.info("Properly classified (technology-science): "
                + properlyClassifiedTechnologyScience.get() + "/" + testArticlesTechnologyScience + ": " + percentTechnologyScience);

    }
}
