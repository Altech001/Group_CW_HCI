package utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Utility class for handling icons and UI components
 */
public class IconUtils {
    
    /**
     * Creates a styled button with an icon and text
     * @param text Button text
     * @param iconPath Path to the icon
     * @param buttonSize Size of the button
     * @return Styled JButton
     */
    public static JButton createStyledButton(String text, String iconPath, Dimension buttonSize) {
        JButton button = new JButton(text);
        try {
            File iconFile = new File(iconPath);
            if (iconFile.exists()) {
                // Try as file path
                ImageIcon icon = new ImageIcon(iconPath);
                button.setIcon(icon);
            } else {
                // Try as resource
                URL resource = IconUtils.class.getResource(iconPath);
                if (resource != null) {
                    ImageIcon icon = new ImageIcon(resource);
                    button.setIcon(icon);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }
        
        button.setPreferredSize(buttonSize);
        button.setBackground(Constants.PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFont(Constants.REGULAR_FONT);
        
        return button;
    }
    
    /**
     * Creates a styled panel with rounded corners
     * @param bgColor Background color
     * @param radius Corner radius
     * @return Styled JPanel
     */
    public static JPanel createRoundedPanel(Color bgColor, int radius) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(bgColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
                g2d.dispose();
            }
        };
    }
    
    /**
     * Creates a styled header label
     * @param text Label text
     * @return Styled JLabel
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Constants.HEADER_FONT);
        label.setForeground(Constants.TEXT_COLOR);
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        return label;
    }
    
    /**
     * Creates a styled subheader label
     * @param text Label text
     * @return Styled JLabel
     */
    public static JLabel createSubHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Constants.SUBHEADER_FONT);
        label.setForeground(Constants.TEXT_COLOR);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        return label;
    }
    
    /**
     * Styles a JTable with alternating row colors and custom header
     * @param table The table to style
     */
    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(Constants.PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(Constants.REGULAR_FONT);
        
        // Style header
        table.getTableHeader().setBackground(Constants.TABLE_HEADER_COLOR);
        table.getTableHeader().setFont(Constants.SUBHEADER_FONT);
        table.getTableHeader().setForeground(Constants.TEXT_COLOR);
        
        // Alternating row colors
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Constants.TABLE_ROW_COLOR : Constants.TABLE_ALTERNATE_ROW_COLOR);
                }
                
                return c;
            }
        });
    }
    
    /**
     * Creates a scroll pane for a component with styled scrollbars
     * @param component The component to add to the scroll pane
     * @return Styled JScrollPane
     */
    public static JScrollPane createStyledScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            private Color thumbColor;
            private Color trackColor;
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Constants.PRIMARY_COLOR;
                this.trackColor = Constants.BACKGROUND_COLOR;
            }
        });
        return scrollPane;
    }
    
    /**
     * Gets SVG content as a string
     * @param iconPath Path to the SVG file
     * @return SVG content as string or null if not found
     */
    public static String getSVGContent(String iconPath) {
        try {
            // Try to load as a file
            File file = new File(iconPath);
            if (file.exists()) {
                try (InputStream is = new FileInputStream(file)) {
                    return new String(is.readAllBytes());
                }
            }
            
            // Try to load as a resource
            URL resource = IconUtils.class.getResource(iconPath);
            if (resource != null) {
                try (InputStream is = resource.openStream()) {
                    return new String(is.readAllBytes());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading SVG: " + e.getMessage());
        }
        return null;
    }
}
