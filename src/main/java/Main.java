import java.io.IOException;

import dataset.Article;
import dataset.Dataset;
import dataset.ReutersDataset;

public class Main {

    public static void main(String[] args) {

        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters_data");

//        for (Article article : reutersDataset) {
//            System.out.println(article.getTopics());
//            System.out.println(article.getTitle());
//        }

        try {

            String body = reutersDataset.get(0).get(0).getBody();

            System.out.println("\n ARTICLE WITH STOPWORDS \n");
            System.out.println(body);

            System.out.println("\n ARTICLE WITHOUT STOPWORDS \n");
            String articleWithoutStopwords = TextUtils.removeStopwords(body);
            System.out.println(articleWithoutStopwords);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
