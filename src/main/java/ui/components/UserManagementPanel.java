package ui.components;

import controllers.AuthController;
import controllers.UserController;
import models.Borrower;
import models.User;
import ui.LibraryUI;
import utils.Constants;
import utils.IconUtils;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for managing users (add, edit borrowers)
 */
public class UserManagementPanel extends JPanel {
    private LibraryUI parentFrame;
    private UserController userController;
    private AuthController authController;
    
    private JTable borrowersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    
    /**
     * Constructor for UserManagementPanel
     * @param parentFrame The parent frame
     */
    public UserManagementPanel(LibraryUI parentFrame) {
        this.parentFrame = parentFrame;
        this.userController = UserController.getInstance();
        this.authController = AuthController.getInstance();
        
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
        
        JLabel titleLabel = IconUtils.createHeaderLabel("Borrower Management");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        
        // Create table panel
        JPanel tablePanel = createTablePanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the search panel
     * @return The search panel
     */
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Constants.BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel searchLabel = new JLabel("Search by name:");
        searchLabel.setFont(Constants.REGULAR_FONT);
        
        searchField = new JTextField(20);
        searchField.setFont(Constants.REGULAR_FONT);
        
        searchButton = IconUtils.createStyledButton("Search", Constants.SEARCH_ICON, Constants.SMALL_BUTTON_SIZE);
        searchButton.addActionListener(e -> searchBorrowers());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        return searchPanel;
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
        tableModel.addColumn("Library Card");
        tableModel.addColumn("Name");
        tableModel.addColumn("Username");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Max Books");
        tableModel.addColumn("Current Loans");
        
        // Create table
        borrowersTable = new JTable(tableModel);
        IconUtils.styleTable(borrowersTable);
        
        // Hide the ID column as it's only for internal use
        borrowersTable.getColumnModel().getColumn(0).setMinWidth(0);
        borrowersTable.getColumnModel().getColumn(0).setMaxWidth(0);
        borrowersTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Create scroll pane
        JScrollPane scrollPane = IconUtils.createStyledScrollPane(borrowersTable);
        
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
        addButton = IconUtils.createStyledButton("Add Borrower", Constants.ADD_ICON, Constants.BUTTON_SIZE);
        editButton = IconUtils.createStyledButton("Edit Borrower", Constants.EDIT_ICON, Constants.BUTTON_SIZE);
        
        // Add listeners
        addButton.addActionListener(e -> showAddBorrowerDialog());
        editButton.addActionListener(e -> showEditBorrowerDialog());
        
        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        
        return buttonPanel;
    }
    
    /**
     * Load all borrowers
     */
    private void loadAllBorrowers() {
        List<Borrower> borrowers = userController.getAllBorrowers();
        populateTable(borrowers);
    }
    
