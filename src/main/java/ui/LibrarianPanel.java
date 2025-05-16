package ui;

import controllers.AuthController;
import models.Librarian;
import ui.components.*;
import utils.Constants;
import utils.IconUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for librarian interface
 */
public class LibrarianPanel extends JPanel {
    private LibraryUI parentFrame;
    private AuthController authController;
    private Librarian librarian;
    
    private JTabbedPane tabbedPane;
    private BookSearchPanel bookSearchPanel;
    private BookManagementPanel bookManagementPanel;
    private UserManagementPanel userManagementPanel;
    private LoanManagementPanel loanManagementPanel;
    private FineManagementPanel fineManagementPanel;
    private LoanHistoryPanel loanHistoryPanel;
    
    /**
     * Constructor for LibrarianPanel
     * @param parentFrame The parent frame
     */
    public LibrarianPanel(LibraryUI parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = AuthController.getInstance();
        
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
        bookSearchPanel = new BookSearchPanel(parentFrame);
        bookManagementPanel = new BookManagementPanel(parentFrame, true); // Librarians can manage books
        userManagementPanel = new UserManagementPanel(parentFrame);
        loanManagementPanel = new LoanManagementPanel(parentFrame);
        fineManagementPanel = new FineManagementPanel(parentFrame);
        loanHistoryPanel = new LoanHistoryPanel(parentFrame, true); // For viewing all borrowers' history
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Search Books", bookSearchPanel);
        tabbedPane.addTab("Manage Books", bookManagementPanel);
        tabbedPane.addTab("Manage Users", userManagementPanel);
        tabbedPane.addTab("Manage Loans", loanManagementPanel);
        tabbedPane.addTab("Manage Fines", fineManagementPanel);
        tabbedPane.addTab("Loan History", loanHistoryPanel);
        
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
        
        JLabel titleLabel = new JLabel("Librarian Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("Logged in as Librarian");
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
        // Update current librarian
        librarian = (Librarian) authController.getCurrentUser();
        
        // Refresh data in all panels
        bookSearchPanel.refreshData();
        bookManagementPanel.refreshData();
        userManagementPanel.refreshData();
        loanManagementPanel.refreshData();
        fineManagementPanel.refreshData();
        loanHistoryPanel.refreshData();
    }
}
