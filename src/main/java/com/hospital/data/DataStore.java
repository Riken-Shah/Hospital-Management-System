package com.hospital.data;

import com.hospital.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataStore {
    private static final Logger logger = LoggerFactory.getLogger(DataStore.class);
    private static DataStore instance;
    private static final Map<Integer, User> users = new HashMap<>();
    private static final Map<Integer, Patient> patients = new HashMap<>();
    private static final Map<Integer, Appointment> appointments = new HashMap<>();
    private static final Map<Integer, Prescription> prescriptions = new HashMap<>();
    private static final Map<Integer, MedicalRecord> medicalRecords = new HashMap<>();
    private static int nextId = 1;

    // Diet plan management
    private final Map<Integer, DietPlan> dietPlans = new HashMap<>();

    private DataStore() {
        // Private constructor to prevent instantiation
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // Initialize with sample data
    static {
        DataStore store = getInstance();
        // Add sample users
        store.addUser(new User(1, "Dr. John Doe", "doctor1", "DOCTOR", 1));
        store.addUser(new User(2, "Dr. Jane Smith", "doctor2", "DOCTOR", 1));
        store.addUser(new User(3, "Nurse Sarah", "nurse1", "NURSE", 1));
        store.addUser(new User(4, "Admin Bob", "admin1", "ADMIN", 1));
        store.addUser(new User(5, "Pharmacist Mike", "pharm1", "PHARMACIST", 1));
        store.addUser(new User(6, "IT Support Joe", "it1", "IT_SUPPORT", 1));
        store.addUser(new User(7, "Dr. Diet Expert", "diet1", "DIETICIAN", 1));
        store.addUser(new User(8, "Patient Alice", "patient1", "PATIENT", 1));

        // Add sample patients
        store.addPatient(new Patient(1, "Alice Johnson", 35, "Hypertension", LocalDate.now().minusDays(30),
                "123-456-7890", "alice@email.com", "123 Main St", "A+", "None",
                LocalDate.of(1988, 5, 15), "Female", "Bob Johnson (Spouse) 123-555-0000"));
        store.addPatient(new Patient(2, "Bob Smith", 45, "Diabetes Type 2", LocalDate.now().minusDays(15),
                "123-456-7891", "bob@email.com", "456 Oak St", "B+", "Penicillin",
                LocalDate.of(1978, 8, 20), "Male", "Mary Smith (Wife) 123-555-0001"));

        // Add sample appointments
        store.addAppointment(new Appointment(1, 1, 1, LocalDateTime.now().plusDays(1), "Check-up",
                "SCHEDULED", "Regular check-up", "Annual physical", 30, "Room 101"));
        store.addAppointment(new Appointment(2, 2, 2, LocalDateTime.now().plusDays(2), "Follow-up",
                "SCHEDULED", "Follow-up for diabetes", "Blood sugar review", 45, "Room 102"));

        // Add sample prescriptions
        List<Prescription.Medication> medications1 = Arrays.asList(
            new Prescription.Medication("Lisinopril", "10mg", "Once daily", 30, "Take with water"),
            new Prescription.Medication("Aspirin", "81mg", "Once daily", 30, "Take with food")
        );
        store.addPrescription(new Prescription(1, 1, 1, LocalDate.now(), medications1, "Hypertension",
                "Take as directed", "Monitor blood pressure", LocalDate.now().plusDays(30),
                true, 2));

        // Add sample medical records
        store.addMedicalRecord(new MedicalRecord(1, 1, 1, "Check-up", "Regular annual check-up"));
    }

    // User management
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public User getUserByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // Patient management
    public void addPatient(Patient patient) {
        patients.put(patient.getId(), patient);
    }

    public Patient getPatientById(int id) {
        return patients.get(id);
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public void updatePatient(Patient patient) {
        patients.put(patient.getId(), patient);
    }

    public void deletePatient(int id) {
        patients.remove(id);
    }

    // Appointment management
    public void addAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    public Appointment getAppointmentById(int id) {
        return appointments.get(id);
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        return appointments.values().stream()
                .filter(a -> a.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        return appointments.values().stream()
                .filter(a -> a.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public void updateAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    public void deleteAppointment(int id) {
        appointments.remove(id);
    }

    // Prescription management
    public void addPrescription(Prescription prescription) {
        prescriptions.put(prescription.getId(), prescription);
    }

    public Prescription getPrescriptionById(int id) {
        return prescriptions.get(id);
    }

    public List<Prescription> getPrescriptionsByDoctorId(int doctorId) {
        return prescriptions.values().stream()
                .filter(p -> p.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<Prescription> getPrescriptionsByPatientId(int patientId) {
        return prescriptions.values().stream()
                .filter(p -> p.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public void updatePrescription(Prescription prescription) {
        prescriptions.put(prescription.getId(), prescription);
    }

    public void deletePrescription(int id) {
        prescriptions.remove(id);
    }

    // Medical record management
    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.put(record.getId(), record);
    }

    public MedicalRecord getMedicalRecordById(int id) {
        return medicalRecords.get(id);
    }

    public List<MedicalRecord> getMedicalRecordsByDoctorId(int doctorId) {
        return medicalRecords.values().stream()
                .filter(r -> r.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<MedicalRecord> getMedicalRecordsByPatientId(int patientId) {
        return medicalRecords.values().stream()
                .filter(r -> r.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public void updateMedicalRecord(MedicalRecord record) {
        medicalRecords.put(record.getId(), record);
    }

    public void deleteMedicalRecord(int id) {
        medicalRecords.remove(id);
    }

    // Utility methods
    public void clear() {
        users.clear();
        patients.clear();
        appointments.clear();
        prescriptions.clear();
        medicalRecords.clear();
        nextId = 1;
    }

    public int getNextId() {
        return nextId++;
    }

    // Add these new methods
    public List<User> getPatients() {
        return users.values().stream()
                .filter(u -> "PATIENT".equals(u.getRole()))
                .collect(Collectors.toList());
    }

    public List<MedicalRecord> getMedicalRecords(int patientId) {
        return medicalRecords.values().stream()
                .filter(r -> r.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    // Add these new methods for DieticianDashboard
    public User getDietician(int id) {
        User user = getUserById(id);
        return user != null && "DIETICIAN".equals(user.getRole()) ? user : null;
    }

    public List<User> getPatientsForDietician(int dieticianId) {
        // For now, return all patients
        // TODO: Implement proper patient-dietician relationship
        return getPatients();
    }

    public Patient getPatient(int id) {
        return patients.get(id);
    }

    public List<DietConsultation> getDietConsultationsForDietician(int dieticianId) {
        // TODO: Implement this method when DietConsultation model is ready
        return new ArrayList<>();
    }

    public List<DietPlan> getDietPlansForDietician(int dieticianId) {
        // TODO: Implement this method when DietPlan model is ready
        return new ArrayList<>();
    }

    // Diet plan management
    public void addDietPlan(DietPlan plan) {
        dietPlans.put(plan.getId(), plan);
    }

    public DietPlan getDietPlan(int id) {
        return dietPlans.get(id);
    }

    public List<DietPlan> getDietPlans() {
        return new ArrayList<>(dietPlans.values());
    }

    public void updateDietPlan(DietPlan plan) {
        dietPlans.put(plan.getId(), plan);
    }

    public void deleteDietPlan(int id) {
        dietPlans.remove(id);
    }
} 