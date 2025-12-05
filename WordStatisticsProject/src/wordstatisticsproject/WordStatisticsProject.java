/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package wordstatisticsproject;

/**
 * comment
 * @author hp
 */
import gui.MainWindow;
import javax.swing.SwingUtilities;

public class WordStatisticsProject {
    public static void main(String[] args) {
        // Launch the GUI
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