    /**
     * Search borrowers by name
     */
    private void searchBorrowers() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            loadAllBorrowers();
            return;
        }
        
        List<Borrower> borrowers = userController.searchBorrowersByName(searchText);
        populateTable(borrowers);
    }
    
    /**
     * Populate the table with borrowers
     * @param borrowers The list of borrowers
     */
    private void populateTable(List<Borrower> borrowers) {
        tableModel.setRowCount(0);
        
        for (Borrower borrower : borrowers) {
            tableModel.addRow(new Object[]{
                    borrower.getId(),
                    borrower.getBorrowerId(),
                    borrower.getFullName(),
                    borrower.getUsername(),
                    borrower.getEmail(),
                    borrower.getPhone(),
                    borrower.getMaxBooksAllowed(),
                    borrower.getCurrentLoans().size()
            });
        }
    }
    
    /**
     * Show dialog to add a new borrower
     */
    private void showAddBorrowerDialog() {
        JDialog dialog = new JDialog(parentFrame, "Add New Borrower", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parentFrame);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form fields
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        
        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(20);
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        
        JLabel phoneLabel = new JLabel("Phone (XXX-XXX-XXXX):");
        JTextField phoneField = new JTextField(20);
        
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(20);
        
        JLabel libraryCardLabel = new JLabel("Library Card Number:");
        JTextField libraryCardField = new JTextField(20);
        
        JLabel maxBooksLabel = new JLabel("Max Books Allowed:");
        JTextField maxBooksField = new JTextField("5", 20);
        
        // Add components to form
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        formPanel.add(libraryCardLabel);
        formPanel.add(libraryCardField);
        formPanel.add(maxBooksLabel);
        formPanel.add(maxBooksField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate input
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String fullName = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                String libraryCard = libraryCardField.getText().trim();
                String maxBooksStr = maxBooksField.getText().trim();
                
                if (!ValidationUtils.isValidString(username)) {
                    parentFrame.showError("Please enter a valid username");
                    return;
                }
                
                if (!ValidationUtils.isValidString(password)) {
                    parentFrame.showError("Please enter a valid password");
                    return;
                }
                
                if (!ValidationUtils.isValidString(fullName)) {
                    parentFrame.showError("Please enter a valid name");
                    return;
                }
                
                if (!ValidationUtils.isValidEmail(email)) {
                    parentFrame.showError("Please enter a valid email address");
                    return;
                }
                
                if (!ValidationUtils.isValidPhone(phone)) {
                    parentFrame.showError("Please enter a valid phone number in format XXX-XXX-XXXX");
                    return;
                }
                
                if (!ValidationUtils.isValidString(address)) {
                    parentFrame.showError("Please enter a valid address");
                    return;
                }
                
                if (!ValidationUtils.isValidString(libraryCard)) {
                    parentFrame.showError("Please enter a valid library card number");
                    return;
                }
                
                if (!ValidationUtils.isValidPositiveInteger(maxBooksStr)) {
                    parentFrame.showError("Please enter a valid number for max books allowed");
                    return;
                }
                
                // Add borrower
                Borrower newBorrower = userController.addBorrower(
                        username, password, fullName, email, phone, address, libraryCard);
                
                if (newBorrower != null) {
                    // Set max books allowed
                    newBorrower.setMaxBooksAllowed(Integer.parseInt(maxBooksStr));
                    userController.updateUser(newBorrower);
                    
                    parentFrame.showSuccess("Borrower added successfully!");
                    dialog.dispose();
                    loadAllBorrowers(); // Refresh the table
                } else {
                    parentFrame.showError("Failed to add borrower. Username may already exist.");
                }
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Show dialog to edit a borrower
     */
    private void showEditBorrowerDialog() {
        int selectedRow = borrowersTable.getSelectedRow();
        
        if (selectedRow == -1) {
            parentFrame.showError("Please select a borrower to edit");
            return;
        }
        
        String borrowerId = (String) tableModel.getValueAt(selectedRow, 0);
        Borrower selectedBorrower = userController.getBorrowerById(borrowerId);
        
        if (selectedBorrower == null) {
            parentFrame.showError("Could not find the selected borrower");
            return;
        }
        
        JDialog dialog = new JDialog(parentFrame, "Edit Borrower", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parentFrame);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form fields - not allowing username change
        JLabel usernameLabel = new JLabel("Username (cannot change):");
        JTextField usernameField = new JTextField(selectedBorrower.getUsername(), 20);
        usernameField.setEditable(false);
        
        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(selectedBorrower.getFullName(), 20);
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(selectedBorrower.getEmail(), 20);
        
        JLabel phoneLabel = new JLabel("Phone (XXX-XXX-XXXX):");
        JTextField phoneField = new JTextField(selectedBorrower.getPhone(), 20);
        
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(selectedBorrower.getAddress(), 20);
        
        JLabel libraryCardLabel = new JLabel("Library Card Number:");
        JTextField libraryCardField = new JTextField(selectedBorrower.getBorrowerId(), 20);
        
        JLabel maxBooksLabel = new JLabel("Max Books Allowed:");
        JTextField maxBooksField = new JTextField(String.valueOf(selectedBorrower.getMaxBooksAllowed()), 20);
        
        // Add components to form
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        formPanel.add(libraryCardLabel);
        formPanel.add(libraryCardField);
        formPanel.add(maxBooksLabel);
        formPanel.add(maxBooksField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate input
                String fullName = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                String libraryCard = libraryCardField.getText().trim();
                String maxBooksStr = maxBooksField.getText().trim();
                
                if (!ValidationUtils.isValidString(fullName)) {
                    parentFrame.showError("Please enter a valid name");
                    return;
                }
                
                if (!ValidationUtils.isValidEmail(email)) {
                    parentFrame.showError("Please enter a valid email address");
                    return;
                }
                
                if (!ValidationUtils.isValidPhone(phone)) {
                    parentFrame.showError("Please enter a valid phone number in format XXX-XXX-XXXX");
                    return;
                }
                
                if (!ValidationUtils.isValidString(address)) {
                    parentFrame.showError("Please enter a valid address");
                    return;
                }
                
                if (!ValidationUtils.isValidString(libraryCard)) {
                    parentFrame.showError("Please enter a valid library card number");
                    return;
                }
                
                if (!ValidationUtils.isValidPositiveInteger(maxBooksStr)) {
                    parentFrame.showError("Please enter a valid number for max books allowed");
                    return;
                }
                
                // Update borrower
                selectedBorrower.setFullName(fullName);
                selectedBorrower.setEmail(email);
                selectedBorrower.setPhone(phone);
                selectedBorrower.setAddress(address);
                selectedBorrower.setBorrowerId(libraryCard);
                selectedBorrower.setMaxBooksAllowed(Integer.parseInt(maxBooksStr));
                
                if (userController.updateUser(selectedBorrower)) {
                    parentFrame.showSuccess("Borrower updated successfully!");
                    dialog.dispose();
                    loadAllBorrowers(); // Refresh the table
                } else {
                    parentFrame.showError("Failed to update borrower");
                }
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Refresh data in the panel
     */
    public void refreshData() {
        loadAllBorrowers();
    }
}
