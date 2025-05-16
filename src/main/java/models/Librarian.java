package models;

import java.io.Serializable;

/**
 * Librarian class - represents a library staff member with full privileges
 */
public class Librarian extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String employeeId;
    private String department;
    
    /**
     * Constructor for Librarian
     */
    public Librarian(String username, String password, String fullName, String email, 
                     String phone, String address, String employeeId, String department) {
        super(username, password, fullName, email, phone, address);
        this.employeeId = employeeId;
        this.department = department;
    }
    
    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    @Override
    public String getRole() {
        return "Librarian";
    }
    
    /**
     * Checks if the librarian has permission to add books
     * @return true since librarians can add books
     */
    public boolean canAddBooks() {
        return true;
    }
    
    /**
     * Checks if the librarian has permission to delete books
     * @return true since librarians can delete books
     */
    public boolean canDeleteBooks() {
        return true;
    }
    
    /**
     * Checks if the librarian has permission to update books
     * @return true since librarians can update books
     */
    public boolean canUpdateBooks() {
        return true;
    }
    
    /**
     * Checks if the librarian has permission to manage borrower profiles
     * @return true since librarians can manage borrower profiles
     */
    public boolean canManageBorrowers() {
        return true;
    }
    
    /**
     * Checks if the librarian has permission to handle loans
     * @return true since librarians can handle loans
     */
    public boolean canManageLoans() {
        return true;
    }
    
    /**
     * Checks if the librarian has permission to record fines
     * @return true since librarians can record fines
     */
    public boolean canRecordFines() {
        return true;
    }
}
