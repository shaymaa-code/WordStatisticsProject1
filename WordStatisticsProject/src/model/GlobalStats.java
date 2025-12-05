/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents overall statistics for all processed files in the directory
 */
public class GlobalStats {
    private int totalFilesProcessed;
    private int totalWordCount;
    private int totalIsCount;
    private int totalAreCount;
    private int totalYouCount;
    private String longestWordInDirectory;
    private String shortestWordInDirectory;
    private List<FileStats> allFileStats;
    
    // Constructor
    public GlobalStats() {
        this.totalFilesProcessed = 0;
        this.totalWordCount = 0;
        this.totalIsCount = 0;
        this.totalAreCount = 0;
        this.totalYouCount = 0;
        this.longestWordInDirectory = "";
        this.shortestWordInDirectory = "";
        this.allFileStats = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getTotalFilesProcessed() {
        return totalFilesProcessed;
    }
    
    public void setTotalFilesProcessed(int totalFilesProcessed) {
        this.totalFilesProcessed = totalFilesProcessed;
    }
    
    public int getTotalWordCount() {
        return totalWordCount;
    }
    
    public void setTotalWordCount(int totalWordCount) {
        this.totalWordCount = totalWordCount;
    }
    
    public int getTotalIsCount() {
        return totalIsCount;
    }
    
    public void setTotalIsCount(int totalIsCount) {
        this.totalIsCount = totalIsCount;
    }
    
    public int getTotalAreCount() {
        return totalAreCount;
    }
    
    public void setTotalAreCount(int totalAreCount) {
        this.totalAreCount = totalAreCount;
    }
    
    public int getTotalYouCount() {
        return totalYouCount;
    }
    
    public void setTotalYouCount(int totalYouCount) {
        this.totalYouCount = totalYouCount;
    }
    
    public String getLongestWordInDirectory() {
        return longestWordInDirectory;
    }
    
    public void setLongestWordInDirectory(String longestWordInDirectory) {
        this.longestWordInDirectory = longestWordInDirectory;
    }
    
    public String getShortestWordInDirectory() {
        return shortestWordInDirectory;
    }
    
    public void setShortestWordInDirectory(String shortestWordInDirectory) {
        this.shortestWordInDirectory = shortestWordInDirectory;
    }
    
    public List<FileStats> getAllFileStats() {
        return new ArrayList<>(allFileStats); // Return copy for thread safety
    }
    
    public void setAllFileStats(List<FileStats> allFileStats) {
        this.allFileStats = new ArrayList<>(allFileStats);
    }
    
    /**
     * Add a file's statistics and update global totals
     */
    public void addFileStats(FileStats fileStats) {
        if (fileStats == null) return;
        
        allFileStats.add(fileStats);
        totalFilesProcessed++;
        
        // Update total counts
        totalWordCount += fileStats.getWordCount();
        totalIsCount += fileStats.getIsCount();
        totalAreCount += fileStats.getAreCount();
        totalYouCount += fileStats.getYouCount();
        
        // Update longest word
        if (fileStats.getLongestWord() != null && !fileStats.getLongestWord().isEmpty()) {
            if (longestWordInDirectory.isEmpty() || 
                fileStats.getLongestWord().length() > longestWordInDirectory.length()) {
                longestWordInDirectory = fileStats.getLongestWord();
            }
        }
        
        // Update shortest word
        if (fileStats.getShortestWord() != null && !fileStats.getShortestWord().isEmpty()) {
            if (shortestWordInDirectory.isEmpty() || 
                fileStats.getShortestWord().length() < shortestWordInDirectory.length()) {
                shortestWordInDirectory = fileStats.getShortestWord();
            }
        }
    }
    
    /**
     * Reset all statistics (useful for new directory processing)
     */
    public void reset() {
        totalFilesProcessed = 0;
        totalWordCount = 0;
        totalIsCount = 0;
        totalAreCount = 0;
        totalYouCount = 0;
        longestWordInDirectory = "";
        shortestWordInDirectory = "";
        allFileStats.clear();
    }
    
    /**
     * Get average words per file
     */
    public double getAverageWordsPerFile() {
        return totalFilesProcessed > 0 ? (double) totalWordCount / totalFilesProcessed : 0.0;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Global Stats: Files: %d, Total Words: %d, is: %d, are: %d, you: %d, Longest: %s, Shortest: %s",
            totalFilesProcessed, totalWordCount, totalIsCount, totalAreCount, totalYouCount,
            longestWordInDirectory, shortestWordInDirectory
        );
    }
    
    /**
     * Get statistics summary for display
     */
    public String getSummary() {
        return String.format(
            "Processed %d files, %d total words\nLongest word: %s\nShortest word: %s",
            totalFilesProcessed, totalWordCount, longestWordInDirectory, shortestWordInDirectory
        );
    }
}
