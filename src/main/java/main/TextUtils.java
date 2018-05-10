package main;

import static org.apache.commons.io.FileUtils.readLines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import main.dataset.Article;

public class TextUtils {

    public static String removeStopwords(String body) throws IOException {

        List<String> stopwordsList = readLines(new File("stopwords.txt"), "utf-8");
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.stream(body.split(StringUtils.SPACE))
                .filter(word -> !stopwordsList.contains(word))
                .map(String::toLowerCase)
                .collect(Collectors.toList())
                .forEach(word -> stringBuilder
                        .append(word)
                        .append(StringUtils.SPACE));

        return stringBuilder.toString();
    }

    public static List<String> removeStopwords(List<String> words) throws IOException {
        List<String> stopwordsList = readLines(new File("stopwords.txt"), "utf-8");
        return words.stream()
                .filter(word -> !stopwordsList.contains(word) && word.length() > 3)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public static String removeSpecialCharacters(String word) {
        Pattern pattern = Pattern.compile("[^a-z A-Z-]");
        Matcher matcher = pattern.matcher(word);
        return matcher.replaceAll("");
    }

    public static List<String> removeSpecialCharacters(List<String> words) {

        List<String> wordsWithoutSpecialCharacters = new ArrayList<>();
        words.forEach(word -> {
            wordsWithoutSpecialCharacters.add(removeSpecialCharacters(word));
        });
        return wordsWithoutSpecialCharacters;
    }

    public static List<String> getWordsInString(String body) {
        List<String> rawWords = Arrays.asList(body.split(StringUtils.SPACE));
        List<String> splittedWords = splitWordsByMyslnik(rawWords);
        return splittedWords;
    }

    public static List<String> splitWordsByMyslnik(List<String> words) {

        Set<String> splittedWords = new HashSet<>();

        words.forEach(word -> {
            String[] newWords = word.split("-");
            splittedWords.addAll(Arrays.asList(newWords));
        });

        return new ArrayList<>(splittedWords);
    }

    public static Set<String> getUniqueWords(Article article) {
        return new HashSet<>(getAllWords(article));
    }

    public static Map<String, Long> getAllWordsCounts(Article article) {
        return getAllWords(article)
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
    }

    public static List<String> getAllWords(Article entity) {
        return Arrays
                .asList(entity.getBody().toLowerCase().split("\\W+"));
    }
}
