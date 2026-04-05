package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.*;

import db.DBConnection;
import utils.UIHelper;

public class ManageSurveys extends JFrame {
    
    private int adminId;
    private String adminName;
    private String adminRole;
    
    private JTable surveysTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public ManageSurveys(int adminId, String adminName, String adminRole) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminRole = adminRole;
        
        setTitle("Manage Surveys - Survey Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
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
        headerPanel.setBackground(UIHelper.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("📝 Manage Surveys");
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
        
        // Top Panel - Search and Actions
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        JLabel searchLabel = UIHelper.createLabel("Search:");
        searchField = new JTextField(20);
        searchField.setFont(UIHelper.NORMAL_FONT);
        searchField.setPreferredSize(new Dimension(250, 35));
        
        JButton searchButton = UIHelper.createStyledButton("🔍 Search", UIHelper.INFO_COLOR);
        searchButton.addActionListener(e -> searchSurveys());
        
        JButton refreshButton = UIHelper.createStyledButton("🔄 Refresh", UIHelper.SUCCESS_COLOR);
        refreshButton.addActionListener(e -> loadSurveys());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        
        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        JButton createButton = UIHelper.createStyledButton("➕ Create New Survey", UIHelper.SUCCESS_COLOR);
        createButton.addActionListener(e -> createNewSurvey());
        
        actionPanel.add(createButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionPanel, BorderLayout.EAST);
        
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
        String[] columns = {"ID", "Title", "Category", "Status", "Responses", "Start Date", "End Date", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only Actions column
            }
        };
        
        surveysTable = new JTable(tableModel);

        // ✅ Use central styling so headers and cells are clearly visible
        UIHelper.styleTable(surveysTable);
        
        // Column widths
        surveysTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        surveysTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        surveysTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        surveysTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        surveysTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        surveysTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        surveysTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        surveysTable.getColumnModel().getColumn(7).setPreferredWidth(200);
        
        // Actions column with buttons
        surveysTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        surveysTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(surveysTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadSurveys() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT s.survey_id, s.title, sc.category_name, s.status, " +
                          "s.total_responses, s.start_date, s.end_date " +
                          "FROM survey s " +
                          "LEFT JOIN survey_categories sc ON s.category_id = sc.category_id " +
                          "ORDER BY s.survey_id DESC";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("survey_id"),
                    rs.getString("title"),
                    rs.getString("category_name"),
                    rs.getString("status"),
                    rs.getInt("total_responses"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    "Actions" // Placeholder for buttons
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading surveys: " + e.getMessage());
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
            String query = "SELECT s.survey_id, s.title, sc.category_name, s.status, " +
                          "s.total_responses, s.start_date, s.end_date " +
                          "FROM survey s " +
                          "LEFT JOIN survey_categories sc ON s.category_id = sc.category_id " +
                          "WHERE s.title LIKE ? OR sc.category_name LIKE ? " +
                          "ORDER BY s.survey_id DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, "%" + searchText + "%");
            pst.setString(2, "%" + searchText + "%");
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("survey_id"),
                    rs.getString("title"),
                    rs.getString("category_name"),
                    rs.getString("status"),
                    rs.getInt("total_responses"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    "Actions"
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error searching surveys: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Button Renderer for Actions column
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            
            setBackground(isSelected
                    ? table.getSelectionBackground()
                    : (row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245)));
            
            JButton viewBtn = new JButton("👁️ View");
            viewBtn.setFont(UIHelper.SMALL_FONT);
            viewBtn.setBackground(UIHelper.INFO_COLOR);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setFocusPainted(false);
            viewBtn.setBorderPainted(false);
            
            JButton deleteBtn = new JButton("🗑️ Delete");
            deleteBtn.setFont(UIHelper.SMALL_FONT);
            deleteBtn.setBackground(UIHelper.DANGER_COLOR);
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorderPainted(false);
            
            add(viewBtn);
            add(deleteBtn);
            
            return this;
        }
    }
    
    // Button Editor for Actions column
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton viewBtn;
        private JButton deleteBtn;
        private int currentRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            
            viewBtn = new JButton("👁️ View");
            viewBtn.setFont(UIHelper.SMALL_FONT);
            viewBtn.setBackground(UIHelper.INFO_COLOR);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setFocusPainted(false);
            viewBtn.setBorderPainted(false);
            
            deleteBtn = new JButton("🗑️ Delete");
            deleteBtn.setFont(UIHelper.SMALL_FONT);
            deleteBtn.setBackground(UIHelper.DANGER_COLOR);
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorderPainted(false);
            
            viewBtn.addActionListener(e -> viewSurvey(currentRow));
            deleteBtn.addActionListener(e -> deleteSurvey(currentRow));
            
            panel.add(viewBtn);
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
    
    private void viewSurvey(int row) {
        int surveyId = (int) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);
        UIHelper.showInfoMessage(this, "Viewing Survey: " + title + " (ID: " + surveyId + ")");
    }
    
    private void deleteSurvey(int row) {
        int surveyId = (int) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);
        
        if (UIHelper.showConfirmDialog(this, "Are you sure you want to delete survey: " + title + "?")) {
            try (Connection conn = DBConnection.getConnection()) {
                String query = "DELETE FROM survey WHERE survey_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, surveyId);
                
                int result = pst.executeUpdate();
                if (result > 0) {
                    UIHelper.showSuccessMessage(this, "Survey deleted successfully!");
                    loadSurveys();
                }
                
            } catch (SQLException e) {
                UIHelper.showErrorMessage(this, "Error deleting survey: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void createNewSurvey() {
        new CreateSurvey(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
    
    private void goBack() {
        new AdminDashboard(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
}