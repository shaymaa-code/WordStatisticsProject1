/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import model.FileStats;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom TableModel for displaying file statistics in real-time
 */
public class StatisticsTableModel extends AbstractTableModel {
    
    private final String[] columnNames = {
        "File", "Words", "is", "are", "you", "Longest", "Shortest"
    };
    
    private final List<FileStats> data;
    
    public StatisticsTableModel() {
        this.data = new ArrayList<>();
    }
    
    /**
     * Add a new file's statistics to the table
     */
    public void addFileStats(FileStats fileStats) {
        data.add(fileStats);
        // Notify table that a new row has been added
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
    
    /**
     * Update existing file statistics
     */
    public void updateFileStats(int row, FileStats fileStats) {
        if (row >= 0 && row < data.size()) {
            data.set(row, fileStats);
            fireTableRowsUpdated(row, row);
        }
    }
    
    /**
     * Clear all data from the table
     */
    public void clear() {
        int oldSize = data.size();
        data.clear();
        if (oldSize > 0) {
            fireTableRowsDeleted(0, oldSize - 1);
        }
    }
    
    /**
     * Get FileStats at specific row
     */
    public FileStats getFileStatsAt(int row) {
        if (row >= 0 && row < data.size()) {
            return data.get(row);
        }
        return null;
    }
    
    /**
     * Get all file statistics
     */
    public List<FileStats> getAllFileStats() {
        return new ArrayList<>(data);
    }
    
    // Required TableModel methods
    
    @Override
    public int getRowCount() {
        return data.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= data.size()) {
            return null;
        }
        
        FileStats stats = data.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return stats.getFileName();
            case 1: return stats.getWordCount();
            case 2: return stats.getIsCount();
            case 3: return stats.getAreCount();
            case 4: return stats.getYouCount();
            case 5: return stats.getLongestWord();
            case 6: return stats.getShortestWord();
            default: return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return String.class;
            case 1: return Integer.class;
            case 2: return Integer.class;
            case 3: return Integer.class;
            case 4: return Integer.class;
            case 5: return String.class;
            case 6: return String.class;
            default: return Object.class;
        }
    }
}