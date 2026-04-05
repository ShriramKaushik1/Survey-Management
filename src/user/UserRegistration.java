package user;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;
import utils.UIHelper;
import main.MainApp;

public class UserRegistration extends JFrame {
    
    private JTextField usernameField;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField phoneField;
    private JComboBox<String> genderCombo;
    private JTextField cityField;
    private JTextField countryField;
    
    public UserRegistration() {
        setTitle("User Registration - Survey Management System");
        setSize(1000, 750);
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
        
        // Right Panel - Registration Form
        JPanel rightPanel = createRightPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIHelper.SUCCESS_COLOR);
        panel.setPreferredSize(new Dimension(350, 750));
        panel.setLayout(new GridBagLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("📝");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Join Us!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Create your account");
        subtitleLabel.setFont(UIHelper.SUBTITLE_FONT);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel benefitsLabel = new JLabel("<html><center>" +
            "✓ Take surveys and earn points<br>" +
            "✓ Redeem rewards<br>" +
            "✓ Track your progress<br>" +
            "✓ Join our community" +
            "</center></html>");
        benefitsLabel.setFont(UIHelper.NORMAL_FONT);
        benefitsLabel.setForeground(new Color(255, 255, 255, 180));
        benefitsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(benefitsLabel);
        
        panel.add(contentPanel);
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(UIHelper.DARK_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Fill in the details below");
        subtitleLabel.setFont(UIHelper.NORMAL_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subtitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(500, 600));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(UIHelper.createLabel("Username *"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(UIHelper.NORMAL_FONT);
        usernameField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(usernameField, gbc);
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(UIHelper.createLabel("Full Name *"), gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(UIHelper.NORMAL_FONT);
        nameField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(UIHelper.createLabel("Email *"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(UIHelper.NORMAL_FONT);
        emailField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(emailField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(UIHelper.createLabel("Password *"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(UIHelper.NORMAL_FONT);
        passwordField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(UIHelper.createLabel("Confirm Password *"), gbc);
        
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(UIHelper.NORMAL_FONT);
        confirmPasswordField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(confirmPasswordField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(UIHelper.createLabel("Phone"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        phoneField.setFont(UIHelper.NORMAL_FONT);
        phoneField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(phoneField, gbc);
        
        // Gender
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(UIHelper.createLabel("Gender"), gbc);
        
        gbc.gridx = 1;
        genderCombo = new JComboBox<>(new String[]{"Select", "Male", "Female", "Other"});
        genderCombo.setFont(UIHelper.NORMAL_FONT);
        genderCombo.setPreferredSize(new Dimension(250, 35));
        formPanel.add(genderCombo, gbc);
        
        // City
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(UIHelper.createLabel("City"), gbc);
        
        gbc.gridx = 1;
        cityField = new JTextField(20);
        cityField.setFont(UIHelper.NORMAL_FONT);
        cityField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(cityField, gbc);
        
        // Country
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(UIHelper.createLabel("Country"), gbc);
        
        gbc.gridx = 1;
        countryField = new JTextField(20);
        countryField.setFont(UIHelper.NORMAL_FONT);
        countryField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(countryField, gbc);
        
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(500, 60));
        
        JButton registerBtn = UIHelper.createLargeButton("Register", UIHelper.SUCCESS_COLOR);
        registerBtn.setPreferredSize(new Dimension(150, 45));
        registerBtn.addActionListener(e -> handleRegistration());
        
        JButton loginBtn = UIHelper.createStyledButton("Already have account? Login", UIHelper.INFO_COLOR);
        loginBtn.setPreferredSize(new Dimension(220, 45));
        loginBtn.addActionListener(e -> openLogin());
        
        JButton backBtn = UIHelper.createStyledButton("← Back", UIHelper.DARK_COLOR);
        backBtn.addActionListener(e -> goBack());
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(loginBtn);
        buttonPanel.add(backBtn);
        
        panel.add(buttonPanel);
        
        return panel;
    }
    
    private void handleRegistration() {
        // Get field values
        String username = usernameField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String phone = phoneField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String city = cityField.getText().trim();
        String country = countryField.getText().trim();
        
        // Validation
        if (username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            UIHelper.showErrorMessage(this, "Please fill all required fields (marked with *)!");
            return;
        }
        
        if (username.length() < 4) {
            UIHelper.showErrorMessage(this, "Username must be at least 4 characters long!");
            usernameField.requestFocus();
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            UIHelper.showErrorMessage(this, "Please enter a valid email address!");
            emailField.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            UIHelper.showErrorMessage(this, "Password must be at least 6 characters long!");
            passwordField.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            UIHelper.showErrorMessage(this, "Passwords do not match!");
            confirmPasswordField.setText("");
            confirmPasswordField.requestFocus();
            return;
        }
        
        if (gender.equals("Select")) {
            gender = null;
        }
        
        // Database insertion
        try (Connection conn = DBConnection.getConnection()) {
            // Check if username exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkQuery);
            checkPst.setString(1, username);
            checkPst.setString(2, email);
            ResultSet rs = checkPst.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                UIHelper.showErrorMessage(this, "Username or Email already exists!");
                return;
            }
            
            // Insert user
            String insertQuery = "INSERT INTO users (username, name, email, password, phone, gender, city, country) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pst = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, username);
            pst.setString(2, name);
            pst.setString(3, email);
            pst.setString(4, password);
            pst.setString(5, phone.isEmpty() ? null : phone);
            pst.setString(6, gender);
            pst.setString(7, city.isEmpty() ? null : city);
            pst.setString(8, country.isEmpty() ? null : country);
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                // Get generated user ID
                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    
                    // Initialize user rewards
                    String rewardQuery = "INSERT INTO user_rewards (user_id, current_balance) VALUES (?, 0)";
                    PreparedStatement rewardPst = conn.prepareStatement(rewardQuery);
                    rewardPst.setInt(1, userId);
                    rewardPst.executeUpdate();
                    
                    // Create welcome notification
                    String notifQuery = "INSERT INTO notifications (user_id, title, message, type) " +
                                       "VALUES (?, ?, ?, ?)";
                    PreparedStatement notifPst = conn.prepareStatement(notifQuery);
                    notifPst.setInt(1, userId);
                    notifPst.setString(2, "Welcome to Survey System!");
                    notifPst.setString(3, "Thank you for registering. Start taking surveys to earn points!");
                    notifPst.setString(4, "System");
                    notifPst.executeUpdate();
                }
                
                UIHelper.showSuccessMessage(this, "Registration successful!\nYou can now login with your credentials.");
                openLogin();
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void openLogin() {
        new UserLogin().setVisible(true);
        this.dispose();
    }
    
    private void goBack() {
        new MainApp().setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        UIHelper.setLookAndFeel();
        SwingUtilities.invokeLater(() -> new UserRegistration().setVisible(true));
    }
}