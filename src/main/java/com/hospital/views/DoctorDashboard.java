package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.hospital.models.MedicalRecord;
import com.hospital.data.DataStore;

public class DoctorDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(DoctorDashboard.class);
    private JPanel menuPanel;

    public DoctorDashboard(User currentUser) {
        super(currentUser);
        setTitle("Doctor Dashboard - " + currentUser.getName());
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
        JButton appointmentsButton = createMenuButton("Appointments");
        JButton prescriptionsButton = createMenuButton("Prescriptions");
        JButton medicalRecordsButton = createMenuButton("Medical Records");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        patientsButton.addActionListener(e -> showPanel(createPatientsPanel()));
        appointmentsButton.addActionListener(e -> showPanel(createAppointmentsPanel()));
        prescriptionsButton.addActionListener(e -> showPanel(createPrescriptionsPanel()));
        medicalRecordsButton.addActionListener(e -> showPanel(createMedicalRecordsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(patientsButton);
        menuPanel.add(appointmentsButton);
        menuPanel.add(prescriptionsButton);
        menuPanel.add(medicalRecordsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show patients panel by default
        showPanel(createPatientsPanel());
    }

    private JPanel createPatientsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Patient ID", "Name", "Age", "Medical History", "Last Visit"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate patient data
        List<User> patients = DataStore.getInstance().getPatients();
        for (User patient : patients) {
            model.addRow(new Object[]{
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                "Click to view",
                "N/A"
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Patient");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit Patient");

        addButton.addActionListener(e -> showAddPatientDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) table.getValueAt(selectedRow, 0);
                showPatientDetails(patientId);
            } else {
                showError("Please select a patient first");
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) table.getValueAt(selectedRow, 0);
                showEditPatientDialog(patientId);
            } else {
                showError("Please select a patient first");
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(editButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog(this, "Add New Patient", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField emailField = new JTextField(20);

        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        dialog.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String email = emailField.getText();
                
                // Create username from name (lowercase, no spaces)
                String username = name.toLowerCase().replaceAll("\\s+", "");
                
                // Create new user with proper constructor
                User newPatient = new User(DataStore.getInstance().getNextId(), name, username, "PATIENT", currentUser.getHospitalId());
                newPatient.setAge(age);
                newPatient.setEmail(email);
                
                DataStore.getInstance().addUser(newPatient);
                dialog.dispose();
                refreshPatientsPanel();
            } catch (NumberFormatException ex) {
                showError("Please enter valid age");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showPatientDetails(int patientId) {
        User patient = DataStore.getInstance().getUserById(patientId);
        if (patient == null) {
            showError("Patient not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Patient Details", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(new JLabel(patient.getName()));
        
        detailsPanel.add(new JLabel("Age:"));
        detailsPanel.add(new JLabel(String.valueOf(patient.getAge())));
        
        detailsPanel.add(new JLabel("Email:"));
        detailsPanel.add(new JLabel(patient.getEmail()));

        // Add medical history
        JTextArea historyArea = new JTextArea(5, 20);
        historyArea.setEditable(false);
        List<MedicalRecord> records = DataStore.getInstance().getMedicalRecords(patientId);
        for (MedicalRecord record : records) {
            historyArea.append(record.getDate() + ": " + record.getDescription() + "\n");
        }

        dialog.add(detailsPanel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void refreshPatientsPanel() {
        // Remove current panel
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component != menuPanel) {
                remove(component);
            }
        }
        // Add refreshed panel
        showPanel(createPatientsPanel());
        revalidate();
        repaint();
    }

    private JPanel createAppointmentsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Time", "Patient", "Type", "Status", "Notes"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton scheduleButton = new JButton("Schedule Appointment");
        JButton viewButton = new JButton("View Details");
        JButton cancelButton = new JButton("Cancel Appointment");

        scheduleButton.addActionListener(e -> showError("Schedule appointment feature coming soon!"));
        viewButton.addActionListener(e -> showError("View appointment details feature coming soon!"));
        cancelButton.addActionListener(e -> showError("Cancel appointment feature coming soon!"));

        buttonsPanel.add(scheduleButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(cancelButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPrescriptionsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Patient", "Medication", "Dosage", "Duration"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("New Prescription");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit Prescription");

        addButton.addActionListener(e -> showError("New prescription feature coming soon!"));
        viewButton.addActionListener(e -> showError("View prescription details feature coming soon!"));
        editButton.addActionListener(e -> showError("Edit prescription feature coming soon!"));

        buttonsPanel.add(addButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(editButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMedicalRecordsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Patient", "Type", "Description", "Attachments"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Record");
        JButton viewButton = new JButton("View Details");
        JButton uploadButton = new JButton("Upload Documents");

        addButton.addActionListener(e -> showError("Add medical record feature coming soon!"));
        viewButton.addActionListener(e -> showError("View record details feature coming soon!"));
        uploadButton.addActionListener(e -> showError("Upload documents feature coming soon!"));

        buttonsPanel.add(addButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(uploadButton);

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

    private void showEditPatientDialog(int patientId) {
        User patient = DataStore.getInstance().getUserById(patientId);
        if (patient == null) {
            showError("Patient not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Patient", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(patient.getName(), 20);
        JTextField ageField = new JTextField(String.valueOf(patient.getAge()), 20);
        JTextField emailField = new JTextField(patient.getEmail(), 20);

        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        dialog.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String email = emailField.getText();

                patient.setName(name);
                patient.setAge(age);
                patient.setEmail(email);

                DataStore.getInstance().addUser(patient);
                dialog.dispose();
                refreshPatientsPanel();
            } catch (NumberFormatException ex) {
                showError("Please enter valid age");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
} 