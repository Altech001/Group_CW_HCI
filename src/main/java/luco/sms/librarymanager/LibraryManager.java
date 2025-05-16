/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package luco.sms.librarymanager;

import controllers.LibrarySystem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.LibraryUI;
import utils.FileHandler;

/**
 *
 * @author altech
 */
public class LibraryManager {

    public static void main(String[] args) {
        // Set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create data directory and initial data if needed
        FileHandler.createInitialData();
        
        // Initialize the library system
        LibrarySystem.getInstance().loadData();
        
        // Start the UI
        SwingUtilities.invokeLater(() -> {
            LibraryUI libraryUI = new LibraryUI();
            // Center the window on screen
            libraryUI.setLocationRelativeTo(null);
            libraryUI.setVisible(true);
        });
    }
}
