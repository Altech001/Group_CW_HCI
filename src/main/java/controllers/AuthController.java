package controllers;

import models.User;

/**
 * Controller for handling authentication
 */
public class AuthController {
    private static AuthController instance;
    private LibrarySystem librarySystem;
    
    private AuthController() {
        librarySystem = LibrarySystem.getInstance();
    }
    
    /**
     * Get the singleton instance
     * @return The AuthController instance
     */
    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }
    
    /**
     * Authenticate a user
     * @param username Username
     * @param password Password
     * @return The authenticated user or null if authentication fails
     */
    public User login(String username, String password) {
        return librarySystem.authenticate(username, password);
    }
    
    /**
     * Log out the current user
     */
    public void logout() {
        librarySystem.logout();
    }
    
    /**
     * Get the current user
     * @return The current logged-in user or null if no user is logged in
     */
    public User getCurrentUser() {
        return librarySystem.getCurrentUser();
    }
    
    /**
     * Check if a user is logged in
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }
    
    /**
     * Check if the current user is a librarian
     * @return true if the current user is a librarian, false otherwise
     */
    public boolean isLibrarian() {
        User user = getCurrentUser();
        return user != null && "Librarian".equals(user.getRole());
    }
    
    /**
     * Check if the current user is a clerk
     * @return true if the current user is a clerk, false otherwise
     */
    public boolean isClerk() {
        User user = getCurrentUser();
        return user != null && "Clerk".equals(user.getRole());
    }
    
    /**
     * Check if the current user is a borrower
     * @return true if the current user is a borrower, false otherwise
     */
    public boolean isBorrower() {
        User user = getCurrentUser();
        return user != null && "Borrower".equals(user.getRole());
    }
}
