package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Borrower class - represents a library user who can borrow books
 */
public class Borrower extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String borrowerId; // Library card number
    private int maxBooksAllowed; // Maximum number of books the borrower can check out
    private List<String> currentLoans; // IDs of current loans
    
    /**
     * Constructor for Borrower
     */
    public Borrower(String username, String password, String fullName, String email, 
                    String phone, String address, String borrowerId) {
        super(username, password, fullName, email, phone, address);
        this.borrowerId = borrowerId;
        this.maxBooksAllowed = 5; // Default maximum books allowed
        this.currentLoans = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getBorrowerId() {
        return borrowerId;
    }
    
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
    
    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }
    
    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }
    
    public List<String> getCurrentLoans() {
        return currentLoans;
    }
    
    /**
     * Adds a loan ID to the borrower's current loans
     * @param loanId The ID of the loan to add
     * @return true if the loan was added successfully, false otherwise
     */
    public boolean addLoan(String loanId) {
        if (currentLoans.size() < maxBooksAllowed) {
            currentLoans.add(loanId);
            return true;
        }
        return false;
    }
    
    /**
     * Removes a loan ID from the borrower's current loans
     * @param loanId The ID of the loan to remove
     * @return true if the loan was found and removed, false otherwise
     */
    public boolean removeLoan(String loanId) {
        return currentLoans.remove(loanId);
    }
    
    /**
     * Checks if the borrower can borrow more books
     * @return true if the borrower can borrow more books, false otherwise
     */
    public boolean canBorrowBooks() {
        return currentLoans.size() < maxBooksAllowed;
    }
    
    /**
     * Gets the number of books the borrower can still borrow
     * @return The number of available loan slots
     */
    public int getAvailableLoanSlots() {
        return maxBooksAllowed - currentLoans.size();
    }
    
    @Override
    public String getRole() {
        return "Borrower";
    }
}
