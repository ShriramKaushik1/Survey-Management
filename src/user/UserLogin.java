package user;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;
import utils.UIHelper;
import main.MainApp;

public class UserLogin extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public UserLogin() {
        setTitle("User Login - Survey Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        UIHelper.centerFrame(this);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Left Panel - Branding
        JPanel leftPanel = createLeftPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        
        // Right Panel - Login Form
        JPanel rightPanel = createRightPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIHelper.INFO_COLOR);
        panel.setPreferredSize(new Dimension(400, 600));
        panel.setLayout(new GridBagLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("User Portal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Survey Management System");
        subtitleLabel.setFont(UIHelper.SUBTITLE_FONT);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("<html><center>Login to access surveys,<br>earn points and rewards</center></html>");
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
        
        gbc.gridy = 2;
        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        
        // Username
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(UIHelper.createLabel("Username or Email"), gbc);
        
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        usernameField = new JTextField(25);
        usernameField.setFont(UIHelper.NORMAL_FONT);
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(UIHelper.createLabel("Password"), gbc);
        
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField(25);
        passwordField.setFont(UIHelper.NORMAL_FONT);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setEchoChar('●');
        panel.add(passwordField, gbc);
        
        gbc.gridy = 7;
        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        
        // Login Button
        gbc.gridy = 8;
        JButton loginBtn = UIHelper.createLargeButton("Login", UIHelper.INFO_COLOR);
        loginBtn.setPreferredSize(new Dimension(300, 50));
        loginBtn.addActionListener(e -> handleLogin());
        panel.add(loginBtn, gbc);
        
        // Register Button
        gbc.gridy = 9;
        JButton registerBtn = UIHelper.createStyledButton("Don't have an account? Register", UIHelper.SUCCESS_COLOR);
        registerBtn.setPreferredSize(new Dimension(300, 40));
        registerBtn.addActionListener(e -> openRegistration());
        panel.add(registerBtn, gbc);
        
        // Back Button
        gbc.gridy = 10;
        JButton backBtn = UIHelper.createStyledButton("← Back to Home", UIHelper.DARK_COLOR);
        backBtn.setPreferredSize(new Dimension(300, 40));
        backBtn.addActionListener(e -> goBack());
        panel.add(backBtn, gbc);
        
        // Demo credentials
        gbc.gridy = 11;
        JLabel demoLabel = new JLabel("<html><center><small>Demo: john_doe / user123</small></center></html>");
        demoLabel.setFont(UIHelper.SMALL_FONT);
        demoLabel.setForeground(Color.GRAY);
        panel.add(demoLabel, gbc);
        
        // Enter key listener
        passwordField.addActionListener(e -> handleLogin());
        
        return panel;
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty()) {
            UIHelper.showErrorMessage(this, "Please enter username or email!");
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            UIHelper.showErrorMessage(this, "Please enter password!");
            passwordField.requestFocus();
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, username);
            pst.setString(3, password);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                // Check account status
                String status = rs.getString("account_status");
                if (status.equals("Suspended")) {
                    UIHelper.showErrorMessage(this, "Your account has been suspended. Please contact admin.");
                    return;
                }
                
                if (status.equals("Inactive")) {
                    UIHelper.showErrorMessage(this, "Your account is inactive. Please contact admin.");
                    return;
                }
                
                // Update last login
                int userId = rs.getInt("user_id");
                String updateQuery = "UPDATE users SET last_login = NOW() WHERE user_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateQuery);
                updatePst.setInt(1, userId);
                updatePst.executeUpdate();
                
                // Get user details
                String name = rs.getString("name");
                String email = rs.getString("email");
                
                UIHelper.showSuccessMessage(this, "Welcome " + name + "!");
                
                // Open User Dashboard
                new UserDashboard(userId, name, email).setVisible(true);
                this.dispose();
                
            } else {
                UIHelper.showErrorMessage(this, "Invalid username/email or password!");
                passwordField.setText("");
                usernameField.requestFocus();
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void openRegistration() {
        new UserRegistration().setVisible(true);
        this.dispose();
    }
    
    private void goBack() {
        new MainApp().setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        UIHelper.setLookAndFeel();
        SwingUtilities.invokeLater(() -> new UserLogin().setVisible(true));
    }
}