package models;

import java.io.Serializable;

/**
 * Clerk class - represents a library staff member with limited privileges
 */
public class Clerk extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String employeeId;
    private String department;
    
    /**
     * Constructor for Clerk
     */
    public Clerk(String username, String password, String fullName, String email, 
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
        return "Clerk";
    }
    
    /**
     * Checks if the clerk has permission to add books
     * @return false since clerks cannot add books
     */
    public boolean canAddBooks() {
        return false;
    }
    
    /**
     * Checks if the clerk has permission to delete books
     * @return false since clerks cannot delete books
     */
    public boolean canDeleteBooks() {
        return false;
    }
    
    /**
     * Checks if the clerk has permission to update books
     * @return false since clerks cannot update books
     */
    public boolean canUpdateBooks() {
        return false;
    }
    
    /**
     * Checks if the clerk has permission to manage borrower profiles
     * @return true since clerks can manage borrower profiles
     */
    public boolean canManageBorrowers() {
        return true;
    }
    
    /**
     * Checks if the clerk has permission to handle loans
     * @return true since clerks can handle loans
     */
    public boolean canManageLoans() {
        return true;
    }
    
    /**
     * Checks if the clerk has permission to record fines
     * @return true since clerks can record fines
     */
    public boolean canRecordFines() {
        return true;
    }
}
