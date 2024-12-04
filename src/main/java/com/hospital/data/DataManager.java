package com.hospital.data;

import com.hospital.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DataManager {
    private static final Logger logger = LoggerFactory.getLogger(DataManager.class);
    
    private final Map<Integer, BaseEntity> users = new ConcurrentHashMap<>();
    private final Map<Integer, Doctor> doctors = new ConcurrentHashMap<>();
    private final Map<Integer, Patient> patients = new ConcurrentHashMap<>();
    private final Map<Integer, Pharmacist> pharmacists = new ConcurrentHashMap<>();
    private final Map<Integer, ITSupport> itSupports = new ConcurrentHashMap<>();
    private final Map<Integer, Dietician> dieticians = new ConcurrentHashMap<>();
    private final Map<Integer, Hospital> hospitals = new ConcurrentHashMap<>();
    private final Map<Integer, PharmacyInventory> inventory = new ConcurrentHashMap<>();
    private final Map<Integer, MedicationTransfer> transfers = new ConcurrentHashMap<>();
    private final Map<Integer, Prescription> prescriptions = new ConcurrentHashMap<>();
    private final Map<Integer, Ticket> tickets = new ConcurrentHashMap<>();
    private final Map<Integer, Appointment> appointments = new ConcurrentHashMap<>();

    private static DataManager instance;

    private DataManager() {}

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void addUser(BaseEntity user) {
        logger.info("Adding user: {}", user.getName());
        users.put(user.getId(), user);
    }

    public Optional<BaseEntity> getUserById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<BaseEntity> getUserByUsername(String username) {
        return users.values().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst();
    }

    public List<BaseEntity> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void addDoctor(Doctor doctor) {
        logger.info("Adding doctor: {}", doctor.getName());
        doctors.put(doctor.getId(), doctor);
        users.put(doctor.getId(), doctor);
    }

    public Optional<Doctor> getDoctorById(int id) {
        return Optional.ofNullable(doctors.get(id));
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors.values());
    }

    public void addPatient(Patient patient) {
        logger.info("Adding patient: {} with ID: {} to patients map", patient.getName(), patient.getId());
        patients.put(patient.getId(), patient);
        logger.info("Adding patient: {} with ID: {} to users map", patient.getName(), patient.getId());
        users.put(patient.getId(), patient);
        logger.info("Successfully added patient to both maps");
    }

    public Optional<Patient> getPatientById(int id) {
        return Optional.ofNullable(patients.get(id));
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public List<Patient> getPatientsForDietician(int dieticianId) {
        logger.debug("Getting patients for dietician ID: {}", dieticianId);
        List<Patient> result = patients.values().stream()
            .filter(patient -> {
                logger.debug("Checking patient: {} with dieticianId: {}", patient.getName(), patient.getDieticianId());
                return patient.getDieticianId() == dieticianId;
            })
            .collect(Collectors.toList());
        logger.info("Found {} patients for dietician ID: {}", result.size(), dieticianId);
        return result;
    }

    public void addPharmacist(Pharmacist pharmacist) {
        logger.info("Adding pharmacist: {}", pharmacist.getName());
        pharmacists.put(pharmacist.getId(), pharmacist);
        users.put(pharmacist.getId(), pharmacist);
    }

    public Optional<Pharmacist> getPharmacistById(int id) {
        return Optional.ofNullable(pharmacists.get(id));
    }

    public void addITSupport(ITSupport itSupport) {
        logger.info("Adding IT support: {}", itSupport.getName());
        itSupports.put(itSupport.getId(), itSupport);
        users.put(itSupport.getId(), itSupport);
    }

    public Optional<ITSupport> getITSupportById(int id) {
        return Optional.ofNullable(itSupports.get(id));
    }

    public void addDietician(Dietician dietician) {
        logger.info("Adding dietician: {}", dietician.getName());
        dieticians.put(dietician.getId(), dietician);
        users.put(dietician.getId(), dietician);
    }

    public Optional<Dietician> getDieticianById(int id) {
        return Optional.ofNullable(dieticians.get(id));
    }

    public void addHospital(Hospital hospital) {
        logger.info("Adding hospital: {}", hospital.getName());
        hospitals.put(hospital.getId(), hospital);
    }

    public Optional<Hospital> getHospitalById(int id) {
        return Optional.ofNullable(hospitals.get(id));
    }

    public List<Hospital> getAllHospitals() {
        return new ArrayList<>(hospitals.values());
    }

    public void addInventoryItem(PharmacyInventory item) {
        logger.info("Adding inventory item: {}", item.getMedicationName());
        inventory.put(item.getId(), item);
    }

    public Optional<PharmacyInventory> getInventoryItemById(int id) {
        return Optional.ofNullable(inventory.get(id));
    }

    public List<PharmacyInventory> getInventoryForHospital(int hospitalId) {
        return inventory.values().stream()
            .filter(item -> item.getHospitalId() == hospitalId)
            .collect(Collectors.toList());
    }

    public void addTransfer(MedicationTransfer transfer) {
        logger.info("Adding medication transfer: {}", transfer.getMedicationName());
        transfers.put(transfer.getId(), transfer);
    }

    public Optional<MedicationTransfer> getTransferById(int id) {
        return Optional.ofNullable(transfers.get(id));
    }

    public List<MedicationTransfer> getTransfersForHospital(int hospitalId) {
        return transfers.values().stream()
            .filter(transfer -> transfer.getSourceHospitalId() == hospitalId || 
                              transfer.getDestinationHospitalId() == hospitalId)
            .collect(Collectors.toList());
    }

    public void addPrescription(Prescription prescription) {
        logger.info("Adding prescription for patient: {}", prescription.getPatientId());
        prescriptions.put(prescription.getId(), prescription);
    }

    public Optional<Prescription> getPrescriptionById(int id) {
        return Optional.ofNullable(prescriptions.get(id));
    }

    public List<Prescription> getPrescriptionsForHospital(int hospitalId) {
        return prescriptions.values().stream()
            .filter(prescription -> prescription.getHospitalId() == hospitalId)
            .collect(Collectors.toList());
    }

    public void addAppointment(Appointment appointment) {
        logger.info("Adding appointment: {}", appointment.getId());
        appointments.put(appointment.getId(), appointment);
    }

    public Optional<Appointment> getAppointmentById(int id) {
        return Optional.ofNullable(appointments.get(id));
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
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

    public void updateAppointment(Appointment appointment) {
        logger.info("Updating appointment: {}", appointment.getId());
        appointments.put(appointment.getId(), appointment);
    }

    public void addTicket(Ticket ticket) {
        logger.info("Adding ticket: {}", ticket.getTitle());
        tickets.put(ticket.getId(), ticket);
    }

    public Optional<Ticket> getTicketById(int id) {
        return Optional.ofNullable(tickets.get(id));
    }

    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets.values());
    }

    public List<Ticket> getTicketsAssignedTo(String username) {
        return tickets.values().stream()
            .filter(ticket -> ticket.getAssignedTo().equals(username))
            .collect(Collectors.toList());
    }

    public long countTicketsByStatus(String status) {
        return tickets.values().stream()
            .filter(ticket -> ticket.getStatus().equals(status))
            .count();
    }

    public long countTicketsResolvedToday() {
        return tickets.values().stream()
            .filter(ticket -> ticket.getStatus().equals("RESOLVED"))
            .filter(ticket -> ticket.getResolvedDate() != null && 
                            ticket.getResolvedDate().toLocalDate().equals(java.time.LocalDate.now()))
            .count();
    }

    public long countTicketsAssignedTo(String username) {
        return tickets.values().stream()
            .filter(ticket -> ticket.getAssignedTo().equals(username))
            .count();
    }

    public <T extends BaseEntity> void updateEntity(T entity) {
        logger.info("Updating entity: {}", entity.getName());
        if (entity instanceof Doctor) {
            doctors.put(entity.getId(), (Doctor) entity);
        } else if (entity instanceof Patient) {
            patients.put(entity.getId(), (Patient) entity);
        } else if (entity instanceof Pharmacist) {
            pharmacists.put(entity.getId(), (Pharmacist) entity);
        } else if (entity instanceof ITSupport) {
            itSupports.put(entity.getId(), (ITSupport) entity);
        } else if (entity instanceof Dietician) {
            dieticians.put(entity.getId(), (Dietician) entity);
        }
        users.put(entity.getId(), entity);
    }

    public void deleteEntity(int id) {
        logger.info("Deleting entity with ID: {}", id);
        BaseEntity entity = users.remove(id);
        if (entity != null) {
            if (entity instanceof Doctor) {
                doctors.remove(id);
            } else if (entity instanceof Patient) {
                patients.remove(id);
            } else if (entity instanceof Pharmacist) {
                pharmacists.remove(id);
            } else if (entity instanceof ITSupport) {
                itSupports.remove(id);
            } else if (entity instanceof Dietician) {
                dieticians.remove(id);
            }
        }
    }

    public int getNextId() {
        return users.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }

    public void clearAll() {
        logger.info("Clearing all data from DataManager");
        users.clear();
        doctors.clear();
        patients.clear();
        pharmacists.clear();
        itSupports.clear();
        dieticians.clear();
        hospitals.clear();
        appointments.clear();
        prescriptions.clear();
        tickets.clear();
    }

    public void initializeData() {
        logger.info("Clearing all data from DataManager");
        users.clear();
        doctors.clear();
        patients.clear();
        pharmacists.clear();
        itSupports.clear();
        dieticians.clear();
        hospitals.clear();
        appointments.clear();
        prescriptions.clear();
        tickets.clear();

        // Add a doctor
        Doctor doctor = new Doctor(1, "Dr. John Smith", "jsmith", "Cardiologist", 1, "12345");
        addDoctor(doctor);
        logger.info("Adding doctor: {}", doctor.getName());

        // Add a patient
        Patient patient = new Patient(2, "Jane Doe", "jdoe", 1, 5);  // Assign to dietician with ID 5
        patient.setGender("Female");
        patient.setContactNumber("555-0123");
        patient.setBloodGroup("A+");
        patient.setMedicalHistory("Regular checkups");
        patient.setAllergies("None");
        patient.setDateOfBirth("1990-01-01");
        addPatient(patient);

        // Add a pharmacist
        Pharmacist pharmacist = new Pharmacist(3, "Bob Wilson", "bwilson", 1, "PH789");
        addPharmacist(pharmacist);
        logger.info("Adding pharmacist: {}", pharmacist.getName());

        // Add IT Support
        ITSupport itSupport = new ITSupport(4, "Mike Brown", "mbrown", 1, "IT456");
        addITSupport(itSupport);
        logger.info("Adding IT support: {}", itSupport.getName());

        // Add a dietician
        Dietician dietician = new Dietician(5, "Sarah Johnson", "sjohnson", 1, "Nutrition", "DT123");
        addDietician(dietician);
        logger.info("Adding dietician: {}", dietician.getName());

        // Add a hospital
        Hospital hospital = new Hospital(1, "City General Hospital", "123 Main St", "555-0000");
        addHospital(hospital);
        logger.info("Adding hospital: {}", hospital.getName());
    }
} 