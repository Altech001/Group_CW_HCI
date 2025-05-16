package controllers;

import models.*;
import utils.FileHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main controller class that handles the core functionality of the library system
 */
public class LibrarySystem {
    private static LibrarySystem instance;
    
    private List<Book> books;
    private List<User> users;
    private List<Loan> loans;
    private List<Fine> fines;
    
    private User currentUser;
    
    // Private constructor for singleton pattern
    private LibrarySystem() {
        loadData();
    }
    
    /**
     * Get the singleton instance
     * @return The LibrarySystem instance
     */
    public static LibrarySystem getInstance() {
        if (instance == null) {
            instance = new LibrarySystem();
        }
        return instance;
    }
    
    /**
     * Load all data from files
     */
    public void loadData() {
        // Create initial data if needed
        FileHandler.createInitialData();
        
        // Load data from files
        books = FileHandler.loadBooks();
        users = FileHandler.loadUsers();
        loans = FileHandler.loadLoans();
        fines = FileHandler.loadFines();
        
        // Initialize transient fields in loans
        updateTransientFields();
    }
    
    /**
     * Update transient fields in loans and fines
     */
    private void updateTransientFields() {
        for (Loan loan : loans) {
            // Set book title
            for (Book book : books) {
                if (book.getId().equals(loan.getBookId())) {
                    loan.setBookTitle(book.getTitle());
                    break;
                }
            }
            
            // Set borrower name
            for (User user : users) {
                if (user instanceof Borrower && user.getId().equals(loan.getBorrowerId())) {
                    loan.setBorrowerName(user.getFullName());
                    break;
                }
            }
        }
        
        for (Fine fine : fines) {
            // Set borrower name
            for (User user : users) {
                if (user instanceof Borrower && user.getId().equals(fine.getBorrowerId())) {
                    fine.setBorrowerName(user.getFullName());
                    break;
                }
            }
            
            // Set book title via loan
            for (Loan loan : loans) {
                if (loan.getId().equals(fine.getLoanId())) {
                    fine.setBookTitle(loan.getBookTitle());
                    break;
                }
            }
        }
    }
    
    /**
     * Save all data to files
     */
    public void saveData() {
        FileHandler.saveBooks(books);
        FileHandler.saveUsers(users);
        FileHandler.saveLoans(loans);
        FileHandler.saveFines(fines);
    }
    
