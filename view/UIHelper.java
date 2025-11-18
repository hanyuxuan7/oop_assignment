package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class UIHelper {
    // Modern color palette
    public static final Color PRIMARY = new Color(59, 130, 246);        // Blue
    public static final Color PRIMARY_DARK = new Color(37, 99, 235);    // Darker blue
    public static final Color SUCCESS = new Color(34, 197, 94);         // Green
    public static final Color WARNING = new Color(234, 179, 8);         // Yellow
    public static final Color DANGER = new Color(239, 68, 68);          // Red
    public static final Color BG_PRIMARY = Color.WHITE;
    public static final Color BG_SECONDARY = new Color(248, 250, 252);  // Light gray
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);     // Dark gray
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128); // Medium gray
    public static final Color BORDER = new Color(229, 231, 235);        // Light border

    public static JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            Color original = bgColor;
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(darker(bgColor));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(original);
            }
        });
        
        return button;
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }

    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(BG_PRIMARY);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }

    public static JLabel createHeading(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setForeground(TEXT_PRIMARY);
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(BG_SECONDARY);
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));
    }

    public static JButton createSidebarButton(String text, String icon) {
        JButton button = new JButton(icon + "  " + text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(TEXT_SECONDARY);
        button.setBackground(BG_PRIMARY);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BG_SECONDARY);
                button.setForeground(PRIMARY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BG_PRIMARY);
                button.setForeground(TEXT_SECONDARY);
            }
        });
        
        return button;
    }

    public static JPanel createTopNav(String title, String subtitle, Runnable logoutAction) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_PRIMARY);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER),
            BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        JLabel titleLabel = new JLabel(title + " • " + subtitle);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(DANGER);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logoutAction.run());

        topPanel.add(titleLabel, "West");
        topPanel.add(logoutButton, "East");

        return topPanel;
    }

    private static Color darker(Color color) {
        return new Color(
            Math.max(color.getRed() - 20, 0),
            Math.max(color.getGreen() - 20, 0),
            Math.max(color.getBlue() - 20, 0)
        );
    }
}
