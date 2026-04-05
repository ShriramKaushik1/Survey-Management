package admin;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

import db.DBConnection;
import utils.UIHelper;

public class ManageUsers extends JFrame {
    
    private int adminId;
    private String adminName;
    private String adminRole;
    
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel totalUsersLabel;
    private JLabel activeUsersLabel;
    
    public ManageUsers(int adminId, String adminName, String adminRole) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminRole = adminRole;
        
        setTitle("Manage Users - Survey Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        loadUsers();
        loadStatistics();
        UIHelper.centerFrame(this);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Content
        mainPanel.add(createContent(), BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIHelper.ACCENT_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("👥 Manage Users");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JButton backButton = UIHelper.createStyledButton("← Back to Dashboard", UIHelper.DARK_COLOR);
        backButton.addActionListener(e -> goBack());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIHelper.LIGHT_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        // Top Panel - Statistics & Search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Statistics
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBackground(UIHelper.LIGHT_COLOR);
        statsPanel.setPreferredSize(new Dimension(500, 100));
        
        JPanel statCard1 = createStatCard("👥", "Total Users", "0");
        totalUsersLabel = (JLabel) ((JPanel)statCard1.getComponent(0)).getComponent(2);
        statsPanel.add(statCard1);
        
        JPanel statCard2 = createStatCard("✅", "Active Users", "0");
        activeUsersLabel = (JLabel) ((JPanel)statCard2.getComponent(0)).getComponent(2);
        statsPanel.add(statCard2);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        JLabel searchLabel = UIHelper.createLabel("Search:");
        searchField = new JTextField(20);
        searchField.setFont(UIHelper.NORMAL_FONT);
        searchField.setPreferredSize(new Dimension(200, 35));
        
        JButton searchBtn = UIHelper.createStyledButton("🔍 Search", UIHelper.INFO_COLOR);
        searchBtn.addActionListener(e -> searchUsers());
        
        JButton refreshBtn = UIHelper.createStyledButton("🔄 Refresh", UIHelper.SUCCESS_COLOR);
        refreshBtn.addActionListener(e -> {
            loadUsers();
            loadStatistics();
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);
        
        topPanel.add(statsPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createStatCard(String icon, String title, String value) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(UIHelper.ACCENT_COLOR);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIHelper.SMALL_FONT);
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(valueLabel);
        contentPanel.add(titleLabel);
        
        card.add(contentPanel);
        return card;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Table Model
        String[] columns = {
            "ID", "Username", "Name", "Email",
            "Phone", "Status", "Surveys Taken", "Total Points", "Actions"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only Actions column
            }
        };
        
        usersTable = new JTable(tableModel);

        // Use central styling so headers and cells are clearly visible
        UIHelper.styleTable(usersTable);
        
        // Column widths
        usersTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        usersTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        usersTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        usersTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        usersTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        usersTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        usersTable.getColumnModel().getColumn(8).setPreferredWidth(200);
        
        // Actions column with buttons
        usersTable.getColumn("Actions").setCellRenderer(new UserActionsRenderer());
        usersTable.getColumn("Actions").setCellEditor(new UserActionsEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadUsers() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT u.user_id, u.username, u.name, u.email, u.phone, " +
                          "u.account_status, u.total_surveys_taken, COALESCE(ur.current_balance, 0) as points " +
                          "FROM users u " +
                          "LEFT JOIN user_rewards ur ON u.user_id = ur.user_id " +
                          "ORDER BY u.user_id DESC";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone") != null ? rs.getString("phone") : "N/A",
                    rs.getString("account_status"),
                    rs.getInt("total_surveys_taken"),
                    rs.getInt("points"),
                    "Actions"
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            
            // Total Users
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next()) {
                totalUsersLabel.setText(String.valueOf(rs.getInt("count")));
            }
            
            // Active Users
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users WHERE account_status = 'Active'");
            if (rs.next()) {
                activeUsersLabel.setText(String.valueOf(rs.getInt("count")));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void searchUsers() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadUsers();
            return;
        }
        
        tableModel.setRowCount(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT u.user_id, u.username, u.name, u.email, u.phone, " +
                          "u.account_status, u.total_surveys_taken, COALESCE(ur.current_balance, 0) as points " +
                          "FROM users u " +
                          "LEFT JOIN user_rewards ur ON u.user_id = ur.user_id " +
                          "WHERE u.username LIKE ? OR u.name LIKE ? OR u.email LIKE ? " +
                          "ORDER BY u.user_id DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            String search = "%" + searchText + "%";
            pst.setString(1, search);
            pst.setString(2, search);
            pst.setString(3, search);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone") != null ? rs.getString("phone") : "N/A",
                    rs.getString("account_status"),
                    rs.getInt("total_surveys_taken"),
                    rs.getInt("points"),
                    "Actions"
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error searching users: " + e.getMessage());
        }
    }
    
    // Button Renderer for Actions column
    class UserActionsRenderer extends JPanel implements TableCellRenderer {
        public UserActionsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            
            setBackground(isSelected
                    ? table.getSelectionBackground()
                    : (row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245)));
            
            JButton viewBtn = new JButton("👁️");
            viewBtn.setFont(UIHelper.SMALL_FONT);
            viewBtn.setBackground(UIHelper.INFO_COLOR);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setFocusPainted(false);
            viewBtn.setBorderPainted(false);
            viewBtn.setToolTipText("View Details");
            
            JButton suspendBtn = new JButton("🚫");
            suspendBtn.setFont(UIHelper.SMALL_FONT);
            suspendBtn.setBackground(UIHelper.WARNING_COLOR);
            suspendBtn.setForeground(Color.WHITE);
            suspendBtn.setFocusPainted(false);
            suspendBtn.setBorderPainted(false);
            suspendBtn.setToolTipText("Suspend User");
            
            JButton deleteBtn = new JButton("🗑️");
            deleteBtn.setFont(UIHelper.SMALL_FONT);
            deleteBtn.setBackground(UIHelper.DANGER_COLOR);
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorderPainted(false);
            deleteBtn.setToolTipText("Delete User");
            
            add(viewBtn);
            add(suspendBtn);
            add(deleteBtn);
            
            return this;
        }
    }
    
    // Button Editor for Actions column
    class UserActionsEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton viewBtn, suspendBtn, deleteBtn;
        private int currentRow;
        
        public UserActionsEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            
            viewBtn = new JButton("👁️");
            viewBtn.setFont(UIHelper.SMALL_FONT);
            viewBtn.setBackground(UIHelper.INFO_COLOR);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setFocusPainted(false);
            viewBtn.setBorderPainted(false);
            
            suspendBtn = new JButton("🚫");
            suspendBtn.setFont(UIHelper.SMALL_FONT);
            suspendBtn.setBackground(UIHelper.WARNING_COLOR);
            suspendBtn.setForeground(Color.WHITE);
            suspendBtn.setFocusPainted(false);
            suspendBtn.setBorderPainted(false);
            
            deleteBtn = new JButton("🗑️");
            deleteBtn.setFont(UIHelper.SMALL_FONT);
            deleteBtn.setBackground(UIHelper.DANGER_COLOR);
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorderPainted(false);
            
            viewBtn.addActionListener(e -> viewUser(currentRow));
            suspendBtn.addActionListener(e -> suspendUser(currentRow));
            deleteBtn.addActionListener(e -> deleteUser(currentRow));
            
            panel.add(viewBtn);
            panel.add(suspendBtn);
            panel.add(deleteBtn);
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }
        
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
    
    private void viewUser(int row) {
        int userId = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 2);
        
        UIHelper.showInfoMessage(this, "Viewing user: " + name + " (ID: " + userId + ")\n\n" +
            "Detailed user view will be implemented here!");
    }
    
    private void suspendUser(int row) {
        int userId = (int) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);
        String currentStatus = (String) tableModel.getValueAt(row, 5);
        
        String newStatus = currentStatus.equals("Active") ? "Suspended" : "Active";
        String action = currentStatus.equals("Active") ? "suspend" : "activate";
        
        if (UIHelper.showConfirmDialog(this, "Are you sure you want to " + action + " user: " + username + "?")) {
            try (Connection conn = DBConnection.getConnection()) {
                String query = "UPDATE users SET account_status = ? WHERE user_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, newStatus);
                pst.setInt(2, userId);
                
                int result = pst.executeUpdate();
                if (result > 0) {
                    UIHelper.showSuccessMessage(this, "User " + action + "d successfully!");
                    loadUsers();
                    loadStatistics();
                }
                
            } catch (SQLException e) {
                UIHelper.showErrorMessage(this, "Error updating user status: " + e.getMessage());
            }
        }
    }
    
    private void deleteUser(int row) {
        int userId = (int) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);
        
        if (UIHelper.showConfirmDialog(this, "Are you sure you want to DELETE user: " + username + "?\n" +
            "This action cannot be undone!")) {
            try (Connection conn = DBConnection.getConnection()) {
                String query = "DELETE FROM users WHERE user_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, userId);
                
                int result = pst.executeUpdate();
                if (result > 0) {
                    UIHelper.showSuccessMessage(this, "User deleted successfully!");
                    loadUsers();
                    loadStatistics();
                }
                
            } catch (SQLException e) {
                UIHelper.showErrorMessage(this, "Error deleting user: " + e.getMessage());
            }
        }
    }
    
    private void goBack() {
        new AdminDashboard(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
}