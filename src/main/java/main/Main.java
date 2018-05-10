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

        List<String> keywords = FileUtils.readLines(new File("bag.txt"), "UTF-8");
        System.out.println("Successfully read bag.txt into memory");


        List<Article> trainingSet = testArticles.subList(0, 8000);
        List<Article> testSet = testArticles.subList(8000, 12000);
        System.out.println("Successfully splitted training set and test set");

        generateBagOfWords(testArticles);

        KNNClassifier knnClassifier = new KNNClassifier(3, trainingSet, keywords, new EuclideanMetric());
        AtomicInteger properlyClassified = new AtomicInteger(0);

        System.out.println("Performing classification task");
        long startTime = System.nanoTime();
        testSet.forEach(testElement -> {

            String expectedLabel = testElement.getPlaces().get(0);
            String returnedLabel = knnClassifier.classifyObject(testElement);

            System.out.println("Article title : " + testElement.getTitle());
            System.out.println("Article country : " + testElement.getPlaces());
            System.out.println("Predicted article country : " + returnedLabel);

            if (returnedLabel.equals(expectedLabel)) {
                properlyClassified.getAndIncrement();
            }
        });

        long stopTime = System.nanoTime();
        long totalTime = stopTime - startTime;

        System.out.println("Properly classified : " + properlyClassified.get() + "/" + testArticles.size());
        System.out.println("Finished classification task for test set. : " + TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS));
    }

    private static void generateBagOfWords(List<Article> testArticles) {

        File bagTxt = new File("bag.txt");
        Set<String> bagSet = new HashSet<>();

        if (!Files.exists(Paths.get("bag.txt"))) {

            testArticles.forEach(testArticle -> {
                try {
                    List<String> bodyWords = TextUtils.getWordsInString(testArticle.getBody());
                    bodyWords = TextUtils.removeSpecialCharacters(bodyWords);
                    bodyWords = TextUtils.removeStopwords(bodyWords);

                    bodyWords.forEach(word -> {

                        if (!bagSet.contains(word)) {
                            bagSet.add(word);
                            try {
                                FileUtils.writeStringToFile(bagTxt, word + "\n", Charset.defaultCharset(), true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
