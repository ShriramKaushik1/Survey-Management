package admin;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;
import utils.UIHelper;
import main.MainApp;

public class AdminDashboard extends JFrame {
    
    private int adminId;
    private String adminName;
    private String adminRole;
    
    private JLabel totalSurveysLabel;
    private JLabel totalUsersLabel;
    private JLabel totalResponsesLabel;
    private JLabel activeSurveysLabel;
    
    public AdminDashboard(int adminId, String adminName, String adminRole) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminRole = adminRole;
        
        setTitle("Admin Dashboard - Survey Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        loadStatistics();
        UIHelper.centerFrame(this);
    }
    
    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Top Panel - Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Left Panel - Menu
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        
        // Center Panel - Dashboard Content
        mainPanel.add(createDashboardContent(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIHelper.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        // Left - Title
        JLabel titleLabel = new JLabel("📊 Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        // Right - User Info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 " + adminName + " (" + adminRole + ")");
        userLabel.setFont(UIHelper.NORMAL_FONT);
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = UIHelper.createStyledButton("Logout", UIHelper.DANGER_COLOR);
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.addActionListener(e -> logout());
        
        userPanel.add(userLabel);
        userPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        userPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIHelper.DARK_COLOR);
        sidebar.setPreferredSize(new Dimension(250, 720));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Menu Items
        sidebar.add(createMenuButton("🏠 Dashboard", UIHelper.PRIMARY_COLOR, 
            () -> refreshDashboard()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(createMenuButton("📝 Manage Surveys", UIHelper.SUCCESS_COLOR, 
            () -> openManageSurveys()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(createMenuButton("➕ Create Survey", UIHelper.INFO_COLOR, 
            () -> openCreateSurvey()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(createMenuButton("📊 View Responses", UIHelper.WARNING_COLOR, 
            () -> openViewResponses()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(createMenuButton("👥 Manage Users", UIHelper.ACCENT_COLOR, 
            () -> openManageUsers()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(createMenuButton("⚙️ My Profile", UIHelper.DARK_COLOR.brighter(), 
            () -> openProfile()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(Box.createVerticalGlue());
        
        sidebar.add(createMenuButton("🚪 Logout", UIHelper.DANGER_COLOR, 
            () -> logout()));
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, Color color, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(UIHelper.NORMAL_FONT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(230, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        button.addActionListener(e -> action.run());
        
        return button;
    }
    
    private JPanel createDashboardContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIHelper.LIGHT_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Welcome Message
        JLabel welcomeLabel = new JLabel("Welcome back, " + adminName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(UIHelper.DARK_COLOR);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(welcomeLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Statistics Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        statsPanel.setBackground(UIHelper.LIGHT_COLOR);
        statsPanel.setMaximumSize(new Dimension(1100, 150));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Card 1: Total Surveys
        JPanel card1 = createStatCard("📝", "Total Surveys", "0", UIHelper.PRIMARY_COLOR);
        totalSurveysLabel = (JLabel) ((JPanel)card1.getComponent(0)).getComponent(2);
        statsPanel.add(card1);
        
        // Card 2: Active Surveys
        JPanel card2 = createStatCard("✅", "Active Surveys", "0", UIHelper.SUCCESS_COLOR);
        activeSurveysLabel = (JLabel) ((JPanel)card2.getComponent(0)).getComponent(2);
        statsPanel.add(card2);
        
        // Card 3: Total Users
        JPanel card3 = createStatCard("👥", "Total Users", "0", UIHelper.INFO_COLOR);
        totalUsersLabel = (JLabel) ((JPanel)card3.getComponent(0)).getComponent(2);
        statsPanel.add(card3);
        
        // Card 4: Total Responses
        JPanel card4 = createStatCard("💬", "Total Responses", "0", UIHelper.WARNING_COLOR);
        totalResponsesLabel = (JLabel) ((JPanel)card4.getComponent(0)).getComponent(2);
        statsPanel.add(card4);
        
        contentPanel.add(statsPanel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Quick Actions
        JLabel actionsLabel = UIHelper.createHeadingLabel("Quick Actions");
        actionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(actionsLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        actionsPanel.setBackground(UIHelper.LIGHT_COLOR);
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton createSurveyBtn = UIHelper.createLargeButton("➕ Create New Survey", UIHelper.SUCCESS_COLOR);
        createSurveyBtn.addActionListener(e -> openCreateSurvey());
        
        JButton viewSurveysBtn = UIHelper.createLargeButton("📝 View All Surveys", UIHelper.PRIMARY_COLOR);
        viewSurveysBtn.addActionListener(e -> openManageSurveys());
        
        JButton viewUsersBtn = UIHelper.createLargeButton("👥 Manage Users", UIHelper.INFO_COLOR);
        viewUsersBtn.addActionListener(e -> openManageUsers());
        
        actionsPanel.add(createSurveyBtn);
        actionsPanel.add(viewSurveysBtn);
        actionsPanel.add(viewUsersBtn);
        
        contentPanel.add(actionsPanel);
        
        contentPanel.add(Box.createVerticalGlue());
        
        return contentPanel;
    }
    
    private JPanel createStatCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIHelper.NORMAL_FONT);
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(titleLabel);
        
        card.add(contentPanel);
        return card;
    }
    
    private void loadStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            // Total Surveys
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM survey");
            if (rs.next()) {
                totalSurveysLabel.setText(String.valueOf(rs.getInt("count")));
            }
            
            // Active Surveys
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM survey WHERE status = 'Active'");
            if (rs.next()) {
                activeSurveysLabel.setText(String.valueOf(rs.getInt("count")));
            }
            
            // Total Users
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next()) {
                totalUsersLabel.setText(String.valueOf(rs.getInt("count")));
            }
            
            // Total Responses
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM survey_submissions");
            if (rs.next()) {
                totalResponsesLabel.setText(String.valueOf(rs.getInt("count")));
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void refreshDashboard() {
        loadStatistics();
        UIHelper.showInfoMessage(this, "Dashboard refreshed!");
    }
    
    private void openManageSurveys() {
        new ManageSurveys(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
    
    private void openCreateSurvey() {
        new CreateSurvey(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
    
    private void openViewResponses() {
        new ViewResponses(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
    
    private void openManageUsers() {
        new ManageUsers(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
    
    private void openProfile() {
        new AdminProfile(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
    
    private void logout() {
        if (UIHelper.showConfirmDialog(this, "Are you sure you want to logout?")) {
            new MainApp().setVisible(true);
            this.dispose();
        }
    }
}