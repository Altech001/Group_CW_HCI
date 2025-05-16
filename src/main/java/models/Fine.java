package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Fine class to store information about fines
 */
public class Fine implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String loanId;
    private String borrowerId;
    private double amount;
    private String reason;
    private Date issueDate;
    private boolean paid;
    private Date paymentDate;
    
    // For keeping track of borrower details
    private transient String borrowerName; // Not persisted
    private transient String bookTitle; // Not persisted
    
    /**
     * Constructor for creating a new fine
     */
    public Fine(String loanId, String borrowerId, double amount, String reason) {
        this.id = UUID.randomUUID().toString();
        this.loanId = loanId;
        this.borrowerId = borrowerId;
        this.amount = amount;
        this.reason = reason;
        this.issueDate = new Date();
        this.paid = false;
        this.paymentDate = null;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public String getLoanId() {
        return loanId;
    }
    
    public String getBorrowerId() {
        return borrowerId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Date getIssueDate() {
        return issueDate;
    }
    
    public boolean isPaid() {
        return paid;
    }
    
    public void setPaid(boolean paid) {
        this.paid = paid;
        if (paid && paymentDate == null) {
            this.paymentDate = new Date();
        }
    }
    
    public Date getPaymentDate() {
        return paymentDate;
    }
    
    public String getBorrowerName() {
        return borrowerName;
    }
    
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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
    
    @Override
    public String toString() {
        return "Fine{" +
                "id='" + id + '\'' +
                ", loanId='" + loanId + '\'' +
                ", borrowerId='" + borrowerId + '\'' +
                ", amount=" + amount +
                ", reason='" + reason + '\'' +
                ", issueDate=" + formatDate(issueDate) +
                ", paid=" + paid +
                ", paymentDate=" + formatDate(paymentDate) +
                '}';
    }
}
