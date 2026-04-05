package utils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class UIHelper {

    // ===== HIGH-CONTRAST COLOR SCHEME =====
    public static final Color PRIMARY_COLOR   = new Color(41, 128, 185);  // Blue
    public static final Color SUCCESS_COLOR   = new Color(39, 174, 96);   // Green
    public static final Color WARNING_COLOR   = new Color(243, 156, 18);  // Orange
    public static final Color DANGER_COLOR    = new Color(231, 76, 60);   // Red
    public static final Color DARK_COLOR      = new Color(44, 62, 80);    // Dark navy
    public static final Color LIGHT_COLOR     = new Color(236, 240, 241); // Light gray
    public static final Color WHITE_COLOR     = Color.WHITE;
    public static final Color TEXT_DARK       = new Color(33, 33, 33);    // Almost black

    // ✅ These two were missing and causing errors
    public static final Color ACCENT_COLOR    = new Color(243, 156, 18);  // Same as WARNING (nice orange)
    public static final Color INFO_COLOR      = new Color(52, 152, 219);  // Info blue

    // ===== FONTS =====
    public static final Font TITLE_FONT        = new Font("Arial", Font.BOLD, 32);
    public static final Font SUBTITLE_FONT     = new Font("Arial", Font.BOLD, 24);
    public static final Font HEADING_FONT      = new Font("Arial", Font.BOLD, 18);
    public static final Font NORMAL_FONT       = new Font("Arial", Font.PLAIN, 15);
    public static final Font BOLD_FONT         = new Font("Arial", Font.BOLD, 15);
    public static final Font SMALL_FONT        = new Font("Arial", Font.PLAIN, 13);
    public static final Font TABLE_HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font TABLE_CELL_FONT   = new Font("Arial", Font.PLAIN, 15);

    // ===== BUTTONS =====
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BOLD_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);   // always white text on colored button
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = bgColor;
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(original.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(original);
            }
        });

        return button;
    }

    public static JButton createLargeButton(String text, Color bgColor) {
        JButton button = createStyledButton(text, bgColor);
        button.setPreferredSize(new Dimension(220, 50));
        button.setFont(HEADING_FONT);
        return button;
    }

    // ===== TEXT FIELDS =====
    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(NORMAL_FONT);
        field.setPreferredSize(new Dimension(300, 40));
        field.setBackground(WHITE_COLOR);
        field.setForeground(TEXT_DARK);   // dark text
        field.setCaretColor(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    // Overload with placeholder (if some code uses it)
    public static JTextField createStyledTextField(String placeholder) {
        JTextField field = createStyledTextField();
        field.setText(placeholder);
        field.setForeground(new Color(150, 150, 150));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(150, 150, 150));
                }
            }
        });

        return field;
    }

    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(NORMAL_FONT);
        field.setPreferredSize(new Dimension(300, 40));
        field.setBackground(WHITE_COLOR);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setEchoChar('●');
        return field;
    }

    // Overload for compatibility (ignored placeholder)
    public static JPasswordField createStyledPasswordField(String placeholder) {
        return createStyledPasswordField();
    }

    public static JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(NORMAL_FONT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(WHITE_COLOR);
        area.setForeground(TEXT_DARK);
        area.setCaretColor(TEXT_DARK);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return area;
    }

    // ===== LABELS =====
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BOLD_FONT);
        label.setForeground(TEXT_DARK);
        return label;
    }

    public static JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADING_FONT);
        label.setForeground(TEXT_DARK);
        return label;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_DARK);
        return label;
    }

    public static JLabel createWhiteLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BOLD_FONT);
        label.setForeground(Color.WHITE); // for dark backgrounds (headers, dark panels)
        return label;
    }

    // ===== TABLE STYLING (HEADERS VISIBLE) =====
    public static void styleTable(JTable table) {
        table.setFont(TABLE_CELL_FONT);
        table.setRowHeight(50);
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));

        // cells: white / light background, dark text
        table.setBackground(WHITE_COLOR);
        table.setForeground(TEXT_DARK);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(LIGHT_COLOR);   // light header background
        header.setForeground(TEXT_DARK);     // DARK TEXT IN HEADER (no white)
        header.setPreferredSize(new Dimension(0, 50));
        header.setReorderingAllowed(false);

        // zebra cell renderer
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        tbl, value, isSelected, hasFocus, row, column);

                setFont(TABLE_CELL_FONT);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 245, 245));
                    }
                    c.setForeground(TEXT_DARK);  // always dark text
                } else {
                    c.setBackground(PRIMARY_COLOR);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    // ===== GLOBAL THEME =====
    public static void applyGlobalTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("Panel.background", LIGHT_COLOR);
        UIManager.put("Label.foreground", TEXT_DARK);
        UIManager.put("Table.background", WHITE_COLOR);
        UIManager.put("Table.foreground", TEXT_DARK);
        UIManager.put("TableHeader.background", LIGHT_COLOR);
        UIManager.put("TableHeader.foreground", TEXT_DARK);
        UIManager.put("TextField.background", WHITE_COLOR);
        UIManager.put("TextField.foreground", TEXT_DARK);
        UIManager.put("PasswordField.background", WHITE_COLOR);
        UIManager.put("PasswordField.foreground", TEXT_DARK);
        UIManager.put("OptionPane.background", LIGHT_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_DARK);

        // FIX: make default buttons (Yes/No, OK/Cancel) dark text on light background
        UIManager.put("Button.background", LIGHT_COLOR);
        UIManager.put("Button.foreground", TEXT_DARK);
    }

    // Some old code may call this name
    public static void setLookAndFeel() {
        applyGlobalTheme();
    }

    // ===== DIALOG METHODS (used in admin/user files) =====
    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarningMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void showInfoMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showConfirmDialog(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message,
                "Confirm", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    // ===== UTIL =====
    public static void centerFrame(JFrame frame) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(
                (screen.width - frame.getWidth()) / 2,
                (screen.height - frame.getHeight()) / 2
        );
    }
}