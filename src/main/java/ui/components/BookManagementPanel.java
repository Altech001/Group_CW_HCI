package ui.components;

import controllers.AuthController;
import controllers.BookController;
import models.Book;
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
 * Panel for managing books (add, edit, delete)
 */
public class BookManagementPanel extends JPanel {
    private LibraryUI parentFrame;
    private BookController bookController;
    private AuthController authController;
    
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton increaseQuantityButton;
    private JButton decreaseQuantityButton;
    
    private boolean canManageBooks;
    
    /**
     * Constructor for BookManagementPanel
     * @param parentFrame The parent frame
     * @param canManageBooks Whether the user can manage books
     */
    public BookManagementPanel(LibraryUI parentFrame, boolean canManageBooks) {
        this.parentFrame = parentFrame;
        this.bookController = BookController.getInstance();
        this.authController = AuthController.getInstance();
        this.canManageBooks = canManageBooks;
        
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
        
        JLabel titleLabel = IconUtils.createHeaderLabel("Book Management");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create table
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
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Subject");
        tableModel.addColumn("Total Qty");
        tableModel.addColumn("Available");
        
        // Create table
        booksTable = new JTable(tableModel);
        IconUtils.styleTable(booksTable);
        
        // Hide the ID column as it's only for internal use
        booksTable.getColumnModel().getColumn(0).setMinWidth(0);
        booksTable.getColumnModel().getColumn(0).setMaxWidth(0);
        booksTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Create scroll pane
        JScrollPane scrollPane = IconUtils.createStyledScrollPane(booksTable);
        
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
        
        if (canManageBooks) {
            // Create buttons
            addButton = IconUtils.createStyledButton("Add Book", Constants.ADD_ICON, Constants.BUTTON_SIZE);
            editButton = IconUtils.createStyledButton("Edit Book", Constants.EDIT_ICON, Constants.BUTTON_SIZE);
            deleteButton = IconUtils.createStyledButton("Delete Book", Constants.DELETE_ICON, Constants.BUTTON_SIZE);
            increaseQuantityButton = IconUtils.createStyledButton("Increase Qty", Constants.ADD_ICON, Constants.BUTTON_SIZE);
            decreaseQuantityButton = IconUtils.createStyledButton("Decrease Qty", Constants.DELETE_ICON, Constants.BUTTON_SIZE);
            
            // Add listeners
            addButton.addActionListener(e -> showAddBookDialog());
            editButton.addActionListener(e -> showEditBookDialog());
            deleteButton.addActionListener(e -> deleteBook());
            increaseQuantityButton.addActionListener(e -> changeBookQuantity(true));
            decreaseQuantityButton.addActionListener(e -> changeBookQuantity(false));
            
            // Add buttons to panel
            buttonPanel.add(addButton);
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(increaseQuantityButton);
            buttonPanel.add(decreaseQuantityButton);
        } else {
            JLabel noAccessLabel = new JLabel("You don't have permission to manage books");
            noAccessLabel.setFont(Constants.REGULAR_FONT);
            noAccessLabel.setForeground(Constants.SECONDARY_TEXT_COLOR);
            buttonPanel.add(noAccessLabel);
        }
        
        return buttonPanel;
    }
    
    /**
     * Load all books
     */
    private void loadAllBooks() {
        List<Book> books = bookController.getAllBooks();
        populateTable(books);
    }
    
