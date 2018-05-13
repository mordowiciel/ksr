package main;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import main.dataset.Article;
import main.dataset.Dataset;
import main.dataset.ReutersDataset;

public class Main {
    public static void main(String[] args) throws IOException {

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters_data");
        List<Article> articleList = reutersDataset.get(10);

        Set<String> stopwords = new HashSet<>(FileUtils.readLines(new File("stopwords.txt"), "utf-8"));


        Article before = articleList.get(0);
        System.out.println(before.getBodyWords().size());
        System.out.println(before.getBodyWords());

        Preprocessing.removeStopwords(before, stopwords);
        Preprocessing.removeSpecialCharacters(before);
        System.out.println(before.getBodyWords().size());
        System.out.println(before.getBodyWords());

    }
}
