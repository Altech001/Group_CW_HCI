package controllers;

import models.Loan;
import models.Fine;
import utils.Constants;

import java.util.Date;
import java.util.List;

/**
 * Controller for handling loan operations
 */
public class LoanController {
    private static LoanController instance;
    private LibrarySystem librarySystem;
    
    private LoanController() {
        librarySystem = LibrarySystem.getInstance();
    }
    
    /**
     * Get the singleton instance
     * @return The LoanController instance
     */
    public static LoanController getInstance() {
        if (instance == null) {
            instance = new LoanController();
        }
        return instance;
    }
    
    /**
     * Get all loans
     * @return List of all loans
     */
    public List<Loan> getAllLoans() {
        return librarySystem.getAllLoans();
    }
    
    /**
     * Get a loan by ID
     * @param loanId Loan ID
     * @return The loan or null if not found
     */
    public Loan getLoanById(String loanId) {
        return librarySystem.getLoanById(loanId);
    }
    
    /**
     * Create a new loan (checkout a book)
     * @param bookId The ID of the book to checkout
     * @param borrowerId The ID of the borrower
     * @return The created loan or null if creation fails
     */
    public Loan createLoan(String bookId, String borrowerId) {
        return librarySystem.createLoan(bookId, borrowerId, Constants.LOAN_DURATION_DAYS);
    }
    
    /**
     * Return a book (check in)
     * @param loanId The ID of the loan
     * @return true if check-in was successful, false otherwise
     */
    public boolean returnBook(String loanId) {
        return librarySystem.returnBook(loanId);
    }
    
    /**
     * Get loan history for a borrower
     * @param borrowerId The ID of the borrower
     * @return List of loans for the borrower
     */
    public List<Loan> getLoanHistoryForBorrower(String borrowerId) {
        return librarySystem.getLoanHistoryForBorrower(borrowerId);
    }
    
    /**
     * Get active loans for a borrower
     * @param borrowerId The ID of the borrower
     * @return List of active loans for the borrower
     */
    public List<Loan> getActiveLoansForBorrower(String borrowerId) {
        return librarySystem.getActiveLoansForBorrower(borrowerId);
    }
    
    /**
     * Get all fines
     * @return List of all fines
     */
    public List<Fine> getAllFines() {
        return librarySystem.getAllFines();
    }
    
    /**
     * Get a fine by ID
     * @param fineId Fine ID
     * @return The fine or null if not found
     */
    public Fine getFineById(String fineId) {
        return librarySystem.getFineById(fineId);
    }
    
    /**
     * Pay a fine
     * @param fineId The ID of the fine
     * @return true if payment was successful, false otherwise
     */
    public boolean payFine(String fineId) {
        return librarySystem.payFine(fineId);
    }
    
    /**
     * Get fines for a borrower
     * @param borrowerId The ID of the borrower
     * @return List of fines for the borrower
     */
    public List<Fine> getFinesForBorrower(String borrowerId) {
        return librarySystem.getFinesForBorrower(borrowerId);
    }
    
    /**
     * Get unpaid fines for a borrower
     * @param borrowerId The ID of the borrower
     * @return List of unpaid fines for the borrower
     */
    public List<Fine> getUnpaidFinesForBorrower(String borrowerId) {
        return librarySystem.getUnpaidFinesForBorrower(borrowerId);
    }
    
    /**
     * Record a fine for a borrower
     * @param loanId The ID of the loan
     * @param amount Fine amount
     * @param reason Fine reason
     * @return The created fine or null if creation fails
     */
    public Fine recordFine(String loanId, double amount, String reason) {
        Loan loan = librarySystem.getLoanById(loanId);
        
        if (loan == null) {
            return null;
        }
        
        Fine fine = new Fine(loanId, loan.getBorrowerId(), amount, reason);
        fine.setBorrowerName(loan.getBorrowerName());
        fine.setBookTitle(loan.getBookTitle());
        
        // Add fine and update loan
        loan.setFineAmount(amount);
        
        List<Fine> fines = librarySystem.getAllFines();
        fines.add(fine);
        
        librarySystem.saveData();
        
        return fine;
    }
    
    /**
     * Check if a loan is overdue
     * @param loanId The ID of the loan
     * @return true if the loan is overdue, false otherwise
     */
    public boolean isLoanOverdue(String loanId) {
        Loan loan = librarySystem.getLoanById(loanId);
        
        if (loan == null) {
            return false;
        }
        
        return loan.isOverdue();
    }
}
