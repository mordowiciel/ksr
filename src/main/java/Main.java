import dataset.Article;
import dataset.Dataset;
import dataset.ReutersDataset;

public class Main {
    public static void main(String[] args) {

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters");

        for (Article article : reutersDataset) {
            System.out.println(article.getTopics());
            System.out.println(article.getTitle());
        }
    }
}
