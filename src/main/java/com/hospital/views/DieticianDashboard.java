package com.hospital.views;

import com.hospital.models.*;
import com.hospital.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DieticianDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(DieticianDashboard.class);
    private JPanel menuPanel;
    private final DataStore dataStore;

    public DieticianDashboard(User currentUser) {
        super(currentUser);
        this.dataStore = DataStore.getInstance();
        setTitle("Dietician Dashboard - " + currentUser.getName());
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
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
        // Show patients panel by default
        showPanel(createPatientsPanel());
    }

    private JPanel createPatientsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Patient ID", "Name", "Age", "Diagnosis", "Last Visit"};
        Object[][] data = {};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate patient data
        List<User> patients = dataStore.getPatientsForDietician(currentUser.getId());
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

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Details");
        JButton assignButton = new JButton("Assign Diet Plan");
        JButton historyButton = new JButton("View History");

        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) table.getValueAt(selectedRow, 0);
                showPatientDetails(patientId);
            } else {
                showError("Please select a patient first");
            }
        });

        assignButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) table.getValueAt(selectedRow, 0);
                showAssignDietPlanDialog(patientId);
            } else {
                showError("Please select a patient first");
            }
        });

        historyButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) table.getValueAt(selectedRow, 0);
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

    private JPanel createConsultationsPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Date", "Patient", "Status", "Notes", "Next Appointment"};
        Object[][] data = {};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate consultation data
        List<DietConsultation> consultations = dataStore.getDietConsultationsForDietician(currentUser.getId());
        for (DietConsultation consultation : consultations) {
            Patient patient = dataStore.getPatient(consultation.getPatientId());
            model.addRow(new Object[]{
                consultation.getConsultationDate(),
                patient.getName(),
                consultation.getStatus(),
                consultation.getNotes(),
                consultation.getNextAppointment()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("New Consultation");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit Consultation");

        addButton.addActionListener(e -> showNewConsultationDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                showConsultationDetails(selectedRow);
            } else {
                showError("Please select a consultation first");
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
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

    private JPanel createDietPlansPanel() {
        JPanel panel = createFormPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columnNames = {"Patient", "Goal", "Start Date", "End Date", "Status"};
        Object[][] data = {};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fetch and populate diet plan data
        List<DietPlan> plans = dataStore.getDietPlansForDietician(currentUser.getId());
        for (DietPlan plan : plans) {
            Patient patient = dataStore.getPatient(plan.getPatientId());
            model.addRow(new Object[]{
                patient.getName(),
                plan.getGoal(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getStatus()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("New Diet Plan");
        JButton viewButton = new JButton("View Details");
        JButton deleteButton = new JButton("Delete Plan");

        addButton.addActionListener(e -> showNewDietPlanDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                showDietPlanDetails(selectedRow);
            } else {
                showError("Please select a diet plan first");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this diet plan?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dataStore.deleteDietPlan((int) table.getValueAt(selectedRow, 0));
                    refreshDietPlansPanel();
                }
            } else {
                showError("Please select a diet plan first");
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(deleteButton);

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

    // Helper methods for dialogs
    private void showPatientDetails(int patientId) {
        // TODO: Implement patient details dialog
        showError("View patient details feature coming soon!");
    }

    private void showAssignDietPlanDialog(int patientId) {
        // TODO: Implement assign diet plan dialog
        showError("Assign diet plan feature coming soon!");
    }

    private void showPatientHistory(int patientId) {
        // TODO: Implement patient history dialog
        showError("View patient history feature coming soon!");
    }

    private void showNewConsultationDialog() {
        // TODO: Implement new consultation dialog
        showError("New consultation feature coming soon!");
    }

    private void showConsultationDetails(int consultationIndex) {
        // TODO: Implement consultation details dialog
        showError("View consultation details feature coming soon!");
    }

    private void showEditConsultationDialog(int consultationIndex) {
        // TODO: Implement edit consultation dialog
        showError("Edit consultation feature coming soon!");
    }

    private void showNewDietPlanDialog() {
        // TODO: Implement new diet plan dialog
        showError("New diet plan feature coming soon!");
    }

    private void showDietPlanDetails(int planIndex) {
        // TODO: Implement diet plan details dialog
        showError("View diet plan details feature coming soon!");
    }

    private void refreshDietPlansPanel() {
        // Remove current panel
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component != menuPanel) {
                remove(component);
            }
        }
        // Add refreshed panel
        showPanel(createDietPlansPanel());
        revalidate();
        repaint();
    }
}
