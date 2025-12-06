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
    // FIX: Added explicit list. Only files with these extensions will be processed.
    private static final List<String> TEXT_FILE_EXTENSIONS = Arrays.asList(
        ".txt", ".text", ".md", ".java", ".c", ".cpp", ".h", ".py", 
        ".js", ".html", ".css", ".xml", ".json", ".csv"
    );
    
    /**
     * Finds all text files in a directory (including subdirectories)
     * * @param directoryPath The path to the directory to search
     * @return List of Path objects for all found text files
     */
    public List<Path> findTextFiles(String directoryPath) {
        // Default to recursive search if not specified
        return findTextFiles(directoryPath, true);
    }
    
    /**
     * Overloaded method to match the StatisticsManager interface
     * * @param directoryPath The path to the directory to search
     * @param includeSubdirs Whether to search subdirectories recursively
     * @return List of Path objects for all found text files
     */
    public List<Path> findTextFiles(String directoryPath, boolean includeSubdirs) {
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
            // Determine depth: Integer.MAX_VALUE for recursive, 1 for current directory only
            int maxDepth = includeSubdirs ? Integer.MAX_VALUE : 1;

            // Use Java's Files.walk with depth control
            try (Stream<Path> paths = Files.walk(dirPath, maxDepth)) {
                paths
                    .filter(Files::isRegularFile)          // Only regular files
                    .filter(this::isTextFile)              // Only allowed text extensions
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
     * Checks if a file is a text file based STRICTLY on its extension.
     * * FIX APPLIED: Removed the 'isLikelyTextFile' fallback check. 
     * Previous version analyzed file content, which caused PDF files 
     * to be mistakenly identified as text files. Now, if the extension 
     * isn't in the allowed list, the file is rejected immediately.
     */
    private boolean isTextFile(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        
        // Check if file has a text extension
        for (String extension : TEXT_FILE_EXTENSIONS) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        
        // Strict Mode: If extension doesn't match, it is NOT a text file.
        return false;
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