package user;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

import db.DBConnection;
import utils.UIHelper;

public class MySurveys extends JFrame {
    
    private int userId;
    private String userName;
    private String userEmail;
    
    private JTable surveysTable;
    private DefaultTableModel tableModel;
    
    public MySurveys(int userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        
        setTitle("My Completed Surveys - Survey Management System");
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
        
        JLabel titleLabel = new JLabel("✅ My Completed Surveys");
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
        
        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        JButton refreshBtn = UIHelper.createStyledButton("🔄 Refresh", UIHelper.SUCCESS_COLOR);
        refreshBtn.addActionListener(e -> loadSurveys());
        
        topPanel.add(refreshBtn);
        
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
        String[] columns = {"Survey Title", "Category", "Submitted At", "Time Taken", "Points Earned", "View Answers"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only View Answers column
            }
        };
        
        surveysTable = new JTable(tableModel);

        // ✅ Use central styling: headers + cells clearly visible
        UIHelper.styleTable(surveysTable);
        surveysTable.setRowHeight(50);
        surveysTable.getTableHeader().setFont(UIHelper.BOLD_FONT);
        
        // Column widths
        surveysTable.getColumnModel().getColumn(0).setPreferredWidth(350);
        surveysTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        surveysTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        surveysTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        surveysTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        surveysTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        // View Answers button column
        surveysTable.getColumn("View Answers").setCellRenderer(new ViewButtonRenderer());
        surveysTable.getColumn("View Answers").setCellEditor(new ViewButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(surveysTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadSurveys() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT s.title, sc.category_name, ss.submitted_at, " +
                          "ss.completion_time, ss.points_earned, ss.submission_id " +
                          "FROM survey_submissions ss " +
                          "JOIN survey s ON ss.survey_id = s.survey_id " +
                          "LEFT JOIN survey_categories sc ON s.category_id = sc.category_id " +
                          "WHERE ss.user_id = ? " +
                          "ORDER BY ss.submitted_at DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                int completionTime = rs.getInt("completion_time");
                int minutes = completionTime / 60;
                int seconds = completionTime % 60;
                
                Object[] row = {
                    rs.getString("title"),
                    rs.getString("category_name"),
                    rs.getTimestamp("submitted_at"),
                    minutes + "m " + seconds + "s",
                    "+" + rs.getInt("points_earned") + " pts",
                    rs.getInt("submission_id") // Store submission_id
                };
                tableModel.addRow(row);
            }
            
            if (tableModel.getRowCount() == 0) {
                Object[] row = {"No surveys completed yet", "", "", "", "", null};
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading surveys: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Button Renderer
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
    
    // Button Editor
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
            
            viewBtn.addActionListener(e -> viewAnswers(currentRow));
            
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
    
    private void viewAnswers(int row) {
        Object submissionIdObj = tableModel.getValueAt(row, 5);
        
        if (submissionIdObj == null) {
            UIHelper.showWarningMessage(this, "No data available!");
            return;
        }
        
        int submissionId = (int) submissionIdObj;
        String surveyTitle = (String) tableModel.getValueAt(row, 0);
        
        showAnswersDialog(submissionId, surveyTitle);
    }
    
    private void showAnswersDialog(int submissionId, String surveyTitle) {
        JDialog dialog = new JDialog(this, "My Answers - " + surveyTitle, true);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = UIHelper.createHeadingLabel("Your Responses");
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Responses Table
        String[] columns = {"Question", "Your Answer"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);

        // ✅ Style inner table headers & cells too
        UIHelper.styleTable(table);
        table.setRowHeight(60);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);
        
        // Load answers
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
                if (answer == null) {
                    answer = "No answer";
                }
                
                Object[] row = {
                    rs.getString("question_text"),
                    answer
                };
                model.addRow(row);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(dialog, "Error loading answers: " + e.getMessage());
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
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
    
    private void goBack() {
        new UserDashboard(userId, userName, userEmail).setVisible(true);
        this.dispose();
    }
}