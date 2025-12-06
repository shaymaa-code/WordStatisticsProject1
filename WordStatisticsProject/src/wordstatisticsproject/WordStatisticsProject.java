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
import javax.swing.UIManager; //for buttons to show their color on Mac

public class WordStatisticsProject {

    public static void main(String[] args) {

        // --- 1. SET THEME TO NIMBUS (Fixes Purple Buttons) ---
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("Nimbus theme not available. Using default.");
        }
        // -----------------------------------------------------

        // --- 2. LAUNCH THE GUI ---
        SwingUtilities.invokeLater(() -> {
            // Your GUI class is named 'MainWindow', so we use that here
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