    /**
     * Authenticate a user
     * @param username Username
     * @param password Password
     * @return The authenticated user or null if authentication fails
     */
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.checkCredentials(username, password)) {
                currentUser = user;
                return user;
            }
        }
        return null;
    }
    
    /**
     * Get the current logged-in user
     * @return The current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Log out the current user
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Get all books
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    /**
     * Get all borrowers
     * @return List of all borrowers
     */
    public List<Borrower> getAllBorrowers() {
        return users.stream()
                .filter(user -> user instanceof Borrower)
                .map(user -> (Borrower) user)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all loans
     * @return List of all loans
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }
    
    /**
     * Get all fines
     * @return List of all fines
     */
    public List<Fine> getAllFines() {
        return new ArrayList<>(fines);
    }
    
    /**
     * Get a book by ID
     * @param bookId Book ID
     * @return The book or null if not found
     */
    public Book getBookById(String bookId) {
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }
    
    /**
     * Get a user by ID
     * @param userId User ID
     * @return The user or null if not found
     */
    public User getUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Get a borrower by ID
     * @param borrowerId Borrower ID
     * @return The borrower or null if not found
     */
    public Borrower getBorrowerById(String borrowerId) {
        for (User user : users) {
            if (user instanceof Borrower && user.getId().equals(borrowerId)) {
                return (Borrower) user;
            }
        }
        return null;
    }
    
    /**
     * Get a loan by ID
     * @param loanId Loan ID
     * @return The loan or null if not found
     */
    public Loan getLoanById(String loanId) {
        for (Loan loan : loans) {
            if (loan.getId().equals(loanId)) {
                return loan;
            }
        }
        return null;
    }
    
    /**
     * Get a fine by ID
     * @param fineId Fine ID
     * @return The fine or null if not found
     */
    public Fine getFineById(String fineId) {
        for (Fine fine : fines) {
            if (fine.getId().equals(fineId)) {
                return fine;
            }
        }
        return null;
    }
    
    /**
     * Add a new book
     * @param book The book to add
     * @return true if adding was successful, false otherwise
     */
    public boolean addBook(Book book) {
        if (book == null) return false;
        
        books.add(book);
        saveData();
        return true;
    }
    
    /**
     * Update a book
     * @param book The book to update
     * @return true if updating was successful, false otherwise
     */
    public boolean updateBook(Book book) {
        if (book == null) return false;
        
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(book.getId())) {
                books.set(i, book);
                saveData();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Delete a book
     * @param bookId The ID of the book to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteBook(String bookId) {
        Book bookToRemove = null;
        
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                bookToRemove = book;
                break;
            }
        }
        
        if (bookToRemove != null) {
            // Check if the book is currently on loan
            for (Loan loan : loans) {
                if (loan.getBookId().equals(bookId) && !loan.isReturned()) {
                    return false; // Cannot delete a book that is currently on loan
                }
            }
            
            books.remove(bookToRemove);
            saveData();
            return true;
        }
        
        return false;
    }
    
    /**
     * Add a new user
     * @param user The user to add
     * @return true if adding was successful, false otherwise
     */
    public boolean addUser(User user) {
        if (user == null) return false;
        
        // Check if username already exists
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        
        users.add(user);
        saveData();
        return true;
    }
    
    /**
     * Update a user
     * @param user The user to update
     * @return true if updating was successful, false otherwise
     */
    public boolean updateUser(User user) {
        if (user == null) return false;
        
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                saveData();
                updateTransientFields();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Delete a user
     * @param userId The ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUser(String userId) {
        User userToRemove = null;
        
        for (User user : users) {
            if (user.getId().equals(userId)) {
                userToRemove = user;
                break;
            }
        }
        
        if (userToRemove != null) {
            // If it's a borrower, check if they have any active loans
            if (userToRemove instanceof Borrower) {
                for (Loan loan : loans) {
                    if (loan.getBorrowerId().equals(userId) && !loan.isReturned()) {
                        return false; // Cannot delete a borrower with active loans
                    }
                }
            }
            
            users.remove(userToRemove);
            saveData();
            return true;
        }
        
        return false;
    }
    
    /**
     * Create a new loan (checkout a book)
     * @param bookId The ID of the book to checkout
     * @param borrowerId The ID of the borrower
     * @param loanDurationDays The duration of the loan in days
     * @return The created loan or null if creation fails
     */
    public Loan createLoan(String bookId, String borrowerId, int loanDurationDays) {
        Book book = getBookById(bookId);
        Borrower borrower = getBorrowerById(borrowerId);
        
        if (book == null || borrower == null) {
            return null;
        }
        
        // Validation
        if (book.getAvailable() <= 0 || !borrower.canBorrowBooks()) {
            return null;
        }
        
        // Create the loan
        Date checkoutDate = new Date();
        Loan loan = new Loan(bookId, borrowerId, checkoutDate, loanDurationDays);
        
        // Update book and borrower
        book.decrementAvailable();
        borrower.addLoan(loan.getId());
        
        // Update transient fields
        loan.setBookTitle(book.getTitle());
        loan.setBorrowerName(borrower.getFullName());
        
        // Add to list and save
        loans.add(loan);
        updateBook(book);
        updateUser(borrower);
        saveData();
        
        return loan;
    }
    
    /**
     * Return a book (check in)
     * @param loanId The ID of the loan
     * @return true if check-in was successful, false otherwise
     */
    public boolean returnBook(String loanId) {
        Loan loan = getLoanById(loanId);
        
        if (loan == null || loan.isReturned()) {
            return false;
        }
        
        Book book = getBookById(loan.getBookId());
        Borrower borrower = getBorrowerById(loan.getBorrowerId());
        
        if (book == null || borrower == null) {
            return false;
        }
        
        // Update loan
        loan.setReturned(true);
        loan.setReturnDate(new Date());
        
        // Calculate if there's a fine
        double fineAmount = loan.calculateFine(0.5); // $0.50 per day
        if (fineAmount > 0) {
            Fine fine = new Fine(
                    loan.getId(),
                    loan.getBorrowerId(),
                    fineAmount,
                    "Overdue book: " + (loan.getBookTitle() != null ? loan.getBookTitle() : "Unknown")
            );
            fine.setBorrowerName(loan.getBorrowerName());
            fine.setBookTitle(loan.getBookTitle());
            fines.add(fine);
            
            // Set the fine amount on the loan as well
            loan.setFineAmount(fineAmount);
        }
        
        // Update book and borrower
        book.incrementAvailable();
        borrower.removeLoan(loan.getId());
        
        // Save all changes
        updateBook(book);
        updateUser(borrower);
        saveData();
        
        return true;
    }
    
    /**
     * Pay a fine
     * @param fineId The ID of the fine
     * @return true if payment was successful, false otherwise
     */
    public boolean payFine(String fineId) {
        Fine fine = getFineById(fineId);
        
        if (fine == null || fine.isPaid()) {
            return false;
        }
        
        fine.setPaid(true);
        
        // Also update the loan to reflect fine payment
        for (Loan loan : loans) {
            if (loan.getId().equals(fine.getLoanId())) {
                loan.setFinePaid(true);
                break;
            }
        }
        
        saveData();
        return true;
    }
    
    /**
     * Search books by title
     * @param title The title to search for
     * @return List of books matching the search criteria
     */
    public List<Book> searchBooksByTitle(String title) {
        return books.stream()
                .filter(book -> book.matchesTitle(title))
                .collect(Collectors.toList());
    }
    
    /**
     * Search books by author
     * @param author The author to search for
     * @return List of books matching the search criteria
     */
    public List<Book> searchBooksByAuthor(String author) {
        return books.stream()
                .filter(book -> book.matchesAuthor(author))
                .collect(Collectors.toList());
    }
    
    /**
     * Search books by subject
     * @param subject The subject to search for
     * @return List of books matching the search criteria
     */
    public List<Book> searchBooksBySubject(String subject) {
        return books.stream()
                .filter(book -> book.matchesSubject(subject))
                .collect(Collectors.toList());
    }
    
    /**
     * Get loan history for a borrower
     * @param borrowerId The ID of the borrower
     * @return List of loans for the borrower
     */
    public List<Loan> getLoanHistoryForBorrower(String borrowerId) {
        return loans.stream()
                .filter(loan -> loan.getBorrowerId().equals(borrowerId))
                .collect(Collectors.toList());
    }
    
    /**
     * Get active loans for a borrower
     * @param borrowerId The ID of the borrower
     * @return List of active loans for the borrower
     */
    public List<Loan> getActiveLoansForBorrower(String borrowerId) {
        return loans.stream()
                .filter(loan -> loan.getBorrowerId().equals(borrowerId) && !loan.isReturned())
                .collect(Collectors.toList());
    }
    
    /**
     * Get fines for a borrower
     * @param borrowerId The ID of the borrower
     * @return List of fines for the borrower
     */
    public List<Fine> getFinesForBorrower(String borrowerId) {
        return fines.stream()
                .filter(fine -> fine.getBorrowerId().equals(borrowerId))
                .collect(Collectors.toList());
    }
    
    /**
     * Get unpaid fines for a borrower
     * @param borrowerId The ID of the borrower
     * @return List of unpaid fines for the borrower
     */
    public List<Fine> getUnpaidFinesForBorrower(String borrowerId) {
        return fines.stream()
                .filter(fine -> fine.getBorrowerId().equals(borrowerId) && !fine.isPaid())
                .collect(Collectors.toList());
    }
}
