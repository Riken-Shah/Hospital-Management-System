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

public class DoctorDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(DoctorDashboard.class);
    private final DataStore dataStore;
    private DefaultTableModel patientTableModel;
    private DefaultTableModel appointmentTableModel;
    private JTable patientTable;
    private JTable appointmentTable;

    public DoctorDashboard(User currentUser) {
        super(currentUser);
        this.dataStore = DataStore.getInstance();
        setTitle("Doctor Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    @Override
    protected void initializeMenu() {
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton patientsButton = createMenuButton("Patients");
        JButton appointmentsButton = createMenuButton("Appointments");
        JButton prescriptionsButton = createMenuButton("Prescriptions");
        JButton medicalRecordsButton = createMenuButton("Medical Records");
        JButton logoutButton = createMenuButton("Logout");

        patientsButton.addActionListener(e -> showPanel(createPatientsPanel()));
        appointmentsButton.addActionListener(e -> showPanel(createAppointmentsPanel()));
        prescriptionsButton.addActionListener(e -> showPanel(createPrescriptionsPanel()));
        medicalRecordsButton.addActionListener(e -> showPanel(createMedicalRecordsPanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(patientsButton);
        menuPanel.add(appointmentsButton);
        menuPanel.add(prescriptionsButton);
        menuPanel.add(medicalRecordsButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        showPanel(createPatientsPanel());
    }

    private JPanel createPatientsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"ID", "Name", "Age", "Gender", "Phone", "Last Visit"};
        patientTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate patient data
        refreshPatientTable();

        patientTable = new JTable(patientTableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Patient");
        JButton viewButton = new JButton("View Details");
        JButton historyButton = new JButton("View History");

        addButton.addActionListener(e -> showAddPatientDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) patientTableModel.getValueAt(selectedRow, 0);
                showPatientDetails(patientId);
            } else {
                showError("Please select a patient first");
            }
        });
        historyButton.addActionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) patientTableModel.getValueAt(selectedRow, 0);
                showPatientHistory(patientId);
            } else {
                showError("Please select a patient first");
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(historyButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshPatientTable() {
        patientTableModel.setRowCount(0);
        List<Patient> patients = dataStore.getAllPatients();
        for (Patient patient : patients) {
            patientTableModel.addRow(new Object[]{
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getPhone(),
                patient.getLastVisit()
            });
        }
    }

    private JPanel createAppointmentsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"ID", "Patient", "Date", "Time", "Type", "Status"};
        appointmentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate appointment data
        refreshAppointmentTable();

        appointmentTable = new JTable(appointmentTableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton scheduleButton = new JButton("Schedule Appointment");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit");
        JButton cancelButton = new JButton("Cancel");

        scheduleButton.addActionListener(e -> showScheduleAppointmentDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int appointmentId = (int) appointmentTableModel.getValueAt(selectedRow, 0);
                showAppointmentDetails(appointmentId);
            } else {
                showError("Please select an appointment first");
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int appointmentId = (int) appointmentTableModel.getValueAt(selectedRow, 0);
                showEditAppointmentDialog(appointmentId);
            } else {
                showError("Please select an appointment first");
            }
        });
        cancelButton.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int appointmentId = (int) appointmentTableModel.getValueAt(selectedRow, 0);
                cancelAppointment(appointmentId);
            } else {
                showError("Please select an appointment first");
            }
        });

        buttonsPanel.add(scheduleButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(cancelButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshAppointmentTable() {
        appointmentTableModel.setRowCount(0);
        List<Appointment> appointments = dataStore.getAppointmentsByDoctorId(currentUser.getId());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Appointment appointment : appointments) {
            Patient patient = dataStore.getPatient(appointment.getPatientId());
            appointmentTableModel.addRow(new Object[]{
                appointment.getId(),
                patient.getName(),
                appointment.getDateTime().format(dateFormatter),
                appointment.getDateTime().format(timeFormatter),
                appointment.getType(),
                appointment.getStatus()
            });
        }
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog(this, "Add New Patient", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = addFormField(formPanel, "Name:", "", gbc);
        JTextField ageField = addFormField(formPanel, "Age:", "", gbc);
        JTextField genderField = addFormField(formPanel, "Gender:", "", gbc);
        JTextField phoneField = addFormField(formPanel, "Phone:", "", gbc);
        JTextField emailField = addFormField(formPanel, "Email:", "", gbc);
        JTextField addressField = addFormField(formPanel, "Address:", "", gbc);
        JTextField bloodGroupField = addFormField(formPanel, "Blood Group:", "", gbc);
        JTextField allergiesField = addFormField(formPanel, "Allergies:", "", gbc);
        JTextField diagnosisField = addFormField(formPanel, "Initial Diagnosis:", "", gbc);
        JTextField emergencyContactField = addFormField(formPanel, "Emergency Contact:", "", gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (nameField.getText().trim().isEmpty() || ageField.getText().trim().isEmpty()) {
                    showError("Name and age are required");
                    return;
                }

                // Create new patient
                Patient patient = new Patient(
                    dataStore.getNextId(),
                    nameField.getText().trim(),
                    Integer.parseInt(ageField.getText().trim()),
                    diagnosisField.getText().trim(),
                    LocalDateTime.now().toLocalDate(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    addressField.getText().trim(),
                    bloodGroupField.getText().trim(),
                    allergiesField.getText().trim(),
                    LocalDateTime.now().toLocalDate(),
                    genderField.getText().trim(),
                    emergencyContactField.getText().trim()
                );

                dataStore.addPatient(patient);
                showSuccess("Patient added successfully");
                dialog.dispose();
                refreshPatientTable();
            } catch (NumberFormatException ex) {
                showError("Invalid age format");
            } catch (Exception ex) {
                showError("Error adding patient: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showPatientDetails(int patientId) {
        Patient patient = dataStore.getPatient(patientId);
        if (patient == null) {
            showError("Patient not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Patient Details", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addDetailField(detailsPanel, "Name", patient.getName(), gbc);
        addDetailField(detailsPanel, "Age", String.valueOf(patient.getAge()), gbc);
        addDetailField(detailsPanel, "Gender", patient.getGender(), gbc);
        addDetailField(detailsPanel, "Phone", patient.getPhone(), gbc);
        addDetailField(detailsPanel, "Email", patient.getEmail(), gbc);
        addDetailField(detailsPanel, "Address", patient.getAddress(), gbc);
        addDetailField(detailsPanel, "Blood Group", patient.getBloodGroup(), gbc);
        addDetailField(detailsPanel, "Allergies", patient.getAllergies(), gbc);
        addDetailField(detailsPanel, "Emergency Contact", patient.getEmergencyContact(), gbc);
        addDetailField(detailsPanel, "Last Visit", patient.getLastVisit().toString(), gbc);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showPatientHistory(int patientId) {
        JDialog dialog = new JDialog(this, "Patient History", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel historyPanel = new JPanel(new GridBagLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add medical records
        gbc.gridwidth = 2;
        historyPanel.add(new JLabel("Medical Records:"), gbc);
        gbc.gridy++;

        JTextArea historyArea = new JTextArea(5, 20);
        historyArea.setEditable(false);
        List<MedicalRecord> records = dataStore.getMedicalRecords(patientId);
        for (MedicalRecord record : records) {
            historyArea.append(record.getDate() + ": " + record.getDescription() + "\n");
        }

        gbc.gridwidth = 2;
        historyPanel.add(new JScrollPane(historyArea), gbc);

        // Add prescriptions
        gbc.gridy++;
        historyPanel.add(new JLabel("Prescriptions:"), gbc);
        gbc.gridy++;

        JTextArea prescriptionsArea = new JTextArea(5, 20);
        prescriptionsArea.setEditable(false);
        List<Prescription> prescriptions = dataStore.getPrescriptionsByPatientId(patientId);
        for (Prescription prescription : prescriptions) {
            prescriptionsArea.append(prescription.getDate() + ": " + prescription.getDiagnosis() + "\n");
            for (Prescription.Medication med : prescription.getMedications()) {
                prescriptionsArea.append("  - " + med.getName() + ": " + med.getDosage() + "\n");
            }
        }

        gbc.gridwidth = 2;
        historyPanel.add(new JScrollPane(prescriptionsArea), gbc);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        dialog.add(new JScrollPane(historyPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showScheduleAppointmentDialog() {
        JDialog dialog = new JDialog(this, "Schedule Appointment", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create patient selection combo box
        List<Patient> patients = dataStore.getAllPatients();
        JComboBox<Patient> patientCombo = new JComboBox<>(patients.toArray(new Patient[0]));
        patientCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Patient) {
                    Patient patient = (Patient) value;
                    setText(patient.getName());
                }
                return this;
            }
        });

        gbc.gridx = 0;
        formPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        formPanel.add(patientCombo, gbc);

        gbc.gridy++;
        JTextField dateField = addFormField(formPanel, "Date (YYYY-MM-DD):", "", gbc);
        JTextField timeField = addFormField(formPanel, "Time (HH:mm):", "", gbc);
        JTextField typeField = addFormField(formPanel, "Type:", "", gbc);
        JTextField roomField = addFormField(formPanel, "Room:", "", gbc);
        JTextField notesField = addFormField(formPanel, "Notes:", "", gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (patientCombo.getSelectedItem() == null) {
                    showError("Please select a patient");
                    return;
                }
                if (dateField.getText().trim().isEmpty() || timeField.getText().trim().isEmpty()) {
                    showError("Please enter date and time");
                    return;
                }

                // Parse date and time
                String dateTimeStr = dateField.getText() + " " + timeField.getText();
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                Patient selectedPatient = (Patient) patientCombo.getSelectedItem();

                // Create new appointment
                Appointment appointment = new Appointment(
                    dataStore.getNextId(),
                    selectedPatient.getId(),
                    currentUser.getId(),
                    dateTime,
                    typeField.getText().trim(),
                    "SCHEDULED",
                    notesField.getText().trim(),
                    "Scheduled by doctor",
                    30, // Default duration
                    roomField.getText().trim()
                );

                dataStore.addAppointment(appointment);
                showSuccess("Appointment scheduled successfully");
                dialog.dispose();
                refreshAppointmentTable();
            } catch (DateTimeParseException ex) {
                showError("Invalid date or time format. Please use YYYY-MM-DD and HH:mm");
            } catch (Exception ex) {
                showError("Error scheduling appointment: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAppointmentDetails(int appointmentId) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            showError("Appointment not found");
            return;
        }

        Patient patient = dataStore.getPatient(appointment.getPatientId());

        JDialog dialog = new JDialog(this, "Appointment Details", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        addDetailField(detailsPanel, "Patient", patient.getName(), gbc);
        addDetailField(detailsPanel, "Date/Time", appointment.getDateTime().format(formatter), gbc);
        addDetailField(detailsPanel, "Type", appointment.getType(), gbc);
        addDetailField(detailsPanel, "Status", appointment.getStatus(), gbc);
        addDetailField(detailsPanel, "Room", appointment.getRoom(), gbc);
        addDetailField(detailsPanel, "Notes", appointment.getNotes(), gbc);

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

    private void showEditAppointmentDialog(int appointmentId) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            showError("Appointment not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Appointment", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        JTextField dateField = addFormField(formPanel, "Date (YYYY-MM-DD):", 
            appointment.getDateTime().format(dateFormatter), gbc);
        JTextField timeField = addFormField(formPanel, "Time (HH:mm):", 
            appointment.getDateTime().format(timeFormatter), gbc);
        JTextField typeField = addFormField(formPanel, "Type:", appointment.getType(), gbc);
        JTextField roomField = addFormField(formPanel, "Room:", appointment.getRoom(), gbc);
        JTextField notesField = addFormField(formPanel, "Notes:", appointment.getNotes(), gbc);

        String[] statuses = {"SCHEDULED", "COMPLETED", "CANCELLED", "NO_SHOW"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        statusCombo.setSelectedItem(appointment.getStatus());
        gbc.gridx = 0;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Parse date and time
                String dateTimeStr = dateField.getText() + " " + timeField.getText();
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                // Update appointment
                appointment.setDateTime(dateTime);
                appointment.setType(typeField.getText().trim());
                appointment.setRoom(roomField.getText().trim());
                appointment.setNotes(notesField.getText().trim());
                appointment.setStatus((String) statusCombo.getSelectedItem());

                dataStore.updateAppointment(appointment);
                showSuccess("Appointment updated successfully");
                dialog.dispose();
                refreshAppointmentTable();
            } catch (DateTimeParseException ex) {
                showError("Invalid date or time format. Please use YYYY-MM-DD and HH:mm");
            } catch (Exception ex) {
                showError("Error updating appointment: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void cancelAppointment(int appointmentId) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            showError("Appointment not found");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this appointment?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            appointment.setStatus("CANCELLED");
            dataStore.updateAppointment(appointment);
            refreshAppointmentTable();
            showSuccess("Appointment cancelled successfully");
        }
    }

    private JPanel createPrescriptionsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"ID", "Patient", "Date", "Diagnosis", "Status"};
        DefaultTableModel prescriptionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate prescription data
        List<Prescription> prescriptions = dataStore.getPrescriptionsByDoctorId(currentUser.getId());
        for (Prescription prescription : prescriptions) {
            Patient patient = dataStore.getPatient(prescription.getPatientId());
            prescriptionTableModel.addRow(new Object[]{
                prescription.getId(),
                patient.getName(),
                prescription.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                prescription.getDiagnosis(),
                prescription.isActive() ? "Active" : "Expired"
            });
        }

        JTable prescriptionTable = new JTable(prescriptionTableModel);
        JScrollPane scrollPane = new JScrollPane(prescriptionTable);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Prescription");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit");
        JButton deactivateButton = new JButton("Deactivate");

        addButton.addActionListener(e -> showAddPrescriptionDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = prescriptionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int prescriptionId = (int) prescriptionTableModel.getValueAt(selectedRow, 0);
                showPrescriptionDetails(prescriptionId);
            } else {
                showError("Please select a prescription first");
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = prescriptionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int prescriptionId = (int) prescriptionTableModel.getValueAt(selectedRow, 0);
                showEditPrescriptionDialog(prescriptionId);
            } else {
                showError("Please select a prescription first");
            }
        });
        deactivateButton.addActionListener(e -> {
            int selectedRow = prescriptionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int prescriptionId = (int) prescriptionTableModel.getValueAt(selectedRow, 0);
                deactivatePrescription(prescriptionId);
            } else {
                showError("Please select a prescription first");
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deactivateButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showAddPrescriptionDialog() {
        JDialog dialog = new JDialog(this, "Add New Prescription", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create patient selection combo box
        List<Patient> patients = dataStore.getAllPatients();
        JComboBox<Patient> patientCombo = new JComboBox<>(patients.toArray(new Patient[0]));
        patientCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Patient) {
                    Patient patient = (Patient) value;
                    setText(patient.getName());
                }
                return this;
            }
        });

        gbc.gridx = 0;
        formPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        formPanel.add(patientCombo, gbc);

        gbc.gridy++;
        JTextField diagnosisField = addFormField(formPanel, "Diagnosis:", "", gbc);
        JTextField instructionsField = addFormField(formPanel, "Instructions:", "", gbc);
        JTextArea notesArea = new JTextArea(4, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(notesArea), gbc);

        // Create medications panel
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Medications:"), gbc);

        DefaultTableModel medicationModel = new DefaultTableModel(
            new String[]{"Name", "Dosage", "Duration (days)", "Instructions"}, 0);
        JTable medicationTable = new JTable(medicationModel);
        gbc.gridy++;
        formPanel.add(new JScrollPane(medicationTable), gbc);

        // Add medication button
        JButton addMedButton = new JButton("Add Medication");
        addMedButton.addActionListener(e -> {
            medicationModel.addRow(new Object[]{"", "", "", ""});
        });
        gbc.gridy++;
        formPanel.add(addMedButton, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (patientCombo.getSelectedItem() == null) {
                    showError("Please select a patient");
                    return;
                }
                if (diagnosisField.getText().trim().isEmpty()) {
                    showError("Please enter a diagnosis");
                    return;
                }
                if (medicationModel.getRowCount() == 0) {
                    showError("Please add at least one medication");
                    return;
                }

                Patient selectedPatient = (Patient) patientCombo.getSelectedItem();

                // Create prescription
                Prescription prescription = new Prescription(
                    dataStore.getNextId(),
                    selectedPatient.getId(),
                    currentUser.getId(),
                    diagnosisField.getText().trim(),
                    instructionsField.getText().trim(),
                    notesArea.getText().trim()
                );

                // Add medications
                for (int i = 0; i < medicationModel.getRowCount(); i++) {
                    String name = (String) medicationModel.getValueAt(i, 0);
                    String dosage = (String) medicationModel.getValueAt(i, 1);
                    String durationStr = (String) medicationModel.getValueAt(i, 2);
                    String instructions = (String) medicationModel.getValueAt(i, 3);

                    if (name.trim().isEmpty() || dosage.trim().isEmpty() || 
                        durationStr.trim().isEmpty()) {
                        showError("Please fill in all medication fields");
                        return;
                    }

                    try {
                        int duration = Integer.parseInt(durationStr.trim());
                        prescription.addMedication(new Prescription.Medication(
                            name.trim(), dosage.trim(), duration, instructions.trim()));
                    } catch (NumberFormatException ex) {
                        showError("Invalid duration for medication: " + name);
                        return;
                    }
                }

                dataStore.addPrescription(prescription);
                showSuccess("Prescription added successfully");
                dialog.dispose();
                showPanel(createPrescriptionsPanel());
            } catch (Exception ex) {
                showError("Error adding prescription: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showPrescriptionDetails(int prescriptionId) {
        Prescription prescription = dataStore.getPrescription(prescriptionId);
        if (prescription == null) {
            showError("Prescription not found");
            return;
        }

        Patient patient = dataStore.getPatient(prescription.getPatientId());

        JDialog dialog = new JDialog(this, "Prescription Details", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addDetailField(detailsPanel, "Patient", patient.getName(), gbc);
        addDetailField(detailsPanel, "Date", prescription.getDate().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")), gbc);
        addDetailField(detailsPanel, "Diagnosis", prescription.getDiagnosis(), gbc);
        addDetailField(detailsPanel, "Instructions", prescription.getInstructions(), gbc);
        addDetailField(detailsPanel, "Status", prescription.isActive() ? "Active" : "Expired", gbc);
        addDetailField(detailsPanel, "Notes", prescription.getNotes(), gbc);

        // Add medications table
        gbc.gridy++;
        gbc.gridwidth = 2;
        detailsPanel.add(new JLabel("Medications:"), gbc);
        gbc.gridy++;

        String[] columnNames = {"Name", "Dosage", "Duration", "Instructions"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Prescription.Medication med : prescription.getMedications()) {
            model.addRow(new Object[]{
                med.getName(),
                med.getDosage(),
                med.getDuration() + " days",
                med.getInstructions()
            });
        }

        JTable table = new JTable(model);
        table.setEnabled(false);
        detailsPanel.add(new JScrollPane(table), gbc);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditPrescriptionDialog(int prescriptionId) {
        // TODO: Implement edit prescription dialog
        showError("Edit prescription feature coming soon!");
    }

    private void deactivatePrescription(int prescriptionId) {
        Prescription prescription = dataStore.getPrescription(prescriptionId);
        if (prescription == null) {
            showError("Prescription not found");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to deactivate this prescription?",
            "Confirm Deactivation",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            prescription.setActive(false);
            dataStore.updatePrescription(prescription);
            showPanel(createPrescriptionsPanel());
            showSuccess("Prescription deactivated successfully");
        }
    }

    private JPanel createMedicalRecordsPanel() {
        // TODO: Implement medical records panel
        JPanel panel = createFormPanel();
        panel.add(new JLabel("Medical records panel coming soon!"));
        return panel;
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
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value != null ? value : "N/A"), gbc);
        gbc.gridy++;
    }

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
} 