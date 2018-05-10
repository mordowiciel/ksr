package main.vectorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class TermFrequency {

    public List<Double> extractVector(String body, List<String> keywords) {

        Map<String, Double> vectorMap = new HashMap<>();
        List<String> bodyWords = Arrays.asList(body.split(StringUtils.SPACE));

        for (String keyword : keywords) {

            Double keywordCount = 0.0;
            if (!bodyWords.contains(keyword)) {
                vectorMap.put(keyword, keywordCount);
                continue;
            }

            keywordCount = (double) Collections.frequency(bodyWords, keyword);
            vectorMap.put(keyword, keywordCount);

//            for (String bodyWord : bodyWords) {
//                if (keyword.equals(bodyWord)) {
//                    keywordCount += 1.0;
//                }
//            }

            vectorMap.put(keyword, keywordCount);
        }

        List<Double> values = new ArrayList<>(vectorMap.values());
        return values;
    }


    public double termFrequency(String articleBody, String term) {

        double result = 0;

        List<String> articleWords = Arrays.asList(articleBody.split(" "));

        for (String word : articleWords) {
            if (term.equalsIgnoreCase(word))
                result++;
        }

        return result / articleWords.size();
    }

}
