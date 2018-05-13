package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import main.dataset.Article;
import main.dataset.Dataset;
import main.dataset.ReutersDataset;
import main.knn.KNNClassifier;
import main.metrics.EuclideanMetric;

public class Main {

    public static void main(String[] args) throws IOException {


        List<Article> allArticles = loadDataWithOnlyOneCountry();
        System.out.println("Successfully loaded articles to memory : " + allArticles.size());


        performDataPreprocessing(allArticles);
        System.out.println("Successfully performed articles preprocessing");

        List<Article> trainingArticles = allArticles.subList(0, 8000);
        List<Article> testArticles = allArticles.subList(8000, 12000);

        KNNClassifier knnClassifier = new KNNClassifier(5, trainingArticles, new EuclideanMetric());
        AtomicInteger properlyClassifiedWestGermany = new AtomicInteger(0);
        AtomicInteger properlyClassifiedUSA = new AtomicInteger(0);
        AtomicInteger properlyClassifiedFrance = new AtomicInteger(0);
        AtomicInteger properlyClassifiedUK = new AtomicInteger(0);
        AtomicInteger properlyClassifiedCanada = new AtomicInteger(0);
        AtomicInteger properlyClassifiedJapan = new AtomicInteger(0);

        long testArticlesWestGermany = testArticles.stream().filter(testElement -> testElement.getPlaces().get(0).equals("west-germany")).count();
        long testArticlesUSA = testArticles.stream().filter(testElement -> testElement.getPlaces().get(0).equals("usa")).count();
        long testArticlesFrance = testArticles.stream().filter(testElement -> testElement.getPlaces().get(0).equals("france")).count();
        long testArticlesUK = testArticles.stream().filter(testElement -> testElement.getPlaces().get(0).equals("uk")).count();
        long testArticlesCanada = testArticles.stream().filter(testElement -> testElement.getPlaces().get(0).equals("canada")).count();
        long testArticlesJapan = testArticles.stream().filter(testElement -> testElement.getPlaces().get(0).equals("japan")).count();

        System.out.println("west-germany : " + testArticlesWestGermany);
        System.out.println("usa: " + testArticlesUSA);
        System.out.println("france : " + testArticlesFrance);
        System.out.println("uk : " + testArticlesUK);
        System.out.println("canada : " + testArticlesCanada);
        System.out.println("japan : " + testArticlesJapan);

        System.out.println("Performing classification task");
        long startTime = System.nanoTime();
        testArticles.forEach(testElement -> {

            String expectedLabel = testElement.getPlaces().get(0);
            String returnedLabel = knnClassifier.classifyObject(testElement);

            System.out.println("Article title : " + testElement.getTitle());
            System.out.println("Article country : " + testElement.getPlaces());
            System.out.println("Predicted article country : " + returnedLabel);

            if (returnedLabel.equals(expectedLabel) && expectedLabel.equals("west-germany")) {
                properlyClassifiedWestGermany.getAndIncrement();
            }
            if (returnedLabel.equals(expectedLabel) && expectedLabel.equals("usa")) {
                properlyClassifiedUSA.getAndIncrement();
            }
            if (returnedLabel.equals(expectedLabel) && expectedLabel.equals("france")) {
                properlyClassifiedFrance.getAndIncrement();
            }
            if (returnedLabel.equals(expectedLabel) && expectedLabel.equals("uk")) {
                properlyClassifiedUK.getAndIncrement();
            }
            if (returnedLabel.equals(expectedLabel) && expectedLabel.equals("canada")) {
                properlyClassifiedCanada.getAndIncrement();
            }
            if (returnedLabel.equals(expectedLabel) && expectedLabel.equals("japan")) {
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

        System.out.println("Properly classified (west-germany): " +
                properlyClassifiedWestGermany.get() + "/" + testArticlesWestGermany + ": " + percentWestGermany);

        System.out.println("Properly classified (usa): " +
                properlyClassifiedUSA.get() + "/" + testArticlesUSA + ": " + percentUSA);

        System.out.println("Properly classified (france): "
                + properlyClassifiedFrance.get() + "/" + testArticlesFrance + ": " + percentFrance);

        System.out.println("Properly classified (uk): "
                + properlyClassifiedUK.get() + "/" + testArticlesUK + ": " + percentUK);

        System.out.println("Properly classified (canada): " +
                properlyClassifiedCanada.get() + "/" + testArticlesCanada + ": " + percentCanada);

        System.out.println("Properly classified (japan): " +
                properlyClassifiedJapan.get() + "/" + testArticlesJapan + ": " + percentJapan);

        System.out.println("Finished classification task for test set. : " + TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS));
    }

    private static void performDataPreprocessing(List<Article> articleList) throws IOException {

        Set<String> stopwords = new HashSet<>(FileUtils.readLines(new File("stopwords.txt"), "utf-8"));
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
}
