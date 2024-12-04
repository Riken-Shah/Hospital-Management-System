package com.hospital;

import com.hospital.views.LoginFrame;
import com.hospital.security.AuthService;
import com.hospital.data.DataManager;
import com.hospital.model.*;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize data manager
        DataManager dataManager = DataManager.getInstance();

        // Add some test data
        Doctor doctor = new Doctor(1, "Dr. John Smith", "jsmith", "Cardiologist", 1, "LIC123");
        dataManager.addDoctor(doctor);

        Patient patient = new Patient(2, "Jane Doe", "jdoe", 1, 1);
        dataManager.addPatient(patient);

        Pharmacist pharmacist = new Pharmacist(3, "Bob Wilson", "bwilson", 1, "PHAR456");
        dataManager.addPharmacist(pharmacist);

        ITSupport itSupport = new ITSupport(4, "Mike Brown", "mbrown", 1, "IT Support");
        dataManager.addITSupport(itSupport);

        Dietician dietician = new Dietician(5, "Sarah Johnson", "sjohnson", 1, "Clinical Nutrition", "DIET789");
        dataManager.addDietician(dietician);

        Hospital hospital = new Hospital(1, "City General Hospital", "123 Main St", "555-0123");
        dataManager.addHospital(hospital);

        // Initialize authentication service
        AuthService authService = new AuthService();

        // Launch the login frame
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(authService);
            loginFrame.setVisible(true);
        });
    }
} 