//package admin;
//
//import javax.swing.*;
//import java.awt.*;
//import java.sql.*;
//import db.DBConnection;
//import utils.UIHelper;
//import main.MainApp;
//
//public class AdminLogin extends JFrame {
//    
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    private JButton loginButton;
//    private JButton backButton;
//    
//    public AdminLogin() {
//        setTitle("Admin Login - Survey Management System");
//        setSize(900, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
//        
//        initComponents();
//        UIHelper.centerFrame(this);
//    }
//    
//    private void initComponents() {
//        // Main Panel
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
//        
//        // Left Panel - Image/Branding
//        JPanel leftPanel = createLeftPanel();
//        mainPanel.add(leftPanel, BorderLayout.WEST);
//        
//        // Right Panel - Login Form
//        JPanel rightPanel = createRightPanel();
//        mainPanel.add(rightPanel, BorderLayout.CENTER);
//        
//        add(mainPanel);
//    }
//    
//    private JPanel createLeftPanel() {
//        JPanel panel = new JPanel();
//        panel.setBackground(UIHelper.PRIMARY_COLOR);
//        panel.setPreferredSize(new Dimension(400, 600));
//        panel.setLayout(new GridBagLayout());
//        
//        JPanel contentPanel = new JPanel();
//        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
//        contentPanel.setOpaque(false);
//        
//        // Icon
//        JLabel iconLabel = new JLabel("👨‍💼");
//        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
//        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Title
//        JLabel titleLabel = new JLabel("Admin Panel");
//        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
//        titleLabel.setForeground(Color.WHITE);
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Subtitle
//        JLabel subtitleLabel = new JLabel("Survey Management System");
//        subtitleLabel.setFont(UIHelper.SUBTITLE_FONT);
//        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
//        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Info
//        JLabel infoLabel = new JLabel("<html><center>Manage surveys, users,<br>and view analytics</center></html>");
//        infoLabel.setFont(UIHelper.NORMAL_FONT);
//        infoLabel.setForeground(new Color(255, 255, 255, 180));
//        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        contentPanel.add(iconLabel);
//        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
//        contentPanel.add(titleLabel);
//        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//        contentPanel.add(subtitleLabel);
//        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
//        contentPanel.add(infoLabel);
//        
//        panel.add(contentPanel);
//        return panel;
//    }
//    
//    private JPanel createRightPanel() {
//        JPanel panel = new JPanel(new GridBagLayout());
//        panel.setBackground(Color.WHITE);
//        
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        
//        // Welcome Text
//        JLabel welcomeLabel = new JLabel("Welcome Back!");
//        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
//        welcomeLabel.setForeground(UIHelper.DARK_COLOR);
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        panel.add(welcomeLabel, gbc);
//        
//        JLabel instructionLabel = new JLabel("Please login to continue");
//        instructionLabel.setFont(UIHelper.NORMAL_FONT);
//        instructionLabel.setForeground(Color.GRAY);
//        gbc.gridy = 1;
//        panel.add(instructionLabel, gbc);
//        
//        // Spacing
//        gbc.gridy = 2;
//        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
//        
//        // Username Label
//        JLabel usernameLabel = UIHelper.createLabel("Username");
//        gbc.gridy = 3;
//        gbc.gridwidth = 1;
//        panel.add(usernameLabel, gbc);
//        
//        // Username Field
//        usernameField = new JTextField(20);
//        usernameField.setFont(UIHelper.NORMAL_FONT);
//        usernameField.setPreferredSize(new Dimension(300, 40));
//        usernameField.setBorder(BorderFactory.createCompoundBorder(
//            BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2),
//            BorderFactory.createEmptyBorder(5, 10, 5, 10)
//        ));
//        gbc.gridy = 4;
//        gbc.gridwidth = 2;
//        panel.add(usernameField, gbc);
//        
//        // Password Label
//        JLabel passwordLabel = UIHelper.createLabel("Password");
//        gbc.gridy = 5;
//        gbc.gridwidth = 1;
//        panel.add(passwordLabel, gbc);
//        
//        // Password Field
//        passwordField = new JPasswordField(20);
//        passwordField.setFont(UIHelper.NORMAL_FONT);
//        passwordField.setPreferredSize(new Dimension(300, 40));
//        passwordField.setBorder(BorderFactory.createCompoundBorder(
//            BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2),
//            BorderFactory.createEmptyBorder(5, 10, 5, 10)
//        ));
//        passwordField.setEchoChar('●');
//        gbc.gridy = 6;
//        gbc.gridwidth = 2;
//        panel.add(passwordField, gbc);
//        
//        // Spacing
//        gbc.gridy = 7;
//        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
//        
//        // Login Button
//        loginButton = UIHelper.createLargeButton("Login", UIHelper.PRIMARY_COLOR);
//        loginButton.setPreferredSize(new Dimension(300, 50));
//        loginButton.addActionListener(e -> handleLogin());
//        gbc.gridy = 8;
//        panel.add(loginButton, gbc);
//        
//        // Back Button
//        backButton = UIHelper.createStyledButton("← Back to Home", UIHelper.DARK_COLOR);
//        backButton.setPreferredSize(new Dimension(300, 40));
//        backButton.addActionListener(e -> goBack());
//        gbc.gridy = 9;
//        panel.add(backButton, gbc);
//        
//        // Default Credentials Info
//        JLabel credLabel = new JLabel("<html><center><small>Default: admin / admin123</small></center></html>");
//        credLabel.setFont(UIHelper.SMALL_FONT);
//        credLabel.setForeground(Color.GRAY);
//        gbc.gridy = 10;
//        panel.add(credLabel, gbc);
//        
//        // Enter key listener
//        passwordField.addActionListener(e -> handleLogin());
//        
//        return panel;
//    }
//    
//    private void handleLogin() {
//        String username = usernameField.getText().trim();
//        String password = new String(passwordField.getPassword());
//        
//        // Validation
//        if (username.isEmpty()) {
//            UIHelper.showErrorMessage(this, "Please enter username!");
//            usernameField.requestFocus();
//            return;
//        }
//        
//        if (password.isEmpty()) {
//            UIHelper.showErrorMessage(this, "Please enter password!");
//            passwordField.requestFocus();
//            return;
//        }
//        
//        // Database Authentication
//        try (Connection conn = DBConnection.getConnection()) {
//            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
//            PreparedStatement pst = conn.prepareStatement(query);
//            pst.setString(1, username);
//            pst.setString(2, password);
//            
//            ResultSet rs = pst.executeQuery();
//            
//            if (rs.next()) {
//                // Update last login
//                String updateQuery = "UPDATE admin SET last_login = NOW() WHERE admin_id = ?";
//                PreparedStatement updatePst = conn.prepareStatement(updateQuery);
//                updatePst.setInt(1, rs.getInt("admin_id"));
//                updatePst.executeUpdate();
//                
//                // Success - Open Dashboard
//                int adminId = rs.getInt("admin_id");
//                String adminName = rs.getString("full_name");
//                String role = rs.getString("role");
//                
//                UIHelper.showSuccessMessage(this, "Welcome " + adminName + "!");
//                
//                // Open Dashboard
//                new AdminDashboard(adminId, adminName, role).setVisible(true);
//                this.dispose();
//                
//            } else {
//                UIHelper.showErrorMessage(this, "Invalid username or password!");
//                passwordField.setText("");
//                usernameField.requestFocus();
//            }
//            
//        } catch (SQLException e) {
//            UIHelper.showErrorMessage(this, "Database error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//    
//    private void goBack() {
//        new MainApp().setVisible(true);
//        this.dispose();
//    }
//    
//    public static void main(String[] args) {
//        UIHelper.setLookAndFeel();
//        SwingUtilities.invokeLater(() -> new AdminLogin().setVisible(true));
//    }
//}
package admin;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;
import utils.UIHelper;
import main.MainApp;

