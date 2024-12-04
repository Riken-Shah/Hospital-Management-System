package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class StaffDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(StaffDashboard.class);
    private JPanel menuPanel;

    public StaffDashboard(User currentUser) {
        super(currentUser);
        setTitle("Staff Dashboard - " + currentUser.getName());
        initializeMenu();
        setVisible(true);
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton scheduleButton = createMenuButton("Schedule");
        JButton tasksButton = createMenuButton("Tasks");
        JButton inventoryButton = createMenuButton("Inventory");
        JButton reportsButton = createMenuButton("Reports");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        scheduleButton.addActionListener(e -> showPanel(createSchedulePanel()));
        tasksButton.addActionListener(e -> showPanel(createTasksPanel()));
        inventoryButton.addActionListener(e -> showPanel(createInventoryPanel()));
        reportsButton.addActionListener(e -> showPanel(createReportsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(scheduleButton);
        menuPanel.add(tasksButton);
        menuPanel.add(inventoryButton);
        menuPanel.add(reportsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show schedule panel by default
        showPanel(createSchedulePanel());
    }

    protected JPanel createSchedulePanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement schedule panel
        return panel;
    }

    protected JPanel createTasksPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement tasks panel
        return panel;
    }

    protected JPanel createInventoryPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement inventory panel
        return panel;
    }

    protected JPanel createReportsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement reports panel
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