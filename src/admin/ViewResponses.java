package admin;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

import db.DBConnection;
import utils.UIHelper;

public class ViewResponses extends JFrame {
    
    private int adminId;
    private String adminName;
    private String adminRole;
    
    private JComboBox<String> surveyCombo;
    private JTable responsesTable;
    private DefaultTableModel tableModel;
    private JLabel totalResponsesLabel;
    private JLabel averageTimeLabel;
    
    public ViewResponses(int adminId, String adminName, String adminRole) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminRole = adminRole;
        
        setTitle("View Survey Responses - Survey Management System");
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
        headerPanel.setBackground(UIHelper.WARNING_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("📊 Survey Responses & Analytics");
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
        
        // Top Panel - Survey Selection & Statistics
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Survey Selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        JLabel selectLabel = UIHelper.createLabel("Select Survey:");
        surveyCombo = new JComboBox<>();
        surveyCombo.setFont(UIHelper.NORMAL_FONT);
        surveyCombo.setPreferredSize(new Dimension(400, 40));
        surveyCombo.addActionListener(e -> loadResponses());
        
        JButton refreshBtn = UIHelper.createStyledButton("🔄 Refresh", UIHelper.SUCCESS_COLOR);
        refreshBtn.addActionListener(e -> loadResponses());
        
        JButton exportBtn = UIHelper.createStyledButton("📥 Export to CSV", UIHelper.INFO_COLOR);
        exportBtn.addActionListener(e -> exportToCSV());
        
        selectionPanel.add(selectLabel);
        selectionPanel.add(surveyCombo);
        selectionPanel.add(refreshBtn);
        selectionPanel.add(exportBtn);
        
        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBackground(UIHelper.LIGHT_COLOR);
        statsPanel.setPreferredSize(new Dimension(600, 100));
        
        JPanel statCard1 = createStatCard("📊", "Total Responses", "0");
        totalResponsesLabel = (JLabel) ((JPanel)statCard1.getComponent(0)).getComponent(2);
        statsPanel.add(statCard1);
        
        JPanel statCard2 = createStatCard("⏱️", "Avg. Completion Time", "0 min");
        averageTimeLabel = (JLabel) ((JPanel)statCard2.getComponent(0)).getComponent(2);
        statsPanel.add(statCard2);
        
        JPanel statsContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsContainer.setBackground(UIHelper.LIGHT_COLOR);
        statsContainer.add(statsPanel);
        
        topPanel.add(selectionPanel, BorderLayout.WEST);
        topPanel.add(statsContainer, BorderLayout.EAST);
        
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
            BorderFactory.createLineBorder(UIHelper.PRIMARY_COLOR, 2),
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
        valueLabel.setForeground(UIHelper.PRIMARY_COLOR);
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
        String[] columns = {"Response ID", "User", "Email", "Submitted At", "Time Taken", "View Details"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only View Details column
            }
        };
        
        responsesTable = new JTable(tableModel);

        // ✅ Let UIHelper handle header & cell styling so text is visible
        UIHelper.styleTable(responsesTable);
        responsesTable.setRowHeight(50);
        
