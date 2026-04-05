package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

import db.DBConnection;
import utils.UIHelper;

public class CreateSurvey extends JFrame {
    
    private int adminId;
    private String adminName;
    private String adminRole;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> statusCombo;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField rewardPointsField;
    private JTextField estimatedTimeField;
    private JCheckBox anonymousCheckbox;
    private JCheckBox multipleSubmissionsCheckbox;
    
    private DefaultTableModel questionsTableModel;
    private JTable questionsTable;
    private ArrayList<QuestionData> questionsList;
    
    public CreateSurvey(int adminId, String adminName, String adminRole) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminRole = adminRole;
        this.questionsList = new ArrayList<>();
        
        setTitle("Create Survey - Survey Management System");
        setSize(1400, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        loadCategories();
        UIHelper.centerFrame(this);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Content - Split Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setTopComponent(createSurveyDetailsPanel());
        splitPane.setBottomComponent(createQuestionsPanel());
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Bottom Buttons
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIHelper.SUCCESS_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel titleLabel = new JLabel("➕ Create New Survey");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JButton backButton = UIHelper.createStyledButton("← Back", UIHelper.DARK_COLOR);
        backButton.addActionListener(e -> goBack());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createSurveyDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIHelper.PRIMARY_COLOR, 2),
            "Survey Details",
            0, 0,
            UIHelper.HEADING_FONT,
            UIHelper.PRIMARY_COLOR
        ));
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0 - Title
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(UIHelper.createLabel("Survey Title:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        titleField = new JTextField(40);
        titleField.setFont(UIHelper.NORMAL_FONT);
        titleField.setPreferredSize(new Dimension(500, 35));
        panel.add(titleField, gbc);
        
        // Row 1 - Category and Status
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(UIHelper.createLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        categoryCombo = new JComboBox<>();
        categoryCombo.setFont(UIHelper.NORMAL_FONT);
        categoryCombo.setPreferredSize(new Dimension(200, 35));
        panel.add(categoryCombo, gbc);
        
        gbc.gridx = 2;
        panel.add(UIHelper.createLabel("Status:"), gbc);
        
        gbc.gridx = 3;
        statusCombo = new JComboBox<>(new String[]{"Draft", "Active", "Closed"});
        statusCombo.setFont(UIHelper.NORMAL_FONT);
        statusCombo.setPreferredSize(new Dimension(150, 35));
        statusCombo.setSelectedItem("Active");
        panel.add(statusCombo, gbc);
        
        // Row 2 - Start Date and End Date
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(UIHelper.createLabel("Start Date:"), gbc);
        
        gbc.gridx = 1;
        startDateField = new JTextField("2024-01-01");
        startDateField.setFont(UIHelper.NORMAL_FONT);
        startDateField.setPreferredSize(new Dimension(150, 35));
        panel.add(startDateField, gbc);
        
        gbc.gridx = 2;
        panel.add(UIHelper.createLabel("End Date:"), gbc);
        
        gbc.gridx = 3;
        endDateField = new JTextField("2024-12-31");
        endDateField.setFont(UIHelper.NORMAL_FONT);
        endDateField.setPreferredSize(new Dimension(150, 35));
        panel.add(endDateField, gbc);
        
        // Row 3 - Reward Points and Estimated Time
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(UIHelper.createLabel("Reward Points:"), gbc);
        
        gbc.gridx = 1;
        rewardPointsField = new JTextField("50");
        rewardPointsField.setFont(UIHelper.NORMAL_FONT);
        rewardPointsField.setPreferredSize(new Dimension(100, 35));
        panel.add(rewardPointsField, gbc);
        
        gbc.gridx = 2;
        panel.add(UIHelper.createLabel("Est. Time (min):"), gbc);
        
        gbc.gridx = 3;
        estimatedTimeField = new JTextField("5");
        estimatedTimeField.setFont(UIHelper.NORMAL_FONT);
        estimatedTimeField.setPreferredSize(new Dimension(100, 35));
        panel.add(estimatedTimeField, gbc);
        
        // Row 4 - Description
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(UIHelper.createLabel("Description:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        descriptionArea = new JTextArea(3, 40);
        descriptionArea.setFont(UIHelper.NORMAL_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(500, 80));
        panel.add(scrollPane, gbc);
        
        // Row 5 - Checkboxes
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        anonymousCheckbox = new JCheckBox("Allow Anonymous Responses");
        anonymousCheckbox.setFont(UIHelper.NORMAL_FONT);
        anonymousCheckbox.setBackground(Color.WHITE);
        panel.add(anonymousCheckbox, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2;
        multipleSubmissionsCheckbox = new JCheckBox("Allow Multiple Submissions");
        multipleSubmissionsCheckbox.setFont(UIHelper.NORMAL_FONT);
        multipleSubmissionsCheckbox.setBackground(Color.WHITE);
        panel.add(multipleSubmissionsCheckbox, gbc);
        
        return panel;
    }
    
    private JPanel createQuestionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIHelper.INFO_COLOR, 2),
            "Survey Questions",
            0, 0,
            UIHelper.HEADING_FONT,
            UIHelper.INFO_COLOR
        ));
        
        // Top - Add Question Button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton addQuestionBtn = UIHelper.createStyledButton("➕ Add Question", UIHelper.SUCCESS_COLOR);
        addQuestionBtn.addActionListener(e -> showAddQuestionDialog());
        
        JButton removeQuestionBtn = UIHelper.createStyledButton("➖ Remove Selected", UIHelper.DANGER_COLOR);
        removeQuestionBtn.addActionListener(e -> removeSelectedQuestion());
        
        topPanel.add(addQuestionBtn);
        topPanel.add(removeQuestionBtn);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Center - Questions Table
        String[] columns = {"#", "Question Text", "Type", "Required", "Options"};
        questionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        questionsTable = new JTable(questionsTableModel);

        // ✅ Ensure headers & cells are visible
        UIHelper.styleTable(questionsTable);
        questionsTable.setRowHeight(40); // optional override
        
        questionsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        questionsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        questionsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        questionsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        questionsTable.getColumnModel().getColumn(4).setPreferredWidth(250);
        
        JScrollPane scrollPane = new JScrollPane(questionsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(UIHelper.LIGHT_COLOR);
        
        JButton saveDraftBtn = UIHelper.createLargeButton("💾 Save as Draft", UIHelper.WARNING_COLOR);
        saveDraftBtn.addActionListener(e -> saveSurvey("Draft"));
        
        JButton publishBtn = UIHelper.createLargeButton("🚀 Publish Survey", UIHelper.SUCCESS_COLOR);
        publishBtn.addActionListener(e -> saveSurvey("Active"));
        
        JButton cancelBtn = UIHelper.createLargeButton("❌ Cancel", UIHelper.DANGER_COLOR);
        cancelBtn.addActionListener(e -> goBack());
        
        panel.add(saveDraftBtn);
        panel.add(publishBtn);
        panel.add(cancelBtn);
        
        return panel;
    }
    
    private void loadCategories() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT category_name FROM survey_categories ORDER BY category_name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            categoryCombo.addItem("-- Select Category --");
            while (rs.next()) {
                categoryCombo.addItem(rs.getString("category_name"));
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading categories: " + e.getMessage());
        }
    }
    
    private void showAddQuestionDialog() {
        JDialog dialog = new JDialog(this, "Add Question", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Question Text
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(UIHelper.createLabel("Question Text:"), gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 2;
        JTextArea questionTextArea = new JTextArea(3, 30);
        questionTextArea.setFont(UIHelper.NORMAL_FONT);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setBorder(BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2));
        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        panel.add(scrollPane, gbc);
        
        // Question Type
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(UIHelper.createLabel("Question Type:"), gbc);
        
        gbc.gridx = 1;
        String[] types = {"Multiple Choice", "Checkbox", "Text", "Rating", "Yes/No", "Dropdown", "Date"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setFont(UIHelper.NORMAL_FONT);
        panel.add(typeCombo, gbc);
        
        // Required Checkbox
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JCheckBox requiredCheckbox = new JCheckBox("This question is required");
        requiredCheckbox.setFont(UIHelper.NORMAL_FONT);
        requiredCheckbox.setSelected(true);
        requiredCheckbox.setBackground(Color.WHITE);
        panel.add(requiredCheckbox, gbc);
        
        // Options (for MCQ, Checkbox, Dropdown)
        gbc.gridy = 4;
        panel.add(UIHelper.createLabel("Options (comma-separated, for MCQ/Checkbox/Dropdown):"), gbc);
        
        gbc.gridy = 5;
        JTextArea optionsArea = new JTextArea(2, 30);
        optionsArea.setFont(UIHelper.NORMAL_FONT);
        optionsArea.setLineWrap(true);
        optionsArea.setBorder(BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2));
        optionsArea.setText("Option 1, Option 2, Option 3");
        JScrollPane optionsScroll = new JScrollPane(optionsArea);
        panel.add(optionsScroll, gbc);
        
        // Buttons
        gbc.gridy = 6;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addBtn = UIHelper.createStyledButton("Add Question", UIHelper.SUCCESS_COLOR);
        addBtn.addActionListener(e -> {
            String questionText = questionTextArea.getText().trim();
            String questionType = (String) typeCombo.getSelectedItem();
            boolean isRequired = requiredCheckbox.isSelected();
            String options = optionsArea.getText().trim();
            
            if (questionText.isEmpty()) {
                UIHelper.showErrorMessage(dialog, "Please enter question text!");
                return;
            }
            
            QuestionData question = new QuestionData(
                questionsList.size() + 1,
                questionText,
                questionType,
                isRequired,
                options
            );
            
            questionsList.add(question);
            
            Object[] row = {
                question.order,
                question.text,
                question.type,
                question.required ? "Yes" : "No",
                question.options
            };
            questionsTableModel.addRow(row);
            
            UIHelper.showSuccessMessage(dialog, "Question added successfully!");
            dialog.dispose();
        });
        
        JButton cancelBtn = UIHelper.createStyledButton("Cancel", UIHelper.DANGER_COLOR);
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void removeSelectedQuestion() {
        int selectedRow = questionsTable.getSelectedRow();
        if (selectedRow >= 0) {
            if (UIHelper.showConfirmDialog(this, "Remove this question?")) {
                questionsList.remove(selectedRow);
                questionsTableModel.removeRow(selectedRow);
                
                // Update order numbers
                for (int i = 0; i < questionsList.size(); i++) {
                    questionsList.get(i).order = i + 1;
                    questionsTableModel.setValueAt(i + 1, i, 0);
                }
            }
        } else {
            UIHelper.showWarningMessage(this, "Please select a question to remove!");
        }
    }
    
    private void saveSurvey(String status) {
        // Validation
        if (titleField.getText().trim().isEmpty()) {
            UIHelper.showErrorMessage(this, "Please enter survey title!");
            titleField.requestFocus();
            return;
        }
        
        if (categoryCombo.getSelectedIndex() == 0) {
            UIHelper.showErrorMessage(this, "Please select a category!");
            return;
        }
        
        if (questionsList.isEmpty()) {
            UIHelper.showErrorMessage(this, "Please add at least one question!");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Get category ID
            String categoryName = (String) categoryCombo.getSelectedItem();
            String catQuery = "SELECT category_id FROM survey_categories WHERE category_name = ?";
            PreparedStatement catPst = conn.prepareStatement(catQuery);
            catPst.setString(1, categoryName);
            ResultSet catRs = catPst.executeQuery();
            int categoryId = 0;
            if (catRs.next()) {
                categoryId = catRs.getInt("category_id");
            }
            
            // Insert Survey
            String surveyQuery = "INSERT INTO survey (title, description, category_id, created_by, " +
                                "start_date, end_date, status, is_anonymous, allow_multiple_submissions, " +
                                "reward_points, estimated_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement surveyPst = conn.prepareStatement(surveyQuery, Statement.RETURN_GENERATED_KEYS);
            surveyPst.setString(1, titleField.getText().trim());
            surveyPst.setString(2, descriptionArea.getText().trim());
            surveyPst.setInt(3, categoryId);
            surveyPst.setInt(4, adminId);
            surveyPst.setString(5, startDateField.getText());
            surveyPst.setString(6, endDateField.getText());
            surveyPst.setString(7, status);
            surveyPst.setBoolean(8, anonymousCheckbox.isSelected());
            surveyPst.setBoolean(9, multipleSubmissionsCheckbox.isSelected());
            surveyPst.setInt(10, Integer.parseInt(rewardPointsField.getText()));
            surveyPst.setInt(11, Integer.parseInt(estimatedTimeField.getText()));
            
            surveyPst.executeUpdate();
            
            ResultSet generatedKeys = surveyPst.getGeneratedKeys();
            int surveyId = 0;
            if (generatedKeys.next()) {
                surveyId = generatedKeys.getInt(1);
            }
            
            // Insert Questions
            String questionQuery = "INSERT INTO questions (survey_id, question_text, question_type, " +
                                  "is_required, question_order) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement questionPst = conn.prepareStatement(questionQuery, Statement.RETURN_GENERATED_KEYS);
            
            for (QuestionData question : questionsList) {
                questionPst.setInt(1, surveyId);
                questionPst.setString(2, question.text);
                questionPst.setString(3, question.type);
                questionPst.setBoolean(4, question.required);
                questionPst.setInt(5, question.order);
                questionPst.executeUpdate();
                
                // Insert options if applicable
                if (question.type.equals("Multiple Choice") || question.type.equals("Checkbox") || 
                    question.type.equals("Dropdown")) {
                    
                    ResultSet qKeys = questionPst.getGeneratedKeys();
                    if (qKeys.next()) {
                        int questionId = qKeys.getInt(1);
                        
                        String[] options = question.options.split(",");
                        String optionQuery = "INSERT INTO question_options (question_id, option_text, option_order) " +
                                           "VALUES (?, ?, ?)";
                        PreparedStatement optionPst = conn.prepareStatement(optionQuery);
                        
                        for (int i = 0; i < options.length; i++) {
                            optionPst.setInt(1, questionId);
                            optionPst.setString(2, options[i].trim());
                            optionPst.setInt(3, i + 1);
                            optionPst.executeUpdate();
                        }
                    }
                }
            }
            
            conn.commit();
            
            UIHelper.showSuccessMessage(this, "Survey created successfully!");
            goBack();
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error saving survey: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            UIHelper.showErrorMessage(this, "Please enter valid numbers for reward points and estimated time!");
        }
    }
    
    private void goBack() {
        if (UIHelper.showConfirmDialog(this, "Are you sure? Unsaved changes will be lost.")) {
            new AdminDashboard(adminId, adminName, adminRole).setVisible(true);
            this.dispose();
        }
    }
    
    // Helper class to store question data
    class QuestionData {
        int order;
        String text;
        String type;
        boolean required;
        String options;
        
        public QuestionData(int order, String text, String type, boolean required, String options) {
            this.order = order;
            this.text = text;
            this.type = type;
            this.required = required;
            this.options = options;
        }
    }
}