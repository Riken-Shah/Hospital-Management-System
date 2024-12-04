package com.hospital.views;

import com.hospital.models.User;
import com.hospital.models.Medicine;
import com.hospital.models.Prescription;
import com.hospital.models.Supplier;
import com.hospital.models.MedicineOrder;
import com.hospital.models.PharmacyInventory;
import com.hospital.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    @Override
    protected void showPanel(JPanel newPanel) {
        // Remove the current content panel
        for (Component comp : getContentPane().getComponents()) {
            if (comp != menuPanel) {
                remove(comp);
            }
        }
        // Add the new panel
        add(newPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
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
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and display all medicines
        List<Medicine> medicines = dataStore.getAllMedicines();
        for (Medicine medicine : medicines) {
            model.addRow(new Object[]{
                medicine.getId(),
                medicine.getName(),
                medicine.getQuantity(),
                medicine.getUnitPrice(),
                medicine.getExpiryDate()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Medicine");
        JButton updateButton = new JButton("Update Stock");
        JButton removeButton = new JButton("Remove Item");

        addButton.addActionListener(e -> showAddMedicineDialog());
        updateButton.addActionListener(e -> showUpdateStockDialog(table));
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
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch all orders
        List<MedicineOrder> orders = dataStore.getAllMedicineOrders();
        for (MedicineOrder order : orders) {
            Supplier supplier = dataStore.getSupplier(order.getSupplierId());
            model.addRow(new Object[]{
                order.getId(),
                supplier.getName(),
                order.getOrderDate(),
                order.getItemCount(),
                order.getStatus()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton newOrderButton = new JButton("New Order");
        JButton viewButton = new JButton("View Order");
        JButton trackButton = new JButton("Track Order");

        newOrderButton.addActionListener(e -> showNewOrderDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int orderId = (int) table.getValueAt(selectedRow, 0);
                showOrderDetails(orderId);
            } else {
                showError("Please select an order to view");
            }
        });

        trackButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int orderId = (int) table.getValueAt(selectedRow, 0);
                trackOrder(orderId);
            } else {
                showError("Please select an order to track");
            }
        });

        buttonsPanel.add(newOrderButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(trackButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showNewOrderDialog() {
        JDialog dialog = new JDialog(this, "New Order", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Supplier selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<Supplier> supplierCombo = new JComboBox<>();
        List<Supplier> suppliers = dataStore.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            supplierCombo.addItem(supplier);
        }
        topPanel.add(new JLabel("Supplier:"));
        topPanel.add(supplierCombo);

        // Medicines table
        String[] columnNames = {"Medicine", "Current Stock", "Order Quantity"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Integer.class : String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only allow editing the Order Quantity column
            }
        };

        List<Medicine> medicines = dataStore.getAllMedicines();
        for (Medicine medicine : medicines) {
            model.addRow(new Object[]{
                medicine.getName(),
                String.valueOf(medicine.getQuantity()),
                0
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton placeOrderButton = new JButton("Place Order");
        JButton cancelButton = new JButton("Cancel");

        placeOrderButton.addActionListener(e -> {
            Supplier selectedSupplier = (Supplier) supplierCombo.getSelectedItem();
            if (selectedSupplier == null) {
                showError("Please select a supplier");
                return;
            }

            MedicineOrder order = new MedicineOrder(0, selectedSupplier.getId(), LocalDate.now(), "PENDING");
            boolean hasItems = false;

            for (int i = 0; i < table.getRowCount(); i++) {
                Object quantityObj = table.getValueAt(i, 2);
                int quantity;
                if (quantityObj instanceof String) {
                    try {
                        quantity = Integer.parseInt((String) quantityObj);
                    } catch (NumberFormatException ex) {
                        quantity = 0;
                    }
                } else {
                    quantity = (Integer) quantityObj;
                }
                
                if (quantity > 0) {
                    Medicine medicine = medicines.get(i);
                    order.addItem(medicine.getId(), quantity);
                    hasItems = true;
                }
            }

            if (!hasItems) {
                showError("Please add at least one item to the order");
                return;
            }

            dataStore.addMedicineOrder(order);
            dialog.dispose();
            showPanel(createOrdersPanel()); // Directly update the panel
            showMessage("Order placed successfully");
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(placeOrderButton);
        buttonsPanel.add(cancelButton);

        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showOrderDetails(int orderId) {
        MedicineOrder order = dataStore.getMedicineOrder(orderId);
        if (order == null) {
            showError("Order not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Order Details", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Order info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Supplier supplier = dataStore.getSupplier(order.getSupplierId());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addDetailField(infoPanel, "Order ID:", String.valueOf(order.getId()), gbc, row++);
        addDetailField(infoPanel, "Supplier:", supplier.getName(), gbc, row++);
        addDetailField(infoPanel, "Order Date:", order.getOrderDate().toString(), gbc, row++);
        addDetailField(infoPanel, "Status:", order.getStatus(), gbc, row);

        // Items table
        String[] columnNames = {"Medicine", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        Map<Integer, Integer> items = order.getItems();
        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            Medicine medicine = dataStore.getMedicine(entry.getKey());
            model.addRow(new Object[]{
                medicine.getName(),
                entry.getValue()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        dialog.add(infoPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void trackOrder(int orderId) {
        MedicineOrder order = dataStore.getMedicineOrder(orderId);
        if (order == null) {
            showError("Order not found");
            return;
        }

        // In a real application, this would show real tracking information
        String status = order.getStatus();
        String message = String.format("Order #%d Status: %s\n", orderId, status);
        
        if (status.equals("PENDING")) {
            message += "Order is being processed by the supplier.";
        } else if (status.equals("SHIPPED")) {
            message += "Order has been shipped and is in transit.";
        } else if (status.equals("DELIVERED")) {
            message += "Order has been delivered successfully.";
        }

        JOptionPane.showMessageDialog(this, message, "Order Tracking", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshOrdersPanel() {
        showPanel(createOrdersPanel());
    }

    private void refreshPrescriptionsPanel() {
        showPanel(createPrescriptionsPanel());
    }

    private void removeInventoryItem(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this item?",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                int medicineId = (int) table.getValueAt(selectedRow, 0);
                Medicine medicine = dataStore.getMedicine(medicineId);
                if (medicine != null) {
                    dataStore.deleteMedicine(medicineId);
                    showMessage("Item removed successfully");
                    refreshInventoryPanel();
                }
            }
        } else {
            showError("Please select an item to remove");
        }
    }

    private JPanel createSuppliersPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model for suppliers
        String[] columnNames = {"Supplier ID", "Name", "Contact", "Email", "Rating"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch all suppliers
        List<Supplier> suppliers = dataStore.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            model.addRow(new Object[]{
                supplier.getId(),
                supplier.getName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getRating()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Supplier");
        JButton editButton = new JButton("Edit Supplier");
        JButton removeButton = new JButton("Remove Supplier");

        addButton.addActionListener(e -> showAddSupplierDialog());
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int supplierId = (int) table.getValueAt(selectedRow, 0);
                showEditSupplierDialog(supplierId);
            } else {
                showError("Please select a supplier to edit");
            }
        });
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int supplierId = (int) table.getValueAt(selectedRow, 0);
                removeSupplier(supplierId);
            } else {
                showError("Please select a supplier to remove");
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(removeButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showAddSupplierDialog() {
        JDialog dialog = new JDialog(this, "Add New Supplier", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JComboBox<String> ratingCombo = new JComboBox<>(new String[]{"A", "B", "C"});

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        dialog.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Rating:"), gbc);
        gbc.gridx = 1;
        dialog.add(ratingCombo, gbc);

        JButton saveButton = new JButton("Save");
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String rating = (String) ratingCombo.getSelectedItem();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                showError("All fields are required");
                return;
            }

            Supplier supplier = new Supplier(0, name, phone, email, rating);
            dataStore.addSupplier(supplier);
            dialog.dispose();
            refreshSuppliersPanel();
            showMessage("Supplier added successfully");
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditSupplierDialog(int supplierId) {
        Supplier supplier = dataStore.getSupplier(supplierId);
        if (supplier == null) {
            showError("Supplier not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Supplier", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField phoneField = new JTextField(supplier.getPhone(), 20);
        JTextField emailField = new JTextField(supplier.getEmail(), 20);
        JComboBox<String> ratingCombo = new JComboBox<>(new String[]{"A", "B", "C"});
        ratingCombo.setSelectedItem(supplier.getRating());

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        dialog.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Rating:"), gbc);
        gbc.gridx = 1;
        dialog.add(ratingCombo, gbc);

        JButton updateButton = new JButton("Update");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(updateButton, gbc);

        updateButton.addActionListener(e -> {
            String phone = phoneField.getText();
            String email = emailField.getText();
            String rating = (String) ratingCombo.getSelectedItem();

            if (phone.isEmpty() || email.isEmpty()) {
                showError("All fields are required");
                return;
            }

            supplier.setPhone(phone);
            supplier.setEmail(email);
            supplier.setRating(rating);
            dataStore.updateSupplier(supplier);
            dialog.dispose();
            refreshSuppliersPanel();
            showMessage("Supplier updated successfully");
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removeSupplier(int supplierId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to remove this supplier?",
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dataStore.deleteSupplier(supplierId);
            refreshSuppliersPanel();
            showMessage("Supplier removed successfully");
        }
    }

    private void refreshSuppliersPanel() {
        showPanel(createSuppliersPanel());
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

        inventoryReportBtn.addActionListener(e -> generateInventoryReport());
        salesReportBtn.addActionListener(e -> generateSalesReport());
        expiryReportBtn.addActionListener(e -> generateExpiryReport());
        supplierReportBtn.addActionListener(e -> generateSupplierReport());
        stockAlertBtn.addActionListener(e -> generateLowStockAlert());
        customReportBtn.addActionListener(e -> showCustomReportDialog());

        reportTypesPanel.add(inventoryReportBtn);
        reportTypesPanel.add(salesReportBtn);
        reportTypesPanel.add(expiryReportBtn);
        reportTypesPanel.add(supplierReportBtn);
        reportTypesPanel.add(stockAlertBtn);
        reportTypesPanel.add(customReportBtn);

        panel.add(reportTypesPanel, BorderLayout.CENTER);

        return panel;
    }

    private void generateInventoryReport() {
        List<Medicine> medicines = dataStore.getAllMedicines();
        StringBuilder report = new StringBuilder();
        report.append("Inventory Report\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        report.append(String.format("%-30s %-10s %-10s %-15s %-10s\n", 
            "Medicine Name", "Quantity", "Price", "Category", "Status"));
        report.append("-".repeat(80)).append("\n");

        for (Medicine medicine : medicines) {
            report.append(String.format("%-30s %-10d $%-9.2f %-15s %-10s\n",
                medicine.getName(),
                medicine.getQuantity(),
                medicine.getUnitPrice(),
                medicine.getCategory(),
                medicine.isLowStock() ? "LOW" : "OK"
            ));
        }

        showReportDialog("Inventory Report", report.toString());
    }

    private void generateSalesReport() {
        // In a real application, this would fetch sales data
        showMessage("Sales report generation is not implemented in this demo");
    }

    private void generateExpiryReport() {
        List<Medicine> medicines = dataStore.getAllMedicines();
        StringBuilder report = new StringBuilder();
        report.append("Expiry Report\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        report.append(String.format("%-30s %-15s %-10s %-10s\n", 
            "Medicine Name", "Expiry Date", "Quantity", "Status"));
        report.append("-".repeat(70)).append("\n");

        for (Medicine medicine : medicines) {
            String status = medicine.isExpired() ? "EXPIRED" : 
                          (medicine.getExpiryDate().isBefore(LocalDate.now().plusMonths(3)) ? "NEAR EXPIRY" : "OK");
            report.append(String.format("%-30s %-15s %-10d %-10s\n",
                medicine.getName(),
                medicine.getExpiryDate(),
                medicine.getQuantity(),
                status
            ));
        }

        showReportDialog("Expiry Report", report.toString());
    }

    private void generateSupplierReport() {
        List<Supplier> suppliers = dataStore.getAllSuppliers();
        StringBuilder report = new StringBuilder();
        report.append("Supplier Report\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        report.append(String.format("%-30s %-15s %-25s %-10s\n", 
            "Supplier Name", "Contact", "Email", "Rating"));
        report.append("-".repeat(80)).append("\n");

        for (Supplier supplier : suppliers) {
            report.append(String.format("%-30s %-15s %-25s %-10s\n",
                supplier.getName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getRating()
            ));
        }

        showReportDialog("Supplier Report", report.toString());
    }

    private void generateLowStockAlert() {
        List<Medicine> medicines = dataStore.getAllMedicines();
        StringBuilder report = new StringBuilder();
        report.append("Low Stock Alert Report\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        report.append(String.format("%-30s %-10s %-15s\n", 
            "Medicine Name", "Quantity", "Category"));
        report.append("-".repeat(60)).append("\n");

        boolean hasLowStock = false;
        for (Medicine medicine : medicines) {
            if (medicine.isLowStock()) {
                hasLowStock = true;
                report.append(String.format("%-30s %-10d %-15s\n",
                    medicine.getName(),
                    medicine.getQuantity(),
                    medicine.getCategory()
                ));
            }
        }

        if (!hasLowStock) {
            report.append("No items are currently low in stock.\n");
        }

        showReportDialog("Low Stock Alert", report.toString());
    }

    private void showCustomReportDialog() {
        // In a real application, this would show a dialog to customize report parameters
        showMessage("Custom report generation is not implemented in this demo");
    }

    private void showReportDialog(String title, String content) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout(10, 10));

        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton printButton = new JButton("Print");
        JButton exportButton = new JButton("Export");
        JButton closeButton = new JButton("Close");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(printButton);
        buttonsPanel.add(exportButton);
        buttonsPanel.add(closeButton);

        printButton.addActionListener(e -> {
            showMessage("Printing functionality is not implemented in this demo");
        });

        exportButton.addActionListener(e -> {
            showMessage("Export functionality is not implemented in this demo");
        });

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add prescription details
        User patient = dataStore.getUser(prescription.getPatientId());
        User doctor = dataStore.getUser(prescription.getDoctorId());

        addDetailField(detailsPanel, "Patient:", patient.getName(), gbc, 0);
        addDetailField(detailsPanel, "Doctor:", doctor.getName(), gbc, 1);
        addDetailField(detailsPanel, "Date:", prescription.getDate().toString(), gbc, 2);
        addDetailField(detailsPanel, "Diagnosis:", prescription.getDiagnosis(), gbc, 3);
        addDetailField(detailsPanel, "Instructions:", prescription.getInstructions(), gbc, 4);

        // Add medications table
        String[] columnNames = {"Medication", "Dosage", "Duration", "Instructions"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        for (Prescription.Medication med : prescription.getMedications()) {
            model.addRow(new Object[]{
                med.getName(),
                med.getDosage(),
                med.getDuration(),
                med.getInstructions()
            });
        }

        JTable medicationsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(medicationsTable);

        dialog.add(detailsPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addDetailField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
    }

    private void fulfillPrescription(int prescriptionId) {
        Prescription prescription = dataStore.getPrescription(prescriptionId);
        if (prescription == null) {
            showError("Prescription not found");
            return;
        }

        // Check if all medications are available in inventory
        boolean canFulfill = true;
        StringBuilder unavailableMeds = new StringBuilder();
        Map<Medicine, Integer> medicineQuantities = new HashMap<>();

        for (Prescription.Medication med : prescription.getMedications()) {
            Medicine medicine = findMedicineByName(med.getName());
            if (medicine == null) {
                canFulfill = false;
                unavailableMeds.append(med.getName()).append(" (not found), ");
            } else if (medicine.getQuantity() <= 0) {
                canFulfill = false;
                unavailableMeds.append(med.getName()).append(" (out of stock), ");
            } else {
                medicineQuantities.put(medicine, 1); // Store medicine and quantity to dispense
            }
        }

        if (!canFulfill) {
            showError("Cannot fulfill prescription. Issues with medications: " + 
                     unavailableMeds.substring(0, unavailableMeds.length() - 2));
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to fulfill this prescription?",
            "Confirm Fulfillment",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Update inventory
            for (Map.Entry<Medicine, Integer> entry : medicineQuantities.entrySet()) {
                Medicine medicine = entry.getKey();
                int quantityToDispense = entry.getValue();
                medicine.setQuantity(medicine.getQuantity() - quantityToDispense);
                dataStore.updateMedicine(medicine);
            }

            prescription.setActive(false);
            dataStore.updatePrescription(prescription);
            showPanel(createPrescriptionsPanel());
            showMessage("Prescription fulfilled successfully");
        }
    }

    private void showAddMedicineDialog() {
        JDialog dialog = new JDialog(this, "Add New Medicine", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add form fields
        JTextField nameField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField formField = new JTextField(20);
        JTextField categoryField = new JTextField(20);
        JSpinner expiryDateSpinner = new JSpinner(new SpinnerDateModel());

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        dialog.add(quantityField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Unit Price:"), gbc);
        gbc.gridx = 1;
        dialog.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Form:"), gbc);
        gbc.gridx = 1;
        dialog.add(formField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        dialog.add(categoryField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Expiry Date:"), gbc);
        gbc.gridx = 1;
        dialog.add(expiryDateSpinner, gbc);

        JButton saveButton = new JButton("Save");
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                String form = formField.getText().trim();
                String category = categoryField.getText().trim();
                Date date = ((SpinnerDateModel)expiryDateSpinner.getModel()).getDate();
                LocalDate expiryDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (name.isEmpty() || form.isEmpty() || category.isEmpty()) {
                    showError("All fields are required");
                    return;
                }

                Medicine medicine = new Medicine(0, name, quantity, price, expiryDate, form, category);
                dataStore.addMedicine(medicine);
                dialog.dispose();
                showPanel(createInventoryPanel());
                showMessage("Medicine added successfully");
            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers for quantity and price");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showUpdateStockDialog(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showError("Please select a medicine to update");
            return;
        }

        int medicineId = (int) table.getValueAt(selectedRow, 0);
        Medicine medicine = dataStore.getMedicine(medicineId);

        JDialog dialog = new JDialog(this, "Update Stock", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField quantityField = new JTextField(20);
        JTextField priceField = new JTextField(String.valueOf(medicine.getUnitPrice()));

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("New Quantity:"), gbc);
        gbc.gridx = 1;
        dialog.add(quantityField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("New Price:"), gbc);
        gbc.gridx = 1;
        dialog.add(priceField, gbc);

        JButton updateButton = new JButton("Update");
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(updateButton, gbc);

        updateButton.addActionListener(e -> {
            try {
                int newQuantity = Integer.parseInt(quantityField.getText());
                double newPrice = Double.parseDouble(priceField.getText());

                medicine.setQuantity(newQuantity);
                medicine.setUnitPrice(newPrice);
                dataStore.updateMedicine(medicine);
                dialog.dispose();
                refreshInventoryPanel();
                showMessage("Stock updated successfully");
            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void refreshInventoryPanel() {
        showPanel(createInventoryPanel());
    }

    @Override
    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Helper method to find medicine by name
    private Medicine findMedicineByName(String name) {
        List<Medicine> medicines = dataStore.getAllMedicines();
        return medicines.stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
} 