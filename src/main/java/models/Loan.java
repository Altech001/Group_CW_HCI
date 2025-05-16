package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Loan class to store information about book loans
 */
public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String bookId;
    private String borrowerId;
    private Date checkoutDate;
    private Date dueDate;
    private Date returnDate;
    private boolean isReturned;
    private double fineAmount;
    private boolean finePaid;
    
    // For keeping track of book and borrower details
    private transient String bookTitle; // Not persisted
    private transient String borrowerName; // Not persisted
    
    /**
     * Constructor for creating a new loan
     */
    public Loan(String bookId, String borrowerId, Date checkoutDate, int loanDurationDays) {
        this.id = UUID.randomUUID().toString();
        this.bookId = bookId;
        this.borrowerId = borrowerId;
        this.checkoutDate = checkoutDate;
        
        // Calculate due date
        Date due = new Date(checkoutDate.getTime());
        due.setTime(due.getTime() + TimeUnit.DAYS.toMillis(loanDurationDays));
        this.dueDate = due;
        
        this.isReturned = false;
        this.returnDate = null;
        this.fineAmount = 0.0;
        this.finePaid = false;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public String getBookId() {
        return bookId;
    }
    
    public String getBorrowerId() {
        return borrowerId;
    }
    
    public Date getCheckoutDate() {
        return checkoutDate;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public Date getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    public boolean isReturned() {
        return isReturned;
    }
    
    public void setReturned(boolean returned) {
        isReturned = returned;
    }
    
    public double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public boolean isFinePaid() {
        return finePaid;
    }
    
    public void setFinePaid(boolean finePaid) {
        this.finePaid = finePaid;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public String getBorrowerName() {
        return borrowerName;
    }
    
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
    
    /**
     * Calculates the fine for an overdue book
     * @param finePerDay Fine amount per day overdue
     * @return The fine amount
     */
    public double calculateFine(double finePerDay) {
        if (isReturned) {
            if (returnDate.after(dueDate)) {
                long diffInMillies = returnDate.getTime() - dueDate.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                return diffInDays * finePerDay;
            }
        } else {
            // Book not yet returned, calculate fine up to current date
            Date currentDate = new Date();
            if (currentDate.after(dueDate)) {
                long diffInMillies = currentDate.getTime() - dueDate.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                return diffInDays * finePerDay;
            }
        }
        return 0.0;
    }
    
    /**
     * Returns a formatted string of a date
     * @param date The date to format
     * @return Formatted date string
     */
    private String formatDate(Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
    /**
     * Checks if the loan is overdue
     * @return true if the loan is overdue, false otherwise
     */
    public boolean isOverdue() {
        if (isReturned) {
            return returnDate.after(dueDate);
        } else {
            Date currentDate = new Date();
            return currentDate.after(dueDate);
        }
    }
    
    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", borrowerId='" + borrowerId + '\'' +
                ", checkoutDate=" + formatDate(checkoutDate) +
                ", dueDate=" + formatDate(dueDate) +
                ", returnDate=" + formatDate(returnDate) +
                ", isReturned=" + isReturned +
                ", fineAmount=" + fineAmount +
                ", finePaid=" + finePaid +
                '}';
    }
}
