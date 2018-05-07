import static org.apache.commons.io.FileUtils.readLines;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {

    public static String removeStopwords(String body) throws IOException {

        List<String> stopwordsList = readLines(new File("stopwords.txt"), "utf-8");
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.stream(body.split(StringUtils.SPACE))
                .filter(word -> !stopwordsList.contains(word))
                .collect(Collectors.toList())
                .forEach(word -> stringBuilder
                        .append(word)
                        .append(StringUtils.SPACE));

        return stringBuilder.toString();
    }
}
