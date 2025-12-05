/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Represents statistics for a single text file
 */
public class FileStats {
    private String fileName;
    private String filePath;
    private int wordCount;
    private int isCount;
    private int areCount;
    private int youCount;
    private String longestWord;
    private String shortestWord;
    
    // Constructor
    public FileStats(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.wordCount = 0;
        this.isCount = 0;
        this.areCount = 0;
        this.youCount = 0;
        this.longestWord = "";
        this.shortestWord = "";
    }
    
    // Default constructor
    public FileStats() {
        this("", "");
    }
    
    // Getters and Setters
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public int getWordCount() {
        return wordCount;
    }
    
    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
    
    public int getIsCount() {
        return isCount;
    }
    
    public void setIsCount(int isCount) {
        this.isCount = isCount;
    }
    
    public int getAreCount() {
        return areCount;
    }
    
    public void setAreCount(int areCount) {
        this.areCount = areCount;
    }
    
    public int getYouCount() {
        return youCount;
    }
    
    public void setYouCount(int youCount) {
        this.youCount = youCount;
    }
    
    public String getLongestWord() {
        return longestWord;
    }
    
    public void setLongestWord(String longestWord) {
        this.longestWord = longestWord;
    }
    
    public String getShortestWord() {
        return shortestWord;
    }
    
    public void setShortestWord(String shortestWord) {
        this.shortestWord = shortestWord;
    }
    
    /**
     * Updates all statistics at once (useful for threading)
     */
    public void updateStats(int wordCount, int isCount, int areCount, int youCount, 
                           String longestWord, String shortestWord) {
        this.wordCount = wordCount;
        this.isCount = isCount;
        this.areCount = areCount;
        this.youCount = youCount;
        this.longestWord = longestWord;
        this.shortestWord = shortestWord;
    }
    
    /**
     * Returns a copy of this FileStats object
     */
    public FileStats copy() {
        FileStats copy = new FileStats(this.fileName, this.filePath);
        copy.updateStats(this.wordCount, this.isCount, this.areCount, this.youCount,
                        this.longestWord, this.shortestWord);
        return copy;
    }
    
    @Override
    public String toString() {
        return String.format("File: %s, Words: %d, is: %d, are: %d, you: %d, Longest: %s, Shortest: %s",
                fileName, wordCount, isCount, areCount, youCount, longestWord, shortestWord);
    }
    
    /**
     * For table display - returns data as Object array
     */
    public Object[] toTableRow() {
        return new Object[] {
            fileName,
            wordCount,
            isCount,
            areCount,
            youCount,
            longestWord,
            shortestWord
        };
    }
}