package dataset;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {

    private String title = "";
    private String date = "";
    private List<String> bodyWords;
    private List<String> topics;
    private List<String> places;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getBodyWords() {
        return bodyWords;
    }

    public void setBodyWords(List<String> bodyWords) {
        this.bodyWords = bodyWords;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }
}
