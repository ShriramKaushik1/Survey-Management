package user;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import db.DBConnection;
import utils.UIHelper;

public class TakeSurvey extends JFrame {
    
    private int userId;
    private String userName;
    private String userEmail;
    private int surveyId;
    private String surveyTitle;
    
    private JPanel questionsPanel;
    private ArrayList<QuestionComponent> questionComponents;
    private long startTime;
    
    public TakeSurvey(int userId, String userName, String userEmail, int surveyId, String surveyTitle) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.surveyId = surveyId;
        this.surveyTitle = surveyTitle;
        this.questionComponents = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
        
        setTitle("Take Survey - " + surveyTitle);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        loadQuestions();
        UIHelper.centerFrame(this);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);
        
        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Questions Panel (Scrollable)
        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(Color.WHITE);
        questionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom Panel - Submit Button
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIHelper.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1000, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(surveyTitle);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Participant: " + userName);
        userLabel.setFont(UIHelper.NORMAL_FONT);
        userLabel.setForeground(new Color(255, 255, 255, 200));
        
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(userLabel);
        
        JButton cancelBtn = UIHelper.createStyledButton("❌ Cancel", UIHelper.DANGER_COLOR);
        cancelBtn.addActionListener(e -> cancelSurvey());
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(cancelBtn, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(UIHelper.LIGHT_COLOR);
        
        JButton submitBtn = UIHelper.createLargeButton("✅ Submit Survey", UIHelper.SUCCESS_COLOR);
        submitBtn.setPreferredSize(new Dimension(200, 50));
        submitBtn.addActionListener(e -> submitSurvey());
        
        panel.add(submitBtn);
        
        return panel;
    }
    
    private void loadQuestions() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT q.question_id, q.question_text, q.question_type, q.is_required " +
                          "FROM questions q " +
                          "WHERE q.survey_id = ? " +
                          "ORDER BY q.question_order";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, surveyId);
            
            ResultSet rs = pst.executeQuery();
            
            int questionNumber = 1;
            
            while (rs.next()) {
                int questionId = rs.getInt("question_id");
                String questionText = rs.getString("question_text");
                String questionType = rs.getString("question_type");
                boolean isRequired = rs.getBoolean("is_required");
                
                // Create question panel
                JPanel questionPanel = createQuestionPanel(
                    questionNumber, questionId, questionText, questionType, isRequired
                );
                
                questionsPanel.add(questionPanel);
                questionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                
                questionNumber++;
            }
            
            if (questionNumber == 1) {
                JLabel noQuestionsLabel = new JLabel("No questions found in this survey.");
                noQuestionsLabel.setFont(UIHelper.HEADING_FONT);
                questionsPanel.add(noQuestionsLabel);
            }
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error loading questions: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private JPanel createQuestionPanel(int number, int questionId, String questionText, 
                                       String questionType, boolean isRequired) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(900, 300));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Question Header
        JLabel questionLabel = new JLabel(number + ". " + questionText + 
                                         (isRequired ? " *" : ""));
        questionLabel.setFont(UIHelper.HEADING_FONT);
        questionLabel.setForeground(UIHelper.DARK_COLOR);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel typeLabel = new JLabel("(" + questionType + ")");
        typeLabel.setFont(UIHelper.SMALL_FONT);
        typeLabel.setForeground(Color.GRAY);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(questionLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(typeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Answer Component based on type
        JComponent answerComponent = createAnswerComponent(questionId, questionType);
        answerComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(answerComponent);
        
        // Store question component
        questionComponents.add(new QuestionComponent(questionId, questionType, 
                                                     answerComponent, isRequired));
        
        return panel;
    }
    
    private JComponent createAnswerComponent(int questionId, String questionType) {
        switch (questionType) {
            case "Multiple Choice":
            case "Dropdown":
                return createMultipleChoiceComponent(questionId);
                
            case "Checkbox":
                return createCheckboxComponent(questionId);
                
            case "Yes/No":
                return createYesNoComponent();
                
            case "Rating":
                return createRatingComponent();
                
            case "Text":
                return createTextComponent();
                
            case "Date":
                return createDateComponent();
                
            default:
                return new JTextField(30);
        }
    }
    
    private JComponent createMultipleChoiceComponent(int questionId) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        ButtonGroup group = new ButtonGroup();
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT option_id, option_text FROM question_options " +
                          "WHERE question_id = ? ORDER BY option_order";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, questionId);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                JRadioButton radioButton = new JRadioButton(rs.getString("option_text"));
                radioButton.setFont(UIHelper.NORMAL_FONT);
                radioButton.setOpaque(false);
                radioButton.setActionCommand(String.valueOf(rs.getInt("option_id")));
                group.add(radioButton);
                panel.add(radioButton);
                panel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Store the ButtonGroup in the panel's client property
        panel.putClientProperty("buttonGroup", group);
        
        return panel;
    }
    
    private JComponent createCheckboxComponent(int questionId) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT option_id, option_text FROM question_options " +
                          "WHERE question_id = ? ORDER BY option_order";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, questionId);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                JCheckBox checkBox = new JCheckBox(rs.getString("option_text"));
                checkBox.setFont(UIHelper.NORMAL_FONT);
                checkBox.setOpaque(false);
                checkBox.setActionCommand(String.valueOf(rs.getInt("option_id")));
                panel.add(checkBox);
                panel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return panel;
    }
    
    private JComponent createYesNoComponent() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        
        ButtonGroup group = new ButtonGroup();
        
        JRadioButton yesBtn = new JRadioButton("Yes");
        yesBtn.setFont(UIHelper.NORMAL_FONT);
        yesBtn.setOpaque(false);
        yesBtn.setActionCommand("Yes");
        
        JRadioButton noBtn = new JRadioButton("No");
        noBtn.setFont(UIHelper.NORMAL_FONT);
        noBtn.setOpaque(false);
        noBtn.setActionCommand("No");
        
        group.add(yesBtn);
        group.add(noBtn);
        
        panel.add(yesBtn);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(noBtn);
        
        panel.putClientProperty("buttonGroup", group);
        
        return panel;
    }
    
    private JComponent createRatingComponent() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        
        ButtonGroup group = new ButtonGroup();
        
        for (int i = 1; i <= 5; i++) {
            JRadioButton ratingBtn = new JRadioButton(i + " ⭐");
            ratingBtn.setFont(UIHelper.NORMAL_FONT);
            ratingBtn.setOpaque(false);
            ratingBtn.setActionCommand(String.valueOf(i));
            group.add(ratingBtn);
            panel.add(ratingBtn);
        }
        
        panel.putClientProperty("buttonGroup", group);
        
        return panel;
    }
    
    private JComponent createTextComponent() {
        JTextArea textArea = new JTextArea(4, 50);
        textArea.setFont(UIHelper.NORMAL_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.LIGHT_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        return new JScrollPane(textArea);
    }
    
    private JComponent createDateComponent() {
        JTextField dateField = new JTextField(15);
        dateField.setFont(UIHelper.NORMAL_FONT);
        dateField.setText("YYYY-MM-DD");
        dateField.setForeground(Color.GRAY);
        
        dateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (dateField.getText().equals("YYYY-MM-DD")) {
                    dateField.setText("");
                    dateField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (dateField.getText().isEmpty()) {
                    dateField.setText("YYYY-MM-DD");
                    dateField.setForeground(Color.GRAY);
                }
            }
        });
        
        return dateField;
    }
    
    private void submitSurvey() {
        // Validate required questions
        for (QuestionComponent qc : questionComponents) {
            if (qc.isRequired && getAnswer(qc).isEmpty()) {
                UIHelper.showErrorMessage(this, "Please answer all required questions (marked with *)!");
                return;
            }
        }
        
        if (!UIHelper.showConfirmDialog(this, "Are you sure you want to submit this survey?")) {
            return;
        }
        
        // Calculate completion time
        long endTime = System.currentTimeMillis();
        int completionTime = (int) ((endTime - startTime) / 1000); // in seconds
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Get reward points for this survey
            String pointsQuery = "SELECT reward_points FROM survey WHERE survey_id = ?";
            PreparedStatement pointsPst = conn.prepareStatement(pointsQuery);
            pointsPst.setInt(1, surveyId);
            ResultSet pointsRs = pointsPst.executeQuery();
            int rewardPoints = 0;
            if (pointsRs.next()) {
                rewardPoints = pointsRs.getInt("reward_points");
            }
            
            // Insert submission
            String submissionQuery = "INSERT INTO survey_submissions " +
                                    "(survey_id, user_id, completion_time, points_earned) " +
                                    "VALUES (?, ?, ?, ?)";
            PreparedStatement submissionPst = conn.prepareStatement(submissionQuery, 
                                                                   Statement.RETURN_GENERATED_KEYS);
            submissionPst.setInt(1, surveyId);
            submissionPst.setInt(2, userId);
            submissionPst.setInt(3, completionTime);
            submissionPst.setInt(4, rewardPoints);
            submissionPst.executeUpdate();
            
            ResultSet generatedKeys = submissionPst.getGeneratedKeys();
            int submissionId = 0;
            if (generatedKeys.next()) {
                submissionId = generatedKeys.getInt(1);
            }
            
            // Insert answers
//            String answerQuery = "INSERT INTO responses " +
//                                "(submission_id, user_id, question_id, answer_text, selected_option_id) " +
//                                "VALUES (?, ?, ?, ?, ?)";
         // ❌ WRONG - Column name is incorrect
            String answerQuery = "INSERT INTO responses " +
                    "(submission_id, user_id, question_id, answer, selected_option_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement answerPst = conn.prepareStatement(answerQuery);
            
            for (QuestionComponent qc : questionComponents) {
                answerPst.setInt(1, submissionId);
                answerPst.setInt(2, userId);
                answerPst.setInt(3, qc.questionId);
                
                String answer = getAnswer(qc);
                Integer optionId = getSelectedOptionId(qc);
                
                answerPst.setString(4, answer);
                if (optionId != null) {
                    answerPst.setInt(5, optionId);
                } else {
                    answerPst.setNull(5, Types.INTEGER);
                }
                
                answerPst.executeUpdate();
            }
            
            // Update user statistics
            String updateUserQuery = "UPDATE users SET total_surveys_taken = total_surveys_taken + 1 " +
                                    "WHERE user_id = ?";
            PreparedStatement updateUserPst = conn.prepareStatement(updateUserQuery);
            updateUserPst.setInt(1, userId);
            updateUserPst.executeUpdate();
            
            // Update user rewards
            String updateRewardsQuery = "UPDATE user_rewards SET " +
                                       "points_earned = points_earned + ?, " +
                                       "current_balance = current_balance + ? " +
                                       "WHERE user_id = ?";
            PreparedStatement updateRewardsPst = conn.prepareStatement(updateRewardsQuery);
            updateRewardsPst.setInt(1, rewardPoints);
            updateRewardsPst.setInt(2, rewardPoints);
            updateRewardsPst.setInt(3, userId);
            updateRewardsPst.executeUpdate();
            
            // Update survey total responses
            String updateSurveyQuery = "UPDATE survey SET total_responses = total_responses + 1 " +
                                      "WHERE survey_id = ?";
            PreparedStatement updateSurveyPst = conn.prepareStatement(updateSurveyQuery);
            updateSurveyPst.setInt(1, surveyId);
            updateSurveyPst.executeUpdate();
            
            // Create notification
            String notifQuery = "INSERT INTO notifications (user_id, title, message, type) " +
                               "VALUES (?, ?, ?, ?)";
            PreparedStatement notifPst = conn.prepareStatement(notifQuery);
            notifPst.setInt(1, userId);
            notifPst.setString(2, "Survey Completed!");
            notifPst.setString(3, "You earned " + rewardPoints + " points for completing: " + surveyTitle);
            notifPst.setString(4, "Reward");
            notifPst.executeUpdate();
            
            conn.commit();
            
            UIHelper.showSuccessMessage(this, 
                "Survey submitted successfully!\n\n" +
                "You earned " + rewardPoints + " points!\n" +
                "Time taken: " + (completionTime / 60) + " minutes " + (completionTime % 60) + " seconds");
            
            // Go back to dashboard
            new UserDashboard(userId, userName, userEmail).setVisible(true);
            this.dispose();
            
        } catch (SQLException e) {
            UIHelper.showErrorMessage(this, "Error submitting survey: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getAnswer(QuestionComponent qc) {
        if (qc.component instanceof JPanel) {
            ButtonGroup group = (ButtonGroup) ((JPanel)qc.component).getClientProperty("buttonGroup");
            if (group != null && group.getSelection() != null) {
                return group.getSelection().getActionCommand();
            }
            
            // For checkboxes
            StringBuilder answer = new StringBuilder();
            for (Component comp : ((JPanel)qc.component).getComponents()) {
                if (comp instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) comp;
                    if (checkBox.isSelected()) {
                        if (answer.length() > 0) answer.append(", ");
                        answer.append(checkBox.getText());
                    }
                }
            }
            return answer.toString();
        } else if (qc.component instanceof JScrollPane) {
            JTextArea textArea = (JTextArea) ((JScrollPane)qc.component).getViewport().getView();
            return textArea.getText().trim();
        } else if (qc.component instanceof JTextField) {
            return ((JTextField)qc.component).getText().trim();
        }
        
        return "";
    }
    
    private Integer getSelectedOptionId(QuestionComponent qc) {
        if (qc.component instanceof JPanel) {
            ButtonGroup group = (ButtonGroup) ((JPanel)qc.component).getClientProperty("buttonGroup");
            if (group != null && group.getSelection() != null) {
                try {
                    return Integer.parseInt(group.getSelection().getActionCommand());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
    
    private void cancelSurvey() {
        if (UIHelper.showConfirmDialog(this, "Are you sure you want to cancel? All answers will be lost.")) {
            new AvailableSurveys(userId, userName, userEmail).setVisible(true);
            this.dispose();
        }
    }
    
    // Helper class
    class QuestionComponent {
        int questionId;
        String questionType;
        JComponent component;
        boolean isRequired;
        
        public QuestionComponent(int questionId, String questionType, 
                                JComponent component, boolean isRequired) {
            this.questionId = questionId;
            this.questionType = questionType;
            this.component = component;
            this.isRequired = isRequired;
        }
    }
}