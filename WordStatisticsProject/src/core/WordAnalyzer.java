/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Analyzes text content and generates statistics
 * This is the core analysis engine of the application
 */
public class WordAnalyzer {
    
    // Pattern to split text into words (handles punctuation, numbers, etc.)
    private static final Pattern WORD_PATTERN = Pattern.compile("[^a-zA-Z]+");
    
    // Words we're specifically counting (case-insensitive)
    private static final String TARGET_WORD_IS = "is";
    private static final String TARGET_WORD_ARE = "are";
    private static final String TARGET_WORD_YOU = "you";
    
    /**
     * Analyzes text and returns statistics
     * 
     * @param text The text content to analyze
     * @return A map containing all statistics
     */
    public Map<String, Object> analyzeText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return createEmptyResults();
        }
        
        String[] words = splitIntoWords(text);
        
        if (words.length == 0) {
            return createEmptyResults();
        }
        
        int totalWords = words.length;
        int isCount = 0;
        int areCount = 0;
        int youCount = 0;
        String longestWord = "";
        String shortestWord = "";
        
        // Process each word
        for (String word : words) {
            String lowerWord = word.toLowerCase();
            
            // Count target words
            if (lowerWord.equals(TARGET_WORD_IS)) {
                isCount++;
            } else if (lowerWord.equals(TARGET_WORD_ARE)) {
                areCount++;
            } else if (lowerWord.equals(TARGET_WORD_YOU)) {
                youCount++;
            }
            
            // Find longest word
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
            
            // Find shortest word (skip if we haven't set it yet)
            if (shortestWord.isEmpty() || 
                (word.length() < shortestWord.length() && word.length() > 0)) {
                shortestWord = word;
            }
        }
        
        // Create and return results map
        Map<String, Object> results = new HashMap<>();
        results.put("wordCount", totalWords);
        results.put("isCount", isCount);
        results.put("areCount", areCount);
        results.put("youCount", youCount);
        results.put("longestWord", longestWord);
        results.put("shortestWord", shortestWord);
        results.put("words", words);
        
        return results;
    }
    
    /**
     * Splits text into individual words
     * Removes punctuation, numbers, and extra spaces
     */
    private String[] splitIntoWords(String text) {
        // Convert to lowercase and split by non-letter characters
        String[] words = WORD_PATTERN.split(text.toLowerCase());
        
        // Filter out empty strings that might result from split
        List<String> validWords = new ArrayList<>();
        for (String word : words) {
            if (word != null && !word.trim().isEmpty() && word.matches("[a-zA-Z]+")) {
                // Preserve original case from text for display
                validWords.add(extractOriginalWord(text, word));
            }
        }
        
        return validWords.toArray(new String[0]);
    }
    
    /**
     * Extracts the original word from text (preserving case)
     */
    private String extractOriginalWord(String text, String lowerWord) {
        // Simple approach: find the word in original text (case-insensitive)
        int index = text.toLowerCase().indexOf(lowerWord);
        if (index >= 0) {
            return text.substring(index, index + lowerWord.length());
        }
        return lowerWord; // Fallback
    }
    
    /**
     * Creates empty results for empty text
     */
    private Map<String, Object> createEmptyResults() {
        Map<String, Object> results = new HashMap<>();
        results.put("wordCount", 0);
        results.put("isCount", 0);
        results.put("areCount", 0);
        results.put("youCount", 0);
        results.put("longestWord", "");
        results.put("shortestWord", "");
        results.put("words", new String[0]);
        return results;
    }
    
    /**
     * For testing or direct use - analyzes text and returns readable summary
     */
    public String analyzeAndSummarize(String text) {
        Map<String, Object> results = analyzeText(text);
        
        return String.format(
            "Words: %d, 'is': %d, 'are': %d, 'you': %d, Longest: '%s', Shortest: '%s'",
            results.get("wordCount"),
            results.get("isCount"),
            results.get("areCount"),
            results.get("youCount"),
            results.get("longestWord"),
            results.get("shortestWord")
        );
    }
}