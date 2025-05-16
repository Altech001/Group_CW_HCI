package ui.components;

import controllers.BookController;
import controllers.LoanController;
import controllers.UserController;
import models.Book;
import models.Borrower;
import models.Loan;
import ui.LibraryUI;
import utils.Constants;
import utils.IconUtils;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Panel for managing loans (checkout, check-in)
 */
public class LoanManagementPanel extends JPanel {
    private LibraryUI parentFrame;
    private LoanController loanController;
    private BookController bookController;
    private UserController userController;
    
    private JTable loansTable;
    private DefaultTableModel tableModel;
    private JButton checkoutButton;
    private JButton checkinButton;
    private JButton refreshButton;
    
    /**
     * Constructor for LoanManagementPanel
     * @param parentFrame The parent frame
     */
    public LoanManagementPanel(LibraryUI parentFrame) {
        this.parentFrame = parentFrame;
        this.loanController = LoanController.getInstance();
        this.bookController = BookController.getInstance();
        this.userController = UserController.getInstance();
        
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
        
        JLabel titleLabel = IconUtils.createHeaderLabel("Loan Management");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create table panel
        JPanel tablePanel = createTablePanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
        
        // Create buttons
        checkoutButton = IconUtils.createStyledButton("Checkout Book", Constants.BOOK_ICON, Constants.BUTTON_SIZE);
        checkinButton = IconUtils.createStyledButton("Return Book", Constants.LOAN_ICON, Constants.BUTTON_SIZE);
        refreshButton = IconUtils.createStyledButton("Refresh", null, Constants.SMALL_BUTTON_SIZE);
        
        // Add listeners
        checkoutButton.addActionListener(e -> showCheckoutDialog());
        checkinButton.addActionListener(e -> checkinBook());
        refreshButton.addActionListener(e -> loadActiveLoans());
        
        // Add buttons to panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(checkinButton);
        
        return buttonPanel;
    }
    
    /**
     * Load active loans
     */
    private void loadActiveLoans() {
        List<Loan> loans = loanController.getAllLoans();
        
        tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Loan loan : loans) {
            // Show only active loans (not returned)
            if (!loan.isReturned()) {
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
    }
    
    /**
     * Show dialog to check out a book
     */
    private void showCheckoutDialog() {
        JDialog dialog = new JDialog(parentFrame, "Checkout Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form fields
        JLabel bookLabel = new JLabel("Select Book:");
        JComboBox<String> bookComboBox = new JComboBox<>();
        
        JLabel borrowerLabel = new JLabel("Select Borrower:");
        JComboBox<String> borrowerComboBox = new JComboBox<>();
        
        JLabel durationLabel = new JLabel("Loan Duration (days):");
        JTextField durationField = new JTextField(String.valueOf(Constants.LOAN_DURATION_DAYS), 10);
        
        // Populate book combo box
        for (Book book : bookController.getAllBooks()) {
            if (book.getAvailable() > 0) {
                bookComboBox.addItem(book.getId() + " - " + book.getTitle() + " (" + book.getAvailable() + " available)");
            }
        }
        
        // Populate borrower combo box
        for (Borrower borrower : userController.getAllBorrowers()) {
            if (borrower.canBorrowBooks()) {
                borrowerComboBox.addItem(borrower.getId() + " - " + borrower.getFullName() + 
                        " (" + borrower.getAvailableLoanSlots() + " slots available)");
            }
        }
        
        // Add components to form
        formPanel.add(bookLabel);
        formPanel.add(bookComboBox);
        formPanel.add(borrowerLabel);
        formPanel.add(borrowerComboBox);
        formPanel.add(durationLabel);
        formPanel.add(durationField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkoutButton = new JButton("Checkout");
        JButton cancelButton = new JButton("Cancel");
        
        checkoutButton.addActionListener(e -> {
            // Validate input
            if (bookComboBox.getSelectedIndex() == -1) {
                parentFrame.showError("Please select a book");
                return;
            }
            
            if (borrowerComboBox.getSelectedIndex() == -1) {
                parentFrame.showError("Please select a borrower");
                return;
            }
            
            String durationStr = durationField.getText().trim();
            if (!ValidationUtils.isValidPositiveInteger(durationStr)) {
                parentFrame.showError("Please enter a valid duration (positive number)");
                return;
            }
            
            // Get book ID and borrower ID from the combo boxes
            String bookIdWithDetails = (String) bookComboBox.getSelectedItem();
            String borrowerIdWithDetails = (String) borrowerComboBox.getSelectedItem();
            
            String bookId = bookIdWithDetails.split(" - ")[0];
            String borrowerId = borrowerIdWithDetails.split(" - ")[0];
            
            Book book = bookController.getBookById(bookId);
            Borrower borrower = userController.getBorrowerById(borrowerId);
            
            if (book == null || borrower == null) {
                parentFrame.showError("Invalid book or borrower selection");
                return;
            }
            
            // Validate checkout
            String validationError = ValidationUtils.validateCheckout(
                    book, borrower, loanController.getAllLoans());
            
            if (validationError != null) {
                parentFrame.showError(validationError);
                return;
            }
            
            // Create loan
            int duration = Integer.parseInt(durationStr);
            Loan loan = loanController.createLoan(bookId, borrowerId);
            
            if (loan != null) {
                parentFrame.showSuccess("Book checked out successfully!");
                dialog.dispose();
                loadActiveLoans(); // Refresh the table
            } else {
                parentFrame.showError("Failed to checkout book");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(checkoutButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Check in a book
     */
    private void checkinBook() {
        int selectedRow = loansTable.getSelectedRow();
        
        if (selectedRow == -1) {
            parentFrame.showError("Please select a loan to return");
            return;
        }
        
        String loanId = (String) tableModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) tableModel.getValueAt(selectedRow, 1);
        String borrowerName = (String) tableModel.getValueAt(selectedRow, 2);
        
        Loan loan = loanController.getLoanById(loanId);
        
        if (loan == null) {
            parentFrame.showError("Could not find the selected loan");
            return;
        }
        
        if (loan.isReturned()) {
            parentFrame.showError("This book has already been returned");
            return;
        }
        
        // Validate check-in
        String validationError = ValidationUtils.validateCheckin(loan);
        
        if (validationError != null) {
            parentFrame.showError(validationError);
            return;
        }
        
        // Confirm return
        boolean confirm = parentFrame.showConfirmation(
                "Are you sure you want to return the book: " + bookTitle + " from " + borrowerName + "?");
        
        if (confirm) {
            if (loanController.returnBook(loanId)) {
                parentFrame.showSuccess("Book returned successfully!");
                
                // Check if there's a fine
                if (loan.isOverdue()) {
                    double fineAmount = loan.calculateFine(Constants.FINE_PER_DAY);
                    parentFrame.showInfo("A fine of $" + String.format("%.2f", fineAmount) + 
                            " has been recorded for this overdue book.");
                }
                
                loadActiveLoans(); // Refresh the table
            } else {
                parentFrame.showError("Failed to return book");
            }
        }
    }
    
    /**
     * Refresh data in the panel
     */
    public void refreshData() {
        loadActiveLoans();
    }
}