        // Column widths
        responsesTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        responsesTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        responsesTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        responsesTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        responsesTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        responsesTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        // View Details button column
        responsesTable.getColumn("View Details").setCellRenderer(new ViewButtonRenderer());
        responsesTable.getColumn("View Details").setCellEditor(new ViewButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(responsesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadSurveys() {
        surveyCombo.removeAllItems();
        surveyCombo.addItem("-- Select Survey --");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT survey_id, title FROM survey ORDER BY survey_id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                String item = rs.getInt("survey_id") + " - " + rs.getString("title");
                surveyCombo.addItem(item);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading surveys: " + e.getMessage());
        }
    }
    
    private void loadResponses() {
        if (surveyCombo.getSelectedIndex() == 0) {
            return;
        }
        
        tableModel.setRowCount(0);
        
        String selectedSurvey = (String) surveyCombo.getSelectedItem();
        int surveyId = Integer.parseInt(selectedSurvey.split(" - ")[0]);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT sr.submission_id, u.name, u.email, sr.submitted_at, sr.completion_time " +
                          "FROM survey_submissions sr " +
                          "LEFT JOIN users u ON sr.user_id = u.user_id " +
                          "WHERE sr.survey_id = ? " +
                          "ORDER BY sr.submitted_at DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, surveyId);
            
            ResultSet rs = pst.executeQuery();
            
            int totalTime = 0;
            int count = 0;
            
            while (rs.next()) {
                int completionTime = rs.getInt("completion_time");
                totalTime += completionTime;
                count++;
                
                Object[] row = {
                    rs.getInt("submission_id"),
                    rs.getString("name") != null ? rs.getString("name") : "Anonymous",
                    rs.getString("email") != null ? rs.getString("email") : "N/A",
                    rs.getTimestamp("submitted_at"),
                    completionTime + " sec",
                    "View"
                };
                tableModel.addRow(row);
            }
            
            // Update statistics
            totalResponsesLabel.setText(String.valueOf(count));
            if (count > 0) {
                int avgTime = totalTime / count / 60; // Convert to minutes
                averageTimeLabel.setText(avgTime + " min");
            } else {
                averageTimeLabel.setText("0 min");
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading responses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Button Renderer for View Details column
    class ViewButtonRenderer extends JPanel implements TableCellRenderer {
        public ViewButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
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
            
            add(viewBtn);
            return this;
        }
    }
    
    // Button Editor for View Details column
    class ViewButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton viewBtn;
        private int currentRow;
        
        public ViewButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            
            viewBtn = new JButton("👁️ View");
            viewBtn.setFont(UIHelper.SMALL_FONT);
            viewBtn.setBackground(UIHelper.INFO_COLOR);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setFocusPainted(false);
            viewBtn.setBorderPainted(false);
            
            viewBtn.addActionListener(e -> viewResponseDetails(currentRow));
            
            panel.add(viewBtn);
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }
        
        public Object getCellEditorValue() {
            return "View";
        }
    }
    
    private void viewResponseDetails(int row) {
        int submissionId = (int) tableModel.getValueAt(row, 0);
        
        JDialog dialog = new JDialog(this, "Response Details - Submission #" + submissionId, true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = UIHelper.createHeadingLabel("Detailed Responses");
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Responses Table
        String[] columns = {"Question", "Answer"};
        DefaultTableModel detailsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable detailsTable = new JTable(detailsModel);

        // ✅ Style detail table as well
        UIHelper.styleTable(detailsTable);
        detailsTable.setRowHeight(60);
        
        detailsTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        detailsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        
        // Load response details
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT q.question_text, r.answer_text, qo.option_text " +
                          "FROM responses r " +
                          "JOIN questions q ON r.question_id = q.question_id " +
                          "LEFT JOIN question_options qo ON r.selected_option_id = qo.option_id " +
                          "WHERE r.submission_id = ? " +
                          "ORDER BY q.question_order";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, submissionId);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String answer = rs.getString("answer_text");
                if (answer == null || answer.isEmpty()) {
                    answer = rs.getString("option_text");
                }
                
                Object[] detailRow = {
                    rs.getString("question_text"),
                    answer != null ? answer : "No answer"
                };
                detailsModel.addRow(detailRow);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(dialog, "Error loading response details: " + e.getMessage());
        }
        
        JScrollPane scrollPane = new JScrollPane(detailsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Close button
        JButton closeBtn = UIHelper.createStyledButton("Close", UIHelper.DARK_COLOR);
        closeBtn.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void exportToCSV() {
        if (surveyCombo.getSelectedIndex() == 0) {
            UIHelper.showWarningMessage(this, "Please select a survey first!");
            return;
        }
        
        UIHelper.showInfoMessage(this, "CSV Export functionality will be implemented here!\n" +
            "This will export all responses to a CSV file.");
    }
    
    private void goBack() {
        new AdminDashboard(adminId, adminName, adminRole).setVisible(true);
        this.dispose();
    }
}