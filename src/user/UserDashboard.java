package user;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;
import utils.UIHelper;
import main.MainApp;

public class UserDashboard extends JFrame {
    
    private int userId;
    private String userName;
    private String userEmail;
    
    private JLabel totalSurveysTakenLabel;
    private JLabel totalPointsLabel;
    private JLabel availableSurveysLabel;
    private JLabel recentActivityLabel;
    
    public UserDashboard(int userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        
        setTitle("User Dashboard - Survey Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        loadStatistics();
        UIHelper.centerFrame(this);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createContent(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIHelper.INFO_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("📊 User Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 " + userName);
        userLabel.setFont(UIHelper.NORMAL_FONT);
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutBtn = UIHelper.createStyledButton("Logout", UIHelper.DANGER_COLOR);
        logoutBtn.setPreferredSize(new Dimension(100, 35));
        logoutBtn.addActionListener(e -> logout());
        
        userPanel.add(userLabel);
        userPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        userPanel.add(logoutBtn);
        
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
        
        sidebar.add(createMenuButton("🏠 Dashboard", UIHelper.INFO_COLOR, () -> refreshDashboard()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("📝 Available Surveys", UIHelper.SUCCESS_COLOR, () -> openAvailableSurveys()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("✅ My Surveys", UIHelper.PRIMARY_COLOR, () -> openMySurveys()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("🎁 My Rewards", UIHelper.WARNING_COLOR, () -> openMyRewards()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("⚙️ My Profile", UIHelper.DARK_COLOR.brighter(), () -> openProfile()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createMenuButton("🚪 Logout", UIHelper.DANGER_COLOR, () -> logout()));
        
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
    
    private JPanel createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIHelper.LIGHT_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + userName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(UIHelper.DARK_COLOR);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        statsPanel.setBackground(UIHelper.LIGHT_COLOR);
        statsPanel.setMaximumSize(new Dimension(1100, 150));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel card1 = createStatCard("✅", "Surveys Taken", "0", UIHelper.SUCCESS_COLOR);
        totalSurveysTakenLabel = (JLabel) ((JPanel)card1.getComponent(0)).getComponent(2);
        statsPanel.add(card1);
        
        JPanel card2 = createStatCard("🎁", "Total Points", "0", UIHelper.WARNING_COLOR);
        totalPointsLabel = (JLabel) ((JPanel)card2.getComponent(0)).getComponent(2);
        statsPanel.add(card2);
        
        JPanel card3 = createStatCard("📝", "Available Surveys", "0", UIHelper.INFO_COLOR);
        availableSurveysLabel = (JLabel) ((JPanel)card3.getComponent(0)).getComponent(2);
        statsPanel.add(card3);
        
        JPanel card4 = createStatCard("📈", "This Month", "0", UIHelper.PRIMARY_COLOR);
        recentActivityLabel = (JLabel) ((JPanel)card4.getComponent(0)).getComponent(2);
        statsPanel.add(card4);
        
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JLabel actionsLabel = UIHelper.createHeadingLabel("Quick Actions");
        actionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(actionsLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        actionsPanel.setBackground(UIHelper.LIGHT_COLOR);
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton takeSurveyBtn = UIHelper.createLargeButton("📝 Take New Survey", UIHelper.SUCCESS_COLOR);
        takeSurveyBtn.addActionListener(e -> openAvailableSurveys());
        
        JButton viewSurveysBtn = UIHelper.createLargeButton("✅ My Completed Surveys", UIHelper.PRIMARY_COLOR);
        viewSurveysBtn.addActionListener(e -> openMySurveys());
        
        JButton viewRewardsBtn = UIHelper.createLargeButton("🎁 View My Rewards", UIHelper.WARNING_COLOR);
        viewRewardsBtn.addActionListener(e -> openMyRewards());
        
        actionsPanel.add(takeSurveyBtn);
        actionsPanel.add(viewSurveysBtn);
        actionsPanel.add(viewRewardsBtn);
        
        contentPanel.add(actionsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JLabel recentLabel = UIHelper.createHeadingLabel("Recent Surveys");
        recentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(recentLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel recentPanel = createRecentSurveysPanel();
        recentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(recentPanel);
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
    
    private JPanel createRecentSurveysPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(1100, 200));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT s.title, ss.submitted_at, ss.points_earned " +
                          "FROM survey_submissions ss " +
                          "JOIN survey s ON ss.survey_id = s.survey_id " +
                          "WHERE ss.user_id = ? " +
                          "ORDER BY ss.submitted_at DESC LIMIT 3";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            
            if (!rs.isBeforeFirst()) {
                JLabel noDataLabel = new JLabel("No surveys taken yet. Start taking surveys to earn points!");
                noDataLabel.setFont(UIHelper.NORMAL_FONT);
                noDataLabel.setForeground(Color.GRAY);
                panel.add(noDataLabel);
            } else {
                while (rs.next()) {
                    JPanel surveyItem = new JPanel(new BorderLayout());
                    surveyItem.setBackground(Color.WHITE);
                    surveyItem.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                    
                    JLabel titleLabel = new JLabel("📝 " + rs.getString("title"));
                    titleLabel.setFont(UIHelper.NORMAL_FONT);
                    
                    JLabel dateLabel = new JLabel(rs.getTimestamp("submitted_at").toString());
                    dateLabel.setFont(UIHelper.SMALL_FONT);
                    dateLabel.setForeground(Color.GRAY);
                    
                    JLabel pointsLabel = new JLabel("+" + rs.getInt("points_earned") + " points");
                    pointsLabel.setFont(UIHelper.BOLD_FONT);
                    pointsLabel.setForeground(UIHelper.SUCCESS_COLOR);
                    
                    surveyItem.add(titleLabel, BorderLayout.WEST);
                    surveyItem.add(dateLabel, BorderLayout.CENTER);
                    surveyItem.add(pointsLabel, BorderLayout.EAST);
                    
                    panel.add(surveyItem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading recent surveys: " + e.getMessage());
            e.printStackTrace();
        }
        
        return panel;
    }
    
    private void loadStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            
            // Total Surveys Taken
            String query1 = "SELECT total_surveys_taken FROM users WHERE user_id = ?";
            PreparedStatement pst1 = conn.prepareStatement(query1);
            pst1.setInt(1, userId);
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                totalSurveysTakenLabel.setText(String.valueOf(rs1.getInt("total_surveys_taken")));
            }
            
            // Total Points
            String query2 = "SELECT current_balance FROM user_rewards WHERE user_id = ?";
            PreparedStatement pst2 = conn.prepareStatement(query2);
            pst2.setInt(1, userId);
            ResultSet rs2 = pst2.executeQuery();
            if (rs2.next()) {
                totalPointsLabel.setText(String.valueOf(rs2.getInt("current_balance")));
            } else {
                totalPointsLabel.setText("0");
            }
            
            // Available Surveys
            String query3 = "SELECT COUNT(*) as count FROM survey WHERE status = 'Active'";
            PreparedStatement pst3 = conn.prepareStatement(query3);
            ResultSet rs3 = pst3.executeQuery();
            if (rs3.next()) {
                availableSurveysLabel.setText(String.valueOf(rs3.getInt("count")));
            }
            
            // This Month Activity
            String query4 = "SELECT COUNT(*) as count FROM survey_submissions " +
                           "WHERE user_id = ? AND MONTH(submitted_at) = MONTH(CURDATE()) " +
                           "AND YEAR(submitted_at) = YEAR(CURDATE())";
            PreparedStatement pst4 = conn.prepareStatement(query4);
            pst4.setInt(1, userId);
            ResultSet rs4 = pst4.executeQuery();
            if (rs4.next()) {
                recentActivityLabel.setText(String.valueOf(rs4.getInt("count")));
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void refreshDashboard() {
        loadStatistics();
        UIHelper.showInfoMessage(this, "Dashboard refreshed!");
    }
    
    private void openAvailableSurveys() {
        new AvailableSurveys(userId, userName, userEmail).setVisible(true);
        this.dispose();
    }
    
    private void openMySurveys() {
        new MySurveys(userId, userName, userEmail).setVisible(true);
        this.dispose();
    }
    
    private void openMyRewards() {
        UIHelper.showInfoMessage(this, "My Rewards feature coming soon!\n\nYou can redeem your points for various rewards.");
    }
    
    private void openProfile() {
        new UserProfile(userId, userName, userEmail).setVisible(true);
        this.dispose();
    }
    
    private void logout() {
        if (UIHelper.showConfirmDialog(this, "Are you sure you want to logout?")) {
            new MainApp().setVisible(true);
            this.dispose();
        }
    }
}