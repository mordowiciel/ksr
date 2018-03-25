import dataset.Article;
import dataset.Dataset;
import dataset.ReutersDataset;
import stopwords.StopwordsRemoval;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        /*
        Dataset<Article> reutersDataset = new ReutersDataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters");

        for (Article article : reutersDataset) {
            System.out.println(article.getTopics());
            System.out.println(article.getTitle());
        }
        */

        StopwordsRemoval stopword = new StopwordsRemoval();
        String[] myStringArray = {"world","flat"};
        stopword.addStopWord(myStringArray);
        String result = stopword.removeStopWords("the world is flat and beautiful");
        System.out.println(result);


    }
}
