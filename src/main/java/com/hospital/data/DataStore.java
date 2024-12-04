package com.hospital.data;

import com.hospital.models.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataStore {
    private static DataStore instance;
    private int nextId = 1;
    private Map<Integer, User> users;
    private Map<Integer, Patient> patients;
    private Map<Integer, Appointment> appointments;
    private Map<Integer, MedicalRecord> medicalRecords;
    private Map<Integer, DietPlan> dietPlans;
    private Map<Integer, Prescription> prescriptions;
    private Map<Integer, DietConsultation> dietConsultations;
    private Map<Integer, Medicine> medicines;
    private Map<Integer, MedicineOrder> medicineOrders;
    private Map<Integer, Supplier> suppliers;
    private Map<Integer, ITSupportTicket> tickets;
    private List<CommunityManager> communityManagers;
    private List<CommunityHealthMetric> healthMetrics;
    private List<VaccinationDrive> vaccinationDrives;

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
        medicines = new HashMap<>();
        medicineOrders = new HashMap<>();
        suppliers = new HashMap<>();
        tickets = new HashMap<>();
        communityManagers = new ArrayList<>();
        healthMetrics = new ArrayList<>();
        vaccinationDrives = new ArrayList<>();
        initializeDummyData();
    }

    private void initializeDummyData() {
        // Add dummy IT Support user
        ITSupport itSupport = new ITSupport(
            getNextId(), 
            "Alex Tech", 
            "alextech", 
            1, 
            "System Administration",
            "123-456-7890"
        );
        users.put(itSupport.getId(), itSupport);

        // Add some dummy tickets
        addTicket(new ITSupportTicket(getNextId(), 2, "Hardware", "Laptop not turning on", "HIGH"));
        addTicket(new ITSupportTicket(getNextId(), 3, "Software", "Email client not working", "MEDIUM"));
        addTicket(new ITSupportTicket(getNextId(), 4, "Network", "Unable to connect to printer", "LOW"));

        // Add dummy community manager
        CommunityManager communityManager = new CommunityManager(getNextId(), "John Community", "community1", 1, "North Region", "555-0123", 50000);
        users.put(communityManager.getId(), communityManager);
        communityManagers.add(communityManager);
        
        // Add dummy health metrics
        healthMetrics.addAll(Arrays.asList(
            new CommunityHealthMetric(1, "North Region", "Diabetes Rate", 8.5, "%", LocalDate.now(), "Monthly tracking"),
            new CommunityHealthMetric(2, "North Region", "Vaccination Rate", 75.0, "%", LocalDate.now(), "COVID-19 vaccination"),
            new CommunityHealthMetric(3, "North Region", "Blood Pressure Cases", 120, "cases", LocalDate.now(), "Hypertension tracking")
        ));

        // Add dummy vaccination drives
        vaccinationDrives.addAll(Arrays.asList(
            new VaccinationDrive(1, "North Region", "COVID-19 Booster", LocalDate.now(), LocalDate.now().plusMonths(1), 
                10000, 2500, "In Progress", "Targeting elderly population"),
            new VaccinationDrive(2, "North Region", "Flu Vaccine", LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2), 
                5000, 3000, "In Progress", "Annual flu vaccination drive")
        ));

        // Add sample medicines and suppliers
        Medicine med1 = new Medicine(getNextId(), "Paracetamol", 100, 5.99, LocalDate.now().plusYears(2), "Tablets", "Pain Relief");
        Medicine med2 = new Medicine(getNextId(), "Amoxicillin", 50, 15.99, LocalDate.now().plusYears(1), "Capsules", "Antibiotics");
        Medicine med3 = new Medicine(getNextId(), "Ibuprofen", 75, 7.99, LocalDate.now().plusYears(2), "Tablets", "Pain Relief");
        addMedicine(med1);
        addMedicine(med2);
        addMedicine(med3);

        Supplier sup1 = new Supplier(getNextId(), "PharmaCorp", "123-456-7890", "supplier@pharmacorp.com", "A");
        Supplier sup2 = new Supplier(getNextId(), "MediSupply", "098-765-4321", "contact@medisupply.com", "A");
        addSupplier(sup1);
        addSupplier(sup2);

        // Add sample orders
        MedicineOrder order1 = new MedicineOrder(getNextId(), sup1.getId(), LocalDate.now().minusDays(5), "DELIVERED");
        order1.addItem(med1.getId(), 50);
        order1.addItem(med2.getId(), 25);
        addMedicineOrder(order1);

        MedicineOrder order2 = new MedicineOrder(getNextId(), sup2.getId(), LocalDate.now().minusDays(2), "PENDING");
        order2.addItem(med3.getId(), 30);
        addMedicineOrder(order2);

        // Add sample users and patients
        addUser(new User(getNextId(), "John Doe", "johndoe", "DOCTOR", 1));
        addUser(new User(getNextId(), "Jane Smith", "janesmith", "PATIENT", 1));

        Patient patient = new Patient(2, "Jane Smith", 35, "None", LocalDate.now(),
                "123-456-7890", "jane@example.com", "123 Main St", "A+", "None",
                LocalDate.of(1988, 1, 1), "Female", "987-654-3210");
        addPatient(patient);

        // Add sample appointments
        Appointment appointment = new Appointment(getNextId(), 2, 1, LocalDateTime.now().plusDays(1),
                "Regular Checkup", "SCHEDULED", "First visit", "Annual physical", 30, "Room 101");
        addAppointment(appointment);

        // Add sample prescriptions
        Prescription prescription = new Prescription(getNextId(), 2, 1, "Hypertension",
                "Take medications as directed", "Monitor blood pressure daily");
        prescription.addMedication(new Prescription.Medication("Lisinopril", "10mg", 30, "Take once daily with water"));
        addPrescription(prescription);

        // Add sample medical records
        addMedicalRecord(new MedicalRecord(getNextId(), 2, 1, "Check-up", "Regular annual check-up"));

        // Add sample diet plans
        List<String> restrictions = Arrays.asList("Dairy", "Gluten");
        List<String> recommendations = Arrays.asList("Eat more vegetables", "Exercise daily");
        DietPlan plan = new DietPlan(getNextId(), 2, 3, LocalDate.now(), LocalDate.now().plusMonths(1),
                "Weight loss", restrictions, recommendations, "Initial plan", "ACTIVE");
        addDietPlan(plan);
    }

    public int getNextId() {
        return nextId++;
    }

    public void clear() {
        users.clear();
        patients.clear();
        appointments.clear();
        medicalRecords.clear();
        dietPlans.clear();
        prescriptions.clear();
        dietConsultations.clear();
        medicines.clear();
        medicineOrders.clear();
        suppliers.clear();
        tickets.clear();
        communityManagers.clear();
        healthMetrics.clear();
        vaccinationDrives.clear();
        nextId = 1;
    }

    // User methods
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
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

    // Patient methods
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

    // Appointment methods
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

    // Medical record methods
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

    // Diet plan methods
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

    // Prescription methods
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

    // Diet consultation methods
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

    // Medicine methods
    public void addMedicine(Medicine medicine) {
        medicines.put(medicine.getId(), medicine);
    }

    public Medicine getMedicine(int id) {
        return medicines.get(id);
    }

    public List<Medicine> getAllMedicines() {
        return new ArrayList<>(medicines.values());
    }

    public void updateMedicine(Medicine medicine) {
        medicines.put(medicine.getId(), medicine);
    }

    public void deleteMedicine(int id) {
        medicines.remove(id);
    }

    // Supplier methods
    public void addSupplier(Supplier supplier) {
        suppliers.put(supplier.getId(), supplier);
    }

    public Supplier getSupplier(int id) {
        return suppliers.get(id);
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    public void updateSupplier(Supplier supplier) {
        suppliers.put(supplier.getId(), supplier);
    }

    public void deleteSupplier(int id) {
        suppliers.remove(id);
    }

    // Medicine Order methods
    public void addMedicineOrder(MedicineOrder order) {
        medicineOrders.put(order.getId(), order);
    }

    public MedicineOrder getMedicineOrder(int id) {
        return medicineOrders.get(id);
    }

    public List<MedicineOrder> getAllMedicineOrders() {
        return new ArrayList<>(medicineOrders.values());
    }

    public void updateMedicineOrder(MedicineOrder order) {
        medicineOrders.put(order.getId(), order);
    }

    public void deleteMedicineOrder(int id) {
        medicineOrders.remove(id);
    }

    // IT Support Ticket methods
    public void addTicket(ITSupportTicket ticket) {
        tickets.put(ticket.getId(), ticket);
    }

    public ITSupportTicket getTicket(int id) {
        return tickets.get(id);
    }

    public List<ITSupportTicket> getAllTickets() {
        return new ArrayList<>(tickets.values());
    }

    public void updateTicket(ITSupportTicket ticket) {
        tickets.put(ticket.getId(), ticket);
    }

    public void deleteTicket(int id) {
        tickets.remove(id);
    }

    // Community Manager methods
    public List<CommunityManager> getCommunityManagers() {
        return new ArrayList<>(communityManagers);
    }

    public CommunityManager getCommunityManager(String username) {
        return communityManagers.stream()
                .filter(cm -> cm.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<CommunityHealthMetric> getHealthMetrics(String region) {
        return healthMetrics.stream()
                .filter(metric -> metric.getRegion().equals(region))
                .collect(Collectors.toList());
    }

    public List<VaccinationDrive> getVaccinationDrives(String region) {
        return vaccinationDrives.stream()
                .filter(drive -> drive.getRegion().equals(region))
                .collect(Collectors.toList());
    }

    public void addHealthMetric(CommunityHealthMetric metric) {
        metric.setId(healthMetrics.size() + 1);
        healthMetrics.add(metric);
    }

    public void addVaccinationDrive(VaccinationDrive drive) {
        drive.setId(vaccinationDrives.size() + 1);
        vaccinationDrives.add(drive);
    }

    public void updateVaccinationDrive(VaccinationDrive drive) {
        int index = IntStream.range(0, vaccinationDrives.size())
                .filter(i -> vaccinationDrives.get(i).getId() == drive.getId())
                .findFirst()
                .orElse(-1);
        if (index != -1) {
            vaccinationDrives.set(index, drive);
        }
    }

    // Dietician-specific methods
    public List<User> getPatientsForDietician(int dieticianId) {
        // For now, return all patients
        return users.values().stream()
                .filter(u -> "PATIENT".equals(u.getRole()))
                .collect(Collectors.toList());
    }
} 