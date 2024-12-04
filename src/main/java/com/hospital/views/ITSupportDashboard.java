package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class ITSupportDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(ITSupportDashboard.class);
    private JPanel menuPanel;

    public ITSupportDashboard(User currentUser) {
        super(currentUser);
        setTitle("IT Support Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton ticketsButton = createMenuButton("Support Tickets");
        JButton systemButton = createMenuButton("System Status");
        JButton maintenanceButton = createMenuButton("Maintenance");
        JButton backupButton = createMenuButton("Backup & Recovery");
        JButton logsButton = createMenuButton("System Logs");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        ticketsButton.addActionListener(e -> showPanel(createTicketsPanel()));
        systemButton.addActionListener(e -> showPanel(createSystemStatusPanel()));
        maintenanceButton.addActionListener(e -> showPanel(createMaintenancePanel()));
        backupButton.addActionListener(e -> showPanel(createBackupPanel()));
        logsButton.addActionListener(e -> showPanel(createLogsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(ticketsButton);
        menuPanel.add(systemButton);
        menuPanel.add(maintenanceButton);
        menuPanel.add(backupButton);
        menuPanel.add(logsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show tickets panel by default
        showPanel(createTicketsPanel());
    }

    private JPanel createTicketsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement support tickets panel
        panel.add(new JLabel("Support tickets panel coming soon!"));
        return panel;
    }

    private JPanel createSystemStatusPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement system status panel
        panel.add(new JLabel("System status panel coming soon!"));
        return panel;
    }

    private JPanel createMaintenancePanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement maintenance panel
        panel.add(new JLabel("System maintenance panel coming soon!"));
        return panel;
    }

    private JPanel createBackupPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement backup panel
        panel.add(new JLabel("Backup & recovery panel coming soon!"));
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