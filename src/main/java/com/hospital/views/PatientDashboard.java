package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class PatientDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(PatientDashboard.class);
    private JPanel menuPanel;

    public PatientDashboard(User currentUser) {
        super(currentUser);
        setTitle("Patient Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton appointmentsButton = createMenuButton("Appointments");
        JButton prescriptionsButton = createMenuButton("Prescriptions");
        JButton medicalRecordsButton = createMenuButton("Medical Records");
        JButton dietPlansButton = createMenuButton("Diet Plans");
        JButton billsButton = createMenuButton("Bills & Payments");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        appointmentsButton.addActionListener(e -> showPanel(createAppointmentsPanel()));
        prescriptionsButton.addActionListener(e -> showPanel(createPrescriptionsPanel()));
        medicalRecordsButton.addActionListener(e -> showPanel(createMedicalRecordsPanel()));
        dietPlansButton.addActionListener(e -> showPanel(createDietPlansPanel()));
        billsButton.addActionListener(e -> showPanel(createBillsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(appointmentsButton);
        menuPanel.add(prescriptionsButton);
        menuPanel.add(medicalRecordsButton);
        menuPanel.add(dietPlansButton);
        menuPanel.add(billsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show appointments panel by default
        showPanel(createAppointmentsPanel());
    }

    private JPanel createAppointmentsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Time", "Doctor", "Department", "Status"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton scheduleButton = new JButton("Schedule Appointment");
        JButton rescheduleButton = new JButton("Reschedule");
        JButton cancelButton = new JButton("Cancel Appointment");

        scheduleButton.addActionListener(e -> showError("Schedule appointment feature coming soon!"));
        rescheduleButton.addActionListener(e -> showError("Reschedule appointment feature coming soon!"));
        cancelButton.addActionListener(e -> showError("Cancel appointment feature coming soon!"));

        buttonsPanel.add(scheduleButton);
        buttonsPanel.add(rescheduleButton);
        buttonsPanel.add(cancelButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPrescriptionsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Medication", "Dosage", "Duration", "Doctor"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton historyButton = new JButton("View History");
        JButton refillButton = new JButton("Request Refill");

        viewButton.addActionListener(e -> showError("View prescription details feature coming soon!"));
        historyButton.addActionListener(e -> showError("View prescription history feature coming soon!"));
        refillButton.addActionListener(e -> showError("Request refill feature coming soon!"));

        buttonsPanel.add(viewButton);
        buttonsPanel.add(historyButton);
        buttonsPanel.add(refillButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMedicalRecordsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Type", "Doctor", "Description", "Attachments"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton downloadButton = new JButton("Download Records");
        JButton shareButton = new JButton("Share Records");

        viewButton.addActionListener(e -> showError("View record details feature coming soon!"));
        downloadButton.addActionListener(e -> showError("Download records feature coming soon!"));
        shareButton.addActionListener(e -> showError("Share records feature coming soon!"));

        buttonsPanel.add(viewButton);
        buttonsPanel.add(downloadButton);
        buttonsPanel.add(shareButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDietPlansPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Plan Name", "Dietician", "Duration", "Status"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Plan");
        JButton progressButton = new JButton("Track Progress");
        JButton consultButton = new JButton("Consult Dietician");

        viewButton.addActionListener(e -> showError("View diet plan feature coming soon!"));
        progressButton.addActionListener(e -> showError("Track progress feature coming soon!"));
        consultButton.addActionListener(e -> showError("Consult dietician feature coming soon!"));

        buttonsPanel.add(viewButton);
        buttonsPanel.add(progressButton);
        buttonsPanel.add(consultButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBillsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Description", "Amount", "Status", "Due Date"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton payButton = new JButton("Make Payment");
        JButton historyButton = new JButton("Payment History");

        viewButton.addActionListener(e -> showError("View bill details feature coming soon!"));
        payButton.addActionListener(e -> showError("Make payment feature coming soon!"));
        historyButton.addActionListener(e -> showError("View payment history feature coming soon!"));

        buttonsPanel.add(viewButton);
        buttonsPanel.add(payButton);
        buttonsPanel.add(historyButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

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