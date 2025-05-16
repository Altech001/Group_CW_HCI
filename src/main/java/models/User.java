package models;

import java.io.Serializable;
import java.util.UUID;

/**
 * Base User class for all user types in the library system
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    
    // Constructor
    public User(String username, String password, String fullName, String email, String phone, String address) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Gets the role of the user
     * @return String representation of the user role
     */
    public abstract String getRole();
    
    /**
     * Checks if the provided credentials match this user
     * @param username Username to check
     * @param password Password to check
     * @return true if credentials match, false otherwise
     */
    public boolean checkCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
