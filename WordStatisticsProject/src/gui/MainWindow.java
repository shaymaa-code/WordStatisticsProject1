/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import core.StatisticsManager;
import model.FileStats;
import model.GlobalStats;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Main GUI window for the Word Statistics application
 */
public class MainWindow extends JFrame implements ProgressListener {
    
    // Components
    private JTextField directoryField;
    private JButton browseButton;
    private JButton startButton;
    private JCheckBox subdirCheckbox;
    private JTable resultsTable;
    private StatisticsTableModel tableModel;
    private JLabel totalFilesLabel;
    private JLabel totalWordsLabel;
    private JLabel longestWordLabel;
    private JLabel shortestWordLabel;
    
    // Core components
    private StatisticsManager statisticsManager;
    
    // Purple colors
    private final Color PURPLE_BG = new Color(230, 220, 255);
    private final Color LIGHT_PURPLE = new Color(245, 240, 255);
    private final Color PURPLE_BUTTON = new Color(138, 43, 226); // Purple
    private final Color DARK_PURPLE = new Color(75, 0, 130); // Indigo
    
    /**
     * Constructor
     */
    public MainWindow() {
        statisticsManager = new StatisticsManager();
        statisticsManager.setProgressListener(this);
        
        initUI();
        setWindowProperties();
    }
    
    /**
     * Initialize the user interface
     */
    private void initUI() {
        // Set layout and purple background
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(PURPLE_BG);
        
        // Create panels
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();
        
        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the top panel (directory selection)
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(LIGHT_PURPLE);
        
        // Directory selection
        JPanel dirPanel = new JPanel(new BorderLayout(5, 5));
        dirPanel.setBackground(LIGHT_PURPLE);
        
        JLabel dirLabel = new JLabel("Directory:");
        dirLabel.setFont(new Font("Arial", Font.BOLD, 12));
        directoryField = new JTextField(30);
        directoryField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        browseButton = new JButton("Browse");
        browseButton.setBackground(PURPLE_BUTTON);
        browseButton.setForeground(Color.WHITE);
        browseButton.setFont(new Font("Arial", Font.BOLD, 12));
        browseButton.setFocusPainted(false);
        
        dirPanel.add(dirLabel, BorderLayout.WEST);
        dirPanel.add(directoryField, BorderLayout.CENTER);
        dirPanel.add(browseButton, BorderLayout.EAST);
        
        // Options panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        optionsPanel.setBackground(LIGHT_PURPLE);
        
        subdirCheckbox = new JCheckBox("Include subdirectories", true);
        subdirCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        subdirCheckbox.setBackground(LIGHT_PURPLE);
        
        startButton = new JButton("Start Processing");
        startButton.setBackground(DARK_PURPLE);
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setFocusPainted(false);
        
        optionsPanel.add(subdirCheckbox);
        optionsPanel.add(startButton);
        
        // Add to main panel
        panel.add(dirPanel, BorderLayout.NORTH);
        panel.add(optionsPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        browseButton.addActionListener(new BrowseAction());
        startButton.addActionListener(new StartAction());
        
        return panel;
    }
    
    /**
     * Create the center panel (results table)
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "File Statistics",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            DARK_PURPLE
        ));
        panel.setBackground(LIGHT_PURPLE);
        
        // Create table with custom model
        tableModel = new StatisticsTableModel();
        resultsTable = new JTable(tableModel);
        
        // Style the table
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        resultsTable.setRowHeight(25);
        resultsTable.setBackground(Color.WHITE);
        resultsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        resultsTable.getTableHeader().setBackground(PURPLE_BUTTON);
        resultsTable.getTableHeader().setForeground(Color.WHITE);
        
        // Make table scrollable
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the bottom panel (overall stats only - NO status/progress bar)
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(LIGHT_PURPLE);
        
        // Create two separate panels for Longest and Shortest
        JPanel longestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        longestPanel.setBackground(LIGHT_PURPLE);
        
        JPanel shortestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        shortestPanel.setBackground(LIGHT_PURPLE);
        
        // Longest section
        JLabel longestTitle = new JLabel("Longest");
        longestTitle.setFont(new Font("Arial", Font.BOLD, 14));
        longestTitle.setForeground(DARK_PURPLE);
        longestWordLabel = new JLabel("");
        longestWordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        longestWordLabel.setForeground(DARK_PURPLE);
        
        longestPanel.add(longestTitle);
        longestPanel.add(longestWordLabel);
        
        // Shortest section
        JLabel shortestTitle = new JLabel("Shortest");
        shortestTitle.setFont(new Font("Arial", Font.BOLD, 14));
        shortestTitle.setForeground(DARK_PURPLE);
        shortestWordLabel = new JLabel("");
        shortestWordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        shortestWordLabel.setForeground(DARK_PURPLE);
        
        shortestPanel.add(shortestTitle);
        shortestPanel.add(shortestWordLabel);
        
        // Combine both panels
        JPanel combinedPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        combinedPanel.setBackground(LIGHT_PURPLE);
        combinedPanel.add(longestPanel);
        combinedPanel.add(shortestPanel);
        
        // Add to main panel
        panel.add(combinedPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Set window properties
     */
    private void setWindowProperties() {
        setTitle("Word Statistics Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null); // Center on screen
        setResizable(true);
    }
    
    /**
     * Action for Browse button
     */
    private class BrowseAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle("Select Directory");
            
            int result = fileChooser.showOpenDialog(MainWindow.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedDir = fileChooser.getSelectedFile();
                directoryField.setText(selectedDir.getAbsolutePath());
            }
        }
    }
    
