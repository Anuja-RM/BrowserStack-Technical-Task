import java.util.*;

public class TextAnalyzer {

    public void analyze(List<String> headers) {

        Map<String, Integer> wordCount = new HashMap<>();

        for (String header : headers) {
            String[] words = header.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+");

            for (String word : words) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > 2) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }
}