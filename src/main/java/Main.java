import controllers.LibrarySystem;
import ui.LibraryUI;
import utils.FileHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Main class to start the Library Management System
 */
public class Main {
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
