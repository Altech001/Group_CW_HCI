package ui.components;

import controllers.AuthController;
import controllers.LoanController;
import controllers.UserController;
import models.Borrower;
import models.Loan;
import ui.LibraryUI;
import utils.Constants;
import utils.IconUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel for viewing loan history
 */
public class LoanHistoryPanel extends JPanel {
    private LibraryUI parentFrame;
    private LoanController loanController;
    private UserController userController;
    private AuthController authController;
    
    private JComboBox<String> borrowerComboBox;
    private JTable loansTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    
    private boolean showAllBorrowers; // Whether to show loans for all borrowers or just the current borrower
    private String borrowerId; // For borrower-specific view
    
    /**
     * Constructor for LoanHistoryPanel
     * @param parentFrame The parent frame
     * @param showAllBorrowers Whether to show loans for all borrowers
     */
    public LoanHistoryPanel(LibraryUI parentFrame, boolean showAllBorrowers) {
        this.parentFrame = parentFrame;
        this.loanController = LoanController.getInstance();
        this.userController = UserController.getInstance();
        this.authController = AuthController.getInstance();
        this.showAllBorrowers = showAllBorrowers;
        
        setupUI();
    }
    
    /**
     * Set up the UI components
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Constants.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = IconUtils.createHeaderLabel("Loan History");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create filter panel if showing all borrowers
        JPanel filterPanel = null;
        if (showAllBorrowers) {
            filterPanel = createFilterPanel();
        }
        
        // Create table panel
        JPanel tablePanel = createTablePanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        if (filterPanel != null) {
            add(filterPanel, BorderLayout.NORTH);
        }
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the filter panel
     * @return The filter panel
     */
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Constants.BACKGROUND_COLOR);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel borrowerLabel = new JLabel("Borrower:");
        borrowerLabel.setFont(Constants.REGULAR_FONT);
        
        borrowerComboBox = new JComboBox<>();
        borrowerComboBox.setFont(Constants.REGULAR_FONT);
        
        // Add "All Borrowers" option
        borrowerComboBox.addItem("All Borrowers");
        
        // Add borrowers
        for (Borrower borrower : userController.getAllBorrowers()) {
            borrowerComboBox.addItem(borrower.getId() + " - " + borrower.getFullName());
        }
        
        borrowerComboBox.addActionListener(e -> loadLoanHistory());
        
        filterPanel.add(borrowerLabel);
        filterPanel.add(borrowerComboBox);
        
        return filterPanel;
    }
    
    /**
     * Create the table panel
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Constants.SECONDARY_BACKGROUND);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table model
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        tableModel.addColumn("ID");
        tableModel.addColumn("Book Title");
        tableModel.addColumn("Borrower");
        tableModel.addColumn("Checkout Date");
        tableModel.addColumn("Due Date");
        tableModel.addColumn("Return Date");
        tableModel.addColumn("Status");
        tableModel.addColumn("Fine");
        
        // Create table
        loansTable = new JTable(tableModel);
        IconUtils.styleTable(loansTable);
        
        // Hide the ID column as it's only for internal use
        loansTable.getColumnModel().getColumn(0).setMinWidth(0);
        loansTable.getColumnModel().getColumn(0).setMaxWidth(0);
        loansTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Create scroll pane
        JScrollPane scrollPane = IconUtils.createStyledScrollPane(loansTable);
        
        // Add to table panel
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * Create the button panel
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Constants.BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Create refresh button
        refreshButton = IconUtils.createStyledButton("Refresh", null, Constants.SMALL_BUTTON_SIZE);
        refreshButton.addActionListener(e -> loadLoanHistory());
        
        // Add button to panel
        buttonPanel.add(refreshButton);
        
        return buttonPanel;
    }
    
    /**
     * Load loan history based on the selected borrower or for all borrowers
     */
    private void loadLoanHistory() {
        List<Loan> loans;
        
        if (showAllBorrowers) {
            // Get the selected borrower from combo box
            String selectedItem = (String) borrowerComboBox.getSelectedItem();
            
            if (selectedItem.equals("All Borrowers")) {
                loans = loanController.getAllLoans();
            } else {
                String borrowerId = selectedItem.split(" - ")[0];
                loans = loanController.getLoanHistoryForBorrower(borrowerId);
            }
        } else {
            // Show only for specific borrower (borrowerId already set)
            loans = loanController.getLoanHistoryForBorrower(borrowerId);
        }
        
        populateTable(loans);
    }
    
    /**
     * Populate the table with loans
     * @param loans The list of loans
     */
    private void populateTable(List<Loan> loans) {
        tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Loan loan : loans) {
            String returnDate = loan.getReturnDate() == null ? "Not returned" : dateFormat.format(loan.getReturnDate());
            String status = loan.isReturned() ? "Returned" : loan.isOverdue() ? "Overdue" : "Active";
            String fine = loan.getFineAmount() > 0 ? String.format("$%.2f", loan.getFineAmount()) : "-";
            
            tableModel.addRow(new Object[]{
                    loan.getId(),
                    loan.getBookTitle(),
                    loan.getBorrowerName(),
                    dateFormat.format(loan.getCheckoutDate()),
                    dateFormat.format(loan.getDueDate()),
                    returnDate,
                    status,
                    fine
            });
        }
    }
    
    /**
     * Set the borrower ID for borrower-specific view
     * @param borrowerId The borrower ID
     */
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
    
    /**
     * Refresh data in the panel
     */
    public void refreshData() {
        loadLoanHistory();
    }
}
