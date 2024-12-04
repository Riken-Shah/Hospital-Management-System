package com.hospital.views;

import com.hospital.models.*;
import com.hospital.data.DataStore;
import com.hospital.security.AuthService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class CommunityManagerDashboard extends JFrame {
    private CommunityManager currentUser;
    private DataStore dataStore;
    private AuthService authService;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable metricsTable;
    private JTable drivesTable;
    private DefaultTableModel metricsTableModel;
    private DefaultTableModel drivesTableModel;

    public CommunityManagerDashboard(User user, AuthService authService) {
        this.currentUser = (CommunityManager) user;
        this.authService = authService;
        this.dataStore = DataStore.getInstance();
        setTitle("Community Manager Dashboard - " + currentUser.getName());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        initializeComponents();
        loadData();
    }

    private void initializeComponents() {
        // Main panel with card layout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Create menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setBackground(new Color(240, 240, 240));

        // Menu buttons
        JButton dashboardButton = new JButton("Dashboard");
        JButton metricsButton = new JButton("Health Metrics");
        JButton drivesButton = new JButton("Vaccination Drives");
        JButton profileButton = new JButton("Profile");
        JButton logoutButton = new JButton("Logout");

        // Add buttons to menu
        menuPanel.add(dashboardButton);
        menuPanel.add(metricsButton);
        menuPanel.add(drivesButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        // Add action listeners
        dashboardButton.addActionListener(e -> showPanel("dashboard"));
        metricsButton.addActionListener(e -> showPanel("metrics"));
        drivesButton.addActionListener(e -> showPanel("drives"));
        profileButton.addActionListener(e -> showPanel("profile"));
        logoutButton.addActionListener(e -> logout());

        // Create panels
        mainPanel.add(createDashboardPanel(), "dashboard");
        mainPanel.add(createMetricsPanel(), "metrics");
        mainPanel.add(createDrivesPanel(), "drives");
        mainPanel.add(createProfilePanel(), "profile");

        // Add components to frame
        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Summary cards
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryPanel.add(createSummaryCard("Population Covered", String.format("%,d", currentUser.getPopulationCovered())));
        summaryPanel.add(createSummaryCard("Active Vaccination Drives", "2"));
        summaryPanel.add(createSummaryCard("Health Metrics Tracked", "3"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(summaryPanel, gbc);

        // Charts
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 1.0;
        panel.add(createVaccinationProgressChart(), gbc);

        gbc.gridx = 1;
        panel.add(createHealthMetricsChart(), gbc);

        return panel;
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        String[] columns = {"ID", "Metric Name", "Value", "Unit", "Date", "Notes"};
        metricsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        metricsTable = new JTable(metricsTableModel);
        JScrollPane scrollPane = new JScrollPane(metricsTable);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add New Metric");
        addButton.addActionListener(e -> showAddMetricDialog());
        buttonsPanel.add(addButton);

        panel.add(buttonsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDrivesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        String[] columns = {"ID", "Vaccine Name", "Start Date", "End Date", "Target", "Vaccinated", "Progress", "Status"};
        drivesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        drivesTable = new JTable(drivesTableModel);
        JScrollPane scrollPane = new JScrollPane(drivesTable);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add New Drive");
        JButton updateButton = new JButton("Update Progress");
        addButton.addActionListener(e -> showAddDriveDialog());
        updateButton.addActionListener(e -> showUpdateDriveDialog());
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);

        panel.add(buttonsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addProfileField(panel, "Name:", currentUser.getName(), gbc, 0);
        addProfileField(panel, "Username:", currentUser.getUsername(), gbc, 1);
        addProfileField(panel, "Role:", currentUser.getRole(), gbc, 2);
        addProfileField(panel, "Region:", currentUser.getRegion(), gbc, 3);
        addProfileField(panel, "Contact:", currentUser.getContactNumber(), gbc, 4);
        addProfileField(panel, "Population Covered:", String.valueOf(currentUser.getPopulationCovered()), gbc, 5);

        return panel;
    }

    private void addProfileField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
    }

    private JPanel createSummaryCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(24.0f));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);

        return card;
    }

    private JPanel createVaccinationProgressChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<VaccinationDrive> drives = dataStore.getVaccinationDrives(currentUser.getRegion());
        
        for (VaccinationDrive drive : drives) {
            dataset.addValue(drive.getProgress(), "Progress", drive.getVaccineName());
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Vaccination Drive Progress",
            "Vaccine",
            "Progress (%)",
            dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return chartPanel;
    }

    private JPanel createHealthMetricsChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<CommunityHealthMetric> metrics = dataStore.getHealthMetrics(currentUser.getRegion());
        
        for (CommunityHealthMetric metric : metrics) {
            dataset.setValue(metric.getMetricName(), metric.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Health Metrics Distribution",
            dataset,
            true,
            true,
            false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return chartPanel;
    }

    private void showAddMetricDialog() {
        JDialog dialog = new JDialog(this, "Add New Health Metric", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JTextField valueField = new JTextField();
        JTextField unitField = new JTextField();
        JTextField notesField = new JTextField();

        dialog.add(new JLabel("Metric Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Value:"));
        dialog.add(valueField);
        dialog.add(new JLabel("Unit:"));
        dialog.add(unitField);
        dialog.add(new JLabel("Notes:"));
        dialog.add(notesField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                CommunityHealthMetric metric = new CommunityHealthMetric(
                    0,
                    currentUser.getRegion(),
                    nameField.getText(),
                    Double.parseDouble(valueField.getText()),
                    unitField.getText(),
                    LocalDate.now(),
                    notesField.getText()
                );
                dataStore.addHealthMetric(metric);
                loadMetricsData();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for value");
            }
        });

        dialog.add(saveButton);
        dialog.setVisible(true);
    }

    private void showAddDriveDialog() {
        JDialog dialog = new JDialog(this, "Add New Vaccination Drive", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JTextField targetField = new JTextField();
        JTextField notesField = new JTextField();

        dialog.add(new JLabel("Vaccine Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Target Population:"));
        dialog.add(targetField);
        dialog.add(new JLabel("Notes:"));
        dialog.add(notesField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                VaccinationDrive drive = new VaccinationDrive(
                    0,
                    currentUser.getRegion(),
                    nameField.getText(),
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1),
                    Integer.parseInt(targetField.getText()),
                    0,
                    "Not Started",
                    notesField.getText()
                );
                dataStore.addVaccinationDrive(drive);
                loadDrivesData();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for target population");
            }
        });

        dialog.add(saveButton);
        dialog.setVisible(true);
    }

    private void showUpdateDriveDialog() {
        int selectedRow = drivesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vaccination drive to update");
            return;
        }

        int driveId = (int) drivesTable.getValueAt(selectedRow, 0);
        VaccinationDrive drive = dataStore.getVaccinationDrives(currentUser.getRegion()).stream()
                .filter(d -> d.getId() == driveId)
                .findFirst()
                .orElse(null);

        if (drive == null) return;

        JDialog dialog = new JDialog(this, "Update Vaccination Progress", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JTextField vaccinatedField = new JTextField(String.valueOf(drive.getVaccinated()));
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Not Started", "In Progress", "Completed"});
        statusBox.setSelectedItem(drive.getStatus());

        dialog.add(new JLabel("Vaccinated Count:"));
        dialog.add(vaccinatedField);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusBox);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                drive.setVaccinated(Integer.parseInt(vaccinatedField.getText()));
                drive.setStatus((String) statusBox.getSelectedItem());
                dataStore.updateVaccinationDrive(drive);
                loadDrivesData();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for vaccinated count");
            }
        });

        dialog.add(saveButton);
        dialog.setVisible(true);
    }

    private void loadData() {
        loadMetricsData();
        loadDrivesData();
    }

    private void loadMetricsData() {
        metricsTableModel.setRowCount(0);
        List<CommunityHealthMetric> metrics = dataStore.getHealthMetrics(currentUser.getRegion());
        for (CommunityHealthMetric metric : metrics) {
            metricsTableModel.addRow(new Object[]{
                metric.getId(),
                metric.getMetricName(),
                metric.getValue(),
                metric.getUnit(),
                metric.getDate(),
                metric.getNotes()
            });
        }
    }

    private void loadDrivesData() {
        drivesTableModel.setRowCount(0);
        List<VaccinationDrive> drives = dataStore.getVaccinationDrives(currentUser.getRegion());
        for (VaccinationDrive drive : drives) {
            drivesTableModel.addRow(new Object[]{
                drive.getId(),
                drive.getVaccineName(),
                drive.getStartDate(),
                drive.getEndDate(),
                drive.getTargetPopulation(),
                drive.getVaccinated(),
                String.format("%.1f%%", drive.getProgress()),
                drive.getStatus()
            });
        }
    }

    private void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    private void logout() {
        dispose();
        new LoginFrame(authService).setVisible(true);
    }
} 