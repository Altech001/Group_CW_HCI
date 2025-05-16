package ui;

import controllers.AuthController;
import models.Borrower;
import models.Clerk;
import models.Librarian;
import models.User;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main UI class for the Library Management System
 */
public class LibraryUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    private LoginPanel loginPanel;
    private LibrarianPanel librarianPanel;
    private ClerkPanel clerkPanel;
    private BorrowerPanel borrowerPanel;
    
    private AuthController authController;
    
    /**
     * Constructor for LibraryUI
     */
    public LibraryUI() {
        authController = AuthController.getInstance();
        
        setupUI();
        setupWindowListener();
    }
    
    /**
     * Set up the UI components
     */
    private void setupUI() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.APP_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create card layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Create panels
        loginPanel = new LoginPanel(this);
        librarianPanel = new LibrarianPanel(this);
        clerkPanel = new ClerkPanel(this);
        borrowerPanel = new BorrowerPanel(this);
        
        // Add panels to card layout
        cardPanel.add(loginPanel, "login");
        cardPanel.add(librarianPanel, "librarian");
        cardPanel.add(clerkPanel, "clerk");
        cardPanel.add(borrowerPanel, "borrower");
        
        // Set initial card
        cardLayout.show(cardPanel, "login");
        
        // Add to frame
        add(cardPanel);
    }
    
    /**
     * Set up window listener to save data when the application is closed
     */
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Make sure all data is saved
                controllers.LibrarySystem.getInstance().saveData();
                System.exit(0);
            }
        });
    }
    
    /**
     * Handle successful login
     * @param user The authenticated user
     */
    public void onLoginSuccess(User user) {
        if (user instanceof Librarian) {
            librarianPanel.refreshData();
            cardLayout.show(cardPanel, "librarian");
        } else if (user instanceof Clerk) {
            clerkPanel.refreshData();
            cardLayout.show(cardPanel, "clerk");
        } else if (user instanceof Borrower) {
            borrowerPanel.refreshData();
            cardLayout.show(cardPanel, "borrower");
        }
    }
    
    /**
     * Handle logout
     */
    public void onLogout() {
        authController.logout();
        loginPanel.clearFields();
        cardLayout.show(cardPanel, "login");
    }
    
    /**
     * Show an error message
     * @param message The error message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show an information message
     * @param message The information message
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show a success message
     * @param message The success message
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show a confirmation dialog
     * @param message The confirmation message
     * @return true if the user confirms, false otherwise
     */
    public boolean showConfirmation(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirmation", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
