package ui;

import controllers.AuthController;
import models.User;
import utils.Constants;
import utils.IconUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URI;

/**
 * Redesigned Panel for user login with modern UI elements aligned with Rhythm theme
 */
public class LoginPanel extends JPanel {
    private LibraryUI parentFrame;
    private AuthController authController;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JToggleButton showPasswordButton;
    
    /**
     * Constructor for LoginPanel
     * @param parentFrame The parent frame
     */
    public LoginPanel(LibraryUI parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = AuthController.getInstance();
        
        setupUI();
    }
    
    /**
     * Set up the UI components
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        
        // Create header panel with logo
        JPanel headerPanel = createHeaderPanel();
        
        // Create center panel containing login form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Constants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Create login form
        JPanel loginFormPanel = createLoginForm();
        centerPanel.add(loginFormPanel, gbc);
        
        // Create footer panel
        JPanel footerPanel = createFooterPanel();
        
        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the header panel with logo
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Constants.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN));
        
        // Logo panel (left side)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(Constants.PRIMARY_COLOR);
        
        // Create logo
        JLabel logoLabel = new JLabel("📚");
        logoLabel.setFont(Constants.HEADER_FONT);
        logoLabel.setForeground(Constants.TEXT_COLOR);
        
        // App title next to logo
        JLabel titleLabel = new JLabel(Constants.APP_TITLE);
        titleLabel.setFont(Constants.HEADER_FONT);
        titleLabel.setForeground(Constants.TEXT_COLOR);
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createHorizontalStrut(Constants.PADDING));
        logoPanel.add(titleLabel);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    /**
     * Create the login form
     * @return The login form panel
     */
    private JPanel createLoginForm() {
        // Create login panel with card-like appearance
        JPanel loginPanel = IconUtils.createRoundedPanel(Constants.SECONDARY_BACKGROUND, 15);
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.SECONDARY_TEXT_COLOR, 1),
                BorderFactory.createEmptyBorder(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
        ));
        
        // Create login header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Constants.SECONDARY_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(0, 0, Constants.MARGIN, 0));
        
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(Constants.HEADER_FONT);
        welcomeLabel.setForeground(Constants.PRIMARY_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subheaderLabel = new JLabel("Login to access your account");
        subheaderLabel.setFont(Constants.REGULAR_FONT);
        subheaderLabel.setForeground(Constants.SECONDARY_TEXT_COLOR);
        subheaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subheaderLabel);
        
        // Create form fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(Constants.SECONDARY_BACKGROUND);
        
        // Username field with icon
        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.setBackground(Constants.SECONDARY_BACKGROUND);
        usernamePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.SECONDARY_TEXT_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(Constants.REGULAR_FONT);
        userIcon.setForeground(Constants.TEXT_COLOR);
        userIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, Constants.PADDING));
        
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createEmptyBorder());
        usernameField.setFont(Constants.REGULAR_FONT);
        usernameField.setForeground(Constants.TEXT_COLOR);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username");
        usernameField.putClientProperty("JTextField.placeholderForeground", Constants.SECONDARY_TEXT_COLOR);
        
        // Add focus listener for highlight effect
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                usernamePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Constants.ACCENT_COLOR, 2, true),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                usernamePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Constants.SECONDARY_TEXT_COLOR, 1, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        usernamePanel.add(userIcon, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        
        // Password field with icon
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Constants.SECONDARY_BACKGROUND);
        passwordPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.SECONDARY_TEXT_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel passwordIcon = new JLabel("🔒");
        passwordIcon.setFont(Constants.REGULAR_FONT);
        passwordIcon.setForeground(Constants.TEXT_COLOR);
        passwordIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, Constants.PADDING));
        
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        passwordField.setFont(Constants.REGULAR_FONT);
        passwordField.setForeground(Constants.TEXT_COLOR);
        passwordField.putClientProperty("JTextField.placeholderText", "Enter your password");
        passwordField.putClientProperty("JTextField.placeholderForeground", Constants.SECONDARY_TEXT_COLOR);
        
        // Add focus listener for highlight effect
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Constants.ACCENT_COLOR, 2, true),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                passwordPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Constants.SECONDARY_TEXT_COLOR, 1, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        // Create show/hide password toggle button
        showPasswordButton = new JToggleButton("👁️");
        showPasswordButton.setFont(Constants.REGULAR_FONT);
        showPasswordButton.setForeground(Constants.SECONDARY_TEXT_COLOR);
        showPasswordButton.setBorder(BorderFactory.createEmptyBorder());
        showPasswordButton.setContentAreaFilled(false);
        showPasswordButton.setFocusPainted(false);
        showPasswordButton.setFocusable(true);
        showPasswordButton.setToolTipText("Toggle password visibility");
        showPasswordButton.addActionListener(e -> {
            if (showPasswordButton.isSelected()) {
                passwordField.setEchoChar((char)0);
                showPasswordButton.setText("👁️‍🗨️");
            } else {
                passwordField.setEchoChar('•');
                showPasswordButton.setText("👁️");
            }
        });
        
        JPanel passwordRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        passwordRightPanel.setBackground(Constants.SECONDARY_BACKGROUND);
        passwordRightPanel.add(showPasswordButton);
        
        passwordPanel.add(passwordIcon, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(passwordRightPanel, BorderLayout.EAST);
        
        // Remember me and forgot password
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(Constants.SECONDARY_BACKGROUND);
        
        JCheckBox rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setFont(Constants.SMALL_FONT);
        rememberMeCheckbox.setBackground(Constants.SECONDARY_BACKGROUND);
        rememberMeCheckbox.setForeground(Constants.SECONDARY_TEXT_COLOR);
        rememberMeCheckbox.setFocusPainted(false);
        
        forgotPasswordButton = new JButton("Forgot password?");
        forgotPasswordButton.setFont(Constants.SMALL_FONT);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setForeground(Constants.ACCENT_COLOR);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setFocusable(true);
        forgotPasswordButton.setToolTipText("Recover your password");
        
        optionsPanel.add(rememberMeCheckbox, BorderLayout.WEST);
        optionsPanel.add(forgotPasswordButton, BorderLayout.EAST);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(Constants.REGULAR_FONT);
        loginButton.setBackground(Constants.ACCENT_COLOR);
        loginButton.setForeground(Constants.TEXT_COLOR);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        loginButton.setPreferredSize(Constants.BUTTON_SIZE);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFocusable(true);
        loginButton.setToolTipText("Log in to your account");
        loginButton.putClientProperty("JComponent.roundRect", true);
        loginButton.putClientProperty("JComponent.roundRadius", Constants.BUTTON_RADIUS);
        
        // Add hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Constants.ACCENT_COLOR.darker());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Constants.ACCENT_COLOR);
            }
        });
        
        // Add action listener
        loginButton.addActionListener(e -> handleLogin());
        
        // Add components to panel
        fieldsPanel.add(Box.createVerticalStrut(Constants.PADDING));
        fieldsPanel.add(usernamePanel);
        fieldsPanel.add(Box.createVerticalStrut(15));
        fieldsPanel.add(passwordPanel);
        fieldsPanel.add(Box.createVerticalStrut(15));
        fieldsPanel.add(optionsPanel);
        fieldsPanel.add(Box.createVerticalStrut(25));
        fieldsPanel.add(loginButton);
        
        // Add components to login panel
        loginPanel.add(headerPanel, BorderLayout.NORTH);
        loginPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Create a wrapper panel for the sample credentials
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        wrapperPanel.setBackground(Constants.BACKGROUND_COLOR);
        wrapperPanel.add(loginPanel);
        wrapperPanel.add(Box.createVerticalStrut(20));
        wrapperPanel.add(createCredentialsPanel());
        
        return wrapperPanel;
    }
    
    /**
     * Create the credentials information panel
     * @return The credentials panel
     */
    private JPanel createCredentialsPanel() {
        JPanel credentialsPanel = IconUtils.createRoundedPanel(Constants.SECONDARY_BACKGROUND, 10);
        credentialsPanel.setLayout(new BorderLayout());
        credentialsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.SECONDARY_TEXT_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel infoIcon = new JLabel("ℹ️");
        infoIcon.setFont(Constants.REGULAR_FONT);
        infoIcon.setForeground(Constants.TEXT_COLOR);
        infoIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, Constants.PADDING));
        
        JPanel credentialsContent = new JPanel();
        credentialsContent.setLayout(new BoxLayout(credentialsContent, BoxLayout.Y_AXIS));
        credentialsContent.setBackground(Constants.SECONDARY_BACKGROUND);
        
        JLabel credentialsHeaderLabel = new JLabel("Credentials");
        credentialsHeaderLabel.setFont(Constants.SUBHEADER_FONT);
        credentialsHeaderLabel.setForeground(Constants.TEXT_COLOR);
        
        JPanel credentialsTable = new JPanel(new GridLayout(3, 1, 0, 8));
        credentialsTable.setBackground(Constants.SECONDARY_BACKGROUND);
        
        credentialsTable.add(createCredentialRow("Librarian", "librarian", "password"));
        credentialsTable.add(createCredentialRow("Clerk", "clerk", "password"));
        credentialsTable.add(createCredentialRow("Borrower", "borrower or sojiambo@vu.ac.ug", "password"));
        
        credentialsContent.add(credentialsHeaderLabel);
        credentialsContent.add(Box.createVerticalStrut(10));
        credentialsContent.add(credentialsTable);
        
        credentialsPanel.add(infoIcon, BorderLayout.WEST);
        credentialsPanel.add(credentialsContent, BorderLayout.CENTER);
        
        return credentialsPanel;
    }
    
    /**
     * Create a credential row for the credentials panel
     * @param role The user role
     * @param username The username
     * @param password The password
     * @return The credential row panel
     */
    private JPanel createCredentialRow(String role, String username, String password) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setOpaque(false);
        
        JLabel roleLabel = new JLabel(role + ":");
        roleLabel.setFont(Constants.REGULAR_FONT);
        roleLabel.setForeground(Constants.TEXT_COLOR);
        
        JPanel credPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        credPanel.setOpaque(false);
        
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(Constants.SMALL_FONT);
        usernameLabel.setForeground(Constants.SECONDARY_TEXT_COLOR);
        usernameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        usernameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                usernameField.setText(username);
                passwordField.setText(password);
            }
        });
        
        JLabel separatorLabel = new JLabel(" / ");
        separatorLabel.setFont(Constants.SMALL_FONT);
        separatorLabel.setForeground(Constants.SECONDARY_TEXT_COLOR);
        
        JLabel passwordLabel = new JLabel(password);
        passwordLabel.setFont(Constants.SMALL_FONT);
        passwordLabel.setForeground(Constants.SECONDARY_TEXT_COLOR);
        
        credPanel.add(usernameLabel);
        credPanel.add(separatorLabel);
        credPanel.add(passwordLabel);
        
        rowPanel.add(roleLabel, BorderLayout.WEST);
        rowPanel.add(credPanel, BorderLayout.CENTER);
        
        return rowPanel;
    }
    
    /**
     * Create the footer panel
     * @return The footer panel
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Constants.BACKGROUND_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(Constants.PADDING, Constants.MARGIN, Constants.PADDING, Constants.MARGIN));
        
        JLabel copyrightLabel = new JLabel("© 2025 Library Management System");
        copyrightLabel.setFont(Constants.SMALL_FONT);
        copyrightLabel.setForeground(Constants.SECONDARY_TEXT_COLOR);
        
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, Constants.PADDING, 0));
        linksPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        String[] links = {"Help", "Privacy Policy", "Terms of Use"};
        for (String link : links) {
            JButton linkButton = new JButton(link);
            linkButton.setFont(Constants.SMALL_FONT);
            linkButton.setBorderPainted(false);
            linkButton.setContentAreaFilled(false);
            linkButton.setForeground(Constants.ACCENT_COLOR);
            linkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            linkButton.setFocusPainted(false);
            linkButton.setFocusable(true);
            linkButton.addActionListener(e -> {
                if (link.equals("Help")) {
                    JOptionPane.showMessageDialog(parentFrame, "Contact support at support@vulibrary.com", "Help", JOptionPane.INFORMATION_MESSAGE);
                } else if (link.equals("Privacy Policy")) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://vulibrary.com/privacy"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (link.equals("Terms of Use")) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://vulibrary.com/terms"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            linksPanel.add(linkButton);
        }
        
        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        footerPanel.add(linksPanel, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    /**
     * Handle login button click
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Please enter both username and password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        User user = authController.login(username, password);
        
        if (user != null) {
            parentFrame.onLoginSuccess(user);
        } else {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Invalid username or password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Clear the login fields
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        showPasswordButton.setSelected(false);
        passwordField.setEchoChar('•');
        showPasswordButton.setText("👁️");
    }
}