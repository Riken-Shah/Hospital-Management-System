package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(AdminDashboard.class);
    private JPanel menuPanel;

    public AdminDashboard(User currentUser) {
        super(currentUser);
        setTitle("Admin Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton usersButton = createMenuButton("User Management");
        JButton departmentsButton = createMenuButton("Departments");
        JButton reportsButton = createMenuButton("Reports");
        JButton settingsButton = createMenuButton("System Settings");
        JButton logsButton = createMenuButton("System Logs");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        usersButton.addActionListener(e -> showPanel(createUsersPanel()));
        departmentsButton.addActionListener(e -> showPanel(createDepartmentsPanel()));
        reportsButton.addActionListener(e -> showPanel(createReportsPanel()));
        settingsButton.addActionListener(e -> showPanel(createSettingsPanel()));
        logsButton.addActionListener(e -> showPanel(createLogsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(usersButton);
        menuPanel.add(departmentsButton);
        menuPanel.add(reportsButton);
        menuPanel.add(settingsButton);
        menuPanel.add(logsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show users panel by default
        showPanel(createUsersPanel());
    }

    private JPanel createUsersPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement user management panel
        panel.add(new JLabel("User management panel coming soon!"));
        return panel;
    }

    private JPanel createDepartmentsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement departments panel
        panel.add(new JLabel("Departments management panel coming soon!"));
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement reports panel
        panel.add(new JLabel("System reports panel coming soon!"));
        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement settings panel
        panel.add(new JLabel("System settings panel coming soon!"));
        return panel;
    }

    private JPanel createLogsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement logs panel
        panel.add(new JLabel("System logs panel coming soon!"));
        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = createFormPanel();
        GridBagConstraints gbc = createGridBagConstraints();

        addProfileField(panel, "Name", currentUser.getName(), gbc);
        addProfileField(panel, "Role", currentUser.getRole(), gbc);
        addProfileField(panel, "Hospital ID", String.valueOf(currentUser.getHospitalId()), gbc);
        addProfileField(panel, "Email", currentUser.getEmail(), gbc);

        return panel;
    }
} 