public class AdminLogin extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    
    public AdminLogin() {
        setTitle("Admin Login - Survey Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        UIHelper.centerFrame(this);
    }
    
    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Left Panel - Image/Branding
        JPanel leftPanel = createLeftPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        
        // Right Panel - Login Form
        JPanel rightPanel = createRightPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIHelper.PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(400, 600));
        panel.setLayout(new GridBagLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Icon
        JLabel iconLabel = new JLabel("👨‍💼");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Survey Management System");
        subtitleLabel.setFont(UIHelper.SUBTITLE_FONT);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Info
        JLabel infoLabel = new JLabel("<html><center>Manage surveys, users,<br>and view analytics</center></html>");
        infoLabel.setFont(UIHelper.NORMAL_FONT);
        infoLabel.setForeground(new Color(255, 255, 255, 180));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(infoLabel);
        
        panel.add(contentPanel);
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Welcome Text
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(UIHelper.DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);
        
        JLabel instructionLabel = new JLabel("Please login to continue");
        instructionLabel.setFont(UIHelper.NORMAL_FONT);
        instructionLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panel.add(instructionLabel, gbc);
        
        // Spacing
        gbc.gridy = 2;
        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        
        // Username Label
        JLabel usernameLabel = UIHelper.createLabel("Username");
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);
        
        // Username Field
        usernameField = new JTextField(20);
        usernameField.setFont(UIHelper.NORMAL_FONT);
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);
        
        // Password Label
        JLabel passwordLabel = UIHelper.createLabel("Password");
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);
        
        // Password Field
        passwordField = new JPasswordField(20);
        passwordField.setFont(UIHelper.NORMAL_FONT);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setEchoChar('●');
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(passwordField, gbc);
        
        // Spacing
        gbc.gridy = 7;
        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        
        // Login Button
        loginButton = UIHelper.createLargeButton("Login", UIHelper.PRIMARY_COLOR);
        loginButton.setPreferredSize(new Dimension(300, 50));
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridy = 8;
        panel.add(loginButton, gbc);
        
        // Back Button
        backButton = UIHelper.createStyledButton("← Back to Home", UIHelper.DARK_COLOR);
        backButton.setPreferredSize(new Dimension(300, 40));
        backButton.addActionListener(e -> goBack());
        gbc.gridy = 9;
        panel.add(backButton, gbc);
        
        // Default Credentials Info
        JLabel credLabel = new JLabel("<html><center><small>Default: admin / admin123</small></center></html>");
        credLabel.setFont(UIHelper.SMALL_FONT);
        credLabel.setForeground(Color.GRAY);
        gbc.gridy = 10;
        panel.add(credLabel, gbc);
        
        // Enter key listener
        passwordField.addActionListener(e -> handleLogin());
        
        return panel;
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validation
        if (username.isEmpty()) {
            UIHelper.showErrorMessage(this, "Please enter username!");
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            UIHelper.showErrorMessage(this, "Please enter password!");
            passwordField.requestFocus();
            return;
        }
        
        // Database Authentication
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                // Update last login
                String updateQuery = "UPDATE admin SET last_login = NOW() WHERE admin_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateQuery);
                updatePst.setInt(1, rs.getInt("admin_id"));
                updatePst.executeUpdate();
                
                // Success - Open Dashboard
                int adminId = rs.getInt("admin_id");
                String adminName = rs.getString("full_name");
                String role = rs.getString("role");
                
                UIHelper.showSuccessMessage(this, "Welcome " + adminName + "!");
                
                // Open Dashboard
                new AdminDashboard(adminId, adminName, role).setVisible(true);
                this.dispose();
                
            } else {
                UIHelper.showErrorMessage(this, "Invalid username or password!");
                passwordField.setText("");
                usernameField.requestFocus();
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void goBack() {
        new MainApp().setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        UIHelper.setLookAndFeel();
        SwingUtilities.invokeLater(() -> new AdminLogin().setVisible(true));
    }
}