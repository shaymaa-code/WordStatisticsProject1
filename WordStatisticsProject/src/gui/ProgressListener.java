/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import model.FileStats;
import model.GlobalStats;

/**
 * Interface for receiving real-time updates during file processing
 * This enables communication between worker threads and the GUI
 */
public interface ProgressListener {
    
    /**
     * Called when a single file has been processed
     * @param fileStats The statistics for the processed file
     * @param filesProcessedSoFar How many files have been processed so far
     * @param totalFiles Total number of files to process
     */
    void onFileProcessed(FileStats fileStats, int filesProcessedSoFar, int totalFiles);
    
    /**
     * Called when all files have been processed
     * @param globalStats The combined statistics for all files
     */
    void onProcessingComplete(GlobalStats globalStats);
    
    /**
     * Called when processing starts
     * @param totalFiles Total number of files that will be processed
     */
    void onProcessingStarted(int totalFiles);
    
    /**
     * Called if an error occurs during processing
     * @param fileName The name of the file that caused the error
     * @param errorMessage Description of the error
     */
    void onError(String fileName, String errorMessage);
    
    /**
     * Called to update progress percentage (optional)
     * @param progress Percentage completed (0-100)
     */
    void onProgressUpdate(int progress);
}
