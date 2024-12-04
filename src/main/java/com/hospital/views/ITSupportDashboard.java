package com.hospital.views;

import com.hospital.models.User;
import com.hospital.models.ITSupportTicket;
import com.hospital.data.DataStore;
import com.hospital.models.ITSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ITSupportDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(ITSupportDashboard.class);
    private JPanel menuPanel;
    private final DataStore dataStore;
    private static final String[] TICKET_PRIORITIES = {"HIGH", "MEDIUM", "LOW"};
    private static final String[] TICKET_CATEGORIES = {
        "Hardware", "Software", "Network", "Account Access", "System Maintenance", "Other"
    };
    private static final String[] TICKET_STATUSES = {
        "OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED", "ON_HOLD"
    };

    public ITSupportDashboard(User currentUser) {
        super(currentUser);
        this.dataStore = DataStore.getInstance();
        setTitle("IT Support Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton ticketsButton = createMenuButton("Support Tickets");
        JButton maintenanceButton = createMenuButton("System Maintenance");
        JButton inventoryButton = createMenuButton("IT Inventory");
        JButton reportsButton = createMenuButton("Reports");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        ticketsButton.addActionListener(e -> showPanel(createTicketsPanel()));
        maintenanceButton.addActionListener(e -> showPanel(createMaintenancePanel()));
        inventoryButton.addActionListener(e -> showPanel(createInventoryPanel()));
        reportsButton.addActionListener(e -> showPanel(createReportsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(ticketsButton);
        menuPanel.add(maintenanceButton);
        menuPanel.add(inventoryButton);
        menuPanel.add(reportsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        showPanel(createTicketsPanel());
    }

    private JPanel createTicketsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model for tickets
        String[] columnNames = {"Ticket ID", "Category", "Description", "Priority", "Status", "Created Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch all tickets
        List<ITSupportTicket> tickets = dataStore.getAllTickets();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (ITSupportTicket ticket : tickets) {
            model.addRow(new Object[]{
                ticket.getId(),
                ticket.getCategory(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedDate().format(formatter)
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> statusFilter = new JComboBox<>(TICKET_STATUSES);
        JComboBox<String> priorityFilter = new JComboBox<>(TICKET_PRIORITIES);
        JButton filterButton = new JButton("Apply Filter");

        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("Priority:"));
        filterPanel.add(priorityFilter);
        filterPanel.add(filterButton);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton newButton = new JButton("New Ticket");
        JButton viewButton = new JButton("View Details");
        JButton updateButton = new JButton("Update Status");

        newButton.addActionListener(e -> showNewTicketDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int ticketId = (int) table.getValueAt(selectedRow, 0);
                showTicketDetails(ticketId);
            } else {
                showError("Please select a ticket to view");
            }
        });
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int ticketId = (int) table.getValueAt(selectedRow, 0);
                showUpdateTicketDialog(ticketId);
            } else {
                showError("Please select a ticket to update");
            }
        });

        filterButton.addActionListener(e -> {
            String status = (String) statusFilter.getSelectedItem();
            String priority = (String) priorityFilter.getSelectedItem();
            filterTickets(model, status, priority);
        });

        buttonsPanel.add(newButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(updateButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void filterTickets(DefaultTableModel model, String status, String priority) {
        model.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<ITSupportTicket> tickets = dataStore.getAllTickets();
        
        for (ITSupportTicket ticket : tickets) {
            if ((status == null || ticket.getStatus().equals(status)) &&
                (priority == null || ticket.getPriority().equals(priority))) {
                model.addRow(new Object[]{
                    ticket.getId(),
                    ticket.getCategory(),
                    ticket.getDescription(),
                    ticket.getPriority(),
                    ticket.getStatus(),
                    ticket.getCreatedDate().format(formatter)
                });
            }
        }
    }

    private void showNewTicketDialog() {
        JDialog dialog = new JDialog(this, "New Support Ticket", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> categoryCombo = new JComboBox<>(TICKET_CATEGORIES);
        JTextArea descriptionArea = new JTextArea(5, 30);
        JComboBox<String> priorityCombo = new JComboBox<>(TICKET_PRIORITIES);

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        dialog.add(categoryCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1;
        dialog.add(priorityCombo, gbc);

        JButton saveButton = new JButton("Create Ticket");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            String category = (String) categoryCombo.getSelectedItem();
            String description = descriptionArea.getText().trim();
            String priority = (String) priorityCombo.getSelectedItem();

            if (description.isEmpty()) {
                showError("Description is required");
                return;
            }

            ITSupportTicket ticket = new ITSupportTicket(0, currentUser.getId(), category, description, priority);
            dataStore.addTicket(ticket);
            dialog.dispose();
            showPanel(createTicketsPanel());
            showMessage("Ticket created successfully");
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showTicketDetails(int ticketId) {
        ITSupportTicket ticket = dataStore.getTicket(ticketId);
        if (ticket == null) {
            showError("Ticket not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Ticket Details", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        addDetailField(detailsPanel, "Ticket ID:", String.valueOf(ticket.getId()), gbc, 0);
        addDetailField(detailsPanel, "Category:", ticket.getCategory(), gbc, 1);
        addDetailField(detailsPanel, "Priority:", ticket.getPriority(), gbc, 2);
        addDetailField(detailsPanel, "Status:", ticket.getStatus(), gbc, 3);
        addDetailField(detailsPanel, "Created:", ticket.getCreatedDate().format(formatter), gbc, 4);
        
        JTextArea descriptionArea = new JTextArea(ticket.getDescription());
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(detailsPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showUpdateTicketDialog(int ticketId) {
        ITSupportTicket ticket = dataStore.getTicket(ticketId);
        if (ticket == null) {
            showError("Ticket not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Update Ticket Status", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> statusCombo = new JComboBox<>(TICKET_STATUSES);
        statusCombo.setSelectedItem(ticket.getStatus());
        JTextArea resolutionArea = new JTextArea(5, 30);
        resolutionArea.setText(ticket.getResolution());

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        dialog.add(statusCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Resolution:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JScrollPane(resolutionArea), gbc);

        JButton updateButton = new JButton("Update");
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(updateButton, gbc);

        updateButton.addActionListener(e -> {
            String newStatus = (String) statusCombo.getSelectedItem();
            String resolution = resolutionArea.getText().trim();

            if (newStatus.equals("RESOLVED") && resolution.isEmpty()) {
                showError("Resolution is required when marking ticket as resolved");
                return;
            }

            ticket.setStatus(newStatus);
            ticket.setResolution(resolution);
            if (newStatus.equals("RESOLVED")) {
                ticket.setResolvedDate(LocalDateTime.now());
                ticket.setResolvedAt(LocalDateTime.now());
            }
            dataStore.updateTicket(ticket);
            dialog.dispose();
            showPanel(createTicketsPanel());
            showMessage("Ticket updated successfully");
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createMaintenancePanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create maintenance schedule table
        String[] columnNames = {"Task", "Frequency", "Last Run", "Next Run", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        // Add some dummy maintenance tasks
        model.addRow(new Object[]{"System Backup", "Daily", "2024-01-03", "2024-01-04", "Scheduled"});
        model.addRow(new Object[]{"Database Cleanup", "Weekly", "2023-12-28", "2024-01-04", "Pending"});
        model.addRow(new Object[]{"Security Scan", "Daily", "2024-01-03", "2024-01-04", "Running"});
        model.addRow(new Object[]{"Log Rotation", "Weekly", "2023-12-28", "2024-01-04", "Scheduled"});

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton runButton = new JButton("Run Now");
        JButton scheduleButton = new JButton("Schedule");
        JButton historyButton = new JButton("View History");

        runButton.addActionListener(e -> showMessage("Maintenance task execution is not implemented in this demo"));
        scheduleButton.addActionListener(e -> showMessage("Task scheduling is not implemented in this demo"));
        historyButton.addActionListener(e -> showMessage("Task history view is not implemented in this demo"));

        buttonsPanel.add(runButton);
        buttonsPanel.add(scheduleButton);
        buttonsPanel.add(historyButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create inventory table
        String[] columnNames = {"Item ID", "Name", "Category", "Status", "Assigned To", "Last Updated"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        // Add some dummy inventory items
        model.addRow(new Object[]{1, "Dell Laptop", "Hardware", "In Use", "John Doe", "2024-01-03"});
        model.addRow(new Object[]{2, "HP Printer", "Hardware", "Available", "-", "2024-01-02"});
        model.addRow(new Object[]{3, "Windows 10 Pro", "Software", "Active", "Multiple", "2024-01-03"});
        model.addRow(new Object[]{4, "Cisco Router", "Network", "Active", "Floor 2", "2024-01-01"});

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton assignButton = new JButton("Assign");

        addButton.addActionListener(e -> showMessage("Inventory management is not implemented in this demo"));
        editButton.addActionListener(e -> showMessage("Item editing is not implemented in this demo"));
        assignButton.addActionListener(e -> showMessage("Item assignment is not implemented in this demo"));

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(assignButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JPanel reportTypesPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        reportTypesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton ticketReportBtn = new JButton("Ticket Statistics");
        JButton performanceReportBtn = new JButton("Performance Report");
        JButton inventoryReportBtn = new JButton("Inventory Report");
        JButton maintenanceReportBtn = new JButton("Maintenance Report");
        JButton auditReportBtn = new JButton("Audit Log");
        JButton customReportBtn = new JButton("Custom Report");

        ActionListener reportListener = e -> showMessage("Report generation is not implemented in this demo");
        
        ticketReportBtn.addActionListener(reportListener);
        performanceReportBtn.addActionListener(reportListener);
        inventoryReportBtn.addActionListener(reportListener);
        maintenanceReportBtn.addActionListener(reportListener);
        auditReportBtn.addActionListener(reportListener);
        customReportBtn.addActionListener(reportListener);

        reportTypesPanel.add(ticketReportBtn);
        reportTypesPanel.add(performanceReportBtn);
        reportTypesPanel.add(inventoryReportBtn);
        reportTypesPanel.add(maintenanceReportBtn);
        reportTypesPanel.add(auditReportBtn);
        reportTypesPanel.add(customReportBtn);

        panel.add(reportTypesPanel, BorderLayout.CENTER);

        return panel;
    }

    private void addDetailField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
    }

    private JPanel createProfilePanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addDetailField(panel, "Name:", currentUser.getName(), gbc, row++);
        addDetailField(panel, "Username:", currentUser.getUsername(), gbc, row++);
        addDetailField(panel, "Role:", currentUser.getRole(), gbc, row++);
        addDetailField(panel, "Email:", currentUser.getEmail(), gbc, row++);
        addDetailField(panel, "Hospital ID:", String.valueOf(currentUser.getHospitalId()), gbc, row++);

        if (currentUser instanceof ITSupport) {
            ITSupport itSupport = (ITSupport) currentUser;
            addDetailField(panel, "Specialization:", itSupport.getSpecialization(), gbc, row++);
            addDetailField(panel, "Contact Number:", itSupport.getContactNumber(), gbc, row++);
            addDetailField(panel, "Available:", String.valueOf(itSupport.isAvailable()), gbc, row++);
        }

        // Add some padding
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }
} 