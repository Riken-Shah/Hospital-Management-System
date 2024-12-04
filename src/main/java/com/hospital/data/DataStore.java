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
    private Map<Integer, Hospital> hospitals;
    private Map<Integer, Community> communities;
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
        hospitals = new HashMap<>();
        communities = new HashMap<>();
        communityManagers = new ArrayList<>();
        healthMetrics = new ArrayList<>();
        vaccinationDrives = new ArrayList<>();
        initializeDummyData();
    }

    private void initializeDummyData() {
        // Add dummy hospital
        Hospital hospital = new Hospital(getNextId(), "City General Hospital", "123 Main St", "555-0000", "city.general@hospital.com");
        hospitals.put(hospital.getId(), hospital);

        // Add dummy community
        Community community = new Community(getNextId(), "Downtown Community", "Downtown Area", 50000, 1);
        communities.put(community.getId(), community);

        // Add dummy users
        User admin = new User(getNextId(), "Admin User", "admin", "ADMIN", 1);
        users.put(admin.getId(), admin);

        User doctor = new User(getNextId(), "Dr. John Smith", "doctor1", "DOCTOR", 1);
        users.put(doctor.getId(), doctor);

        // Add dummy patient
        Patient patient = new Patient(getNextId(), "Jane Doe", "patient1", 1);
        patients.put(patient.getId(), patient);

        // Add dummy appointments
        Appointment appointment1 = new Appointment(1, 1, 2, LocalDateTime.now().plusDays(1), "Checkup", 
            "SCHEDULED", "Regular checkup", "", 30, "Room 101");
        appointments.put(appointment1.getId(), appointment1);

        // Add dummy medical records
        MedicalRecord record1 = new MedicalRecord(1, 1, 2, "Checkup", "Regular health checkup");
        medicalRecords.put(record1.getId(), record1);

        // Add dummy diet plan
        List<String> restrictions = Arrays.asList("Nuts", "Dairy");
        List<String> recommendations = Arrays.asList("More vegetables", "Less sugar");
        DietPlan plan = new DietPlan(getNextId(), 2, 3, LocalDate.now(), LocalDate.now().plusMonths(1),
                "Weight loss", restrictions, recommendations, "Initial plan", "ACTIVE");
        addDietPlan(plan);

        // Add dummy tickets
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
    }

    // Hospital methods
    public void addHospital(Hospital hospital) {
        hospitals.put(hospital.getId(), hospital);
    }

    public Hospital getHospital(int id) {
        return hospitals.get(id);
    }

    public List<Hospital> getAllHospitals() {
        return new ArrayList<>(hospitals.values());
    }

    public void updateHospital(Hospital hospital) {
        hospitals.put(hospital.getId(), hospital);
    }

    public void deleteHospital(int id) {
        hospitals.remove(id);
    }

    // Community methods
    public void addCommunity(Community community) {
        communities.put(community.getId(), community);
    }

    public Community getCommunity(int id) {
        return communities.get(id);
    }

    public List<Community> getAllCommunities() {
        return new ArrayList<>(communities.values());
    }

    public void updateCommunity(Community community) {
        communities.put(community.getId(), community);
    }

    public void deleteCommunity(int id) {
        communities.remove(id);
    }

    // Existing methods...
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
        hospitals.clear();
        communities.clear();
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

    public User getUserByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

    // Patient methods
    public void addPatient(Patient patient) {
        patients.put(patient.getId(), patient);
    }

    public Patient getPatient(int id) {
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

    // Appointment methods
    public void addAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    public Appointment getAppointment(int id) {
        return appointments.get(id);
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsForPatient(int patientId) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    // Medical record methods
    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.put(record.getId(), record);
    }

    public MedicalRecord getMedicalRecord(int id) {
        return medicalRecords.get(id);
    }

    public List<MedicalRecord> getMedicalRecordsForPatient(int patientId) {
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

    public List<Prescription> getPrescriptionsForPatient(int patientId) {
        return prescriptions.values().stream()
                .filter(prescription -> prescription.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions.values());
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

    // Medicine order methods
    public void addMedicineOrder(MedicineOrder order) {
        medicineOrders.put(order.getId(), order);
    }

    public MedicineOrder getMedicineOrder(int id) {
        return medicineOrders.get(id);
    }

    public List<MedicineOrder> getAllMedicineOrders() {
        return new ArrayList<>(medicineOrders.values());
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

    // IT Support ticket methods
    public void addTicket(ITSupportTicket ticket) {
        tickets.put(ticket.getId(), ticket);
    }

    public ITSupportTicket getTicket(int id) {
        return tickets.get(id);
    }

    public List<ITSupportTicket> getAllTickets() {
        return new ArrayList<>(tickets.values());
    }

    public List<ITSupportTicket> getTicketsForUser(int userId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public void updateTicket(ITSupportTicket ticket) {
        tickets.put(ticket.getId(), ticket);
    }

    public void deleteTicket(int id) {
        tickets.remove(id);
    }

    // Community manager methods
    public void addCommunityManager(CommunityManager manager) {
        communityManagers.add(manager);
        users.put(manager.getId(), manager);
    }

    public List<CommunityManager> getAllCommunityManagers() {
        return new ArrayList<>(communityManagers);
    }

    public CommunityManager getCommunityManagerByRegion(String region) {
        return communityManagers.stream()
                .filter(manager -> manager.getRegion().equals(region))
                .findFirst()
                .orElse(null);
    }

    // Health metrics methods
    public void addHealthMetric(CommunityHealthMetric metric) {
        healthMetrics.add(metric);
    }

    public List<CommunityHealthMetric> getAllHealthMetrics() {
        return new ArrayList<>(healthMetrics);
    }

    public List<CommunityHealthMetric> getHealthMetricsForRegion(String region) {
        return healthMetrics.stream()
                .filter(metric -> metric.getRegion().equals(region))
                .collect(Collectors.toList());
    }

    public List<CommunityHealthMetric> getHealthMetrics(String region) {
        return healthMetrics.stream()
                .filter(metric -> metric.getRegion().equals(region))
                .collect(Collectors.toList());
    }

    // Vaccination drive methods
    public void addVaccinationDrive(VaccinationDrive drive) {
        vaccinationDrives.add(drive);
    }

    public List<VaccinationDrive> getAllVaccinationDrives() {
        return new ArrayList<>(vaccinationDrives);
    }

    public List<VaccinationDrive> getVaccinationDrivesForRegion(String region) {
        return vaccinationDrives.stream()
                .filter(drive -> drive.getRegion().equals(region))
                .collect(Collectors.toList());
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

    public List<VaccinationDrive> getVaccinationDrives(String region) {
        return vaccinationDrives.stream()
                .filter(drive -> drive.getRegion().equals(region))
                .collect(Collectors.toList());
    }

    public List<User> getPatientsForDietician(int dieticianId) {
        return users.values().stream()
                .filter(user -> "PATIENT".equals(user.getRole()))
                .collect(Collectors.toList());
    }

    public List<DietConsultation> getDietConsultationsForDietician(int dieticianId) {
        return dietConsultations.values().stream()
                .filter(consultation -> consultation.getDieticianId() == dieticianId)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public List<Prescription> getPrescriptionsByPatientId(int patientId) {
        return prescriptions.values().stream()
                .filter(prescription -> prescription.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public List<MedicalRecord> getMedicalRecordsByPatientId(int patientId) {
        return medicalRecords.values().stream()
                .filter(record -> record.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public List<User> getDoctors() {
        return users.values().stream()
                .filter(user -> "DOCTOR".equals(user.getRole()))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<MedicalRecord> getMedicalRecords(int patientId) {
        return getMedicalRecordsByPatientId(patientId);
    }

    public List<Prescription> getPrescriptionsByDoctorId(int doctorId) {
        return prescriptions.values().stream()
                .filter(prescription -> prescription.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    public void updateAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }
} 