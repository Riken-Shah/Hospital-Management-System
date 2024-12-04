package com.hospital.data;

import com.hospital.models.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataStore {
    private static DataStore instance;
    private static int nextId = 1;

    private Map<Integer, User> users;
    private Map<Integer, Patient> patients;
    private Map<Integer, Appointment> appointments;
    private Map<Integer, MedicalRecord> medicalRecords;
    private Map<Integer, DietPlan> dietPlans;
    private Map<Integer, Prescription> prescriptions;
    private Map<Integer, DietConsultation> dietConsultations;

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private DataStore() {
        users = new HashMap<>();
        patients = new HashMap<>();
        appointments = new HashMap<>();
        medicalRecords = new HashMap<>();
        dietPlans = new HashMap<>();
        prescriptions = new HashMap<>();
        dietConsultations = new HashMap<>();
        addSampleData();
    }

    private void addSampleData() {
        // Add sample users
        addUser(new User(getNextId(), "John Doe", "johndoe", "DOCTOR", 1));
        addUser(new User(getNextId(), "Jane Smith", "janesmith", "PATIENT", 1));
        addUser(new User(getNextId(), "Alice Brown", "alicebrown", "DIETICIAN", 1));

        // Add sample patients
        Patient patient = new Patient(2, "Jane Smith", 35, "None", LocalDate.now(),
                "123-456-7890", "jane@example.com", "123 Main St", "A+", "None",
                LocalDate.of(1988, 1, 1), "Female", "987-654-3210");
        addPatient(patient);

        // Add sample appointments
        Appointment appointment = new Appointment(getNextId(), 2, 1, LocalDateTime.now().plusDays(1),
                "Regular Checkup", "SCHEDULED", "First visit", "Annual physical", 30, "Room 101");
        addAppointment(appointment);

        // Add sample prescriptions
        Prescription prescription1 = new Prescription(getNextId(), 2, 1, "Hypertension",
                "Take medications as directed", "Monitor blood pressure daily");
        prescription1.addMedication(new Prescription.Medication("Lisinopril", "10mg", 30, "Take once daily with water"));
        prescription1.addMedication(new Prescription.Medication("Aspirin", "81mg", 30, "Take once daily with food"));
        addPrescription(prescription1);

        Prescription prescription2 = new Prescription(getNextId(), 2, 1, "Seasonal Allergies",
                "Take as needed", "Avoid known allergens");
        prescription2.addMedication(new Prescription.Medication("Cetirizine", "10mg", 90, "Take once daily"));
        prescription2.addMedication(new Prescription.Medication("Nasal Spray", "2 sprays per nostril", 30, "Use twice daily"));
        addPrescription(prescription2);

        // Add sample medical records
        addMedicalRecord(new MedicalRecord(getNextId(), 2, 1, "Check-up", "Regular annual check-up"));

        // Add sample diet plans
        List<String> restrictions = Arrays.asList("Dairy", "Gluten");
        List<String> recommendations = Arrays.asList("Eat more vegetables", "Exercise daily");
        DietPlan plan = new DietPlan(getNextId(), 2, 3, LocalDate.now(), LocalDate.now().plusMonths(1),
                "Weight loss", restrictions, recommendations, "Initial plan", "ACTIVE");
        addDietPlan(plan);
    }

    public void clear() {
        users.clear();
        patients.clear();
        appointments.clear();
        medicalRecords.clear();
        dietPlans.clear();
        prescriptions.clear();
        dietConsultations.clear();
        nextId = 1;
    }

    public static synchronized int getNextId() {
        return nextId++;
    }

    // User operations
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public User getUserByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public List<User> getDoctors() {
        return users.values().stream()
                .filter(user -> "DOCTOR".equals(user.getRole()))
                .collect(Collectors.toList());
    }

    // Patient operations
    public void addPatient(Patient patient) {
        patients.put(patient.getId(), patient);
    }

    public Patient getPatient(int id) {
        return patients.get(id);
    }

    public void updatePatient(Patient patient) {
        patients.put(patient.getId(), patient);
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    // Appointment operations
    public void addAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    public Appointment getAppointment(int id) {
        return appointments.get(id);
    }

    public void updateAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    // Medical record operations
    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.put(record.getId(), record);
    }

    public MedicalRecord getMedicalRecord(int id) {
        return medicalRecords.get(id);
    }

    public List<MedicalRecord> getMedicalRecords(int patientId) {
        return medicalRecords.values().stream()
                .filter(r -> r.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public List<MedicalRecord> getMedicalRecordsByPatientId(int patientId) {
        return medicalRecords.values().stream()
                .filter(record -> record.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    // Diet plan operations
    public void addDietPlan(DietPlan plan) {
        dietPlans.put(plan.getId(), plan);
    }

    public DietPlan getDietPlan(int id) {
        return dietPlans.get(id);
    }

    public void updateDietPlan(DietPlan plan) {
        dietPlans.put(plan.getId(), plan);
    }

    public void deleteDietPlan(int id) {
        dietPlans.remove(id);
    }

    public List<DietPlan> getDietPlansForDietician(int dieticianId) {
        return dietPlans.values().stream()
                .filter(plan -> plan.getDieticianId() == dieticianId)
                .collect(Collectors.toList());
    }

    // Prescription operations
    public void addPrescription(Prescription prescription) {
        prescriptions.put(prescription.getId(), prescription);
    }

    public Prescription getPrescription(int id) {
        return prescriptions.get(id);
    }

    public void updatePrescription(Prescription prescription) {
        prescriptions.put(prescription.getId(), prescription);
    }

    public void deletePrescription(int id) {
        prescriptions.remove(id);
    }

    public List<Prescription> getPrescriptionsByDoctorId(int doctorId) {
        return prescriptions.values().stream()
                .filter(prescription -> prescription.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<Prescription> getPrescriptionsByPatientId(int patientId) {
        return prescriptions.values().stream()
                .filter(prescription -> prescription.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions.values());
    }

    // Dietician-specific operations
    public List<User> getPatientsForDietician(int dieticianId) {
        // For now, return all patients with PATIENT role
        // TODO: Implement proper patient-dietician relationship
        return users.values().stream()
                .filter(u -> "PATIENT".equals(u.getRole()))
                .collect(Collectors.toList());
    }

    public List<DietConsultation> getDietConsultationsForDietician(int dieticianId) {
        return dietConsultations.values().stream()
                .filter(consultation -> consultation.getDieticianId() == dieticianId)
                .collect(Collectors.toList());
    }

    public void addDietConsultation(DietConsultation consultation) {
        dietConsultations.put(consultation.getId(), consultation);
    }

    public DietConsultation getDietConsultation(int id) {
        return dietConsultations.get(id);
    }

    public void updateDietConsultation(DietConsultation consultation) {
        dietConsultations.put(consultation.getId(), consultation);
    }

    public void deleteDietConsultation(int id) {
        dietConsultations.remove(id);
    }

    public List<DietConsultation> getDietConsultationsForPatient(int patientId) {
        return dietConsultations.values().stream()
                .filter(consultation -> consultation.getPatientId() == patientId)
                .collect(Collectors.toList());
    }
} 