package controllers;

import models.Borrower;
import models.Clerk;
import models.Librarian;
import models.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling user operations
 */
public class UserController {
    private static UserController instance;
    private LibrarySystem librarySystem;
    
    private UserController() {
        librarySystem = LibrarySystem.getInstance();
    }
    
    /**
     * Get the singleton instance
     * @return The UserController instance
     */
    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return librarySystem.getAllUsers();
    }
    
    /**
     * Get all borrowers
     * @return List of all borrowers
     */
    public List<Borrower> getAllBorrowers() {
        return librarySystem.getAllBorrowers();
    }
    
    /**
     * Get a user by ID
     * @param userId User ID
     * @return The user or null if not found
     */
    public User getUserById(String userId) {
        return librarySystem.getUserById(userId);
    }
    
    /**
     * Get a borrower by ID
     * @param borrowerId Borrower ID
     * @return The borrower or null if not found
     */
    public Borrower getBorrowerById(String borrowerId) {
        return librarySystem.getBorrowerById(borrowerId);
    }
    
    /**
     * Add a new borrower
     * @param username Username
     * @param password Password
     * @param fullName Full name
     * @param email Email
     * @param phone Phone
     * @param address Address
     * @param borrowerId Borrower ID (library card number)
     * @return The created borrower or null if creation fails
     */
    public Borrower addBorrower(String username, String password, String fullName, 
                               String email, String phone, String address, String borrowerId) {
        Borrower borrower = new Borrower(username, password, fullName, email, phone, address, borrowerId);
        boolean success = librarySystem.addUser(borrower);
        return success ? borrower : null;
    }
    
    /**
     * Add a new librarian
     * @param username Username
     * @param password Password
     * @param fullName Full name
     * @param email Email
     * @param phone Phone
     * @param address Address
     * @param employeeId Employee ID
     * @param department Department
     * @return The created librarian or null if creation fails
     */
    public Librarian addLibrarian(String username, String password, String fullName, 
                                 String email, String phone, String address, 
                                 String employeeId, String department) {
        Librarian librarian = new Librarian(username, password, fullName, email, phone, address, employeeId, department);
        boolean success = librarySystem.addUser(librarian);
        return success ? librarian : null;
    }
    
    /**
     * Add a new clerk
     * @param username Username
     * @param password Password
     * @param fullName Full name
     * @param email Email
     * @param phone Phone
     * @param address Address
     * @param employeeId Employee ID
     * @param department Department
     * @return The created clerk or null if creation fails
     */
    public Clerk addClerk(String username, String password, String fullName, 
                         String email, String phone, String address, 
                         String employeeId, String department) {
        Clerk clerk = new Clerk(username, password, fullName, email, phone, address, employeeId, department);
        boolean success = librarySystem.addUser(clerk);
        return success ? clerk : null;
    }
    
    /**
     * Update a user
     * @param user The user to update
     * @return true if updating was successful, false otherwise
     */
    public boolean updateUser(User user) {
        return librarySystem.updateUser(user);
    }
    
    /**
     * Delete a user
     * @param userId The ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUser(String userId) {
        return librarySystem.deleteUser(userId);
    }
    
    /**
     * Search users by name
     * @param name The name to search for
     * @return List of users matching the search criteria
     */
    public List<User> searchUsersByName(String name) {
        String searchTerm = name.toLowerCase();
        return librarySystem.getAllUsers().stream()
                .filter(user -> user.getFullName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    /**
     * Search borrowers by name
     * @param name The name to search for
     * @return List of borrowers matching the search criteria
     */
    public List<Borrower> searchBorrowersByName(String name) {
        String searchTerm = name.toLowerCase();
        return librarySystem.getAllBorrowers().stream()
                .filter(borrower -> borrower.getFullName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
}
