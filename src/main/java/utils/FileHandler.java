package utils;

import models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler class to handle file operations
 */
public class FileHandler {
    // File paths
    private static final String BOOKS_FILE = "data/books.ser";
    private static final String USERS_FILE = "data/users.ser";
    private static final String LOANS_FILE = "data/loans.ser";
    private static final String FINES_FILE = "data/fines.ser";
    
    // Create data directory if it doesn't exist
    static {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
    
    /**
     * Save a list of objects to a file
     * @param objects List of objects to save
     * @param filePath Path to save the file
     * @return true if saving was successful, false otherwise
     */
    public static <T> boolean saveToFile(List<T> objects, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(objects);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load a list of objects from a file
     * @param filePath Path to load the file from
     * @return List of objects loaded from the file, or an empty list if the file doesn't exist
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadFromFile(String filePath) {
        File file = new File(filePath);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from file: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Save books to file
     * @param books List of books to save
     * @return true if saving was successful, false otherwise
     */
    public static boolean saveBooks(List<Book> books) {
        return saveToFile(books, BOOKS_FILE);
    }
    
    /**
     * Load books from file
     * @return List of books loaded from file
     */
    public static List<Book> loadBooks() {
        return loadFromFile(BOOKS_FILE);
    }
    
    /**
     * Save users to file
     * @param users List of users to save
     * @return true if saving was successful, false otherwise
     */
    public static boolean saveUsers(List<User> users) {
        return saveToFile(users, USERS_FILE);
    }
    
    /**
     * Load users from file
     * @return List of users loaded from file
     */
    public static List<User> loadUsers() {
        return loadFromFile(USERS_FILE);
    }
    
    /**
     * Save loans to file
     * @param loans List of loans to save
     * @return true if saving was successful, false otherwise
     */
    public static boolean saveLoans(List<Loan> loans) {
        return saveToFile(loans, LOANS_FILE);
    }
    
    /**
     * Load loans from file
     * @return List of loans loaded from file
     */
    public static List<Loan> loadLoans() {
        return loadFromFile(LOANS_FILE);
    }
    
    /**
     * Save fines to file
     * @param fines List of fines to save
     * @return true if saving was successful, false otherwise
     */
    public static boolean saveFines(List<Fine> fines) {
        return saveToFile(fines, FINES_FILE);
    }
    
    /**
     * Load fines from file
     * @return List of fines loaded from file
     */
    public static List<Fine> loadFines() {
        return loadFromFile(FINES_FILE);
    }
    
    /**
     * Create initial data for the application if it doesn't exist
     * This method is called when the application starts
     */
    public static void createInitialData() {
        // Create sample users if users file doesn't exist
        File usersFile = new File(USERS_FILE);
        
        if (!usersFile.exists()) {
            List<User> users = new ArrayList<>();
            
            // Add a librarian
            Librarian librarian = new Librarian(
                    "librarian", "password", "Head Librarian",
                    "librarian@library.com", "123-456-7890", "Library Building",
                    "L001", "Management"
            );
            
            // Add a clerk
            Clerk clerk = new Clerk(
                    "clerk", "password", "Library Clerk",
                    "clerk@library.com", "123-456-7891", "Library Building",
                    "C001", "Front Desk"
            );
            
            // Add a borrower
            Borrower borrower = new Borrower(
                    "borrower", "password", "GroupA",
                    "groupa@vu.ac", "+25670568923", "Victoria University",
                    "B001"
            );
            
            users.add(librarian);
            users.add(clerk);
            users.add(borrower);
            
            saveUsers(users);
        }
        
        // Create sample books if books file doesn't exist
        File booksFile = new File(BOOKS_FILE);
        
        if (!booksFile.exists()) {
            List<Book> books = new ArrayList<>();
            
            // Add some sample books
            books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 3));
            books.add(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 5));
            books.add(new Book("1984", "George Orwell", "Dystopian", 2));
            books.add(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", 4));
            books.add(new Book("Pride and Prejudice", "Jane Austen", "Romance", 3));
            books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", 2));
            books.add(new Book("Python Programming", "John Smith", "Computer Science", 3));
            books.add(new Book("Data Structures and Algorithms", "Jane Johnson", "Computer Science", 2));
            books.add(new Book("The Art of War", "Sun Tzu", "Philosophy", 1));
            books.add(new Book("The Origin of Species", "Charles Darwin", "Science", 2));
            
            saveBooks(books);
        }
    }
}
