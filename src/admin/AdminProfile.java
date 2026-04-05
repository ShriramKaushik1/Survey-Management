package admin;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;
import utils.UIHelper;

public class AdminProfile extends JFrame {
    
    private int adminId;
    private String adminName;
    private String adminRole;
    
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    
    public AdminProfile(int adminId, String adminName, String adminRole) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminRole = adminRole;
        
        setTitle("Admin Profile - Survey Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        loadAdminData();
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
        
        JLabel titleLabel = new JLabel("⚙️ Admin Profile Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JButton backButton = UIHelper.createStyledButton("← Back to Dashboard", UIHelper.PRIMARY_COLOR);
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
            BorderFactory.createLineBorder(UIHelper.PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel profileIcon = new JLabel("👨‍💼");
        profileIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(adminName);
        nameLabel.setFont(UIHelper.SUBTITLE_FONT);
        nameLabel.setForeground(UIHelper.DARK_COLOR);
        
        JLabel roleLabel = new JLabel(adminRole);
        roleLabel.setFont(UIHelper.NORMAL_FONT);
        roleLabel.setForeground(UIHelper.PRIMARY_COLOR);
        
        infoPanel.add(nameLabel);
        infoPanel.add(roleLabel);
        
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
        panel.setMaximumSize(new Dimension(800, 300));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.SUCCESS_COLOR, 2),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                "Profile Information",
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
        usernameField.setPreferredSize(new Dimension(300, 35));
        usernameField.setEnabled(false); // Username cannot be changed
        panel.add(usernameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(UIHelper.createLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(UIHelper.NORMAL_FONT);
        emailField.setPreferredSize(new Dimension(300, 35));
        panel.add(emailField, gbc);
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(UIHelper.createLabel("Full Name:"), gbc);
        
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        fullNameField.setFont(UIHelper.NORMAL_FONT);
        fullNameField.setPreferredSize(new Dimension(300, 35));
        panel.add(fullNameField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(UIHelper.createLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        phoneField.setFont(UIHelper.NORMAL_FONT);
        phoneField.setPreferredSize(new Dimension(300, 35));
        panel.add(phoneField, gbc);
        
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
        currentPasswordField.setPreferredSize(new Dimension(300, 35));
        panel.add(currentPasswordField, gbc);
        
        // New Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(UIHelper.createLabel("New Password:"), gbc);
        
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(UIHelper.NORMAL_FONT);
        newPasswordField.setPreferredSize(new Dimension(300, 35));
        panel.add(newPasswordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(UIHelper.createLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(UIHelper.NORMAL_FONT);
        confirmPasswordField.setPreferredSize(new Dimension(300, 35));
        panel.add(confirmPasswordField, gbc);
        
        return panel;
    }
    
    private void loadAdminData() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM admin WHERE admin_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, adminId);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                emailField.setText(rs.getString("email"));
                fullNameField.setText(rs.getString("full_name"));
                phoneField.setText(rs.getString("phone"));
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading profile: " + e.getMessage());
        }
    }
    
    private void updateProfile() {
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (email.isEmpty() || fullName.isEmpty()) {
            UIHelper.showErrorMessage(this, "Email and Full Name are required!");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "UPDATE admin SET email = ?, full_name = ?, phone = ? WHERE admin_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, fullName);
            pst.setString(3, phone);
            pst.setInt(4, adminId);
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                UIHelper.showSuccessMessage(this, "Profile updated successfully!");
                this.adminName = fullName;
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
            String verifyQuery = "SELECT password FROM admin WHERE admin_id = ?";
            PreparedStatement verifyPst = conn.prepareStatement(verifyQuery);
            verifyPst.setInt(1, adminId);
            ResultSet rs = verifyPst.executeQuery();
            
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                
                if (!currentPassword.equals(dbPassword)) {
                    UIHelper.showErrorMessage(this, "Current password is incorrect!");
                    currentPasswordField.setText("");
                    return;
                }
                
                // Update password
                String updateQuery = "UPDATE admin SET password = ? WHERE admin_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateQuery);
                updatePst.setString(1, newPassword);
                updatePst.setInt(2, adminId);
                
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
        new AdminDashboard(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
}