/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Discovers all text files in a directory and its subdirectories
 */
public class FileDiscoverer {
    
    // Supported text file extensions
    private static final List<String> TEXT_FILE_EXTENSIONS = Arrays.asList(
        ".txt", ".text", ".md", ".java", ".c", ".cpp", ".h", ".py", 
        ".js", ".html", ".css", ".xml", ".json", ".csv"
    );
    
    /**
     * Finds all text files in a directory (including subdirectories)
     * 
     * @param directoryPath The path to the directory to search
     * @return List of Path objects for all found text files
     */
    public List<Path> findTextFiles(String directoryPath) {
        List<Path> textFiles = new ArrayList<>();
        
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            return textFiles;
        }
        
        Path dirPath = Paths.get(directoryPath);
        
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            System.err.println("Directory does not exist or is not a directory: " + directoryPath);
            return textFiles;
        }
        
        try {
            // Use Java's Files.walk to traverse directory tree
            try (Stream<Path> paths = Files.walk(dirPath)) {
                paths
                    .filter(Files::isRegularFile)          // Only regular files
                    .filter(this::isTextFile)              // Only text files
                    .forEach(textFiles::add);              // Add to list
            }
            
        } catch (IOException e) {
            System.err.println("Error scanning directory: " + directoryPath + " - " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Security exception (no permission): " + e.getMessage());
        }
        
        return textFiles;
    }
    
    /**
     * Overloaded method to match the StatisticsManager interface
     * This method is called by StatisticsManager
     * 
     * @param directoryPath The path to the directory to search
     * @param includeSubdirs Whether to include subdirectories (ignored - always true)
     * @return List of Path objects for all found text files
     */
    public List<Path> findTextFiles(String directoryPath, boolean includeSubdirs) {
        // For now, we'll always include subdirectories
        // You can modify this later to respect the includeSubdirs parameter
        System.out.println("Searching directory: " + directoryPath + 
                         " (include subdirs: " + includeSubdirs + ")");
        return findTextFiles(directoryPath);
    }
    
    /**
     * Checks if a file is a text file based on its extension
     */
    private boolean isTextFile(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        
        // Check if file has a text extension
        for (String extension : TEXT_FILE_EXTENSIONS) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        
        // Additional check: try to read first few bytes to detect binary files
        return isLikelyTextFile(filePath);
    }
    
    /**
     * Attempts to determine if a file is text-based by reading first few bytes
     * This is a simple heuristic and not 100% accurate
     */
    private boolean isLikelyTextFile(Path filePath) {
        try {
            // Read first 1024 bytes
            byte[] buffer = new byte[1024];
            int bytesRead = Files.newInputStream(filePath).read(buffer);
            
            if (bytesRead <= 0) {
                return false; // Empty file
            }
            
            // Check for non-text characters
            for (int i = 0; i < bytesRead; i++) {
                byte b = buffer[i];
                // If byte is 0 (null character) or certain control characters, likely binary
                if (b == 0 || (b < 32 && b != 9 && b != 10 && b != 13)) {
                    return false;
                }
            }
            
            return true;
            
        } catch (IOException e) {
            return false; // If we can't read it, skip it
        }
    }
    
    /**
     * Gets file count without actually processing them
     */
    public int countTextFiles(String directoryPath) {
        return findTextFiles(directoryPath).size();
    }
    
    /**
     * Gets list of file names (just names, not full paths)
     */
    public List<String> getTextFileNames(String directoryPath) {
        List<Path> files = findTextFiles(directoryPath);
        return files.stream()
                   .map(path -> path.getFileName().toString())
                   .collect(Collectors.toList());
    }
    
    /**
     * Test method to verify file discovery
     */
    public static void testFileDiscovery(String directoryPath) {
        FileDiscoverer discoverer = new FileDiscoverer();
        
        System.out.println("Searching for text files in: " + directoryPath);
        System.out.println("Supported extensions: " + TEXT_FILE_EXTENSIONS);
        
        List<Path> files = discoverer.findTextFiles(directoryPath);
        
        if (files.isEmpty()) {
            System.out.println("No text files found.");
        } else {
            System.out.println("Found " + files.size() + " text files:");
            files.forEach(path -> System.out.println("  - " + path));
        }
    }
}