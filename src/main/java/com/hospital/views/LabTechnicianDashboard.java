package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class LabTechnicianDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(LabTechnicianDashboard.class);
    private JPanel menuPanel;

    public LabTechnicianDashboard(User currentUser) {
        super(currentUser);
        setTitle("Lab Technician Dashboard - " + currentUser.getName());
        initializeMenu();
        setVisible(true);
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton testsButton = createMenuButton("Lab Tests");
        JButton samplesButton = createMenuButton("Sample Management");
        JButton inventoryButton = createMenuButton("Lab Inventory");
        JButton reportsButton = createMenuButton("Reports");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        testsButton.addActionListener(e -> showPanel(createTestsPanel()));
        samplesButton.addActionListener(e -> showPanel(createSamplesPanel()));
        inventoryButton.addActionListener(e -> showPanel(createInventoryPanel()));
        reportsButton.addActionListener(e -> showPanel(createReportsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(testsButton);
        menuPanel.add(samplesButton);
        menuPanel.add(inventoryButton);
        menuPanel.add(reportsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show tests panel by default
        showPanel(createTestsPanel());
    }

    protected JPanel createTestsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement lab tests panel
        return panel;
    }

    protected JPanel createSamplesPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement sample management panel
        return panel;
    }

    protected JPanel createInventoryPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement lab inventory panel
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