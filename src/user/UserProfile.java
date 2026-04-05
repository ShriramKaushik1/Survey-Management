package user;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;
import utils.UIHelper;

public class UserProfile extends JFrame {
    
    private int userId;
    private String userName;
    private String userEmail;
    
    private JTextField usernameField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> genderCombo;
    private JTextField cityField;
    private JTextField countryField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    
    public UserProfile(int userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        
        setTitle("My Profile - Survey Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        loadUserData();
        UIHelper.centerFrame(this);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Content
        mainPanel.add(createContent(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIHelper.DARK_COLOR);
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("⚙️ My Profile Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JButton backButton = UIHelper.createStyledButton("← Back to Dashboard", UIHelper.INFO_COLOR);
        backButton.addActionListener(e -> goBack());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIHelper.LIGHT_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        
        // Profile Picture Section
        JPanel picturePanel = new JPanel();
        picturePanel.setBackground(Color.WHITE);
        picturePanel.setMaximumSize(new Dimension(800, 150));
        picturePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        picturePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.INFO_COLOR, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel profileIcon = new JLabel("👤");
        profileIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(userName);
        nameLabel.setFont(UIHelper.SUBTITLE_FONT);
        nameLabel.setForeground(UIHelper.DARK_COLOR);
        
        JLabel emailLabel = new JLabel(userEmail);
        emailLabel.setFont(UIHelper.NORMAL_FONT);
        emailLabel.setForeground(UIHelper.INFO_COLOR);
        
        infoPanel.add(nameLabel);
        infoPanel.add(emailLabel);
        
        picturePanel.add(profileIcon);
        picturePanel.add(Box.createRigidArea(new Dimension(30, 0)));
        picturePanel.add(infoPanel);
        
        contentPanel.add(picturePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Profile Information Panel
        JPanel profileInfoPanel = createProfileInfoPanel();
        profileInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(profileInfoPanel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Change Password Panel
        JPanel passwordPanel = createPasswordPanel();
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(passwordPanel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(UIHelper.LIGHT_COLOR);
        buttonPanel.setMaximumSize(new Dimension(800, 60));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton updateBtn = UIHelper.createLargeButton("💾 Update Profile", UIHelper.SUCCESS_COLOR);
        updateBtn.addActionListener(e -> updateProfile());
        
        JButton changePasswordBtn = UIHelper.createLargeButton("🔐 Change Password", UIHelper.WARNING_COLOR);
        changePasswordBtn.addActionListener(e -> changePassword());
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(changePasswordBtn);
        
        contentPanel.add(buttonPanel);
        
        return contentPanel;
    }
    
    private JPanel createProfileInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(800, 350));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.SUCCESS_COLOR, 2),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                "Personal Information",
                0, 0,
                UIHelper.HEADING_FONT,
                UIHelper.SUCCESS_COLOR
            )
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(UIHelper.createLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(UIHelper.NORMAL_FONT);
        usernameField.setEnabled(false);
        panel.add(usernameField, gbc);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(UIHelper.createLabel("Full Name:"), gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(UIHelper.NORMAL_FONT);
        panel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(UIHelper.createLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(UIHelper.NORMAL_FONT);
        panel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(UIHelper.createLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        phoneField.setFont(UIHelper.NORMAL_FONT);
        panel.add(phoneField, gbc);
        
        // Gender
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(UIHelper.createLabel("Gender:"), gbc);
        
        gbc.gridx = 1;
        genderCombo = new JComboBox<>(new String[]{"Select", "Male", "Female", "Other"});
        genderCombo.setFont(UIHelper.NORMAL_FONT);
        panel.add(genderCombo, gbc);
        
        // City
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(UIHelper.createLabel("City:"), gbc);
        
        gbc.gridx = 1;
        cityField = new JTextField(20);
        cityField.setFont(UIHelper.NORMAL_FONT);
        panel.add(cityField, gbc);
        
        // Country
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(UIHelper.createLabel("Country:"), gbc);
        
        gbc.gridx = 1;
        countryField = new JTextField(20);
        countryField.setFont(UIHelper.NORMAL_FONT);
        panel.add(countryField, gbc);
        
        return panel;
    }
    
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(800, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.WARNING_COLOR, 2),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                "Change Password",
                0, 0,
                UIHelper.HEADING_FONT,
                UIHelper.WARNING_COLOR
            )
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Current Password
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(UIHelper.createLabel("Current Password:"), gbc);
        
        gbc.gridx = 1;
        currentPasswordField = new JPasswordField(20);
        currentPasswordField.setFont(UIHelper.NORMAL_FONT);
        panel.add(currentPasswordField, gbc);
        
        // New Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(UIHelper.createLabel("New Password:"), gbc);
        
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(UIHelper.NORMAL_FONT);
        panel.add(newPasswordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(UIHelper.createLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(UIHelper.NORMAL_FONT);
        panel.add(confirmPasswordField, gbc);
        
        return panel;
    }
    
    private void loadUserData() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                
                String gender = rs.getString("gender");
                if (gender != null) {
                    genderCombo.setSelectedItem(gender);
                }
                
                cityField.setText(rs.getString("city"));
                countryField.setText(rs.getString("country"));
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading profile: " + e.getMessage());
        }
    }
    
    private void updateProfile() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String city = cityField.getText().trim();
        String country = countryField.getText().trim();
        
        if (name.isEmpty() || email.isEmpty()) {
            UIHelper.showErrorMessage(this, "Name and Email are required!");
            return;
        }
        
        if (gender.equals("Select")) {
            gender = null;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "UPDATE users SET name = ?, email = ?, phone = ?, " +
                          "gender = ?, city = ?, country = ? WHERE user_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone.isEmpty() ? null : phone);
            pst.setString(4, gender);
            pst.setString(5, city.isEmpty() ? null : city);
            pst.setString(6, country.isEmpty() ? null : country);
            pst.setInt(7, userId);
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                this.userName = name;
                this.userEmail = email;
                UIHelper.showSuccessMessage(this, "Profile updated successfully!");
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error updating profile: " + e.getMessage());
        }
    }
    
    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            UIHelper.showErrorMessage(this, "All password fields are required!");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            UIHelper.showErrorMessage(this, "New password and confirm password do not match!");
            return;
        }
        
        if (newPassword.length() < 6) {
            UIHelper.showErrorMessage(this, "New password must be at least 6 characters long!");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            // Verify current password
            String verifyQuery = "SELECT password FROM users WHERE user_id = ?";
            PreparedStatement verifyPst = conn.prepareStatement(verifyQuery);
            verifyPst.setInt(1, userId);
            ResultSet rs = verifyPst.executeQuery();
            
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                
                if (!currentPassword.equals(dbPassword)) {
                    UIHelper.showErrorMessage(this, "Current password is incorrect!");
                    currentPasswordField.setText("");
                    return;
                }
                
                // Update password
                String updateQuery = "UPDATE users SET password = ? WHERE user_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateQuery);
                updatePst.setString(1, newPassword);
                updatePst.setInt(2, userId);
                
                int result = updatePst.executeUpdate();
                
                if (result > 0) {
                    UIHelper.showSuccessMessage(this, "Password changed successfully!");
                    currentPasswordField.setText("");
                    newPasswordField.setText("");
                    confirmPasswordField.setText("");
                }
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error changing password: " + e.getMessage());
        }
    }
    
    private void goBack() {
        new UserDashboard(userId, userName, userEmail).setVisible(true);
        this.dispose();
    }
}