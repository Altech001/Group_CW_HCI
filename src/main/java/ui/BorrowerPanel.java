package ui;

import controllers.AuthController;
import controllers.LoanController;
import models.Borrower;
import ui.components.BookSearchPanel;
import ui.components.LoanHistoryPanel;
import utils.Constants;
import utils.IconUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for borrower interface
 */
public class BorrowerPanel extends JPanel {
    private LibraryUI parentFrame;
    private AuthController authController;
    private LoanController loanController;
    private Borrower borrower;
    
    private JTabbedPane tabbedPane;
    private BookSearchPanel bookSearchPanel;
    private LoanHistoryPanel loanHistoryPanel;
    
    /**
     * Constructor for BorrowerPanel
     * @param parentFrame The parent frame
     */
    public BorrowerPanel(LibraryUI parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = AuthController.getInstance();
        this.loanController = LoanController.getInstance();
        
        setupUI();
    }
    
    /**
     * Set up the UI components
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Constants.REGULAR_FONT);
        tabbedPane.setBackground(Constants.BACKGROUND_COLOR);
        
        // Create panels
        bookSearchPanel = new BookSearchPanel(parentFrame, true); // Enable borrow functionality
        loanHistoryPanel = new LoanHistoryPanel(parentFrame, false); // For viewing only this borrower's history
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Search Books", bookSearchPanel);
        tabbedPane.addTab("My Loans", loanHistoryPanel);
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Create the header panel
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Constants.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Borrower Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("Logged in as Borrower");
        userLabel.setFont(Constants.REGULAR_FONT);
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = IconUtils.createStyledButton("Logout", Constants.LOGOUT_ICON, Constants.SMALL_BUTTON_SIZE);
        logoutButton.addActionListener(e -> parentFrame.onLogout());
        
        userInfoPanel.add(userLabel);
        userInfoPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Refresh data in all panels
     */
    public void refreshData() {
        // Update current borrower
        borrower = (Borrower) authController.getCurrentUser();
        
        // Refresh data in all panels
        bookSearchPanel.refreshData();
        loanHistoryPanel.setBorrowerId(borrower.getId());
        loanHistoryPanel.refreshData();
    }
}
