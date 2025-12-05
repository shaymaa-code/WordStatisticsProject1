/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import model.FileStats;
import model.GlobalStats;
import gui.ProgressListener;
import threading.ProcessingTask;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;

/**
 * Manages multithreaded processing of files and coordinates between components
 * This is the brain of the application
 */
public class StatisticsManager {
    
    private ExecutorService executorService;
    private FileDiscoverer fileDiscoverer;
    private FileProcessor fileProcessor;
    private GlobalStats globalStats;
    private ProgressListener progressListener;
    
    private volatile boolean isProcessing;
    private AtomicInteger filesProcessed;
    
    /**
     * Constructor
     */
    public StatisticsManager() {
        this.fileDiscoverer = new FileDiscoverer();
        this.fileProcessor = new FileProcessor();
        this.globalStats = new GlobalStats();
        this.filesProcessed = new AtomicInteger(0);
        this.isProcessing = false;
    }
    
    /**
     * Set the progress listener for real-time updates
     */
    public void setProgressListener(ProgressListener listener) {
        this.progressListener = listener;
    }
    
    /**
     * Start processing files in a directory
     * @param directoryPath Path to the directory
     * @param includeSubdirs Whether to include subdirectories
     */
    public void processDirectory(String directoryPath, boolean includeSubdirs) {
        if (isProcessing) {
            System.out.println("Already processing files!");
            return;
        }
        
        // Reset for new processing
        globalStats.reset();
        filesProcessed.set(0);
        isProcessing = true;
        
        // Create thread pool based on available processors
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(availableProcessors);
        
        // Find all text files (FileDiscoverer always includes subdirectories)
        List<Path> textFiles = fileDiscoverer.findTextFiles(directoryPath);
        
        if (textFiles.isEmpty()) {
            notifyError("No text files found", "No text files found in the selected directory");
            isProcessing = false;
            return;
        }
        
        // Notify that processing has started
        notifyProcessingStarted(textFiles.size());
        
        System.out.println("Found " + textFiles.size() + " text files to process");
        System.out.println("Using " + availableProcessors + " threads");
        
        // Submit all files for processing
        for (Path filePath : textFiles) {
            ProcessingTask task = new ProcessingTask(filePath, fileProcessor);
            Future<FileStats> future = executorService.submit(task);
            
            // Handle results as they complete
            handleFutureResult(future, textFiles.size());
        }
        
        // Shutdown executor and wait for completion
        executorService.shutdown();
        
        // Wait for all tasks to complete in a separate thread
        new Thread(() -> {
            try {
                executorService.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
                
                // All tasks completed
                SwingUtilities.invokeLater(() -> {
                    isProcessing = false;
                    if (progressListener != null) {
                        progressListener.onProcessingComplete(globalStats);
                    }
                });
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                notifyError("Processing interrupted", e.getMessage());
            }
        }).start();
    }
    
    /**
     * Handle the result of a Future (when a file processing completes)
     */
    private void handleFutureResult(Future<FileStats> future, int totalFiles) {
        new Thread(() -> {
            try {
                // Get the result (this blocks until the task completes)
                FileStats fileStats = future.get();
                
                // Update global statistics
                globalStats.addFileStats(fileStats);
                
                // Increment processed count
                int processedCount = filesProcessed.incrementAndGet();
                
                // Calculate progress percentage
                int progress = (int) ((processedCount / (double) totalFiles) * 100);
                
                // Notify GUI with the result
                SwingUtilities.invokeLater(() -> {
                    if (progressListener != null) {
                        progressListener.onFileProcessed(fileStats, processedCount, totalFiles);
                        progressListener.onProgressUpdate(progress);
                    }
                });
                
                System.out.println("Processed " + processedCount + "/" + totalFiles + 
                                 " files: " + fileStats.getFileName());
                
            } catch (Exception e) {
                System.err.println("Error getting result from future: " + e.getMessage());
                notifyError("Processing error", e.getMessage());
            }
        }).start();
    }
    
    /**
     * Stop processing (if needed)
     */
    public void stopProcessing() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            isProcessing = false;
            System.out.println("Processing stopped by user");
        }
    }
    
    /**
     * Check if currently processing
     */
    public boolean isProcessing() {
        return isProcessing;
    }
    
    /**
     * Get current global statistics
     */
    public GlobalStats getGlobalStats() {
        return globalStats;
    }
    
    /**
     * Notify that processing has started
     */
    private void notifyProcessingStarted(int totalFiles) {
        if (progressListener != null) {
            SwingUtilities.invokeLater(() -> {
                progressListener.onProcessingStarted(totalFiles);
            });
        }
    }
    
    /**
     * Notify about an error
     */
    private void notifyError(String fileName, String errorMessage) {
        if (progressListener != null) {
            SwingUtilities.invokeLater(() -> {
                progressListener.onError(fileName, errorMessage);
            });
        }
    }
}