    /**
     * Populate the table with books
     * @param books The list of books
     */
    private void populateTable(List<Book> books) {
        tableModel.setRowCount(0);
        
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getSubject(),
                    book.getQuantity(),
                    book.getAvailable()
            });
        }
    }
    
    /**
     * Show dialog to add a new book
     */
    private void showAddBookDialog() {
        JDialog dialog = new JDialog(parentFrame, "Add New Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form fields
        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(20);
        
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField(20);
        
        JLabel subjectLabel = new JLabel("Subject:");
        JTextField subjectField = new JTextField(20);
        
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(20);
        
        // Add components to form
        formPanel.add(titleLabel);
        formPanel.add(titleField);
        formPanel.add(authorLabel);
        formPanel.add(authorField);
        formPanel.add(subjectLabel);
        formPanel.add(subjectField);
        formPanel.add(quantityLabel);
        formPanel.add(quantityField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate input
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String subject = subjectField.getText().trim();
                String quantityStr = quantityField.getText().trim();
                
                if (!ValidationUtils.isValidString(title)) {
                    parentFrame.showError("Please enter a valid title");
                    return;
                }
                
                if (!ValidationUtils.isValidString(author)) {
                    parentFrame.showError("Please enter a valid author");
                    return;
                }
                
                if (!ValidationUtils.isValidString(subject)) {
                    parentFrame.showError("Please enter a valid subject");
                    return;
                }
                
                if (!ValidationUtils.isValidPositiveInteger(quantityStr)) {
                    parentFrame.showError("Please enter a valid quantity (positive number)");
                    return;
                }
                
                int quantity = Integer.parseInt(quantityStr);
                
                // Add book
                Book newBook = bookController.addBook(title, author, subject, quantity);
                
                if (newBook != null) {
                    parentFrame.showSuccess("Book added successfully!");
                    dialog.dispose();
                    loadAllBooks(); // Refresh the table
                } else {
                    parentFrame.showError("Failed to add book");
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
     * Show dialog to edit a book
     */
    private void showEditBookDialog() {
        int selectedRow = booksTable.getSelectedRow();
        
        if (selectedRow == -1) {
            parentFrame.showError("Please select a book to edit");
            return;
        }
        
        String bookId = (String) tableModel.getValueAt(selectedRow, 0);
        Book selectedBook = bookController.getBookById(bookId);
        
        if (selectedBook == null) {
            parentFrame.showError("Could not find the selected book");
            return;
        }
        
        JDialog dialog = new JDialog(parentFrame, "Edit Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form fields
        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(selectedBook.getTitle(), 20);
        
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField(selectedBook.getAuthor(), 20);
        
        JLabel subjectLabel = new JLabel("Subject:");
        JTextField subjectField = new JTextField(selectedBook.getSubject(), 20);
        
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(String.valueOf(selectedBook.getQuantity()), 20);
        
        // Add components to form
        formPanel.add(titleLabel);
        formPanel.add(titleField);
        formPanel.add(authorLabel);
        formPanel.add(authorField);
        formPanel.add(subjectLabel);
        formPanel.add(subjectField);
        formPanel.add(quantityLabel);
        formPanel.add(quantityField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate input
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String subject = subjectField.getText().trim();
                String quantityStr = quantityField.getText().trim();
                
                if (!ValidationUtils.isValidString(title)) {
                    parentFrame.showError("Please enter a valid title");
                    return;
                }
                
                if (!ValidationUtils.isValidString(author)) {
                    parentFrame.showError("Please enter a valid author");
                    return;
                }
                
                if (!ValidationUtils.isValidString(subject)) {
                    parentFrame.showError("Please enter a valid subject");
                    return;
                }
                
                if (!ValidationUtils.isValidPositiveInteger(quantityStr)) {
                    parentFrame.showError("Please enter a valid quantity (positive number)");
                    return;
                }
                
                int quantity = Integer.parseInt(quantityStr);
                
                // Update book
                selectedBook.setTitle(title);
                selectedBook.setAuthor(author);
                selectedBook.setSubject(subject);
                selectedBook.setQuantity(quantity);
                
                if (bookController.updateBook(selectedBook)) {
                    parentFrame.showSuccess("Book updated successfully!");
                    dialog.dispose();
                    loadAllBooks(); // Refresh the table
                } else {
                    parentFrame.showError("Failed to update book");
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
     * Delete a book
     */
    private void deleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        
        if (selectedRow == -1) {
            parentFrame.showError("Please select a book to delete");
            return;
        }
        
        String bookId = (String) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Confirm deletion
        boolean confirm = parentFrame.showConfirmation(
                "Are you sure you want to delete the book: " + title + "?");
        
        if (confirm) {
            if (bookController.deleteBook(bookId)) {
                parentFrame.showSuccess("Book deleted successfully!");
                loadAllBooks(); // Refresh the table
            } else {
                parentFrame.showError("Failed to delete book. It may be currently on loan.");
            }
        }
    }
    
    /**
     * Change the quantity of a book
     * @param increase True to increase, false to decrease
     */
    private void changeBookQuantity(boolean increase) {
        int selectedRow = booksTable.getSelectedRow();
        
        if (selectedRow == -1) {
            parentFrame.showError("Please select a book to update quantity");
            return;
        }
        
        String bookId = (String) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Ask for quantity
        String action = increase ? "increase" : "decrease";
        String input = JOptionPane.showInputDialog(parentFrame, 
                "Enter the amount to " + action + " for book: " + title);
        
        if (input == null) {
            return; // User canceled
        }
        
        if (!ValidationUtils.isValidPositiveInteger(input)) {
            parentFrame.showError("Please enter a valid positive number");
            return;
        }
        
        int amount = Integer.parseInt(input);
        boolean success;
        
        if (increase) {
            success = bookController.increaseBookQuantity(bookId, amount);
        } else {
            success = bookController.decreaseBookQuantity(bookId, amount);
        }
        
        if (success) {
            parentFrame.showSuccess("Book quantity updated successfully!");
            loadAllBooks(); // Refresh the table
        } else {
            parentFrame.showError("Failed to update book quantity. Make sure it doesn't go below zero or the number of books currently on loan.");
        }
    }
    
    /**
     * Refresh data in the panel
     */
    public void refreshData() {
        loadAllBooks();
    }
}
