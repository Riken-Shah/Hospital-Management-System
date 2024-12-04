package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class PharmacistDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(PharmacistDashboard.class);
    private JPanel menuPanel;

    public PharmacistDashboard(User currentUser) {
        super(currentUser);
        setTitle("Pharmacist Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton prescriptionsButton = createMenuButton("Prescriptions");
        JButton inventoryButton = createMenuButton("Inventory");
        JButton ordersButton = createMenuButton("Orders");
        JButton suppliersButton = createMenuButton("Suppliers");
        JButton reportsButton = createMenuButton("Reports");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        prescriptionsButton.addActionListener(e -> showPanel(createPrescriptionsPanel()));
        inventoryButton.addActionListener(e -> showPanel(createInventoryPanel()));
        ordersButton.addActionListener(e -> showPanel(createOrdersPanel()));
        suppliersButton.addActionListener(e -> showPanel(createSuppliersPanel()));
        reportsButton.addActionListener(e -> showPanel(createReportsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(prescriptionsButton);
        menuPanel.add(inventoryButton);
        menuPanel.add(ordersButton);
        menuPanel.add(suppliersButton);
        menuPanel.add(reportsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show prescriptions panel by default
        showPanel(createPrescriptionsPanel());
    }

    private JPanel createPrescriptionsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement prescriptions panel
        panel.add(new JLabel("Prescriptions panel coming soon!"));
        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement inventory panel
        panel.add(new JLabel("Inventory management panel coming soon!"));
        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement orders panel
        panel.add(new JLabel("Orders management panel coming soon!"));
        return panel;
    }

    private JPanel createSuppliersPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement suppliers panel
        panel.add(new JLabel("Suppliers management panel coming soon!"));
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement reports panel
        panel.add(new JLabel("Reports panel coming soon!"));
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