package utils;

import models.Book;
import models.Borrower;
import models.Loan;

import java.util.List;

/**
 * Utility class for various validations in the library system
 */
public class ValidationUtils {
    
    /**
     * Validates the input string is not null or empty
     * @param input The string to validate
     * @return true if the string is valid, false otherwise
     */
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    /**
     * Validates the numeric input is a positive integer
     * @param input The string to validate
     * @return true if the input is a valid positive integer, false otherwise
     */
    public static boolean isValidPositiveInteger(String input) {
        try {
            int value = Integer.parseInt(input);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates the book checkout process
     * @param book The book to checkout
     * @param borrower The borrower
     * @return An error message if validation fails, null otherwise
     */
    public static String validateCheckout(Book book, Borrower borrower, List<Loan> loans) {
        // Check if the book is available
        if (book.getAvailable() <= 0) {
            return "This book is not available for checkout.";
        }
        
        // Check if the borrower has reached their checkout limit
        if (!borrower.canBorrowBooks()) {
            return "The borrower has reached their checkout limit.";
        }
        
        // Check if the borrower already has this book checked out
        for (String loanId : borrower.getCurrentLoans()) {
            for (Loan loan : loans) {
                if (loan.getId().equals(loanId) && loan.getBookId().equals(book.getId()) && !loan.isReturned()) {
                    return "The borrower already has this book checked out.";
                }
            }
        }
        
        return null; // No validation errors
    }
    
    /**
     * Validates the book check-in process
     * @param loan The loan to check in
     * @return An error message if validation fails, null otherwise
     */
    public static String validateCheckin(Loan loan) {
        // Check if the loan exists
        if (loan == null) {
            return "Loan not found.";
        }
        
        // Check if the book is already returned
        if (loan.isReturned()) {
            return "This book has already been returned.";
        }
        
        return null; // No validation errors
    }
    
    /**
     * Validates an email address
     * @param email The email to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Validates a phone number (simple validation)
     * @param phone The phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        // Simple validation for demo purposes
        String phoneRegex = "^\\d{3}-\\d{3}-\\d{4}$";
        return phone.matches(phoneRegex);
    }
    
    /**
     * Validates the fine payment amount
     * @param amount The payment amount as a string
     * @param totalFine The total fine amount
     * @return true if the payment amount is valid, false otherwise
     */
    public static boolean isValidPaymentAmount(String amount, double totalFine) {
        try {
            double paymentAmount = Double.parseDouble(amount);
            return paymentAmount > 0 && paymentAmount <= totalFine;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
