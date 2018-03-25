package stopwords;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

public class StopwordsRemoval {
    private StandardAnalyzer analyzer;
    private StringBuilder stringBuilder;
    private CharArraySet stopWordsSet;
    private static Version LUCENE_VERSION = Version.LUCENE_36;

    public StopwordsRemoval() {
        this.analyzer = new StandardAnalyzer(LUCENE_VERSION);
        this.stringBuilder = new StringBuilder();
        this.stopWordsSet = CharArraySet.copy(LUCENE_VERSION, StandardAnalyzer.STOP_WORDS_SET);
    }

    public void addStopWord(String word) {
        stopWordsSet.add(word);
    }

    public void addStopWord(String[] words) {
        stopWordsSet.addAll(Arrays.asList(words));
    }

    public String removeStopWords(String text) throws IOException {
        TokenStream tokenStream = new StandardTokenizer(LUCENE_VERSION, new StringReader(text));
        tokenStream = new StopFilter(LUCENE_VERSION, tokenStream, this.stopWordsSet);
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()){
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(token.toString());
        }
        return stringBuilder.toString();
    }

}