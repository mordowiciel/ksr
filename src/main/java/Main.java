import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import dataset.Article;
import dataset.Dataset;
import dataset.ReutersDataset;
import similarity.SimilarityUtils;
import vectorization.TermFrequency;

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
                .collect(Collectors.toList())
                .subList(0, 8000);

        generateBagOfWords(testArticles);

        TermFrequency termFrequency = new TermFrequency();
        List<String> keywords = FileUtils.readLines(new File("bag.txt"), "UTF-8");

        List<Double> firstTermFrequencyVector = termFrequency.extractVector(testArticles.get(0).getBody(), keywords);
        System.out.println(firstTermFrequencyVector);

        testArticles.forEach(testArticle -> {
            List<Double> testArticleFrequencyVector = termFrequency.extractVector(testArticle.getBody(), keywords);
            double similarity = SimilarityUtils.calculateCosineAmplitude(firstTermFrequencyVector, testArticleFrequencyVector);
            System.out.println("Article title : " + testArticle.getTitle());
            System.out.println("Article country : " + testArticle.getPlaces());
            System.out.println(similarity);
        });

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
