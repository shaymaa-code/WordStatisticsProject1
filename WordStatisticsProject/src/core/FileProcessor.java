/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import model.FileStats;
import java.io.*;
import java.nio.file.*;

/**
 * Handles file reading and text extraction
 * Works with WordAnalyzer to process file content
 */
public class FileProcessor {
    
    private WordAnalyzer wordAnalyzer;
    
    // Constructor
    public FileProcessor() {
        this.wordAnalyzer = new WordAnalyzer();
    }
    
    /**
     * Process a single file and return its statistics
     * 
     * @param filePath The path to the file to process
     * @return FileStats object containing all statistics, or null if error
     */
    public FileStats processFile(Path filePath) {
        if (filePath == null || !Files.exists(filePath)) {
            return null;
        }
        
        String fileName = filePath.getFileName().toString();
        
        try {
            // Read file content
            String content = readFileContent(filePath);
            
            // Analyze the content
            java.util.Map<String, Object> results = wordAnalyzer.analyzeText(content);
            
            // Create and populate FileStats object
            FileStats fileStats = new FileStats(fileName, filePath.toString());
            fileStats.setWordCount((Integer) results.get("wordCount"));
            fileStats.setIsCount((Integer) results.get("isCount"));
            fileStats.setAreCount((Integer) results.get("areCount"));
            fileStats.setYouCount((Integer) results.get("youCount"));
            fileStats.setLongestWord((String) results.get("longestWord"));
            fileStats.setShortestWord((String) results.get("shortestWord"));
            
            return fileStats;
            
        } catch (IOException e) {
            System.err.println("Error processing file: " + fileName + " - " + e.getMessage());
            return createErrorFileStats(fileName, filePath.toString(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error processing file: " + fileName + " - " + e.getMessage());
            return createErrorFileStats(fileName, filePath.toString(), "Unexpected error");
        }
    }
    
    /**
     * Reads the entire content of a file
     */
    private String readFileContent(Path filePath) throws IOException {
        // Using Files.readString for Java 11+ (simpler)
        // For compatibility with older Java, we can use BufferedReader
        StringBuilder content = new StringBuilder();
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        
        return content.toString();
    }
    
    /**
     * Creates a FileStats object for files that couldn't be processed
     */
    private FileStats createErrorFileStats(String fileName, String filePath, String error) {
        FileStats errorStats = new FileStats(fileName, filePath);
        errorStats.setWordCount(0);
        errorStats.setIsCount(0);
        errorStats.setAreCount(0);
        errorStats.setYouCount(0);
        errorStats.setLongestWord("ERROR: " + error);
        errorStats.setShortestWord("ERROR");
        return errorStats;
    }
    
    /**
     * Quick test method - processes file and prints results
     */
    public static void testFileProcessing(String filePath) {
        FileProcessor processor = new FileProcessor();
        Path path = Paths.get(filePath);
        
        FileStats stats = processor.processFile(path);
        
        if (stats != null) {
            System.out.println("Processed: " + stats.getFileName());
            System.out.println(stats);
        } else {
            System.out.println("Failed to process: " + filePath);
        }
    }
    
    
}