/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threading;

import model.FileStats;
import core.FileProcessor;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * Represents a worker task that processes a single file
 * Implements Callable to return results and work with ExecutorService
 */
public class ProcessingTask implements Callable<FileStats> {
    
    private final Path filePath;
    private final FileProcessor fileProcessor;
    
    /**
     * Constructor using File
     */
    public ProcessingTask(File file, FileProcessor fileProcessor) {
        this.filePath = file.toPath();
        this.fileProcessor = fileProcessor;
    }
    
    /**
     * Constructor using Path
     */
    public ProcessingTask(Path filePath, FileProcessor fileProcessor) {
        this.filePath = filePath;
        this.fileProcessor = fileProcessor;
    }
    
    /**
     * The main processing method called by the thread pool
     * @return FileStats containing all statistics for this file
     * @throws Exception if file cannot be processed
     */
    @Override
    public FileStats call() throws Exception {
        try {
            // Process the file using the existing FileProcessor
            FileStats stats = fileProcessor.processFile(filePath);
            
            // Log processing (optional - can be removed)
            System.out.println("Thread " + Thread.currentThread().getName() + 
                             " processed: " + filePath.getFileName());
            
            return stats;
            
        } catch (Exception e) {
            // Create error statistics if processing fails
            System.err.println("Error processing file: " + filePath.getFileName() + 
                             " - " + e.getMessage());
            
            FileStats errorStats = new FileStats(
                filePath.getFileName().toString(), 
                filePath.toString()
            );
            errorStats.setWordCount(0);
            errorStats.setIsCount(0);
            errorStats.setAreCount(0);
            errorStats.setYouCount(0);
            errorStats.setLongestWord("ERROR");
            errorStats.setShortestWord("ERROR");
            
            return errorStats;
        }
    }
    
    /**
     * Get the file being processed as Path
     */
    public Path getFilePath() {
        return filePath;
    }
    
    /**
     * Get the file being processed as File
     */
    public File getFile() {
        return filePath.toFile();
    }
    
    /**
     * Get the file processor
     */
    public FileProcessor getFileProcessor() {
        return fileProcessor;
    }
}
