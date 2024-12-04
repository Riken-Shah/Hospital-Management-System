package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import com.hospital.models.Prescription;
import com.hospital.data.DataStore;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PharmacistDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(PharmacistDashboard.class);
    private JPanel menuPanel;
    private final DataStore dataStore;

    public PharmacistDashboard(User currentUser) {
        super(currentUser);
        this.dataStore = DataStore.getInstance();
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
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Prescription ID", "Patient", "Doctor", "Diagnosis", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch all prescriptions
        List<Prescription> prescriptions = dataStore.getAllPrescriptions();
        for (Prescription prescription : prescriptions) {
            User patient = dataStore.getUser(prescription.getPatientId());
            User doctor = dataStore.getUser(prescription.getDoctorId());
            model.addRow(new Object[]{
                prescription.getId(),
                patient.getName(),
                doctor.getName(),
                prescription.getDiagnosis(),
                "Pending" // You might want to add a status field to Prescription
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton fulfillButton = new JButton("Fulfill Prescription");
        
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int prescriptionId = (int) table.getValueAt(selectedRow, 0);
                showPrescriptionDetails(prescriptionId);
            } else {
                showError("Please select a prescription first");
            }
        });

        fulfillButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int prescriptionId = (int) table.getValueAt(selectedRow, 0);
                fulfillPrescription(prescriptionId);
            } else {
                showError("Please select a prescription first");
            }
        });

        buttonsPanel.add(viewButton);
        buttonsPanel.add(fulfillButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model for inventory
        String[] columnNames = {"Medicine ID", "Name", "Quantity", "Unit Price", "Expiry Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Medicine");
        JButton updateButton = new JButton("Update Stock");
        JButton removeButton = new JButton("Remove Item");

        addButton.addActionListener(e -> showAddMedicineDialog());
        updateButton.addActionListener(e -> showUpdateStockDialog());
        removeButton.addActionListener(e -> removeInventoryItem(table));

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(removeButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model for orders
        String[] columnNames = {"Order ID", "Supplier", "Date", "Total Items", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton newOrderButton = new JButton("New Order");
        JButton viewButton = new JButton("View Order");
        JButton trackButton = new JButton("Track Order");

        buttonsPanel.add(newOrderButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(trackButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSuppliersPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model for suppliers
        String[] columnNames = {"Supplier ID", "Name", "Contact", "Email", "Rating"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Supplier");
        JButton editButton = new JButton("Edit Supplier");
        JButton removeButton = new JButton("Remove Supplier");

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(removeButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JPanel reportTypesPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        reportTypesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton inventoryReportBtn = new JButton("Inventory Report");
        JButton salesReportBtn = new JButton("Sales Report");
        JButton expiryReportBtn = new JButton("Expiry Report");
        JButton supplierReportBtn = new JButton("Supplier Report");
        JButton stockAlertBtn = new JButton("Low Stock Alert");
        JButton customReportBtn = new JButton("Custom Report");

        reportTypesPanel.add(inventoryReportBtn);
        reportTypesPanel.add(salesReportBtn);
        reportTypesPanel.add(expiryReportBtn);
        reportTypesPanel.add(supplierReportBtn);
        reportTypesPanel.add(stockAlertBtn);
        reportTypesPanel.add(customReportBtn);

        panel.add(reportTypesPanel, BorderLayout.CENTER);

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

    // Helper methods
    private void showPrescriptionDetails(int prescriptionId) {
        Prescription prescription = dataStore.getPrescription(prescriptionId);
        if (prescription == null) {
            showError("Prescription not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Prescription Details", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add prescription details
        addDetailField(detailsPanel, "Patient:", dataStore.getUser(prescription.getPatientId()).getName());
        addDetailField(detailsPanel, "Doctor:", dataStore.getUser(prescription.getDoctorId()).getName());
        addDetailField(detailsPanel, "Diagnosis:", prescription.getDiagnosis());
        addDetailField(detailsPanel, "Instructions:", prescription.getInstructions());

        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addDetailField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value));
    }

    private void fulfillPrescription(int prescriptionId) {
        // Implement prescription fulfillment logic
        int confirm = JOptionPane.showConfirmDialog(this,
            "Mark this prescription as fulfilled?",
            "Fulfill Prescription",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Update prescription status
            showMessage("Prescription fulfilled successfully");
            refreshPrescriptionsPanel();
        }
    }

    private void showAddMedicineDialog() {
        // Implement add medicine dialog
        JDialog dialog = new JDialog(this, "Add New Medicine", true);
        // Add form fields and save functionality
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showUpdateStockDialog() {
        // Implement update stock dialog
        JDialog dialog = new JDialog(this, "Update Stock", true);
        // Add form fields and update functionality
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removeInventoryItem(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this item?",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Remove item logic
                showMessage("Item removed successfully");
                refreshInventoryPanel();
            }
        } else {
            showError("Please select an item to remove");
        }
    }

    private void refreshPrescriptionsPanel() {
        showPanel(createPrescriptionsPanel());
    }

    private void refreshInventoryPanel() {
        showPanel(createInventoryPanel());
    }
} 