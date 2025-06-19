package com.browserstack.assignment;

import java.util.*;

public class Analyzer {

    public static void analyzeRepeatedWordsToBuilder(List<String> translatedTitles, StringBuilder output) {
        Map<String, Integer> wordCount = new HashMap<>();
        for (String title : translatedTitles) {
            String cleaned = title.replaceAll("[^a-zA-Z ]", "").toLowerCase();
            String[] words = cleaned.split("\\s+");
            for (String word : words) {
                if (word.isBlank()) continue;
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }
        output.append("🔁 Words repeated more than once across titles:\n");
        boolean found = false;
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > 1) {
                output.append("🔹 ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" times\n");
                found = true;
            }
        }
        output.append("\n✅ Finished analysis.\n");
        output.append("---------------------------------------------------------\n\n");
        if (!found) {
            output.append("✅ No words repeated more than once.\n");
        }
    }
}