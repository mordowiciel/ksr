package frequency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TermFrequency {

    public List<Integer> initTermFrequencyVector(String articleBody) {

        List<String> wordContainer = new ArrayList<>();

        wordContainer.add("oil");
        wordContainer.add("money");
        wordContainer.add("investment");
        wordContainer.add("company");
        wordContainer.add("management");
        wordContainer.add("dlrs");
        wordContainer.add("inc");


        Map<String, Integer> wordCountMap = new HashMap<>();

        for (String word: wordContainer) {
            wordCountMap.put(word, 0);
        }

        for(String word : articleBody.split(" ")) {

            if(wordContainer.contains(word)) {

                Integer wordCount = wordCountMap.get(word);
                if(wordCount == null) {
                    wordCountMap.put(word, 1);
                }
                else {
                    wordCountMap.put(word, ++wordCount);
                }
            }
        }

        List<Integer> wordCountVector = wordCountMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        return wordCountVector;
    }
}
