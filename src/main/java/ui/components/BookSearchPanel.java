package ui.components;

import controllers.AuthController;
import controllers.BookController;
import controllers.LoanController;
import models.Book;
import models.Borrower;
import ui.LibraryUI;
import utils.Constants;
import utils.IconUtils;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for searching books
 */
public class BookSearchPanel extends JPanel {
    private LibraryUI parentFrame;
    private BookController bookController;
    private LoanController loanController;
    private AuthController authController;
    
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JButton borrowButton;
    
    private boolean enableBorrow;
    
    /**
     * Constructor for BookSearchPanel
     * @param parentFrame The parent frame
     */
    public BookSearchPanel(LibraryUI parentFrame) {
        this(parentFrame, false);
    }
    
    /**
     * Constructor for BookSearchPanel
     * @param parentFrame The parent frame
     * @param enableBorrow Whether to enable the borrow functionality
     */
    public BookSearchPanel(LibraryUI parentFrame, boolean enableBorrow) {
        this.parentFrame = parentFrame;
        this.bookController = BookController.getInstance();
        this.loanController = LoanController.getInstance();
        this.authController = AuthController.getInstance();
        this.enableBorrow = enableBorrow;
        
        setupUI();
    }
    
    /**
     * Set up the UI components
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        
        // Create table
        JPanel tablePanel = createTablePanel();
        
        // Add to main panel
        add(searchPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }
    
    /**
     * Create the search panel
     * @return The search panel
     */
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Constants.BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel searchLabel = new JLabel("Search by:");
        searchLabel.setFont(Constants.REGULAR_FONT);
        
        // Create search type combo box
        searchTypeComboBox = new JComboBox<>(new String[]{"Title", "Author", "Subject"});
        searchTypeComboBox.setFont(Constants.REGULAR_FONT);
        
        // Create search field
        searchField = new JTextField(30);
        searchField.setFont(Constants.REGULAR_FONT);
        
        // Create search button
        searchButton = IconUtils.createStyledButton("Search", Constants.SEARCH_ICON, Constants.SMALL_BUTTON_SIZE);
        searchButton.addActionListener(e -> searchBooks());
        
        // Add components to search panel
        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeComboBox);
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
        
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Subject");
        tableModel.addColumn("Total Qty");
        tableModel.addColumn("Available");
        
        // Create table
        booksTable = new JTable(tableModel);
        IconUtils.styleTable(booksTable);
        
        // Create scroll pane
        JScrollPane scrollPane = IconUtils.createStyledScrollPane(booksTable);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Constants.SECONDARY_BACKGROUND);
        
        if (enableBorrow) {
            borrowButton = IconUtils.createStyledButton("Borrow Book", Constants.LOAN_ICON, Constants.BUTTON_SIZE);
            borrowButton.addActionListener(e -> borrowBook());
            buttonPanel.add(borrowButton);
        }
        
        // Add to table panel
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    /**
     * Search books based on the selected criteria
     */
    private void searchBooks() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            loadAllBooks();
            return;
        }
        
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        List<Book> books;
        
        switch (searchType) {
            case "Title":
                books = bookController.searchBooksByTitle(searchText);
                break;
            case "Author":
                books = bookController.searchBooksByAuthor(searchText);
                break;
            case "Subject":
                books = bookController.searchBooksBySubject(searchText);
                break;
            default:
                books = bookController.getAllBooks();
        }
        
        populateTable(books);
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
                    book.getTitle(),
                    book.getAuthor(),
                    book.getSubject(),
                    book.getQuantity(),
                    book.getAvailable()
            });
        }
    }
    
    /**
     * Borrow a book
     */
    private void borrowBook() {
        int selectedRow = booksTable.getSelectedRow();
        
        if (selectedRow == -1) {
            parentFrame.showError("Please select a book to borrow");
            return;
        }
        
        String title = (String) tableModel.getValueAt(selectedRow, 0);
        String author = (String) tableModel.getValueAt(selectedRow, 1);
        int available = (int) tableModel.getValueAt(selectedRow, 4);
        
        if (available <= 0) {
            parentFrame.showError("This book is not available for borrowing");
            return;
        }
        
        // Find the book by title and author
        List<Book> books = bookController.getAllBooks();
        Book selectedBook = null;
        
        for (Book book : books) {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
                selectedBook = book;
                break;
            }
        }
        
        if (selectedBook == null) {
            parentFrame.showError("Book not found");
            return;
        }
        
        // Get the current borrower
        Borrower borrower = (Borrower) authController.getCurrentUser();
        
        if (!borrower.canBorrowBooks()) {
            parentFrame.showError("You have reached your borrowing limit");
            return;
        }
        
        // Create the loan
        String validationError = ValidationUtils.validateCheckout(
                selectedBook, borrower, loanController.getAllLoans());
        
        if (validationError != null) {
            parentFrame.showError(validationError);
            return;
        }
        
        if (loanController.createLoan(selectedBook.getId(), borrower.getId()) != null) {
            parentFrame.showSuccess("Book borrowed successfully");
            refreshData(); // Refresh the table
        } else {
            parentFrame.showError("Failed to borrow book");
        }
    }
    
    /**
     * Refresh data in the panel
     */
    public void refreshData() {
        loadAllBooks();
    }
}
