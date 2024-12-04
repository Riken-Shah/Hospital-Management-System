package com.hospital.views;

import com.hospital.models.User;
import com.hospital.security.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(LoginFrame.class);
    private final AuthService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(AuthService authService) {
        this.authService = authService;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hospital Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add title label
        JLabel titleLabel = new JLabel("Welcome to Hospital Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, gbc);

        // Add some vertical spacing
        gbc.insets = new Insets(20, 5, 5, 5);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // Add some vertical spacing
        gbc.insets = new Insets(20, 5, 5, 5);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton, gbc);

        add(mainPanel);
        setVisible(true);

        // Set default button
        getRootPane().setDefaultButton(loginButton);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Invalid username or password");
            return;
        }

        User user = authService.login(username, password);
        if (user == null) {
            showError("Invalid username or password");
            return;
        }

        openDashboard(user);
    }

    private void openDashboard(User user) {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame dashboard = null;
                switch (user.getRole()) {
                    case "DOCTOR":
                        dashboard = new DoctorDashboard(user);
                        break;
                    case "NURSE":
                        dashboard = new NurseDashboard(user);
                        break;
                    case "ADMIN":
                        dashboard = new AdminDashboard(user);
                        break;
                    case "PHARMACIST":
                        dashboard = new PharmacistDashboard(user);
                        break;
                    case "PATIENT":
                        dashboard = new PatientDashboard(user);
                        break;
                    case "IT_SUPPORT":
                        dashboard = new ITSupportDashboard(user);
                        break;
                    case "DIETICIAN":
                        dashboard = new DieticianDashboard(user);
                        break;
                    default:
                        showError("Invalid user role: " + user.getRole());
                        return;
                }
                if (dashboard != null) {
                    dashboard.setVisible(true);
                    dispose();
                }
            } catch (Exception e) {
                logger.error("Error opening dashboard: ", e);
                showError("Error opening dashboard");
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
} 