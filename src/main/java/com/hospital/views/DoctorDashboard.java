package com.hospital.views;

import com.hospital.models.*;
import com.hospital.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DoctorDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(DoctorDashboard.class);
    private JPanel menuPanel;
    private final DataStore dataStore;

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

        // Create table model
        String[] columnNames = {"Patient ID", "Name", "Age", "Diagnosis", "Last Visit"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate patient data
        List<Patient> patients = dataStore.getAllPatients();
        for (Patient patient : patients) {
            model.addRow(new Object[]{
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getDiagnosis(),
                patient.getLastVisit()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
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
        JTextField nameField = addFormField(formPanel, "Name:", "", gbc);
        JTextField ageField = addFormField(formPanel, "Age:", "", gbc);
        JTextField genderField = addFormField(formPanel, "Gender:", "", gbc);
        JTextField bloodGroupField = addFormField(formPanel, "Blood Group:", "", gbc);
        JTextField phoneField = addFormField(formPanel, "Phone:", "", gbc);
        JTextField emailField = addFormField(formPanel, "Email:", "", gbc);
        JTextField addressField = addFormField(formPanel, "Address:", "", gbc);
        JTextField emergencyContactField = addFormField(formPanel, "Emergency Contact:", "", gbc);
        JTextField allergiesField = addFormField(formPanel, "Allergies:", "", gbc);
        JTextField diagnosisField = addFormField(formPanel, "Current Diagnosis:", "", gbc);

        // Add buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Create new patient
                Patient patient = new Patient(
                    dataStore.getNextId(),
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    diagnosisField.getText(),
                    LocalDate.now(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    bloodGroupField.getText(),
                    allergiesField.getText(),
                    LocalDate.now().minusYears(Integer.parseInt(ageField.getText())),
                    genderField.getText(),
                    emergencyContactField.getText()
                );

                // Save to data store
                dataStore.addPatient(patient);
                dialog.dispose();
                refreshPatientsPanel();
                showSuccess("Patient added successfully");
            } catch (NumberFormatException ex) {
                showError("Please enter a valid age");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        // Add panels to dialog
        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 700);
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
        List<MedicalRecord> records = dataStore.getMedicalRecords(patientId);
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
        String[] columnNames = {"ID", "Patient", "Date", "Type", "Status", "Notes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate appointment data
        List<Appointment> appointments = dataStore.getAppointmentsByDoctorId(currentUser.getId());
        for (Appointment appointment : appointments) {
            Patient patient = dataStore.getPatient(appointment.getPatientId());
            model.addRow(new Object[]{
                appointment.getId(),
                patient.getName(),
                appointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                appointment.getType(),
                appointment.getStatus(),
                appointment.getNotes()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton scheduleButton = new JButton("Schedule Appointment");
        JButton viewButton = new JButton("View Details");
        JButton cancelButton = new JButton("Cancel Appointment");

        scheduleButton.addActionListener(e -> showScheduleAppointmentDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int appointmentId = (int) table.getValueAt(selectedRow, 0);
                showAppointmentDetails(appointmentId);
            } else {
                showError("Please select an appointment first");
            }
        });
        cancelButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int appointmentId = (int) table.getValueAt(selectedRow, 0);
                cancelAppointment(appointmentId);
            } else {
                showError("Please select an appointment first");
            }
        });

        buttonsPanel.add(scheduleButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(cancelButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showScheduleAppointmentDialog() {
        JDialog dialog = new JDialog(this, "Schedule Appointment", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Create form panel
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

        // Add form fields
        gbc.gridx = 0;
        formPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        formPanel.add(patientCombo, gbc);

        gbc.gridy++;
        JTextField dateField = addFormField(formPanel, "Date (YYYY-MM-DD):", "", gbc);
        JTextField timeField = addFormField(formPanel, "Time (HH:mm):", "", gbc);
        JTextField typeField = addFormField(formPanel, "Type:", "", gbc);
        JTextField durationField = addFormField(formPanel, "Duration (minutes):", "30", gbc);
        JTextField roomField = addFormField(formPanel, "Room:", "", gbc);

        // Add notes area
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridy++;
        JTextArea notesArea = new JTextArea(5, 30);
        formPanel.add(new JScrollPane(notesArea), gbc);

        // Add buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                Patient selectedPatient = (Patient) patientCombo.getSelectedItem();
                if (selectedPatient == null) {
                    showError("Please select a patient");
                    return;
                }

                // Parse date and time
                LocalDateTime dateTime = LocalDateTime.parse(
                    dateField.getText() + " " + timeField.getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                );

                // Create new appointment
                Appointment appointment = new Appointment(
                    dataStore.getNextId(),
                    selectedPatient.getId(),
                    currentUser.getId(),
                    dateTime,
                    typeField.getText(),
                    "SCHEDULED",
                    notesArea.getText(),
                    "Initial consultation",
                    Integer.parseInt(durationField.getText()),
                    roomField.getText()
                );

                // Save to data store
                dataStore.addAppointment(appointment);
                dialog.dispose();
                showPanel(createAppointmentsPanel());
                showSuccess("Appointment scheduled successfully");
            } catch (Exception ex) {
                showError("Please enter valid date and time (YYYY-MM-DD HH:mm)");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        // Add panels to dialog
        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAppointmentDetails(int appointmentId) {
        Appointment appointment = dataStore.getAppointmentById(appointmentId);
        if (appointment == null) {
            showError("Appointment not found");
            return;
        }

        Patient patient = dataStore.getPatient(appointment.getPatientId());
        if (patient == null) {
            showError("Patient not found");
            return;
        }

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

        // Add appointment details
        addDetailField(detailsPanel, "Patient:", patient.getName(), gbc);
        addDetailField(detailsPanel, "Date:", appointment.getDateTime().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")), gbc);
        addDetailField(detailsPanel, "Time:", appointment.getDateTime().format(
            DateTimeFormatter.ofPattern("HH:mm")), gbc);
        addDetailField(detailsPanel, "Type:", appointment.getType(), gbc);
        addDetailField(detailsPanel, "Status:", appointment.getStatus(), gbc);
        addDetailField(detailsPanel, "Duration:", appointment.getDuration() + " minutes", gbc);
        addDetailField(detailsPanel, "Room:", appointment.getRoom(), gbc);

        // Add notes section
        gbc.gridy++;
        gbc.gridwidth = 2;
        detailsPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridy++;
        JTextArea notesArea = new JTextArea(appointment.getNotes(), 5, 30);
        notesArea.setEditable(false);
        detailsPanel.add(new JScrollPane(notesArea), gbc);

        // Add buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton closeButton = new JButton("Close");

        editButton.addActionListener(e -> {
            dialog.dispose();
            showEditAppointmentDialog(appointmentId);
        });
        closeButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(editButton);
        buttonsPanel.add(closeButton);

        // Add panels to dialog
        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditAppointmentDialog(int appointmentId) {
        Appointment appointment = dataStore.getAppointmentById(appointmentId);
        if (appointment == null) {
            showError("Appointment not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Appointment", true);
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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        JTextField dateField = addFormField(formPanel, "Date (YYYY-MM-DD):",
            appointment.getDateTime().format(dateFormatter), gbc);
        JTextField timeField = addFormField(formPanel, "Time (HH:mm):",
            appointment.getDateTime().format(timeFormatter), gbc);
        JTextField typeField = addFormField(formPanel, "Type:", appointment.getType(), gbc);
        JTextField durationField = addFormField(formPanel, "Duration (minutes):",
            String.valueOf(appointment.getDuration()), gbc);
        JTextField roomField = addFormField(formPanel, "Room:", appointment.getRoom(), gbc);

        // Add status combo box
        String[] statuses = {"SCHEDULED", "COMPLETED", "CANCELLED", "NO_SHOW"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        statusCombo.setSelectedItem(appointment.getStatus());
        gbc.gridx = 0;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        gbc.gridy++;

        // Add notes area
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridy++;
        JTextArea notesArea = new JTextArea(appointment.getNotes(), 5, 30);
        formPanel.add(new JScrollPane(notesArea), gbc);

        // Add buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Parse date and time
                LocalDateTime dateTime = LocalDateTime.parse(
                    dateField.getText() + " " + timeField.getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                );

                // Update appointment
                appointment.setDateTime(dateTime);
                appointment.setType(typeField.getText());
                appointment.setDuration(Integer.parseInt(durationField.getText()));
                appointment.setRoom(roomField.getText());
                appointment.setStatus((String) statusCombo.getSelectedItem());
                appointment.setNotes(notesArea.getText());

                // Save to data store
                dataStore.updateAppointment(appointment);
                dialog.dispose();
                showPanel(createAppointmentsPanel());
                showSuccess("Appointment updated successfully");
            } catch (Exception ex) {
                showError("Please enter valid date and time (YYYY-MM-DD HH:mm)");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        // Add panels to dialog
        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void cancelAppointment(int appointmentId) {
        Appointment appointment = dataStore.getAppointmentById(appointmentId);
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
            showPanel(createAppointmentsPanel());
            showSuccess("Appointment cancelled successfully");
        }
    }

    private JPanel createPrescriptionsPanel() {
        // TODO: Implement prescriptions panel
        JPanel panel = createFormPanel();
        showError("Prescriptions feature coming soon!");
        return panel;
    }

    private JPanel createMedicalRecordsPanel() {
        // TODO: Implement medical records panel
        JPanel panel = createFormPanel();
        showError("Medical records feature coming soon!");
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

    private void showEditPatientDialog(int patientId) {
        Patient patient = dataStore.getPatient(patientId);
        if (patient == null) {
            showError("Patient not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Patient - " + patient.getName(), true);
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
        JTextField nameField = addFormField(formPanel, "Name:", patient.getName(), gbc);
        JTextField ageField = addFormField(formPanel, "Age:", String.valueOf(patient.getAge()), gbc);
        JTextField genderField = addFormField(formPanel, "Gender:", patient.getGender(), gbc);
        JTextField bloodGroupField = addFormField(formPanel, "Blood Group:", patient.getBloodGroup(), gbc);
        JTextField phoneField = addFormField(formPanel, "Phone:", patient.getPhone(), gbc);
        JTextField emailField = addFormField(formPanel, "Email:", patient.getEmail(), gbc);
        JTextField addressField = addFormField(formPanel, "Address:", patient.getAddress(), gbc);
        JTextField emergencyContactField = addFormField(formPanel, "Emergency Contact:", patient.getEmergencyContact(), gbc);
        JTextField allergiesField = addFormField(formPanel, "Allergies:", patient.getAllergies(), gbc);
        JTextField diagnosisField = addFormField(formPanel, "Current Diagnosis:", patient.getDiagnosis(), gbc);

        // Add medications section
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Current Medications (one per line):"), gbc);
        gbc.gridy++;
        JTextArea medicationsArea = new JTextArea(5, 30);
        for (String medication : patient.getCurrentMedications()) {
            medicationsArea.append(medication + "\n");
        }
        formPanel.add(new JScrollPane(medicationsArea), gbc);

        // Add buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Update patient information
                patient.setName(nameField.getText());
                patient.setAge(Integer.parseInt(ageField.getText()));
                patient.setGender(genderField.getText());
                patient.setBloodGroup(bloodGroupField.getText());
                patient.setPhone(phoneField.getText());
                patient.setEmail(emailField.getText());
                patient.setAddress(addressField.getText());
                patient.setEmergencyContact(emergencyContactField.getText());
                patient.setAllergies(allergiesField.getText());
                patient.setDiagnosis(diagnosisField.getText());

                // Update medications
                List<String> medications = Arrays.asList(medicationsArea.getText().split("\n"));
                patient.setCurrentMedications(medications);

                // Save to data store
                dataStore.updatePatient(patient);
                dialog.dispose();
                refreshPatientsPanel();
                showSuccess("Patient information updated successfully");
            } catch (NumberFormatException ex) {
                showError("Please enter a valid age");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        // Add panels to dialog
        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
} 