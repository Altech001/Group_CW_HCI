package models;

import java.io.Serializable;
import java.util.UUID;

/**
 * Book class to store information about books in the library.
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String title;
    private String author;
    private String subject;
    private int quantity;
    private int available; // Number of copies currently available

    /**
     * Constructor for creating a new book
     */
    public Book(String title, String author, String subject, int quantity) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.quantity = quantity;
        this.available = quantity; // Initially all copies are available
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        // Update available copies based on change in quantity
        int difference = quantity - this.quantity;
        this.quantity = quantity;
        this.available += difference;
        if (this.available < 0) {
            this.available = 0;
        }
    }

    public int getAvailable() {
        return available;
    }

    public void incrementAvailable() {
        if (available < quantity) {
            available++;
        }
    }

    public void decrementAvailable() {
        if (available > 0) {
            available--;
        }
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", subject='" + subject + '\'' +
                ", quantity=" + quantity +
                ", available=" + available +
                '}';
    }
    
    /**
     * Checks if the book contains the search term in its title, author or subject
     */
    public boolean containsSearchTerm(String searchTerm) {
        searchTerm = searchTerm.toLowerCase();
        return title.toLowerCase().contains(searchTerm) ||
               author.toLowerCase().contains(searchTerm) ||
               subject.toLowerCase().contains(searchTerm);
    }

    /**
     * Checks if the book contains the title search term
     */
    public boolean matchesTitle(String searchTerm) {
        return title.toLowerCase().contains(searchTerm.toLowerCase());
    }

    /**
     * Checks if the book contains the author search term
     */
    public boolean matchesAuthor(String searchTerm) {
        return author.toLowerCase().contains(searchTerm.toLowerCase());
    }

    /**
     * Checks if the book contains the subject search term
     */
    public boolean matchesSubject(String searchTerm) {
        return subject.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
