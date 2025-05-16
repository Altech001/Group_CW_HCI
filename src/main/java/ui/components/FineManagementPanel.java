package ui.components;

import controllers.LoanController;
import controllers.UserController;
import models.Fine;
import models.Loan;
import ui.LibraryUI;
import utils.Constants;
import utils.IconUtils;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel for managing fines
 */
public class FineManagementPanel extends JPanel {
    private LibraryUI parentFrame;
    private LoanController loanController;
    private UserController userController;
    
    private JTable finesTable;
    private DefaultTableModel tableModel;
    private JButton recordFineButton;
    private JButton payFineButton;
    private JButton refreshButton;
    private JComboBox<String> filterComboBox;
    
    /**
     * Constructor for FineManagementPanel
     * @param parentFrame The parent frame
     */
    public FineManagementPanel(LibraryUI parentFrame) {
        this.parentFrame = parentFrame;
        this.loanController = LoanController.getInstance();
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
        
        JLabel titleLabel = IconUtils.createHeaderLabel("Fine Management");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create filter panel
        JPanel filterPanel = createFilterPanel();
        
        // Create table panel
        JPanel tablePanel = createTablePanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.NORTH);
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
        
        JLabel filterLabel = new JLabel("Show:");
        filterLabel.setFont(Constants.REGULAR_FONT);
        
        filterComboBox = new JComboBox<>(new String[]{"All Fines", "Unpaid Fines", "Paid Fines"});
        filterComboBox.setFont(Constants.REGULAR_FONT);
        filterComboBox.addActionListener(e -> loadFines());
        
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        
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
        tableModel.addColumn("Borrower");
        tableModel.addColumn("Book");
        tableModel.addColumn("Fine Amount");
        tableModel.addColumn("Reason");
        tableModel.addColumn("Issue Date");
        tableModel.addColumn("Status");
        tableModel.addColumn("Payment Date");
        
        // Create table
        finesTable = new JTable(tableModel);
        IconUtils.styleTable(finesTable);
        
        // Hide the ID column as it's only for internal use
        finesTable.getColumnModel().getColumn(0).setMinWidth(0);
        finesTable.getColumnModel().getColumn(0).setMaxWidth(0);
        finesTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Create scroll pane
        JScrollPane scrollPane = IconUtils.createStyledScrollPane(finesTable);
        
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
        recordFineButton = IconUtils.createStyledButton("Record Fine", Constants.FINE_ICON, Constants.BUTTON_SIZE);
        payFineButton = IconUtils.createStyledButton("Pay Fine", null, Constants.BUTTON_SIZE);
        refreshButton = IconUtils.createStyledButton("Refresh", null, Constants.SMALL_BUTTON_SIZE);
        
        // Add listeners
        recordFineButton.addActionListener(e -> showRecordFineDialog());
        payFineButton.addActionListener(e -> payFine());
        refreshButton.addActionListener(e -> loadFines());
        
        // Add buttons to panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(recordFineButton);
        buttonPanel.add(payFineButton);
        
        return buttonPanel;
    }
    
    /**
     * Load fines based on the selected filter
     */
    private void loadFines() {
        List<Fine> fines;
        String filter = (String) filterComboBox.getSelectedItem();
        
        if (filter.equals("Unpaid Fines")) {
            fines = loanController.getAllFines().stream()
                    .filter(fine -> !fine.isPaid())
                    .toList();
        } else if (filter.equals("Paid Fines")) {
            fines = loanController.getAllFines().stream()
                    .filter(Fine::isPaid)
                    .toList();
        } else {
            fines = loanController.getAllFines();
        }
        
        tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Fine fine : fines) {
            String status = fine.isPaid() ? "Paid" : "Unpaid";
            String paymentDate = fine.getPaymentDate() == null ? "-" : dateFormat.format(fine.getPaymentDate());
            
            tableModel.addRow(new Object[]{
                    fine.getId(),
                    fine.getBorrowerName(),
                    fine.getBookTitle(),
                    String.format("$%.2f", fine.getAmount()),
                    fine.getReason(),
                    dateFormat.format(fine.getIssueDate()),
                    status,
                    paymentDate
            });
        }
    }
    
    /**
     * Show dialog to record a fine
     */
    private void showRecordFineDialog() {
        JDialog dialog = new JDialog(parentFrame, "Record Fine", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form fields
        JLabel loanLabel = new JLabel("Select Loan:");
        JComboBox<String> loanComboBox = new JComboBox<>();
        
        JLabel amountLabel = new JLabel("Fine Amount ($):");
        JTextField amountField = new JTextField(10);
        
        JLabel reasonLabel = new JLabel("Reason:");
        JTextField reasonField = new JTextField(20);
        
        // Populate loan combo box
        for (Loan loan : loanController.getAllLoans()) {
            loanComboBox.addItem(loan.getId() + " - " + loan.getBookTitle() + " - " + loan.getBorrowerName());
        }
        
        // Add components to form
        formPanel.add(loanLabel);
        formPanel.add(loanComboBox);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(reasonLabel);
        formPanel.add(reasonField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton recordButton = new JButton("Record Fine");
        JButton cancelButton = new JButton("Cancel");
        
        recordButton.addActionListener(e -> {
            // Validate input
            if (loanComboBox.getSelectedIndex() == -1) {
                parentFrame.showError("Please select a loan");
                return;
            }
            
            String amountStr = amountField.getText().trim();
            if (amountStr.isEmpty()) {
                parentFrame.showError("Please enter a fine amount");
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    parentFrame.showError("Fine amount must be positive");
                    return;
                }
            } catch (NumberFormatException ex) {
                parentFrame.showError("Please enter a valid amount");
                return;
            }
            
            String reason = reasonField.getText().trim();
            if (reason.isEmpty()) {
                parentFrame.showError("Please enter a reason for the fine");
                return;
            }
            
            // Get loan ID from the combo box
            String loanIdWithDetails = (String) loanComboBox.getSelectedItem();
            String loanId = loanIdWithDetails.split(" - ")[0];
            
            // Record fine
            Fine fine = loanController.recordFine(loanId, amount, reason);
            
            if (fine != null) {
                parentFrame.showSuccess("Fine recorded successfully!");
                dialog.dispose();
                loadFines(); // Refresh the table
            } else {
                parentFrame.showError("Failed to record fine");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(recordButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Pay a fine
     */
    private void payFine() {
        int selectedRow = finesTable.getSelectedRow();
        
        if (selectedRow == -1) {
            parentFrame.showError("Please select a fine to pay");
            return;
        }
        
        String fineId = (String) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 6);
        
        if (status.equals("Paid")) {
            parentFrame.showError("This fine has already been paid");
            return;
        }
        
        String borrowerName = (String) tableModel.getValueAt(selectedRow, 1);
        String fineAmount = (String) tableModel.getValueAt(selectedRow, 3);
        
        // Confirm payment
        boolean confirm = parentFrame.showConfirmation(
                "Mark fine " + fineAmount + " for " + borrowerName + " as paid?");
        
        if (confirm) {
            if (loanController.payFine(fineId)) {
                parentFrame.showSuccess("Fine marked as paid successfully!");
                loadFines(); // Refresh the table
            } else {
                parentFrame.showError("Failed to pay fine");
            }
        }
    }
    
    /**
     * Refresh data in the panel
     */
    public void refreshData() {
        loadFines();
    }
}