    /**
     * Action for Start button
     */
    private class StartAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String directoryPath = directoryField.getText().trim();
            
            if (directoryPath.isEmpty()) {
                JOptionPane.showMessageDialog(MainWindow.this,
                    "Please select a directory first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            File dir = new File(directoryPath);
            if (!dir.exists() || !dir.isDirectory()) {
                JOptionPane.showMessageDialog(MainWindow.this,
                    "The selected directory does not exist or is not a directory.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Clear previous results
            tableModel.clear();
            clearOverallStats();
            
            // Disable start button during processing
            startButton.setEnabled(false);
            browseButton.setEnabled(false);
            startButton.setText("Processing...");
            startButton.setBackground(Color.GRAY);
            
            // Get checkbox state
            boolean includeSubdirs = subdirCheckbox.isSelected();
            
            // Start processing in a separate thread to keep GUI responsive
            new Thread(() -> {
                statisticsManager.processDirectory(directoryPath, includeSubdirs);
            }).start();
        }
    }
    
    /**
     * Clear overall statistics display
     */
    private void clearOverallStats() {
        longestWordLabel.setText("");
        shortestWordLabel.setText("");
    }
    
    // ProgressListener implementation methods
    
    @Override
    public void onProcessingStarted(int totalFiles) {
        // Not used - no status display
    }
    
    @Override
    public void onFileProcessed(FileStats fileStats, int filesProcessedSoFar, int totalFiles) {
        SwingUtilities.invokeLater(() -> {
            // Add to table in real-time
            tableModel.addFileStats(fileStats);
        });
    }
    
    @Override
    public void onProcessingComplete(GlobalStats globalStats) {
        SwingUtilities.invokeLater(() -> {
            // Update overall statistics
            updateOverallStats(globalStats);
            
            // Re-enable buttons
            startButton.setEnabled(true);
            browseButton.setEnabled(true);
            startButton.setText("Start Processing");
            startButton.setBackground(DARK_PURPLE);
            
            // Show completion message
            JOptionPane.showMessageDialog(MainWindow.this,
                "Processing complete!\n" +
                "Processed " + globalStats.getTotalFilesProcessed() + " files.",
                "Complete",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    @Override
    public void onError(String fileName, String errorMessage) {
        SwingUtilities.invokeLater(() -> {
            // Re-enable buttons on error
            startButton.setEnabled(true);
            browseButton.setEnabled(true);
            startButton.setText("Start Processing");
            startButton.setBackground(DARK_PURPLE);
            
            JOptionPane.showMessageDialog(MainWindow.this,
                "Error processing file: " + fileName + "\n" + errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        });
    }
    
    @Override
    public void onProgressUpdate(int progress) {
        // Not used - no progress bar
    }
    
    /**
     * Update the overall statistics display
     */
    private void updateOverallStats(GlobalStats globalStats) {
        longestWordLabel.setText(globalStats.getLongestWordInDirectory());
        shortestWordLabel.setText(globalStats.getShortestWordInDirectory());
    }
    
    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}