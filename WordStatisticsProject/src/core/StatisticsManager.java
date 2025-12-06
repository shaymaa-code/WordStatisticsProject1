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
import java.util.concurrent.CompletionService; // Added for the efficient solution
import java.util.concurrent.ExecutorCompletionService; // Added for the efficient solution
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;// Added for the efficient solution
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;

/**
 * Manages multithreaded processing of files and coordinates between components
 * This is the brain of the application
 * UPDATED: Uses ExecutorCompletionService to avoid creating unnecessary threads.
 */
public class StatisticsManager {
    
    private ExecutorService executorService;
    // New field for handling results efficiently
    private CompletionService<FileStats> completionService;
    
    private FileDiscoverer fileDiscoverer;
    private FileProcessor fileProcessor;
    private GlobalStats globalStats;
    private ProgressListener progressListener;
    
    private volatile boolean isProcessing;
    
    public StatisticsManager() {
        this.fileDiscoverer = new FileDiscoverer();
        this.fileProcessor = new FileProcessor();
        this.globalStats = new GlobalStats();
        this.isProcessing = false;
    }
    
    public void setProgressListener(ProgressListener listener) {
        this.progressListener = listener;
    }
    
    /**
     * Start processing files in a directory
     */
    public void processDirectory(String directoryPath, boolean includeSubdirs) {
        if (isProcessing) {
            System.out.println("Already processing files!");
            return;
        }
        
        // Reset state
        globalStats.reset();
        isProcessing = true;
        
        // 1. Initialize Thread Pool
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(availableProcessors);
        
        // 2. Initialize CompletionService
        // This acts as a queue for finished tasks so we don't need to wait for them one by one
        completionService = new ExecutorCompletionService<>(executorService);
        
        // 3. Find Files
        List<Path> textFiles = fileDiscoverer.findTextFiles(directoryPath, includeSubdirs);
        
        if (textFiles.isEmpty()) {
            notifyError("No Files", "No text files found in the selected directory");
            isProcessing = false;
            executorService.shutdown();
            return;
        }
        
        notifyProcessingStarted(textFiles.size());
        System.out.println("Found " + textFiles.size() + " files. Using " + availableProcessors + " threads.");
        
        // 4. Submit Tasks
        for (Path filePath : textFiles) {
            // ProcessingTask must implement Callable<FileStats>
            ProcessingTask task = new ProcessingTask(filePath, fileProcessor);
            completionService.submit(task);
        }
        
        // We can shutdown the executor immediately (it will still finish submitted tasks)
        executorService.shutdown();
        
        // 5. Start Single Result Consumer
        // Instead of creating a thread per file, we create ONE thread to handle ALL results
        startResultConsumer(textFiles.size());
    }
    
    /**
     * Efficiently consumes results on a SINGLE thread as they complete.
     */
    private void startResultConsumer(int totalFiles) {
        new Thread(() -> {
            int processedCount = 0;
            
            try {
                for (int i = 0; i < totalFiles; i++) {
                    // .take() blocks until the NEXT task is finished
                    // This is much more efficient than checking futures in a loop
                    Future<FileStats> future = completionService.take();
                    
                    try {
                        FileStats stats = future.get();
                        
                        // Update Shared Data
                        globalStats.addFileStats(stats);
                        processedCount++;
                        
                        // Update GUI
                        int currentCount = processedCount;
                        SwingUtilities.invokeLater(() -> {
                            if (progressListener != null) {
                                progressListener.onFileProcessed(stats, currentCount, totalFiles);
                                int progress = (int) ((currentCount / (double) totalFiles) * 100);
                                progressListener.onProgressUpdate(progress);
                            }
                        });
                        
                    } catch (ExecutionException e) {
                        System.err.println("Task execution failed: " + e.getMessage());
                    }
                }
                
                // All tasks finished
                SwingUtilities.invokeLater(() -> {
                    isProcessing = false;
                    if (progressListener != null) {
                        progressListener.onProcessingComplete(globalStats);
                    }
                });
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                notifyError("Interrupted", "Processing was interrupted.");
            }
        }).start();
    }
    
    public void stopProcessing() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            isProcessing = false;
        }
    }
    
    public boolean isProcessing() {
        return isProcessing;
    }
    
    public GlobalStats getGlobalStats() {
        return globalStats;
    }
    
    private void notifyProcessingStarted(int totalFiles) {
        if (progressListener != null) {
            SwingUtilities.invokeLater(() -> progressListener.onProcessingStarted(totalFiles));
        }
    }
    
    private void notifyError(String fileName, String errorMessage) {
        if (progressListener != null) {
            SwingUtilities.invokeLater(() -> progressListener.onError(fileName, errorMessage));
        }
    }
}