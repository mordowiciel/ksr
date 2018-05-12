import java.util.List;

import dataset.Article;
import dataset.Dataset;
import dataset.ReutersDataset;

public class Main {
    public static void main(String[] args) {

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters_data");
        List<Article> articleList = reutersDataset.get(10);

        TermFrequency termFrequency = new TermFrequency();
        System.out.println(termFrequency.calculateTermFrequency(articleList.get(0)));
    }
}
