import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dataset.Article;
import dataset.Dataset;
import dataset.ReutersDataset;
import frequency.TermFrequency;
import knn.ClassificationSubject;
import knn.KNNClassifier;
import metrics.EuclideanMetric;

public class Main {
    public static void main(String[] args) {

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters_data");


        List<Article> articles = reutersDataset
                .get(1000);

        List<Article> oneLabelArticles = articles.stream()
                .filter(article -> article.getPlaces().size() == 1)
                .collect(Collectors.toList());

        Article first = oneLabelArticles.get(0);        // USA -> oil, money, investment, company, management
        Article second = oneLabelArticles.get(635);     // China
        Article third = oneLabelArticles.get(707);

        System.out.println(third.getPlaces());
        System.out.println(third.getBody());

        TermFrequency termFrequency = new TermFrequency();

        List<Double> termFrequencyVectorFirst = termFrequency.initTermFrequencyVector(first.getBody())
                .stream()
                .map(Double::valueOf)
                .collect(Collectors.toList());
        List<Double> termFrequencyVectorSecond = termFrequency.initTermFrequencyVector(second.getBody())
                .stream()
                .map(Double::valueOf)
                .collect(Collectors.toList());

        List<Double> termFrequencyVectorThird = termFrequency.initTermFrequencyVector(third.getBody())
                .stream()
                .map(Double::valueOf)
                .collect(Collectors.toList());


        System.out.println("First: " + termFrequencyVectorFirst);
        System.out.println("Second: " + termFrequencyVectorSecond);
        System.out.println("Third: " + termFrequencyVectorThird);

        ClassificationSubject firstClassification = new ClassificationSubject(first.getPlaces().get(0), termFrequencyVectorFirst);
        ClassificationSubject secondClassification = new ClassificationSubject(second.getPlaces().get(0), termFrequencyVectorSecond);
        ClassificationSubject thirdClassification = new ClassificationSubject(third.getPlaces().get(0), termFrequencyVectorSecond);

        List<ClassificationSubject> trainingSet = new ArrayList<>();
        trainingSet.add(firstClassification);
        trainingSet.add(secondClassification);

        KNNClassifier knn = new KNNClassifier(1, trainingSet, new EuclideanMetric());
        String label = knn.classifyObject(thirdClassification);

        System.out.println(label);
    }
}
