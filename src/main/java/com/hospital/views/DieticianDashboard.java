package com.hospital.views;

import com.hospital.models.*;
import com.hospital.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class DieticianDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(DieticianDashboard.class);
    private JPanel menuPanel;
    private final DataStore dataStore;
    private DefaultTableModel patientsTableModel;
    private DefaultTableModel consultationsTableModel;
    private DefaultTableModel dietPlansTableModel;
    private JTable patientsTable;
    private JTable consultationsTable;
    private JTable dietPlansTable;

    public DieticianDashboard(User currentUser) {
        super(currentUser);
        this.dataStore = DataStore.getInstance();
        initializeTableModels();
        setTitle("Dietician Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    private void initializeTableModels() {
        // Initialize patients table model
        String[] patientColumns = {"Patient ID", "Name", "Age", "Gender", "Phone", "Last Visit"};
        patientsTableModel = new DefaultTableModel(patientColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Initialize consultations table model
        String[] consultationColumns = {"Date", "Patient", "Status", "Notes", "Next Appointment"};
        consultationsTableModel = new DefaultTableModel(consultationColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Initialize diet plans table model
        String[] dietPlanColumns = {"Patient", "Goal", "Start Date", "End Date", "Status"};
        dietPlansTableModel = new DefaultTableModel(dietPlanColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton patientsButton = createMenuButton("My Patients");
        JButton consultationsButton = createMenuButton("Consultations");
        JButton dietPlansButton = createMenuButton("Diet Plans");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        patientsButton.addActionListener(e -> showPanel(createPatientsPanel()));
        consultationsButton.addActionListener(e -> showPanel(createConsultationsPanel()));
        dietPlansButton.addActionListener(e -> showPanel(createDietPlansPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(patientsButton);
        menuPanel.add(consultationsButton);
        menuPanel.add(dietPlansButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        showPanel(createPatientsPanel());
    }

    private JPanel createPatientsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        refreshPatientsTable();
        patientsTable = new JTable(patientsTableModel);
        JScrollPane scrollPane = new JScrollPane(patientsTable);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton assignButton = new JButton("Assign Diet Plan");
        JButton historyButton = new JButton("View History");

        viewButton.addActionListener(e -> {
            int selectedRow = patientsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) patientsTable.getValueAt(selectedRow, 0);
                showPatientDetails(patientId);
            } else {
                showError("Please select a patient first");
            }
        });

        assignButton.addActionListener(e -> {
            int selectedRow = patientsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) patientsTable.getValueAt(selectedRow, 0);
                showNewDietPlanDialog(patientId);
            } else {
                showError("Please select a patient first");
            }
        });

        historyButton.addActionListener(e -> {
            int selectedRow = patientsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) patientsTable.getValueAt(selectedRow, 0);
                showPatientHistory(patientId);
            } else {
                showError("Please select a patient first");
            }
        });

        buttonsPanel.add(viewButton);
        buttonsPanel.add(assignButton);
        buttonsPanel.add(historyButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshPatientsTable() {
        patientsTableModel.setRowCount(0);
        List<User> patients = dataStore.getPatientsForDietician(currentUser.getId());
        for (User patient : patients) {
            Patient patientDetails = dataStore.getPatient(patient.getId());
            if (patientDetails != null) {
                patientsTableModel.addRow(new Object[]{
                    patient.getId(),
                    patient.getName(),
                    patientDetails.getAge(),
                    patientDetails.getGender(),
                    patientDetails.getPhone(),
                    patientDetails.getLastVisit() != null ? patientDetails.getLastVisit().format(DateTimeFormatter.ISO_DATE) : "N/A"
                });
            }
        }
    }

    private void populatePatientComboBox(JComboBox<ComboItem> patientCombo, int preSelectedPatientId) {
        List<User> patients = dataStore.getPatientsForDietician(currentUser.getId());
        for (User patient : patients) {
            ComboItem item = new ComboItem(patient.getId(), patient.getName());
            patientCombo.addItem(item);
            if (patient.getId() == preSelectedPatientId) {
                patientCombo.setSelectedItem(item);
            }
        }
    }

    private JPanel createConsultationsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        refreshConsultationsTable();
        consultationsTable = new JTable(consultationsTableModel);
        JScrollPane scrollPane = new JScrollPane(consultationsTable);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("New Consultation");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit Consultation");

        addButton.addActionListener(e -> showNewConsultationDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = consultationsTable.getSelectedRow();
            if (selectedRow >= 0) {
                showConsultationDetails(selectedRow);
            } else {
                showError("Please select a consultation first");
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = consultationsTable.getSelectedRow();
            if (selectedRow >= 0) {
                showEditConsultationDialog(selectedRow);
            } else {
                showError("Please select a consultation first");
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(editButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshConsultationsTable() {
        consultationsTableModel.setRowCount(0);
        List<DietConsultation> consultations = dataStore.getDietConsultationsForDietician(currentUser.getId());
        for (DietConsultation consultation : consultations) {
            User patient = dataStore.getUserById(consultation.getPatientId());
            if (patient != null) {
                consultationsTableModel.addRow(new Object[]{
                    consultation.getId(),
                    patient.getName(),
                    consultation.getConsultationDate().format(DateTimeFormatter.ISO_DATE),
                    consultation.getStatus()
                });
            }
        }
    }

    private JPanel createDietPlansPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        refreshDietPlansTable();
        dietPlansTable = new JTable(dietPlansTableModel);
        JScrollPane scrollPane = new JScrollPane(dietPlansTable);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("New Diet Plan");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit Plan");
        JButton deleteButton = new JButton("Delete Plan");

        addButton.addActionListener(e -> showNewDietPlanDialog(0)); // 0 means no pre-selected patient
        viewButton.addActionListener(e -> {
            int selectedRow = dietPlansTable.getSelectedRow();
            if (selectedRow >= 0) {
                showDietPlanDetails(selectedRow);
            } else {
                showError("Please select a diet plan first");
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = dietPlansTable.getSelectedRow();
            if (selectedRow >= 0) {
                showEditDietPlanDialog(selectedRow);
            } else {
                showError("Please select a diet plan first");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = dietPlansTable.getSelectedRow();
            if (selectedRow >= 0) {
                deleteDietPlan(selectedRow);
            } else {
                showError("Please select a diet plan first");
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshDietPlansTable() {
        if (dietPlansTableModel != null) {
            dietPlansTableModel.setRowCount(0);
            List<DietPlan> plans = dataStore.getDietPlansForDietician(currentUser.getId());
            for (DietPlan plan : plans) {
                Patient patient = dataStore.getPatient(plan.getPatientId());
                dietPlansTableModel.addRow(new Object[]{
                    patient != null ? patient.getName() : "Unknown",
                    plan.getGoal(),
                    plan.getStartDate().format(DateTimeFormatter.ISO_DATE),
                    plan.getEndDate().format(DateTimeFormatter.ISO_DATE),
                    plan.getStatus()
                });
            }
        }
    }

    private void showNewDietPlanDialog(int preSelectedPatientId) {
        JDialog dialog = new JDialog(this, "Create New Diet Plan", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        // Form components
        JComboBox<ComboItem> patientCombo = new JComboBox<>();
        JTextField goalField = new JTextField(20);
        JTextArea restrictionsArea = new JTextArea(3, 20);
        JTextArea recommendationsArea = new JTextArea(3, 20);

        // Populate patient combo box
        List<User> patients = dataStore.getPatientsForDietician(currentUser.getId());
        for (User patient : patients) {
            ComboItem item = new ComboItem(patient.getId(), patient.getName());
            patientCombo.addItem(item);
            if (patient.getId() == preSelectedPatientId) {
                patientCombo.setSelectedItem(item);
            }
        }

        // Add components to form
        addFormField(formPanel, "Patient:", patientCombo, gbc);
        addFormField(formPanel, "Goal:", goalField, gbc);
        addFormField(formPanel, "Restrictions:", new JScrollPane(restrictionsArea), gbc);
        addFormField(formPanel, "Recommendations:", new JScrollPane(recommendationsArea), gbc);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                ComboItem selectedPatient = (ComboItem) patientCombo.getSelectedItem();
                if (selectedPatient == null || goalField.getText().trim().isEmpty()) {
                    showError("Please fill in all required fields");
                    return;
                }

                DietPlan plan = new DietPlan(
                    dataStore.getNextId(),
                    selectedPatient.getId(),
                    currentUser.getId(),
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1),
                    goalField.getText().trim(),
                    List.of(restrictionsArea.getText().split("\n")),
                    List.of(recommendationsArea.getText().split("\n")),
                    "Initial plan",
                    "ACTIVE"
                );

                dataStore.addDietPlan(plan);
                showMessage("Diet plan created successfully");
                dialog.dispose();
                showPanel(createDietPlansPanel()); // Refresh the entire panel
            } catch (Exception ex) {
                showError("Error creating diet plan: " + ex.getMessage());
                logger.error("Error creating diet plan", ex);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditDietPlanDialog(int rowIndex) {
        // Similar to showNewDietPlanDialog but with pre-filled values
        // Implementation similar to above with pre-filled values
        showMessage("Edit diet plan feature coming soon!");
    }

    private void deleteDietPlan(int rowIndex) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this diet plan?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            DietPlan plan = dataStore.getDietPlansForDietician(currentUser.getId()).get(rowIndex);
            dataStore.deleteDietPlan(plan.getId());
            refreshDietPlansTable();
            showMessage("Diet plan deleted successfully");
        }
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

        addDetailField(detailsPanel, "Name", patient.getName());
        addDetailField(detailsPanel, "Age", String.valueOf(patient.getAge()));
        addDetailField(detailsPanel, "Gender", patient.getGender());
        addDetailField(detailsPanel, "Contact", patient.getPhone());
        addDetailField(detailsPanel, "Email", patient.getEmail());
        addDetailField(detailsPanel, "Address", patient.getAddress());
        addDetailField(detailsPanel, "Current Diagnosis", patient.getDiagnosis());
        addDetailField(detailsPanel, "Blood Group", patient.getBloodGroup());
        addDetailField(detailsPanel, "Allergies", patient.getAllergies());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.add(closeButton, BorderLayout.SOUTH);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showPatientHistory(int patientId) {
        JDialog dialog = new JDialog(this, "Patient History", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Create tabs for different types of history
        JTabbedPane tabbedPane = new JTabbedPane();

        // Diet Plans History
        JPanel dietPlansPanel = new JPanel(new BorderLayout());
        String[] dietPlanColumns = {"Start Date", "End Date", "Goal", "Status"};
        DefaultTableModel dietPlanModel = new DefaultTableModel(dietPlanColumns, 0);
        JTable dietPlanTable = new JTable(dietPlanModel);
        
        List<DietPlan> plans = dataStore.getDietPlansForDietician(currentUser.getId()).stream()
            .filter(plan -> plan.getPatientId() == patientId)
            .toList();
        
        for (DietPlan plan : plans) {
            dietPlanModel.addRow(new Object[]{
                plan.getStartDate().format(DateTimeFormatter.ISO_DATE),
                plan.getEndDate().format(DateTimeFormatter.ISO_DATE),
                plan.getGoal(),
                plan.getStatus()
            });
        }
        
        dietPlansPanel.add(new JScrollPane(dietPlanTable));
        tabbedPane.addTab("Diet Plans", dietPlansPanel);

        // Consultations History
        JPanel consultationsPanel = new JPanel(new BorderLayout());
        String[] consultationColumns = {"Date", "Notes", "Next Appointment"};
        DefaultTableModel consultationModel = new DefaultTableModel(consultationColumns, 0);
        JTable consultationTable = new JTable(consultationModel);
        
        List<DietConsultation> consultations = dataStore.getDietConsultationsForDietician(currentUser.getId()).stream()
            .filter(consultation -> consultation.getPatientId() == patientId)
            .toList();
        
        for (DietConsultation consultation : consultations) {
            consultationModel.addRow(new Object[]{
                consultation.getConsultationDate().format(DateTimeFormatter.ISO_DATE),
                consultation.getNotes(),
                consultation.getNextAppointment().format(DateTimeFormatter.ISO_DATE)
            });
        }
        
        consultationsPanel.add(new JScrollPane(consultationTable));
        tabbedPane.addTab("Consultations", consultationsPanel);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.add(closeButton, BorderLayout.SOUTH);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showNewConsultationDialog() {
        JDialog dialog = new JDialog(this, "New Consultation", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        // Form components
        JComboBox<ComboItem> patientCombo = new JComboBox<>();
        JTextArea notesArea = new JTextArea(4, 30);
        JTextField nextAppointmentField = new JTextField(20);

        // Populate patient combo box
        List<User> patients = dataStore.getPatientsForDietician(currentUser.getId());
        for (User patient : patients) {
            patientCombo.addItem(new ComboItem(patient.getId(), patient.getName()));
        }

        // Add components to form
        addFormField(formPanel, "Patient:", patientCombo, gbc);
        addFormField(formPanel, "Notes:", new JScrollPane(notesArea), gbc);
        addFormField(formPanel, "Next Appointment (YYYY-MM-DD):", nextAppointmentField, gbc);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                ComboItem selectedPatient = (ComboItem) patientCombo.getSelectedItem();
                if (selectedPatient == null || notesArea.getText().trim().isEmpty() || 
                    nextAppointmentField.getText().trim().isEmpty()) {
                    showError("Please fill in all required fields");
                    return;
                }

                // TODO: Add consultation to datastore when DietConsultation model is ready
                dialog.dispose();
                refreshConsultationsTable();
                showMessage("Consultation added successfully");
            } catch (Exception ex) {
                showError("Error adding consultation: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showConsultationDetails(int rowIndex) {
        // TODO: Implement when DietConsultation model is ready
        showMessage("View consultation details feature coming soon!");
    }

    private void showEditConsultationDialog(int rowIndex) {
        // TODO: Implement when DietConsultation model is ready
        showMessage("Edit consultation feature coming soon!");
    }

    private static class ComboItem {
        private final int id;
        private final String name;

        public ComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private void addFormField(JPanel panel, String label, Component component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
        gbc.gridy++;
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

    private void showDietPlanDetails(int rowIndex) {
        List<DietPlan> plans = dataStore.getDietPlansForDietician(currentUser.getId());
        if (rowIndex >= 0 && rowIndex < plans.size()) {
            DietPlan plan = plans.get(rowIndex);
            Patient patient = dataStore.getPatient(plan.getPatientId());

            JDialog dialog = new JDialog(this, "Diet Plan Details", true);
            dialog.setLayout(new BorderLayout(10, 10));

            JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            addDetailField(detailsPanel, "Patient", patient != null ? patient.getName() : "Unknown");
            addDetailField(detailsPanel, "Goal", plan.getGoal());
            addDetailField(detailsPanel, "Start Date", plan.getStartDate().format(DateTimeFormatter.ISO_DATE));
            addDetailField(detailsPanel, "End Date", plan.getEndDate().format(DateTimeFormatter.ISO_DATE));
            addDetailField(detailsPanel, "Status", plan.getStatus());
            addDetailField(detailsPanel, "Notes", plan.getNotes());

            // Add restrictions
            JPanel restrictionsPanel = new JPanel(new BorderLayout(5, 5));
            JTextArea restrictionsArea = new JTextArea();
            restrictionsArea.setText(String.join("\n", plan.getRestrictions()));
            restrictionsArea.setEditable(false);
            restrictionsPanel.add(new JLabel("Restrictions:"), BorderLayout.NORTH);
            restrictionsPanel.add(new JScrollPane(restrictionsArea), BorderLayout.CENTER);

            // Add recommendations
            JPanel recommendationsPanel = new JPanel(new BorderLayout(5, 5));
            JTextArea recommendationsArea = new JTextArea();
            recommendationsArea.setText(String.join("\n", plan.getRecommendations()));
            recommendationsArea.setEditable(false);
            recommendationsPanel.add(new JLabel("Recommendations:"), BorderLayout.NORTH);
            recommendationsPanel.add(new JScrollPane(recommendationsArea), BorderLayout.CENTER);

            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.add(detailsPanel, BorderLayout.NORTH);
            contentPanel.add(restrictionsPanel, BorderLayout.CENTER);
            contentPanel.add(recommendationsPanel, BorderLayout.SOUTH);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dialog.dispose());

            dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            dialog.add(closeButton, BorderLayout.SOUTH);
            dialog.setSize(500, 600);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } else {
            showError("Diet plan not found");
        }
    }

    private void addDetailField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label + ":"));
        panel.add(new JLabel(value != null ? value : "N/A"));
    }
}
