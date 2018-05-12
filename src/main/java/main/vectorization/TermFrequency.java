package main.vectorization;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import main.TextUtils;
import main.dataset.Article;

public class TermFrequency {


    public static double getTermFrequency(Article article, String term, Set<String> keywords, Set<String> stopwords) {

        List<String> articleWords = TextUtils.getAllWords(article, stopwords, keywords);
        double termCount = 0;
        for (String word : articleWords) {
            if (term.equalsIgnoreCase(word)) {
                termCount++;
            }
        }
        return termCount / articleWords.size();
    }

    public static double getInverseTermFrequency(Set<Article> articles, String term, Set<String> keywords, Set<String> stopwords) {
        double termCount = 0;
        for (Article article : articles) {

            Set<String> articleWords = new HashSet<>(TextUtils.getAllWords(article, stopwords, keywords));
            if (articleWords.contains(term)) {
                termCount++;
            }
        }
        return Math.log(articles.size() / termCount);
    }

    public static double getTFIDF(Set<Article> articleList, Article article, String term, Set<String> keywords, Set<String> stopwords) {
        return getTermFrequency(article, term, keywords, stopwords) * getInverseTermFrequency(articleList, term, keywords, stopwords);
    }

    //
//
    public static Map<String, Double> getTDMap(Set<Article> articleList, Article article, Set<String> keywords, Set<String> stopwords) {
        Map<String, Double> wordCount = TextUtils.getAllWordsCounts(article, stopwords, keywords);
        return wordCount.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
//                   return (double) e.getValue() / TextUtils.getAllWords(article).size();
                    return getTFIDF(articleList, article, e.getKey(), keywords, stopwords);
                }));
    }
}
