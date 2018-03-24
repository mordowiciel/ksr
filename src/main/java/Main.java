import datasets.reuters.*;

public class Main {
    public static void main(String[] args) {

        Dataset reuters_dataset = new Dataset("C:\\Users\\marcinis\\Politechnika\\KSR\\ksr\\reuters");

        for (News news : reuters_dataset) {
            System.out.println(news.topics);
            System.out.println(news.title);
        }
    }
}
