package main;

import javax.swing.*;
import java.awt.*;

import admin.AdminLogin;
import user.UserLogin;
import user.UserRegistration;
import utils.UIHelper;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("Survey Management System - Admin & User Portal");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
        UIHelper.centerFrame(this);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.LIGHT_COLOR);

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createCenter(), BorderLayout.CENTER);
        mainPanel.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    // ===== HEADER =====
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(1400, 80));
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel title = UIHelper.createWhiteLabel("Survey Management System");
        title.setFont(UIHelper.TITLE_FONT);

        JLabel subtitle = UIHelper.createWhiteLabel("Admin & User Portals");
        subtitle.setFont(UIHelper.SUBTITLE_FONT.deriveFont(Font.PLAIN, 18f));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(title);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);

        return header;
    }

    // ===== CENTER: LEFT (ADMIN) + RIGHT (USER) =====
    private JPanel createCenter() {
        JPanel center = new JPanel(new GridLayout(1, 2, 20, 0));
        center.setBackground(UIHelper.LIGHT_COLOR);
        center.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        center.add(createAdminCard());
        center.add(createUserCard());

        return center;
    }

    private JPanel createAdminCard() {
        JPanel card = new JPanel();
        card.setBackground(UIHelper.WHITE_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("ADMIN");
        icon.setFont(new Font("Arial", Font.BOLD, 22));
        icon.setForeground(UIHelper.PRIMARY_COLOR);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heading = UIHelper.createHeadingLabel("Admin Portal");
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("<html><center>Login here to manage:<br>" +
                "• Surveys<br>" +
                "• Users<br>" +
                "• Responses & Analytics</center></html>");
        desc.setFont(UIHelper.NORMAL_FONT);
        desc.setForeground(UIHelper.TEXT_DARK);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = UIHelper.createLargeButton("Admin Login", UIHelper.PRIMARY_COLOR);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> openAdminLogin());

        card.add(Box.createVerticalGlue());
        card.add(icon);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(heading);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(desc);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(loginBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel createUserCard() {
        JPanel card = new JPanel();
        card.setBackground(UIHelper.WHITE_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.SUCCESS_COLOR, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("USER");
        icon.setFont(new Font("Arial", Font.BOLD, 22));
        icon.setForeground(UIHelper.SUCCESS_COLOR);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heading = UIHelper.createHeadingLabel("User Portal");
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("<html><center>Login or register to:<br>" +
                "• Take surveys<br>" +
                "• Earn reward points<br>" +
                "• View your history</center></html>");
        desc.setFont(UIHelper.NORMAL_FONT);
        desc.setForeground(UIHelper.TEXT_DARK);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = UIHelper.createLargeButton("User Login", UIHelper.SUCCESS_COLOR);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> openUserLogin());

        JButton registerBtn = UIHelper.createLargeButton("Register", UIHelper.WARNING_COLOR);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.addActionListener(e -> openUserRegistration());

        card.add(Box.createVerticalGlue());
        card.add(icon);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(heading);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(desc);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(registerBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }

    // ===== FOOTER =====
    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(UIHelper.DARK_COLOR);
        footer.setPreferredSize(new Dimension(1400, 50));

        JLabel txt = UIHelper.createWhiteLabel("© 2024 Survey Management System");
        txt.setFont(UIHelper.SMALL_FONT);

        footer.add(txt);
        return footer;
    }

    // ===== NAVIGATION METHODS =====
    private void openAdminLogin() {
        new AdminLogin().setVisible(true);
        this.dispose();
    }

    private void openUserLogin() {
        new UserLogin().setVisible(true);
        this.dispose();
    }

    private void openUserRegistration() {
        new UserRegistration().setVisible(true);
        this.dispose();
    }

    // ===== MAIN ENTRY POINT =====
    public static void main(String[] args) {
        UIHelper.applyGlobalTheme();  // apply high-contrast theme once

        SwingUtilities.invokeLater(() -> {
            new MainApp().setVisible(true);
        });
    }
}