package user;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

import db.DBConnection;
import utils.UIHelper;

public class AvailableSurveys extends JFrame {
    
    private int userId;
    private String userName;
    private String userEmail;
    
    private JTable surveysTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;
    private JTextField searchField;
    
    public AvailableSurveys(int userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        
        setTitle("Available Surveys - Survey Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        loadCategories();
        loadSurveys();
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
        headerPanel.setBackground(UIHelper.SUCCESS_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("📝 Available Surveys");
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
        
        // Top Panel - Filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        JLabel categoryLabel = UIHelper.createLabel("Category:");
        categoryFilter = new JComboBox<>();
        categoryFilter.setFont(UIHelper.NORMAL_FONT);
        categoryFilter.setPreferredSize(new Dimension(200, 35));
        categoryFilter.addActionListener(e -> filterSurveys());
        
        JLabel searchLabel = UIHelper.createLabel("Search:");
        searchField = new JTextField(20);
        searchField.setFont(UIHelper.NORMAL_FONT);
        searchField.setPreferredSize(new Dimension(200, 35));
        
        JButton searchBtn = UIHelper.createStyledButton("🔍 Search", UIHelper.INFO_COLOR);
        searchBtn.addActionListener(e -> searchSurveys());
        
        JButton refreshBtn = UIHelper.createStyledButton("🔄 Refresh", UIHelper.SUCCESS_COLOR);
        refreshBtn.addActionListener(e -> loadSurveys());
        
        filterPanel.add(categoryLabel);
        filterPanel.add(categoryFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);
        filterPanel.add(refreshBtn);
        
        topPanel.add(filterPanel, BorderLayout.WEST);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Table Model
        String[] columns = {
            "ID", "Title", "Category", "Description",
            "Reward Points", "Est. Time", "Status", "Take Survey"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only Take Survey column
            }
        };
        
        surveysTable = new JTable(tableModel);

        // ✅ Use UIHelper to style headers and cells so text is visible
        UIHelper.styleTable(surveysTable);
        surveysTable.setRowHeight(60);
        
        // You can still override header font if you want it bolder
        surveysTable.getTableHeader().setFont(UIHelper.BOLD_FONT);
        
        // Column widths
        surveysTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        surveysTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        surveysTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        surveysTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        surveysTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        surveysTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        surveysTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        surveysTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        
        // Take Survey button column
        surveysTable.getColumn("Take Survey").setCellRenderer(new TakeSurveyButtonRenderer());
        surveysTable.getColumn("Take Survey").setCellEditor(new TakeSurveyButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(surveysTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadCategories() {
        categoryFilter.removeAllItems();
        categoryFilter.addItem("All Categories");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT category_name FROM survey_categories ORDER BY category_name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                categoryFilter.addItem(rs.getString("category_name"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadSurveys() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT s.survey_id, s.title, sc.category_name, s.description, " +
                          "s.reward_points, s.estimated_time, s.status " +
                          "FROM survey s " +
                          "LEFT JOIN survey_categories sc ON s.category_id = sc.category_id " +
                          "WHERE s.status = 'Active' " +
                          "AND s.survey_id NOT IN (" +
                          "    SELECT survey_id FROM survey_submissions WHERE user_id = ?" +
                          ") " +
                          "ORDER BY s.created_at DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String description = rs.getString("description");
                if (description != null && description.length() > 50) {
                    description = description.substring(0, 50) + "...";
                }
                
                Object[] row = {
                    rs.getInt("survey_id"),
                    rs.getString("title"),
                    rs.getString("category_name"),
                    description,
                    rs.getInt("reward_points") + " pts",
                    rs.getInt("estimated_time") + " min",
                    rs.getString("status"),
                    "Take"
                };
                tableModel.addRow(row);
            }
            
            if (tableModel.getRowCount() == 0) {
                Object[] row = {"", "No surveys available", "", "", "", "", "", ""};
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading surveys: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void filterSurveys() {
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        
        if (selectedCategory.equals("All Categories")) {
            loadSurveys();
            return;
        }
        
        tableModel.setRowCount(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT s.survey_id, s.title, sc.category_name, s.description, " +
                          "s.reward_points, s.estimated_time, s.status " +
                          "FROM survey s " +
                          "LEFT JOIN survey_categories sc ON s.category_id = sc.category_id " +
                          "WHERE s.status = 'Active' " +
                          "AND sc.category_name = ? " +
                          "AND s.survey_id NOT IN (" +
                          "    SELECT survey_id FROM survey_submissions WHERE user_id = ?" +
                          ") " +
                          "ORDER BY s.created_at DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, selectedCategory);
            pst.setInt(2, userId);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String description = rs.getString("description");
                if (description != null && description.length() > 50) {
                    description = description.substring(0, 50) + "...";
                }
                
                Object[] row = {
                    rs.getInt("survey_id"),
                    rs.getString("title"),
                    rs.getString("category_name"),
                    description,
                    rs.getInt("reward_points") + " pts",
                    rs.getInt("estimated_time") + " min",
                    rs.getString("status"),
                    "Take"
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void searchSurveys() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            loadSurveys();
            return;
        }
        
        tableModel.setRowCount(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT s.survey_id, s.title, sc.category_name, s.description, " +
                          "s.reward_points, s.estimated_time, s.status " +
                          "FROM survey s " +
                          "LEFT JOIN survey_categories sc ON s.category_id = sc.category_id " +
                          "WHERE s.status = 'Active' " +
                          "AND (s.title LIKE ? OR s.description LIKE ?) " +
                          "AND s.survey_id NOT IN (" +
                          "    SELECT survey_id FROM survey_submissions WHERE user_id = ?" +
                          ") " +
                          "ORDER BY s.created_at DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            String search = "%" + searchText + "%";
            pst.setString(1, search);
            pst.setString(2, search);
            pst.setInt(3, userId);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String description = rs.getString("description");
                if (description != null && description.length() > 50) {
                    description = description.substring(0, 50) + "...";
                }
                
                Object[] row = {
                    rs.getInt("survey_id"),
                    rs.getString("title"),
                    rs.getString("category_name"),
                    description,
                    rs.getInt("reward_points") + " pts",
                    rs.getInt("estimated_time") + " min",
                    rs.getString("status"),
                    "Take"
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Button Renderer
    class TakeSurveyButtonRenderer extends JPanel implements TableCellRenderer {
        public TakeSurveyButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            
            setBackground(isSelected
                    ? table.getSelectionBackground()
                    : (row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245)));
            
            JButton takeBtn = new JButton("▶️ Take Survey");
            takeBtn.setFont(UIHelper.NORMAL_FONT);
            takeBtn.setBackground(UIHelper.SUCCESS_COLOR);
            takeBtn.setForeground(Color.WHITE);
            takeBtn.setFocusPainted(false);
            takeBtn.setBorderPainted(false);
            
            add(takeBtn);
            return this;
        }
    }
    
    // Button Editor
    class TakeSurveyButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton takeBtn;
        private int currentRow;
        
        public TakeSurveyButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            
            takeBtn = new JButton("▶️ Take Survey");
            takeBtn.setFont(UIHelper.NORMAL_FONT);
            takeBtn.setBackground(UIHelper.SUCCESS_COLOR);
            takeBtn.setForeground(Color.WHITE);
            takeBtn.setFocusPainted(false);
            takeBtn.setBorderPainted(false);
            
            takeBtn.addActionListener(e -> takeSurvey(currentRow));
            
            panel.add(takeBtn);
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }
        
        public Object getCellEditorValue() {
            return "Take";
        }
    }
    
    private void takeSurvey(int row) {
        Object surveyIdObj = tableModel.getValueAt(row, 0);
        
        if (surveyIdObj == null || surveyIdObj.toString().isEmpty()) {
            UIHelper.showWarningMessage(this, "No surveys available to take!");
            return;
        }
        
        int surveyId = (int) surveyIdObj;
        String surveyTitle = (String) tableModel.getValueAt(row, 1);
        
        if (UIHelper.showConfirmDialog(this, "Start survey: " + surveyTitle + "?")) {
            new TakeSurvey(userId, userName, userEmail, surveyId, surveyTitle).setVisible(true);
            this.dispose();
        }
    }
    
    private void goBack() {
        new UserDashboard(userId, userName, userEmail).setVisible(true);
        this.dispose();
    }
}