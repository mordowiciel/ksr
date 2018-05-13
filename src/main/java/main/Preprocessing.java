package main;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import main.dataset.Article;

public class Preprocessing {

//    public static void mergeTitleAndArticleBody(Article article) {
//
//        List<String> titleWords = new ArrayList<>(Arrays.asList(article.getTitle().split(" ")));
//        titleWords.addAll(article.getBodyWords());
//
//        article.setBodyWords(titleWords);
//    }

    public static void removeStopwords(Article article, Set<String> stopwords) {

        List<String> processedWords = article.getBodyWords().stream()
                .filter(word -> !stopwords.contains(word))
                .collect(Collectors.toList());

        article.setBodyWords(processedWords);
    }

    public static void removeSpecialCharacters(Article article) {

        List<String> processedWords = article.getBodyWords()
                .stream()
                .map(String::toLowerCase)
                .map(Preprocessing::splitStringBySlash)
                .flatMap(Arrays::stream)
                .map(Preprocessing::removeNumbersFromString)
                .map(Preprocessing::removeSpecialCharactersFromString)
                .filter(word -> word.length() > 2)
                .collect(Collectors.toList());

        article.setBodyWords(processedWords);
    }

    public static String removeSpecialCharactersFromString(String string) {
        Pattern pattern = Pattern.compile("\\W+");
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll("");
    }

    public static String removeNumbersFromString(String string) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll("");
    }

    public static String[] splitStringBySlash(String string) {
        return string.split("/");
    }
}
