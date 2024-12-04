package com.hospital.views;

import com.hospital.models.*;
import com.hospital.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PatientDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(PatientDashboard.class);
    private JPanel menuPanel;
    private final DataStore dataStore;

    public PatientDashboard(User currentUser) {
        super(currentUser);
        this.dataStore = DataStore.getInstance();
        setTitle("Patient Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton appointmentsButton = createMenuButton("My Appointments");
        JButton prescriptionsButton = createMenuButton("My Prescriptions");
        JButton medicalRecordsButton = createMenuButton("Medical Records");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        appointmentsButton.addActionListener(e -> showPanel(createAppointmentsPanel()));
        prescriptionsButton.addActionListener(e -> showPanel(createPrescriptionsPanel()));
        medicalRecordsButton.addActionListener(e -> showPanel(createMedicalRecordsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(appointmentsButton);
        menuPanel.add(prescriptionsButton);
        menuPanel.add(medicalRecordsButton);
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
        String[] columnNames = {"Date", "Doctor", "Type", "Status", "Room"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate appointment data
        List<Appointment> appointments = dataStore.getAppointmentsByPatientId(currentUser.getId());
        for (Appointment appointment : appointments) {
            User doctor = dataStore.getUserById(appointment.getDoctorId());
            model.addRow(new Object[]{
                appointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                doctor.getName(),
                appointment.getType(),
                appointment.getStatus(),
                appointment.getRoom()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton requestButton = new JButton("Request Appointment");
        JButton viewButton = new JButton("View Details");
        JButton cancelButton = new JButton("Cancel Appointment");

        requestButton.addActionListener(e -> showRequestAppointmentDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                showAppointmentDetails(appointments.get(selectedRow));
            } else {
                showError("Please select an appointment first");
            }
        });
        cancelButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                cancelAppointment(appointments.get(selectedRow));
            } else {
                showError("Please select an appointment first");
            }
        });

        buttonsPanel.add(requestButton);
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
        String[] columnNames = {"Date", "Doctor", "Medication", "Dosage", "Duration", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate prescription data
        List<Prescription> prescriptions = dataStore.getPrescriptionsByPatientId(currentUser.getId());
        for (Prescription prescription : prescriptions) {
            User doctor = dataStore.getUserById(prescription.getDoctorId());
            for (Prescription.Medication medication : prescription.getMedications()) {
                model.addRow(new Object[]{
                    prescription.getDate(),
                    doctor.getName(),
                    medication.getName(),
                    medication.getDosage(),
                    medication.getDuration() + " days",
                    prescription.isActive() ? "Active" : "Expired"
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton historyButton = new JButton("View History");

        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                showPrescriptionDetails(prescriptions.get(selectedRow));
            } else {
                showError("Please select a prescription first");
            }
        });
        historyButton.addActionListener(e -> showPrescriptionHistory());

        buttonsPanel.add(viewButton);
        buttonsPanel.add(historyButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMedicalRecordsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Doctor", "Type", "Description"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate medical records data
        List<MedicalRecord> records = dataStore.getMedicalRecordsByPatientId(currentUser.getId());
        for (MedicalRecord record : records) {
            User doctor = dataStore.getUserById(record.getDoctorId());
            model.addRow(new Object[]{
                record.getDate(),
                doctor.getName(),
                record.getType(),
                record.getDescription()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton downloadButton = new JButton("Download Records");

        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                showMedicalRecordDetails(records.get(selectedRow));
            } else {
                showError("Please select a record first");
            }
        });
        downloadButton.addActionListener(e -> downloadMedicalRecords());

        buttonsPanel.add(viewButton);
        buttonsPanel.add(downloadButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = createFormPanel();
        GridBagConstraints gbc = createGridBagConstraints();

        // Get patient details
        Patient patient = dataStore.getPatient(currentUser.getId());
        if (patient != null) {
            addProfileField(panel, "Name", patient.getName(), gbc);
            addProfileField(panel, "Age", String.valueOf(patient.getAge()), gbc);
            addProfileField(panel, "Gender", patient.getGender(), gbc);
            addProfileField(panel, "Blood Group", patient.getBloodGroup(), gbc);
            addProfileField(panel, "Phone", patient.getPhone(), gbc);
            addProfileField(panel, "Email", patient.getEmail(), gbc);
            addProfileField(panel, "Address", patient.getAddress(), gbc);
            addProfileField(panel, "Emergency Contact", patient.getEmergencyContact(), gbc);
            addProfileField(panel, "Allergies", patient.getAllergies(), gbc);

            // Add edit button
            gbc.gridy++;
            gbc.gridwidth = 2;
            JButton editButton = new JButton("Edit Profile");
            editButton.addActionListener(e -> showEditProfileDialog(patient));
            panel.add(editButton, gbc);
        }

        return panel;
    }

    private void showRequestAppointmentDialog() {
        JDialog dialog = new JDialog(this, "Request Appointment", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create doctor selection combo box
        List<User> doctors = dataStore.getDoctors();
        JComboBox<User> doctorCombo = new JComboBox<>(doctors.toArray(new User[0]));
        doctorCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    User doctor = (User) value;
                    setText(doctor.getName());
                }
                return this;
            }
        });

        // Add form fields
        gbc.gridx = 0;
        formPanel.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        formPanel.add(doctorCombo, gbc);

        gbc.gridy++;
        JTextField dateField = addFormField(formPanel, "Preferred Date (YYYY-MM-DD):", "", gbc);
        JTextField timeField = addFormField(formPanel, "Preferred Time (HH:mm):", "", gbc);
        JTextField typeField = addFormField(formPanel, "Appointment Type:", "", gbc);
        JTextField reasonField = addFormField(formPanel, "Reason:", "", gbc);

        // Add buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit Request");
        JButton cancelButton = new JButton("Cancel");

        submitButton.addActionListener(e -> {
            try {
                // Validate input
                if (doctorCombo.getSelectedItem() == null) {
                    showError("Please select a doctor");
                    return;
                }
                if (dateField.getText().trim().isEmpty() || timeField.getText().trim().isEmpty()) {
                    showError("Please enter date and time");
                    return;
                }
                if (typeField.getText().trim().isEmpty()) {
                    showError("Please enter appointment type");
                    return;
                }

                // Parse date and time
                LocalDateTime dateTime;
                try {
                    String dateTimeStr = dateField.getText() + " " + timeField.getText();
                    dateTime = LocalDateTime.parse(dateTimeStr, 
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                } catch (DateTimeParseException ex) {
                    showError("Invalid date or time format. Please use YYYY-MM-DD and HH:mm");
                    return;
                }

                // Create and save appointment
                User selectedDoctor = (User) doctorCombo.getSelectedItem();
                Appointment appointment = new Appointment(
                    dataStore.getNextId(),
                    currentUser.getId(),
                    selectedDoctor.getId(),
                    dateTime,
                    typeField.getText().trim(),
                    "REQUESTED",
                    reasonField.getText().trim(),
                    "New appointment request",
                    30, // Default duration
                    "TBD" // Room to be determined
                );
                dataStore.addAppointment(appointment);

                showSuccess("Appointment request submitted successfully");
                dialog.dispose();
                
                // Refresh the appointments panel
                showPanel(createAppointmentsPanel());
            } catch (Exception ex) {
                showError("Error creating appointment: " + ex.getMessage());
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(submitButton);
        buttonsPanel.add(cancelButton);

        // Add panels to dialog
        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAppointmentDetails(Appointment appointment) {
        JDialog dialog = new JDialog(this, "Appointment Details", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Create details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        User doctor = dataStore.getUserById(appointment.getDoctorId());
        addDetailField(detailsPanel, "Doctor:", doctor.getName(), gbc);
        addDetailField(detailsPanel, "Date:", appointment.getDateTime().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")), gbc);
        addDetailField(detailsPanel, "Time:", appointment.getDateTime().format(
            DateTimeFormatter.ofPattern("HH:mm")), gbc);
        addDetailField(detailsPanel, "Type:", appointment.getType(), gbc);
        addDetailField(detailsPanel, "Status:", appointment.getStatus(), gbc);
        addDetailField(detailsPanel, "Room:", appointment.getRoom(), gbc);
        addDetailField(detailsPanel, "Notes:", appointment.getNotes(), gbc);

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void cancelAppointment(Appointment appointment) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this appointment?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            appointment.setStatus("CANCELLED");
            dataStore.updateAppointment(appointment);
            showPanel(createAppointmentsPanel());
            showSuccess("Appointment cancelled successfully");
        }
    }

    private void showPrescriptionDetails(Prescription prescription) {
        JDialog dialog = new JDialog(this, "Prescription Details", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Create details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        User doctor = dataStore.getUserById(prescription.getDoctorId());
        addDetailField(detailsPanel, "Doctor:", doctor.getName(), gbc);
        addDetailField(detailsPanel, "Date:", prescription.getDate().toString(), gbc);
        addDetailField(detailsPanel, "Diagnosis:", prescription.getDiagnosis(), gbc);
        addDetailField(detailsPanel, "Instructions:", prescription.getInstructions(), gbc);
        addDetailField(detailsPanel, "Notes:", prescription.getNotes(), gbc);

        // Add medications table
        gbc.gridy++;
        gbc.gridwidth = 2;
        detailsPanel.add(new JLabel("Medications:"), gbc);
        gbc.gridy++;

        String[] columnNames = {"Medication", "Dosage", "Duration", "Instructions"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Prescription.Medication medication : prescription.getMedications()) {
            model.addRow(new Object[]{
                medication.getName(),
                medication.getDosage(),
                medication.getDuration() + " days",
                medication.getInstructions()
            });
        }

        JTable table = new JTable(model);
        table.setEnabled(false);
        detailsPanel.add(new JScrollPane(table), gbc);

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showPrescriptionHistory() {
        // TODO: Implement prescription history view
        showError("Prescription history feature coming soon!");
    }

    private void showMedicalRecordDetails(MedicalRecord record) {
        JDialog dialog = new JDialog(this, "Medical Record Details", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Create details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        User doctor = dataStore.getUserById(record.getDoctorId());
        addDetailField(detailsPanel, "Doctor:", doctor.getName(), gbc);
        addDetailField(detailsPanel, "Date:", record.getDate().toString(), gbc);
        addDetailField(detailsPanel, "Type:", record.getType(), gbc);
        addDetailField(detailsPanel, "Description:", record.getDescription(), gbc);

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void downloadMedicalRecords() {
        // TODO: Implement medical records download
        showError("Download feature coming soon!");
    }

    private void showEditProfileDialog(Patient patient) {
        JDialog dialog = new JDialog(this, "Edit Profile", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add form fields
        JTextField phoneField = addFormField(formPanel, "Phone:", patient.getPhone(), gbc);
        JTextField emailField = addFormField(formPanel, "Email:", patient.getEmail(), gbc);
        JTextField addressField = addFormField(formPanel, "Address:", patient.getAddress(), gbc);
        JTextField emergencyContactField = addFormField(formPanel, "Emergency Contact:", patient.getEmergencyContact(), gbc);

        // Add buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            patient.setPhone(phoneField.getText());
            patient.setEmail(emailField.getText());
            patient.setAddress(addressField.getText());
            patient.setEmergencyContact(emergencyContactField.getText());

            dataStore.updatePatient(patient);
            dialog.dispose();
            showPanel(createProfilePanel());
            showSuccess("Profile updated successfully");
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        // Add panels to dialog
        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JTextField addFormField(JPanel panel, String label, String value, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JTextField field = new JTextField(value, 20);
        panel.add(field, gbc);
        gbc.gridy++;
        return field;
    }

    private void addDetailField(JPanel panel, String label, String value, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value != null ? value : "N/A"), gbc);
        gbc.gridy++;
    }

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
} 