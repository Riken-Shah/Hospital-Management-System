package com.hospital.views;

import com.hospital.models.*;
import com.hospital.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(AdminDashboard.class);
    private JPanel menuPanel;
    private final DataStore dataStore;
    private DefaultTableModel hospitalTableModel;
    private DefaultTableModel communityTableModel;
    private DefaultTableModel doctorTableModel;
    private DefaultTableModel patientTableModel;
    private JTable hospitalTable;
    private JTable communityTable;
    private JTable doctorTable;
    private JTable patientTable;

    public AdminDashboard(User currentUser) {
        super(currentUser);
        this.dataStore = DataStore.getInstance();
        setTitle("Admin Dashboard - " + currentUser.getName());
        initializeTableModels();
        initializeMenu();
        if (!isHeadless) {
            setVisible(true);
        }
    }

    private void initializeTableModels() {
        // Hospital table model
        hospitalTableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Location", "Contact", "Capacity"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Community table model
        communityTableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Location", "Population", "Manager"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Doctor table model
        doctorTableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Specialization", "Hospital", "Contact", "Available"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Patient table model
        patientTableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Age", "Gender", "Contact", "Blood Group"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton hospitalsButton = createMenuButton("Hospitals");
        JButton communitiesButton = createMenuButton("Communities");
        JButton doctorsButton = createMenuButton("Doctors");
        JButton patientsButton = createMenuButton("Patients");
        JButton reportsButton = createMenuButton("Reports");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        hospitalsButton.addActionListener(e -> showPanel(createHospitalsPanel()));
        communitiesButton.addActionListener(e -> showPanel(createCommunitiesPanel()));
        doctorsButton.addActionListener(e -> showPanel(createDoctorsPanel()));
        patientsButton.addActionListener(e -> showPanel(createPatientsPanel()));
        reportsButton.addActionListener(e -> showPanel(createReportsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(hospitalsButton);
        menuPanel.add(communitiesButton);
        menuPanel.add(doctorsButton);
        menuPanel.add(patientsButton);
        menuPanel.add(reportsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show hospitals panel by default
        showPanel(createHospitalsPanel());
    }

    private JPanel createHospitalsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        hospitalTable = new JTable(hospitalTableModel);
        JScrollPane scrollPane = new JScrollPane(hospitalTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Hospital");
        JButton editButton = new JButton("Edit Hospital");
        JButton deleteButton = new JButton("Delete Hospital");

        addButton.addActionListener(e -> showAddHospitalDialog());
        editButton.addActionListener(e -> showEditHospitalDialog());
        deleteButton.addActionListener(e -> deleteSelectedHospital());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        panel.add(buttonsPanel, BorderLayout.NORTH);

        refreshHospitalTable();
        return panel;
    }

    private JPanel createCommunitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        communityTable = new JTable(communityTableModel);
        JScrollPane scrollPane = new JScrollPane(communityTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Community");
        JButton editButton = new JButton("Edit Community");
        JButton deleteButton = new JButton("Delete Community");

        addButton.addActionListener(e -> showAddCommunityDialog());
        editButton.addActionListener(e -> showEditCommunityDialog());
        deleteButton.addActionListener(e -> deleteSelectedCommunity());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        panel.add(buttonsPanel, BorderLayout.NORTH);

        refreshCommunityTable();
        return panel;
    }

    private JPanel createDoctorsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        doctorTable = new JTable(doctorTableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Doctor");
        JButton editButton = new JButton("Edit Doctor");
        JButton deleteButton = new JButton("Delete Doctor");

        addButton.addActionListener(e -> showAddDoctorDialog());
        editButton.addActionListener(e -> showEditDoctorDialog());
        deleteButton.addActionListener(e -> deleteSelectedDoctor());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        panel.add(buttonsPanel, BorderLayout.NORTH);

        refreshDoctorTable();
        return panel;
    }

    private JPanel createPatientsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        patientTable = new JTable(patientTableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Patient");
        JButton editButton = new JButton("Edit Patient");
        JButton deleteButton = new JButton("Delete Patient");

        addButton.addActionListener(e -> showAddPatientDialog());
        editButton.addActionListener(e -> showEditPatientDialog());
        deleteButton.addActionListener(e -> deleteSelectedPatient());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        panel.add(buttonsPanel, BorderLayout.NORTH);

        refreshPatientTable();
        return panel;
    }

    private void showAddHospitalDialog() {
        JDialog dialog = new JDialog(this, "Add Hospital", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JTextField nameField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField contactField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 10));

        addFormField(formPanel, "Name:", nameField, gbc);
        addFormField(formPanel, "Location:", locationField, gbc);
        addFormField(formPanel, "Contact:", contactField, gbc);
        addFormField(formPanel, "Email:", emailField, gbc);
        addFormField(formPanel, "Capacity:", capacitySpinner, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String contact = contactField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || location.isEmpty() || contact.isEmpty() || email.isEmpty()) {
                showError("All fields are required");
                return;
            }

            Hospital hospital = new Hospital(dataStore.getNextId(), name, location, contact, email);
            dataStore.addHospital(hospital);
            dialog.dispose();
            refreshHospitalTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddCommunityDialog() {
        JDialog dialog = new JDialog(this, "Add Community", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JTextField nameField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JSpinner populationSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 1000000, 100));
        JComboBox<CommunityManager> managerComboBox = new JComboBox<>();

        // Populate manager combo box
        for (CommunityManager manager : dataStore.getAllCommunityManagers()) {
            managerComboBox.addItem(manager);
        }

        addFormField(formPanel, "Name:", nameField, gbc);
        addFormField(formPanel, "Location:", locationField, gbc);
        addFormField(formPanel, "Population:", populationSpinner, gbc);
        addFormField(formPanel, "Manager:", managerComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            int population = (Integer) populationSpinner.getValue();
            CommunityManager manager = (CommunityManager) managerComboBox.getSelectedItem();

            if (name.isEmpty() || location.isEmpty() || manager == null) {
                showError("All fields are required");
                return;
            }

            Community community = new Community(dataStore.getNextId(), name, location, population, manager.getId());
            dataStore.addCommunity(community);
            dialog.dispose();
            refreshCommunityTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddDoctorDialog() {
        JDialog dialog = new JDialog(this, "Add Doctor", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JTextField nameField = new JTextField(20);
        JTextField usernameField = new JTextField(20);
        JTextField specializationField = new JTextField(20);
        JTextField contactField = new JTextField(20);
        JComboBox<Hospital> hospitalComboBox = new JComboBox<>();

        // Populate hospital combo box
        for (Hospital hospital : dataStore.getAllHospitals()) {
            hospitalComboBox.addItem(hospital);
        }

        addFormField(formPanel, "Name:", nameField, gbc);
        addFormField(formPanel, "Username:", usernameField, gbc);
        addFormField(formPanel, "Specialization:", specializationField, gbc);
        addFormField(formPanel, "Contact:", contactField, gbc);
        addFormField(formPanel, "Hospital:", hospitalComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String contact = contactField.getText().trim();
            Hospital hospital = (Hospital) hospitalComboBox.getSelectedItem();

            if (name.isEmpty() || username.isEmpty() || specialization.isEmpty() || contact.isEmpty() || hospital == null) {
                showError("All fields are required");
                return;
            }

            User doctor = new User(dataStore.getNextId(), name, username, "DOCTOR", hospital.getId());
            dataStore.addUser(doctor);
            dialog.dispose();
            refreshDoctorTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog(this, "Add Patient", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JTextField nameField = new JTextField(20);
        JTextField usernameField = new JTextField(20);
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 150, 1));
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField contactField = new JTextField(20);
        JComboBox<String> bloodGroupComboBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JTextArea allergiesArea = new JTextArea(3, 20);
        JScrollPane allergiesScroll = new JScrollPane(allergiesArea);

        addFormField(formPanel, "Name:", nameField, gbc);
        addFormField(formPanel, "Username:", usernameField, gbc);
        addFormField(formPanel, "Age:", ageSpinner, gbc);
        addFormField(formPanel, "Gender:", genderComboBox, gbc);
        addFormField(formPanel, "Contact:", contactField, gbc);
        addFormField(formPanel, "Blood Group:", bloodGroupComboBox, gbc);
        addFormField(formPanel, "Allergies:", allergiesScroll, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            int age = (Integer) ageSpinner.getValue();
            String gender = (String) genderComboBox.getSelectedItem();
            String contact = contactField.getText().trim();
            String bloodGroup = (String) bloodGroupComboBox.getSelectedItem();
            String allergies = allergiesArea.getText().trim();

            if (name.isEmpty() || username.isEmpty() || contact.isEmpty()) {
                showError("Name, username and contact are required");
                return;
            }

            Patient patient = new Patient(dataStore.getNextId(), name, username, 1);
            patient.setAge(age);
            patient.setGender(gender);
            patient.setPhone(contact);
            patient.setBloodGroup(bloodGroup);
            patient.setAllergies(allergies);

            dataStore.addPatient(patient);
            dialog.dispose();
            refreshPatientTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditHospitalDialog() {
        int selectedRow = hospitalTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a hospital to edit");
            return;
        }

        int hospitalId = (Integer) hospitalTable.getValueAt(selectedRow, 0);
        Hospital hospital = dataStore.getHospital(hospitalId);
        if (hospital == null) {
            showError("Hospital not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Hospital", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JTextField nameField = new JTextField(hospital.getName(), 20);
        JTextField locationField = new JTextField(hospital.getAddress(), 20);
        JTextField contactField = new JTextField(hospital.getContactNumber(), 20);
        JTextField emailField = new JTextField(hospital.getEmail(), 20);

        addFormField(formPanel, "Name:", nameField, gbc);
        addFormField(formPanel, "Location:", locationField, gbc);
        addFormField(formPanel, "Contact:", contactField, gbc);
        addFormField(formPanel, "Email:", emailField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String contact = contactField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || location.isEmpty() || contact.isEmpty() || email.isEmpty()) {
                showError("All fields are required");
                return;
            }

            hospital.setName(name);
            hospital.setAddress(location);
            hospital.setContactNumber(contact);
            hospital.setEmail(email);

            dataStore.updateHospital(hospital);
            dialog.dispose();
            refreshHospitalTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditCommunityDialog() {
        int selectedRow = communityTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a community to edit");
            return;
        }

        int communityId = (Integer) communityTable.getValueAt(selectedRow, 0);
        Community community = dataStore.getCommunity(communityId);
        if (community == null) {
            showError("Community not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Community", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JTextField nameField = new JTextField(community.getName(), 20);
        JTextField locationField = new JTextField(community.getLocation(), 20);
        JSpinner populationSpinner = new JSpinner(new SpinnerNumberModel(community.getPopulation(), 1, 1000000, 100));
        JComboBox<CommunityManager> managerComboBox = new JComboBox<>();

        // Populate manager combo box
        for (CommunityManager manager : dataStore.getAllCommunityManagers()) {
            managerComboBox.addItem(manager);
            if (manager.getId() == community.getManagerId()) {
                managerComboBox.setSelectedItem(manager);
            }
        }

        addFormField(formPanel, "Name:", nameField, gbc);
        addFormField(formPanel, "Location:", locationField, gbc);
        addFormField(formPanel, "Population:", populationSpinner, gbc);
        addFormField(formPanel, "Manager:", managerComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            int population = (Integer) populationSpinner.getValue();
            CommunityManager manager = (CommunityManager) managerComboBox.getSelectedItem();

            if (name.isEmpty() || location.isEmpty() || manager == null) {
                showError("All fields are required");
                return;
            }

            community.setName(name);
            community.setLocation(location);
            community.setPopulation(population);
            community.setManagerId(manager.getId());

            dataStore.updateCommunity(community);
            dialog.dispose();
            refreshCommunityTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditDoctorDialog() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a doctor to edit");
            return;
        }

        int doctorId = (Integer) doctorTable.getValueAt(selectedRow, 0);
        User doctor = dataStore.getUser(doctorId);
        if (doctor == null || !"DOCTOR".equals(doctor.getRole())) {
            showError("Doctor not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Doctor", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JTextField nameField = new JTextField(doctor.getName(), 20);
        JTextField usernameField = new JTextField(doctor.getUsername(), 20);
        JTextField emailField = new JTextField(doctor.getEmail(), 20);
        JComboBox<Hospital> hospitalComboBox = new JComboBox<>();

        // Populate hospital combo box
        for (Hospital hospital : dataStore.getAllHospitals()) {
            hospitalComboBox.addItem(hospital);
            if (hospital.getId() == doctor.getHospitalId()) {
                hospitalComboBox.setSelectedItem(hospital);
            }
        }

        addFormField(formPanel, "Name:", nameField, gbc);
        addFormField(formPanel, "Username:", usernameField, gbc);
        addFormField(formPanel, "Email:", emailField, gbc);
        addFormField(formPanel, "Hospital:", hospitalComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            Hospital hospital = (Hospital) hospitalComboBox.getSelectedItem();

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || hospital == null) {
                showError("All fields are required");
                return;
            }

            doctor.setName(name);
            doctor.setUsername(username);
            doctor.setEmail(email);
            doctor.setHospitalId(hospital.getId());

            dataStore.updateUser(doctor);
            dialog.dispose();
            refreshDoctorTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditPatientDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a patient to edit");
            return;
        }

        int patientId = (Integer) patientTable.getValueAt(selectedRow, 0);
        Patient patient = dataStore.getPatient(patientId);
        if (patient == null) {
            showError("Patient not found");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Patient", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JTextField nameField = new JTextField(patient.getName(), 20);
        JTextField usernameField = new JTextField(patient.getUsername(), 20);
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(patient.getAge(), 0, 150, 1));
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setSelectedItem(patient.getGender());
        JTextField contactField = new JTextField(patient.getPhone(), 20);
        JComboBox<String> bloodGroupComboBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodGroupComboBox.setSelectedItem(patient.getBloodGroup());
        JTextArea allergiesArea = new JTextArea(patient.getAllergies(), 3, 20);
        JScrollPane allergiesScroll = new JScrollPane(allergiesArea);

        addFormField(formPanel, "Name:", nameField, gbc);
        addFormField(formPanel, "Username:", usernameField, gbc);
        addFormField(formPanel, "Age:", ageSpinner, gbc);
        addFormField(formPanel, "Gender:", genderComboBox, gbc);
        addFormField(formPanel, "Contact:", contactField, gbc);
        addFormField(formPanel, "Blood Group:", bloodGroupComboBox, gbc);
        addFormField(formPanel, "Allergies:", allergiesScroll, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            int age = (Integer) ageSpinner.getValue();
            String gender = (String) genderComboBox.getSelectedItem();
            String contact = contactField.getText().trim();
            String bloodGroup = (String) bloodGroupComboBox.getSelectedItem();
            String allergies = allergiesArea.getText().trim();

            if (name.isEmpty() || username.isEmpty() || contact.isEmpty()) {
                showError("Name, username and contact are required");
                return;
            }

            patient.setName(name);
            patient.setUsername(username);
            patient.setAge(age);
            patient.setGender(gender);
            patient.setPhone(contact);
            patient.setBloodGroup(bloodGroup);
            patient.setAllergies(allergies);

            dataStore.updatePatient(patient);
            dialog.dispose();
            refreshPatientTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedHospital() {
        int selectedRow = hospitalTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a hospital to delete");
            return;
        }

        int hospitalId = (Integer) hospitalTable.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this hospital?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            dataStore.deleteHospital(hospitalId);
            refreshHospitalTable();
        }
    }

    private void deleteSelectedCommunity() {
        int selectedRow = communityTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a community to delete");
            return;
        }

        int communityId = (Integer) communityTable.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this community?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            dataStore.deleteCommunity(communityId);
            refreshCommunityTable();
        }
    }

    private void deleteSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a doctor to delete");
            return;
        }

        int doctorId = (Integer) doctorTable.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this doctor?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            dataStore.deleteUser(doctorId);
            refreshDoctorTable();
        }
    }

    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a patient to delete");
            return;
        }

        int patientId = (Integer) patientTable.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this patient?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            dataStore.deletePatient(patientId);
            refreshPatientTable();
        }
    }

    private void refreshHospitalTable() {
        hospitalTableModel.setRowCount(0);
        List<Hospital> hospitals = dataStore.getAllHospitals();
        for (Hospital hospital : hospitals) {
            hospitalTableModel.addRow(new Object[]{
                hospital.getId(),
                hospital.getName(),
                hospital.getAddress(),
                hospital.getContactNumber(),
                hospital.getEmail()
            });
        }
    }

    private void refreshCommunityTable() {
        communityTableModel.setRowCount(0);
        List<Community> communities = dataStore.getAllCommunities();
        for (Community community : communities) {
            CommunityManager manager = dataStore.getCommunityManagerByRegion(community.getLocation());
            communityTableModel.addRow(new Object[]{
                community.getId(),
                community.getName(),
                community.getLocation(),
                community.getPopulation(),
                manager != null ? manager.getName() : "No Manager"
            });
        }
    }

    private void refreshDoctorTable() {
        doctorTableModel.setRowCount(0);
        List<User> users = dataStore.getAllUsers();
        for (User user : users) {
            if ("DOCTOR".equals(user.getRole())) {
                Hospital hospital = dataStore.getHospital(user.getHospitalId());
                doctorTableModel.addRow(new Object[]{
                    user.getId(),
                    user.getName(),
                    "General", // TODO: Add specialization to Doctor model
                    hospital != null ? hospital.getName() : "Unknown Hospital",
                    user.getEmail(),
                    "Available" // TODO: Add availability to Doctor model
                });
            }
        }
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
                patient.getBloodGroup()
            });
        }
    }

    private JPanel createReportsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement reports panel
        panel.add(new JLabel("System reports panel coming soon!"));
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

    private void addFormField(JPanel panel, String label, Component component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
        gbc.gridy++;
    }
} 