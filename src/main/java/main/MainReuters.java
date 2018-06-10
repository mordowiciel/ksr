package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

        long startTime = System.currentTimeMillis();

        if (inputArgs.getLabels().equals("topics")) {
            topicsClassification(trainingArticles, testArticles);
        } else if (inputArgs.getLabels().equals("places")) {
            countryClassification(trainingArticles, testArticles);
        }

        long stopTime = System.currentTimeMillis();
        long elapsed = stopTime - startTime;
        LOG.info("Time : " + TimeUnit.SECONDS.convert(elapsed, TimeUnit.MILLISECONDS));
    }

    private static void performDataPreprocessing(List<Article> articleList) throws IOException {

//        Set<String> stopwords = new HashSet<>(FileUtils.readLines(new File("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\src\\main\\resources\\stopwords.txt"), "utf-8"));
        File stopwordsFile = new File(MainReuters.class
                .getClassLoader()
                .getResource("stopwords.txt")
                .getFile());

        Set<String> stopwords = new HashSet<>(FileUtils.readLines(stopwordsFile, "utf-8"));
        articleList.forEach(article -> {
            Preprocessing.removeStopwords(article, stopwords);
            Preprocessing.removeSpecialCharacters(article);
        });
    }

    private static List<Article> loadDataWithOnlyOneCountry() {

        Dataset<Article> reutersDataset = new ReutersDataset(inputArgs.getDatasetPath());
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

        Dataset<Article> reutersDataset = new ReutersDataset(inputArgs.getDatasetPath());
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
        Map<String, Double> features = featureExtractor.extractFeatures(article.getBodyWords(), inputArgs.getVectorSize());
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

        double percentWestGermany = ((double) properlyClassifiedWestGermany.get() / (double) testArticlesWestGermany) * 100.0;
        double percentUSA = ((double) properlyClassifiedUSA.get() / (double) testArticlesUSA) * 100.0;
        double percentFrance = ((double) properlyClassifiedFrance.get() / (double) testArticlesFrance) * 100.0;
        double percentUK = ((double) properlyClassifiedUK.get() / (double) testArticlesUK) * 100.0;
        double percentCanada = ((double) properlyClassifiedCanada.get() / (double) testArticlesCanada) * 100.0;
        double percentJapan = ((double) properlyClassifiedJapan.get() / (double) testArticlesJapan) * 100.0;
        double totalPercent = ((double) properlyClassified.get() / (double) testArticles.size()) * 100.0;

        long arithmeticAverage = properlyClassified.get() / 6;

        long weights = properlyClassifiedWestGermany.get() * testArticlesWestGermany
                + properlyClassifiedUSA.get() * testArticlesUSA
                + properlyClassifiedFrance.get() * testArticlesFrance
                + properlyClassifiedUK.get() * testArticlesUK
                + properlyClassifiedCanada.get() * testArticlesCanada
                + properlyClassifiedJapan.get() * testArticlesJapan;

        long sum = testArticlesWestGermany + testArticlesUSA + testArticlesFrance + testArticlesUK
                + testArticlesCanada + testArticlesJapan;

        long weightedAverage = weights / sum;


        LOG.info("west-germany " +
                properlyClassifiedWestGermany.get() + " " + testArticlesWestGermany + " " + String.format("%.2f", percentWestGermany));

        LOG.info("usa " +
                properlyClassifiedUSA.get() + " " + testArticlesUSA + " " + String.format("%.2f", percentUSA));

        LOG.info("france "
                + properlyClassifiedFrance.get() + " " + testArticlesFrance + " " + String.format("%.2f", percentFrance));

        LOG.info("uk "
                + properlyClassifiedUK.get() + " " + testArticlesUK + " " + String.format("%.2f", percentUK));

        LOG.info("canada " +
                properlyClassifiedCanada.get() + " " + testArticlesCanada + " " + String.format("%.2f", percentCanada));

        LOG.info("japan " +
                properlyClassifiedJapan.get() + " " + testArticlesJapan + " " + String.format("%.2f", percentJapan));

        LOG.info("total percent " +
                properlyClassified.get() + " " + testArticles.size() + " " + String.format("%.2f", totalPercent));

        LOG.info("arithmetic average " +
                arithmeticAverage + " " + testArticles.size() + " " +
                String.format("%.2f", (double) arithmeticAverage / testArticles.size()));

        LOG.info("weighted average " + weightedAverage + " " + testArticles.size() + " " +
                String.format("%.2f", (double) weightedAverage / testArticles.size()));

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

        double percentMoneyFX = ((double) properlyClassifiedMoneyFX.get() / (double) testArticlesMoneyFX) * 100.0;
        double percentAcq = ((double) properlyClassifiedAcq.get() / (double) testArticlesAcq) * 100.0;
        double percentEarn = ((double) properlyClassifiedEarn.get() / (double) testArticlesEarn) * 100.0;
        double percentInterest = ((double) properlyClassifiedInterest.get() / (double) testArticlesInterest) * 100.0;
        double totalPercent = ((double) properlyClassified.get() / (double) testArticles.size()) * 100.0;

        long arithmeticAverage = properlyClassified.get() / 6;

        long weights = properlyClassifiedMoneyFX.get() * testArticlesMoneyFX
                + properlyClassifiedAcq.get() * testArticlesAcq
                + properlyClassifiedEarn.get() * testArticlesEarn
                + properlyClassifiedInterest.get() * testArticlesInterest;

        long sum = testArticlesAcq + testArticlesEarn + testArticlesInterest + testArticlesMoneyFX;
        long weightedAverage = weights / sum;

        LOG.info("money-fx " +
                properlyClassifiedMoneyFX.get() + " " + testArticlesMoneyFX + " " + percentMoneyFX);

        LOG.info("acq " +
                properlyClassifiedAcq.get() + " " + testArticlesAcq + " " + percentAcq);

        LOG.info("earn "
                + properlyClassifiedEarn.get() + " " + testArticlesEarn + " " + percentEarn);

        LOG.info("interest "
                + properlyClassifiedInterest.get() + " " + testArticlesInterest + " " + percentInterest);

        LOG.info("total percent " +
                properlyClassified.get() + " " + testArticles.size() + " " + String.format("%.2f", totalPercent));

        LOG.info("arithmetic average " +
                arithmeticAverage + " " + testArticles.size() + " " +
                String.format("%.2f", (double) arithmeticAverage / testArticles.size()));

        LOG.info("weighted average " + weightedAverage + " " + testArticles.size() + " " +
                String.format("%.2f", (double) weightedAverage / testArticles.size()));
    }
}
