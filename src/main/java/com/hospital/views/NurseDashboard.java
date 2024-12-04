package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class NurseDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(NurseDashboard.class);
    private JPanel menuPanel;

    public NurseDashboard(User currentUser) {
        super(currentUser);
        setTitle("Nurse Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton patientsButton = createMenuButton("My Patients");
        JButton vitalsButton = createMenuButton("Patient Vitals");
        JButton medicationsButton = createMenuButton("Medications");
        JButton scheduleButton = createMenuButton("Schedule");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        patientsButton.addActionListener(e -> showPanel(createPatientsPanel()));
        vitalsButton.addActionListener(e -> showPanel(createVitalsPanel()));
        medicationsButton.addActionListener(e -> showPanel(createMedicationsPanel()));
        scheduleButton.addActionListener(e -> showPanel(createSchedulePanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(patientsButton);
        menuPanel.add(vitalsButton);
        menuPanel.add(medicationsButton);
        menuPanel.add(scheduleButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show patients panel by default
        showPanel(createPatientsPanel());
    }

    private JPanel createPatientsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Patient ID", "Name", "Room", "Condition", "Assigned Doctor"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton notesButton = new JButton("Add Notes");
        JButton alertButton = new JButton("Alert Doctor");

        viewButton.addActionListener(e -> showError("View patient details feature coming soon!"));
        notesButton.addActionListener(e -> showError("Add patient notes feature coming soon!"));
        alertButton.addActionListener(e -> showError("Alert doctor feature coming soon!"));

        buttonsPanel.add(viewButton);
        buttonsPanel.add(notesButton);
        buttonsPanel.add(alertButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createVitalsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Time", "Patient", "Temperature", "Blood Pressure", "Heart Rate", "SpO2"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton recordButton = new JButton("Record Vitals");
        JButton historyButton = new JButton("View History");
        JButton alertButton = new JButton("Alert Doctor");

        recordButton.addActionListener(e -> showError("Record vitals feature coming soon!"));
        historyButton.addActionListener(e -> showError("View vitals history feature coming soon!"));
        alertButton.addActionListener(e -> showError("Alert doctor feature coming soon!"));

        buttonsPanel.add(recordButton);
        buttonsPanel.add(historyButton);
        buttonsPanel.add(alertButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMedicationsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Time", "Patient", "Medication", "Dosage", "Status"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton administerButton = new JButton("Administer Medication");
        JButton recordButton = new JButton("Record Administration");
        JButton historyButton = new JButton("View History");

        administerButton.addActionListener(e -> showError("Administer medication feature coming soon!"));
        recordButton.addActionListener(e -> showError("Record administration feature coming soon!"));
        historyButton.addActionListener(e -> showError("View medication history feature coming soon!"));

        buttonsPanel.add(administerButton);
        buttonsPanel.add(recordButton);
        buttonsPanel.add(historyButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSchedulePanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Time", "Patient", "Task", "Priority", "Status"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Task");
        JButton completeButton = new JButton("Mark Complete");
        JButton reassignButton = new JButton("Reassign Task");

        addButton.addActionListener(e -> showError("Add task feature coming soon!"));
        completeButton.addActionListener(e -> showError("Mark task complete feature coming soon!"));
        reassignButton.addActionListener(e -> showError("Reassign task feature coming soon!"));

        buttonsPanel.add(addButton);
        buttonsPanel.add(completeButton);
        buttonsPanel.add(reassignButton);

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