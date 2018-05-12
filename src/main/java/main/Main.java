package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        List<String> allowedCountries = new ArrayList<>();
        allowedCountries.add("west-germany");
        allowedCountries.add("usa");
        allowedCountries.add("france");
        allowedCountries.add("uk");
        allowedCountries.add("canada");
        allowedCountries.add("japan");

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters_data");
        List<Article> testArticles = reutersDataset.getAll()
                .stream()
                .filter(testArticle -> testArticle.getPlaces().size() == 1)
                .filter(testArticle -> allowedCountries.contains(testArticle.getPlaces().get(0)))
                .collect(Collectors.toList());

        System.out.println("Successfully loaded articles to memory : " + testArticles.size());


        Set<Article> trainingSet = new HashSet<>(testArticles.subList(0, 8000));
        Set<Article> testSet = new HashSet<>(testArticles.subList(8000, 12000));
        System.out.println("Successfully splitted training set and test set");

        Set<String> stopwords = new HashSet<>(FileUtils.readLines(new File("stopwords.txt"), "utf-8"));
        generateBagOfWords(testArticles, stopwords);

        Set<String> keywords = new HashSet<>(FileUtils.readLines(new File("bag.txt"), "UTF-8"));
        System.out.println("Successfully read bag.txt into memory");

        KNNClassifier knnClassifier = new KNNClassifier(3, trainingSet, keywords, stopwords, new EuclideanMetric());
        AtomicInteger properlyClassifiedWestGermany = new AtomicInteger(0);
        AtomicInteger properlyClassifiedUSA = new AtomicInteger(0);
        AtomicInteger properlyClassifiedFrance = new AtomicInteger(0);
        AtomicInteger properlyClassifiedUK = new AtomicInteger(0);
        AtomicInteger properlyClassifiedCanada = new AtomicInteger(0);
        AtomicInteger properlyClassifiedJapan = new AtomicInteger(0);

        long testSetWestGermany = testSet.stream().filter(testElement -> testElement.getPlaces().get(0).equals("west-germany")).count();
        long testSetUSA = testSet.stream().filter(testElement -> testElement.getPlaces().get(0).equals("usa")).count();
        long testSetFrance = testSet.stream().filter(testElement -> testElement.getPlaces().get(0).equals("france")).count();
        long testSetUK = testSet.stream().filter(testElement -> testElement.getPlaces().get(0).equals("uk")).count();
        long testSetCanada = testSet.stream().filter(testElement -> testElement.getPlaces().get(0).equals("canada")).count();
        long testSetJapan = testSet.stream().filter(testElement -> testElement.getPlaces().get(0).equals("japan")).count();

        System.out.println("west-germany : " + testSetWestGermany);
        System.out.println("usa: " + testSetUSA);
        System.out.println("france : " + testSetFrance);
        System.out.println("uk : " + testSetUK);
        System.out.println("canada : " + testSetCanada);
        System.out.println("japan : " + testSetJapan);

        System.out.println("Performing classification task");
        long startTime = System.nanoTime();
        testSet.forEach(testElement -> {

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

        System.out.println("Properly classified (west-germany): " + properlyClassifiedWestGermany.get() + "/" + testSetWestGermany);
        System.out.println("Properly classified (usa): " + properlyClassifiedUSA.get() + "/" + testSetUSA);
        System.out.println("Properly classified (france): " + properlyClassifiedFrance.get() + "/" + testSetFrance);
        System.out.println("Properly classified (uk): " + properlyClassifiedUK.get() + "/" + testSetUK);
        System.out.println("Properly classified (canada): " + properlyClassifiedCanada.get() + "/" + testSetCanada);
        System.out.println("Properly classified (japan): " + properlyClassifiedJapan.get() + "/" + testSetJapan);
        System.out.println("Finished classification task for test set. : " + TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS));
    }

    // TODO : ujednolicic bag of words i pobieranie artykulow
    private static void generateBagOfWords(List<Article> testArticles, Set<String> stopwords) {

        File bagTxt = new File("bag.txt");
        Set<String> bagSet = new HashSet<>();

        if (!Files.exists(Paths.get("bag.txt"))) {

            testArticles.forEach(testArticle -> {
                try {
//                    List<String> bodyWords = TextUtils.getWordsInString(testArticle.getBody());
//                    bodyWords = TextUtils.removeSpecialCharacters(bodyWords);
//                    bodyWords = TextUtils.removeStopwords(bodyWords);

                    List<String> articleWords = TextUtils.getAllWords(testArticle, stopwords);

                    articleWords.forEach(word -> {

                        if (!bagSet.contains(word)) {
                            bagSet.add(word);
                            try {
                                FileUtils.writeStringToFile(bagTxt, word + "\n", Charset.defaultCharset(), true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
