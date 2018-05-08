package vectorization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class TermFrequency {

    public List<Double> extractVector(String body, List<String> keywords) {

        Map<String, Double> vectorMap = new HashMap<>();
        List<String> bodyWords = Arrays.asList(body.split(StringUtils.SPACE));

        for (String keyword : keywords) {

            Double keywordCount = 0.0;

            for (String bodyWord : bodyWords) {
                if (keyword.equals(bodyWord)) {
                    keywordCount += 1.0;
                }
            }

            vectorMap.put(keyword, keywordCount);
        }

        return vectorMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .filter(value -> value > 0.0)
                .collect(Collectors.toList());
    }

}
