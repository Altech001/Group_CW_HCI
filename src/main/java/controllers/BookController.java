package controllers;

import models.Book;

import java.util.List;

/**
 * Controller for handling book operations
 */
public class BookController {
    private static BookController instance;
    private LibrarySystem librarySystem;
    
    private BookController() {
        librarySystem = LibrarySystem.getInstance();
    }
    
    /**
     * Get the singleton instance
     * @return The BookController instance
     */
    public static BookController getInstance() {
        if (instance == null) {
            instance = new BookController();
        }
        return instance;
    }
    
    /**
     * Get all books
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return librarySystem.getAllBooks();
    }
    
    /**
     * Get a book by ID
     * @param bookId Book ID
     * @return The book or null if not found
     */
    public Book getBookById(String bookId) {
        return librarySystem.getBookById(bookId);
    }
    
    /**
     * Add a new book
     * @param title Book title
     * @param author Book author
     * @param subject Book subject
     * @param quantity Book quantity
     * @return The created book or null if creation fails
     */
    public Book addBook(String title, String author, String subject, int quantity) {
        Book book = new Book(title, author, subject, quantity);
        boolean success = librarySystem.addBook(book);
        return success ? book : null;
    }
    
    /**
     * Update a book
     * @param book The book to update
     * @return true if updating was successful, false otherwise
     */
    public boolean updateBook(Book book) {
        return librarySystem.updateBook(book);
    }
    
    /**
     * Delete a book
     * @param bookId The ID of the book to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteBook(String bookId) {
        return librarySystem.deleteBook(bookId);
    }
    
    /**
     * Search books by title
     * @param title The title to search for
     * @return List of books matching the search criteria
     */
    public List<Book> searchBooksByTitle(String title) {
        return librarySystem.searchBooksByTitle(title);
    }
    
    /**
     * Search books by author
     * @param author The author to search for
     * @return List of books matching the search criteria
     */
    public List<Book> searchBooksByAuthor(String author) {
        return librarySystem.searchBooksByAuthor(author);
    }
    
    /**
     * Search books by subject
     * @param subject The subject to search for
     * @return List of books matching the search criteria
     */
    public List<Book> searchBooksBySubject(String subject) {
        return librarySystem.searchBooksBySubject(subject);
    }
    
    /**
     * Increase quantity of a book
     * @param bookId Book ID
     * @param amount Amount to increase by
     * @return true if operation was successful, false otherwise
     */
    public boolean increaseBookQuantity(String bookId, int amount) {
        Book book = librarySystem.getBookById(bookId);
        
        if (book == null || amount <= 0) {
            return false;
        }
        
        int newQuantity = book.getQuantity() + amount;
        book.setQuantity(newQuantity);
        
        return librarySystem.updateBook(book);
    }
    
    /**
     * Decrease quantity of a book
     * @param bookId Book ID
     * @param amount Amount to decrease by
     * @return true if operation was successful, false otherwise
     */
    public boolean decreaseBookQuantity(String bookId, int amount) {
        Book book = librarySystem.getBookById(bookId);
        
        if (book == null || amount <= 0) {
            return false;
        }
        
        int newQuantity = book.getQuantity() - amount;
        
        if (newQuantity < 0) {
            return false;
        }
        
        book.setQuantity(newQuantity);
        
        return librarySystem.updateBook(book);
    }
}
