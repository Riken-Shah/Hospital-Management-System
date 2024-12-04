package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class SystemAdminDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(SystemAdminDashboard.class);
    private JPanel menuPanel;

    public SystemAdminDashboard(User currentUser) {
        super(currentUser);
        setTitle("System Admin Dashboard - " + currentUser.getName());
        initializeMenu();
        setVisible(true);
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton usersButton = createMenuButton("Users");
        JButton rolesButton = createMenuButton("Roles");
        JButton systemButton = createMenuButton("System Settings");
        JButton backupButton = createMenuButton("Backup/Restore");
        JButton logsButton = createMenuButton("System Logs");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        usersButton.addActionListener(e -> showPanel(createUsersPanel()));
        rolesButton.addActionListener(e -> showPanel(createRolesPanel()));
        systemButton.addActionListener(e -> showPanel(createSystemSettingsPanel()));
        backupButton.addActionListener(e -> showPanel(createBackupPanel()));
        logsButton.addActionListener(e -> showPanel(createLogsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(usersButton);
        menuPanel.add(rolesButton);
        menuPanel.add(systemButton);
        menuPanel.add(backupButton);
        menuPanel.add(logsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show users panel by default
        showPanel(createUsersPanel());
    }

    protected JPanel createUsersPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement users panel
        return panel;
    }

    protected JPanel createRolesPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement roles panel
        return panel;
    }

    protected JPanel createSystemSettingsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement system settings panel
        return panel;
    }

    protected JPanel createBackupPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement backup/restore panel
        return panel;
    }

    protected JPanel createLogsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement system logs panel
        return panel;
    }

    protected JPanel createProfilePanel() {
        JPanel panel = createFormPanel();
        GridBagConstraints gbc = createGridBagConstraints();

        addProfileField(panel, "Name", currentUser.getName(), gbc);
        addProfileField(panel, "Role", currentUser.getRole(), gbc);
        addProfileField(panel, "Hospital ID", String.valueOf(currentUser.getHospitalId()), gbc);
        addProfileField(panel, "Email", currentUser.getEmail(), gbc);

        return panel;
    }
} 