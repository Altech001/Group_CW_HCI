package utils;

import java.awt.*;

/**
 * Constants used throughout the application with an elegant dark UI theme.
 */
public class Constants {
    // Application constants
    public static final String APP_TITLE = "VU Library Management System";
    public static final Dimension APP_SIZE = new Dimension(900, 680);
    public static final int LOAN_DURATION_DAYS = 14;
    public static final double FINE_PER_DAY = 0.5;

    // Elegant dark color scheme
    public static final Color PRIMARY_COLOR = new Color(33, 37, 41);        // Dark slate
    public static final Color ACCENT_COLOR = new Color(231, 76, 60);        // Soft red
    public static final Color APP_COLOR = new Color(24, 26, 27);            // Near-black
    public static final Color BACKGROUND_COLOR = new Color(18, 18, 18);     // Dark background
    public static final Color SECONDARY_BACKGROUND = new Color(63, 63, 70); // Tailwind Zinc 700

    public static final Color TEXT_COLOR = new Color(245, 245, 245);             // Light text
    public static final Color SECONDARY_TEXT_COLOR = new Color(180, 180, 180);   // Muted light gray

    // Font settings - elegant sans-serif
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Table settings - dark theme adjusted
    public static final Color TABLE_HEADER_COLOR = new Color(44, 44, 50);         // Dark header
    public static final Color TABLE_ROW_COLOR = new Color(30, 30, 30);            // Dark row
    public static final Color TABLE_ALTERNATE_ROW_COLOR = new Color(38, 38, 45);  // Slightly lighter for zebra

    // Button settings
    public static final Dimension BUTTON_SIZE = new Dimension(150, 40);
    public static final Dimension SMALL_BUTTON_SIZE = new Dimension(120, 35);
    public static final int BUTTON_RADIUS = 8;

    // Padding and margin
    public static final int PADDING = 12;
    public static final int MARGIN = 24;

    // Icons (unchanged)
    public static final String BOOK_ICON = "/resources/book.svg";
    public static final String SEARCH_ICON = "/resources/search.svg";
    public static final String USER_ICON = "/resources/user.svg";
    public static final String LOAN_ICON = "/resources/loan.svg";
    public static final String FINE_ICON = "/resources/fine.svg";
    public static final String ADD_ICON = "/resources/add.svg";
    public static final String DELETE_ICON = "/resources/delete.svg";
    public static final String EDIT_ICON = "/resources/edit.svg";
    public static final String LOGOUT_ICON = "/resources/logout.svg";
}
