package main;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import main.dataset.Article;

public class Preprocessing {

    public static void removeStopwords(Article article, Set<String> stopwords) {

        List<String> processedWords = article.getBodyWords().stream()
                .filter(word -> !stopwords.contains(word))
                .collect(Collectors.toList());

        article.setBodyWords(processedWords);
    }

    public static void removeSpecialCharacters(Article article) {

        List<String> processedWords = article.getBodyWords()
                .stream()
                .map(Preprocessing::removeSpecialCharactersFromString)
                .collect(Collectors.toList());

        article.setBodyWords(processedWords);
    }

    private static String removeSpecialCharactersFromString(String string) {
        return string.replaceAll("\\W+", "");
    }
}
