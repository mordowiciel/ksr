package main.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ReutersDataset extends Dataset<Article> {

    private final String directory;
    private Pattern EXTRACTION_PATTERN = Pattern.compile(
            "<DATE>(.*?)</DATE>.*?<TOPICS>(.*?)</TOPICS>.*?<PLACES>(.*?)</PLACES>.*?<TITLE>(.*?)</TITLE>" +
                    ".*?<BODY>(.*?)</BODY>");

    public ReutersDataset(String reutersDir) {
        this.directory = reutersDir;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.directory != null ? this.directory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReutersDataset other = (ReutersDataset) obj;
        if ((this.directory == null) ? (other.directory != null) : !this.directory.equals(other.directory)) {
            return false;
        }
        return true;
    }

    private ArrayList<Article> parseString(String reuters_string) {
        ArrayList<Article> reuters_feed = new ArrayList<Article>();
        Matcher matcher = EXTRACTION_PATTERN.matcher(reuters_string);
        while (matcher.find()) {
            Article reuters = new Article();
            reuters.setDate(matcher.group(1));
            reuters.setTopics(parseList(matcher.group(2)));
            reuters.setPlaces(parseList(matcher.group(3)));
            reuters.setTitle(matcher.group(4).replaceAll("&lt;", "<"));

            String articleBody = matcher.group(5).replaceAll("&lt;", "<");
            List<String> bodyWords = Arrays.asList(articleBody.split(" "));
            reuters.setBodyWords(bodyWords);

            reuters_feed.add(reuters);
        }
        return reuters_feed;
    }

    private static List<String> parseList(String listString) {

        List<String> topicsList = new ArrayList<>();
        Pattern topicPattern = Pattern.compile("<D>(.*?)</D>");
        Matcher matcher = topicPattern.matcher(listString);

        while (matcher.find()) {
            String topic = StringUtils.substringBetween(matcher.group(), "<D>", "</D>");
            topicsList.add(topic);
        }
        return topicsList;
    }



    @Override
    public Iterator<Article> iterator() {
        return new ReutersIterator(directory);
    }

    private class ReutersIterator implements Iterator<Article> {

        private LinkedList<Article> available = new LinkedList<Article>();
        private LinkedList<File> files = new LinkedList<File>();
        private BufferedReader file_reader;

        public ReutersIterator(String dir_name) {
            File directory = new File(dir_name);
            files.addAll(Arrays.asList(Objects.requireNonNull(directory.listFiles(file -> file.getName().endsWith(".sgm")))));

            openNextFile();
            readNextElements();
        }

        @Override
        public boolean hasNext() {
            return !available.isEmpty();
        }

        @Override
        public Article next() {

            Article current = available.removeFirst();
            if (available.isEmpty()) {
                readNextElements();
            }

            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported!");
        }

        private void readNextElements() {

            StringBuilder buffer = new StringBuilder(1024);

            while (true) {
                try {
                    String line = file_reader.readLine();

                    // We reached the end of this file...
                    if (line == null) {
                        file_reader.close();
                        if (!openNextFile()) {
                            // No file left
                            return;
                        }

                        // Might trigger an exception if next file is empty..
                        line = file_reader.readLine();
                    }

                    // Read and append lines until we have a complete reuters news
                    if (line.indexOf("</REUTERS") == -1) {
                        buffer.append(line);
                        continue;
                    }

                    available.addAll(parseString(buffer.toString()));

                    if (!available.isEmpty()) {
                        return;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ReutersDataset.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private boolean openNextFile() {

            if (files.isEmpty()) {
                return false;
            }

            try {
                file_reader = new BufferedReader(new FileReader(files.removeFirst()));
            } catch (FileNotFoundException ex) {
                return false;
            }

            return true;
        }
    }
}
