package com.hospital.views;

import com.hospital.data.DataStore;
import com.hospital.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PatientDashboardTest {
    private DataStore dataStore;
    private User testUser;
    private User testDoctor;
    private Patient testPatient;
    private PatientDashboard dashboard;

    @BeforeEach
    void setUp() {
        dataStore = DataStore.getInstance();
        dataStore.clear();

        // Create test user with PATIENT role
        testUser = new User(1, "Test Patient", "testpatient", "PATIENT", 1);
        dataStore.addUser(testUser);

        // Create test doctor
        testDoctor = new User(2, "Test Doctor", "testdoctor", "DOCTOR", 1);
        dataStore.addUser(testDoctor);

        // Create test patient
        testPatient = new Patient(1, "Test Patient", 30, "None", LocalDateTime.now().toLocalDate(),
            "1234567890", "test@example.com", "123 Test St", "A+", "None",
            LocalDateTime.now().toLocalDate(), "Male", "9876543210");
        dataStore.addPatient(testPatient);

        // Create test appointment
        Appointment appointment = new Appointment(1, testUser.getId(), testDoctor.getId(), 
            LocalDateTime.now().plusDays(1), "Checkup", "SCHEDULED", "Regular checkup", 
            "Annual physical", 30, "Room 101");
        dataStore.addAppointment(appointment);

        // Create test prescription
        Prescription prescription = new Prescription(1, testUser.getId(), testDoctor.getId(), 
            "Test diagnosis", "Take with food", "Test notes");
        prescription.addMedication(new Prescription.Medication("Test Med", "1 pill", 7, "Once daily"));
        dataStore.addPrescription(prescription);

        // Create test medical record
        MedicalRecord record = new MedicalRecord(1, testUser.getId(), testDoctor.getId(), 
            "Test type", "Test description");
        dataStore.addMedicalRecord(record);

        dashboard = new PatientDashboard(testUser);
    }

    @Test
    void testDashboardInitialization() {
        assertNotNull(dashboard);
        assertEquals("Patient Dashboard - " + testUser.getName(), dashboard.getTitle());
    }

    @Test
    void testAppointmentRetrieval() {
        var appointments = dataStore.getAppointmentsByPatientId(testUser.getId());
        assertNotNull(appointments);
        assertEquals(1, appointments.size());
        assertEquals("Checkup", appointments.get(0).getType());
    }

    @Test
    void testPrescriptionRetrieval() {
        var prescriptions = dataStore.getPrescriptionsByPatientId(testUser.getId());
        assertNotNull(prescriptions);
        assertEquals(1, prescriptions.size());
        assertEquals("Test diagnosis", prescriptions.get(0).getDiagnosis());
    }

    @Test
    void testMedicalRecordRetrieval() {
        var records = dataStore.getMedicalRecordsByPatientId(testUser.getId());
        assertNotNull(records);
        assertEquals(1, records.size());
        assertEquals("Test type", records.get(0).getType());
    }

    @Test
    void testPatientRetrieval() {
        var patient = dataStore.getPatient(testUser.getId());
        assertNotNull(patient);
        assertEquals(testPatient.getName(), patient.getName());
        assertEquals(testPatient.getEmail(), patient.getEmail());
    }

    @Test
    void testCleanup() {
        dataStore.clear();
    }
} 