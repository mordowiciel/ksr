package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

public class MainReuters {

    private static Logger LOG = Logger.getLogger("app");
    private static InputArgs inputArgs;
    private static FeatureExtractor featureExtractor;
    private static Distance distanceProvider;

    public static void setInputArgs(InputArgs inputArgs) {
        MainReuters.inputArgs = inputArgs;
    }

    public static void main(String[] args) throws IOException {


        if (inputArgs.getFeatureExtractor().equals("ngrams")) {
            featureExtractor = new NGram();
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

        List<Article> allArticles = new ArrayList<>();
        if (inputArgs.getLabels().equals("topics")) {
            allArticles = loadDataWithOnlyOneTopic();
            LOG.info("Successfully loaded articles to memory : " + allArticles.size());
        } else if (inputArgs.getLabels().equals("places")) {
            allArticles = loadDataWithOnlyOneCountry();
            LOG.info("Successfully loaded articles to memory : " + allArticles.size());
        }

        performDataPreprocessing(allArticles);
        LOG.info("Successfully performed articles preprocessing");

        List<ClassificationSubject> classificationSubjects = allArticles.stream()
                .map(MainReuters::mapArticleToClassificationSubject)
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

    private static List<Article> loadDataWithOnlyOneCountry() {

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters_data");
        List<String> allowedCountries = new ArrayList<>();
        allowedCountries.add("west-germany");
        allowedCountries.add("usa");
        allowedCountries.add("france");
        allowedCountries.add("uk");
        allowedCountries.add("canada");
        allowedCountries.add("japan");

        List<Article> allArticles = reutersDataset.getAll()
                .stream()
                .filter(testArticle -> testArticle.getPlaces().size() == 1)
                .filter(testArticle -> allowedCountries.contains(testArticle.getPlaces().get(0)))
                .collect(Collectors.toList());

        return allArticles;
    }

    private static List<Article> loadDataWithOnlyOneTopic() {

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters_data");
        List<String> allowedTopics = new ArrayList<>();
        allowedTopics.add("money-fx");
        allowedTopics.add("acq");
        allowedTopics.add("earn");
        allowedTopics.add("interest");

        List<Article> allArticles = reutersDataset.getAll()
                .stream()
                .filter(article -> article.getTopics().size() == 1)
                .filter(article -> allowedTopics.contains(article.getTopics().get(0)))
                .collect(Collectors.toList());

        return allArticles;
    }

    private static ClassificationSubject mapArticleToClassificationSubject(Article article) {

        ClassificationSubject classificationSubject = new ClassificationSubject();

        if (inputArgs.getLabels().equals("topics")) {
            classificationSubject.setLabels(article.getTopics());
        } else if (inputArgs.getLabels().equals("places")) {
            classificationSubject.setLabels(article.getPlaces());
        }
        Map<String, Double> features = featureExtractor.extractFeatures(article.getBodyWords());
        classificationSubject.setFeatures(features);

        return classificationSubject;
    }

    private static void countryClassification(List<ClassificationSubject> trainingArticles,
                                              List<ClassificationSubject> testArticles) {

        KNNClassifier knnClassifier = new KNNClassifier(inputArgs.getNeighbours(), trainingArticles,
                distanceProvider);

        AtomicInteger properlyClassifiedWestGermany = new AtomicInteger(0);
        AtomicInteger properlyClassifiedUSA = new AtomicInteger(0);
        AtomicInteger properlyClassifiedFrance = new AtomicInteger(0);
        AtomicInteger properlyClassifiedUK = new AtomicInteger(0);
        AtomicInteger properlyClassifiedCanada = new AtomicInteger(0);
        AtomicInteger properlyClassifiedJapan = new AtomicInteger(0);

        long testArticlesWestGermany = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("west-germany")).count();
        long testArticlesUSA = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("usa")).count();
        long testArticlesFrance = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("france")).count();
        long testArticlesUK = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("uk")).count();
        long testArticlesCanada = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("canada")).count();
        long testArticlesJapan = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("japan")).count();

        LOG.info("west-germany : " + testArticlesWestGermany);
        LOG.info("usa: " + testArticlesUSA);
        LOG.info("france : " + testArticlesFrance);
        LOG.info("uk : " + testArticlesUK);
        LOG.info("canada : " + testArticlesCanada);
        LOG.info("japan : " + testArticlesJapan);

        AtomicInteger properlyClassified = new AtomicInteger(0);

        LOG.info("Performing classification task");
        long startTime = System.nanoTime();
        testArticles.forEach(testElement -> {

            List<String> expectedLabel = testElement.getLabels();
            String returnedLabel = knnClassifier.classifyObject(testElement);

//            LOG.info("Article countries : " + testElement.getLabels());
//            LOG.info("Predicted article country : " + returnedLabel);

            if (expectedLabel.contains(returnedLabel)) {
                properlyClassified.getAndIncrement();
            }

            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("west-germany")) {
                properlyClassifiedWestGermany.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("usa")) {
                properlyClassifiedUSA.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("france")) {
                properlyClassifiedFrance.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("uk")) {
                properlyClassifiedUK.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("canada")) {
                properlyClassifiedCanada.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("japan")) {
                properlyClassifiedJapan.getAndIncrement();
            }
        });

        long stopTime = System.nanoTime();
        long totalTime = stopTime - startTime;

        double percentWestGermany = (double) properlyClassifiedWestGermany.get() / (double) testArticlesWestGermany;
        double percentUSA = (double) properlyClassifiedUSA.get() / (double) testArticlesUSA;
        double percentFrance = (double) properlyClassifiedFrance.get() / (double) testArticlesFrance;
        double percentUK = (double) properlyClassifiedUK.get() / (double) testArticlesUK;
        double percentCanada = (double) properlyClassifiedCanada.get() / (double) testArticlesCanada;
        double percentJapan = (double) properlyClassifiedJapan.get() / (double) testArticlesJapan;

        LOG.info("Properly classified (west-germany): " +
                properlyClassifiedWestGermany.get() + "/" + testArticlesWestGermany + ": " + percentWestGermany);

        LOG.info("Properly classified (usa): " +
                properlyClassifiedUSA.get() + "/" + testArticlesUSA + ": " + percentUSA);

        LOG.info("Properly classified (france): "
                + properlyClassifiedFrance.get() + "/" + testArticlesFrance + ": " + percentFrance);

        LOG.info("Properly classified (uk): "
                + properlyClassifiedUK.get() + "/" + testArticlesUK + ": " + percentUK);

        LOG.info("Properly classified (canada): " +
                properlyClassifiedCanada.get() + "/" + testArticlesCanada + ": " + percentCanada);

        LOG.info("Properly classified (japan): " +
                properlyClassifiedJapan.get() + "/" + testArticlesJapan + ": " + percentJapan);

    }

    private static void topicsClassification(List<ClassificationSubject> trainingArticles,
                                             List<ClassificationSubject> testArticles) {

        KNNClassifier knnClassifier = new KNNClassifier(inputArgs.getNeighbours(), trainingArticles,
                distanceProvider);

        AtomicInteger properlyClassifiedMoneyFX = new AtomicInteger(0);
        AtomicInteger properlyClassifiedAcq = new AtomicInteger(0);
        AtomicInteger properlyClassifiedEarn = new AtomicInteger(0);
        AtomicInteger properlyClassifiedInterest = new AtomicInteger(0);

        long testArticlesMoneyFX = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("money-fx")).count();
        long testArticlesAcq = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("acq")).count();
        long testArticlesEarn = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("earn")).count();
        long testArticlesInterest = testArticles.stream().filter(testElement -> testElement.getLabels().get(0).equals("interest")).count();

        LOG.info("money-fx : " + testArticlesMoneyFX);
        LOG.info("acq: " + testArticlesAcq);
        LOG.info("earn : " + testArticlesEarn);
        LOG.info("interest : " + testArticlesInterest);

        AtomicInteger properlyClassified = new AtomicInteger(0);

        LOG.info("Performing classification task");
        testArticles.forEach(testElement -> {

            List<String> expectedLabel = testElement.getLabels();
            String returnedLabel = knnClassifier.classifyObject(testElement);

//            LOG.info("Article topics : " + testElement.getLabels());
//            LOG.info("Predicted article topic : " + returnedLabel);

            if (expectedLabel.contains(returnedLabel)) {
                properlyClassified.getAndIncrement();
            }

            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("money-fx")) {
                properlyClassifiedMoneyFX.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("acq")) {
                properlyClassifiedAcq.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("earn")) {
                properlyClassifiedEarn.getAndIncrement();
            }
            if (expectedLabel.contains(returnedLabel) && expectedLabel.contains("interest")) {
                properlyClassifiedInterest.getAndIncrement();
            }
        });

        double percentMoneyFX = (double) properlyClassifiedMoneyFX.get() / (double) testArticlesMoneyFX;
        double percentAcq = (double) properlyClassifiedAcq.get() / (double) testArticlesAcq;
        double percentEarn = (double) properlyClassifiedEarn.get() / (double) testArticlesEarn;
        double percentInterest = (double) properlyClassifiedInterest.get() / (double) testArticlesInterest;

        LOG.info("Properly classified (money-fx): " +
                properlyClassifiedMoneyFX.get() + "/" + testArticlesMoneyFX + ": " + percentMoneyFX);

        LOG.info("Properly classified (acq): " +
                properlyClassifiedAcq.get() + "/" + testArticlesAcq + ": " + percentAcq);

        LOG.info("Properly classified (earn): "
                + properlyClassifiedEarn.get() + "/" + testArticlesEarn + ": " + percentEarn);

        LOG.info("Properly classified (interest): "
                + properlyClassifiedInterest.get() + "/" + testArticlesInterest + ": " + percentInterest);

        LOG.info("Properly clasified : " + properlyClassified + "/" + testArticles.size());
    }
}
