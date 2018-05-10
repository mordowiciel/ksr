package main.vectorization;

import java.util.Map;

public class FrequencyMatrix extends Base<Map<String, Long>> {
    public FrequencyMatrix(String label, Map<String, Long> content) {
        super(label, content);
    }
}