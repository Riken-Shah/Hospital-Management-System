package com.hospital.utils;

import com.hospital.model.*;
import com.hospital.data.DataManager;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;

public class DataStore {
    private static final DataManager dataManager = DataManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(DataStore.class);
    private static final Map<Integer, DietConsultation> consultations = new ConcurrentHashMap<>();
    private static final Map<Integer, Ticket> tickets = new ConcurrentHashMap<>();
    private static final Map<Integer, PharmacyInventory> inventory = new ConcurrentHashMap<>();
    private static int nextId = 1000;
    private static AtomicInteger ticketIdCounter = new AtomicInteger(1);
    private static Map<Integer, MedicationTransfer> medicationTransfers = new ConcurrentHashMap<>();
    private static Map<Integer, DietConsultation> dietConsultations = new ConcurrentHashMap<>();
    private static Map<Integer, DietPlan> dietPlans = new ConcurrentHashMap<>();

    public static Hospital getHospital(int id) {
        return dataManager.getHospitalById(id).orElse(null);
    }

    public static Doctor getDoctor(int id) {
        return dataManager.getDoctorById(id).orElse(null);
    }

    public static Patient getPatient(int id) {
        logger.info("Attempting to get patient with ID: {}", id);
        Patient patient = dataManager.getPatientById(id).orElse(null);
        if (patient == null) {
            logger.warn("No patient found with ID: {}", id);
        } else {
            logger.info("Successfully retrieved patient: {}", patient.getName());
        }
        return patient;
    }

    public static BaseEntity getUserById(int id) {
        return dataManager.getUserById(id).orElse(null);
    }

    public static List<BaseEntity> getAllUsers() {
        return dataManager.getAllUsers();
    }

    public static List<Doctor> getAllDoctors() {
        return dataManager.getAllDoctors();
    }

    public static List<Patient> getAllPatients() {
        return dataManager.getAllPatients();
    }

    public static List<Hospital> getAllHospitals() {
        return dataManager.getAllHospitals();
    }

    public static List<PharmacyInventory> getInventoryForHospital(int hospitalId) {
        return inventory.values().stream()
            .filter(item -> item.getHospitalId() == hospitalId)
            .collect(Collectors.toList());
    }

    public static void addInventoryItem(PharmacyInventory item) {
        logger.info("Adding inventory item: {}", item.getId());
        inventory.put(item.getId(), item);
    }

    public static PharmacyInventory getInventoryItem(int id) {
        return inventory.get(id);
    }

    public static List<MedicationTransfer> getTransfersForHospital(int hospitalId) {
        return dataManager.getTransfersForHospital(hospitalId);
    }

    public static void addTransfer(MedicationTransfer transfer) {
        dataManager.addTransfer(transfer);
    }

    public static List<Prescription> getPrescriptionsForHospital(int hospitalId) {
        return dataManager.getPrescriptionsForHospital(hospitalId);
    }

    public static List<Patient> getPatientsForDietician(int dieticianId) {
        logger.debug("Getting patients for dietician ID: {}", dieticianId);
        List<Patient> patients = DataManager.getInstance().getPatientsForDietician(dieticianId);
        logger.info("Found {} patients for dietician ID: {}", patients.size(), dieticianId);
        return patients;
    }

    public static List<Ticket> getAllTickets() {
        logger.debug("Fetching all tickets");
        List<Ticket> allTickets = new ArrayList<>(tickets.values());
        logger.debug("Found {} total tickets", allTickets.size());
        return allTickets;
    }

    public static List<Ticket> getTicketsAssignedTo(String username) {
        return dataManager.getTicketsAssignedTo(username);
    }

    public static long countTicketsByStatus(String status) {
        return dataManager.countTicketsByStatus(status);
    }

    public static long countTicketsResolvedToday() {
        return dataManager.countTicketsResolvedToday();
    }

    public static long countTicketsAssignedTo(String username) {
        return dataManager.countTicketsAssignedTo(username);
    }

    public static void updateUser(BaseEntity user) {
        dataManager.updateEntity(user);
    }

    public static void addUser(BaseEntity user) {
        dataManager.addUser(user);
    }

    public static void addDoctor(Doctor doctor) {
        dataManager.addDoctor(doctor);
    }

    public static void updateDoctor(Doctor doctor) {
        dataManager.updateEntity(doctor);
    }

    public static void addPatient(Patient patient) {
        dataManager.addPatient(patient);
    }

    public static void updatePatient(Patient patient) {
        dataManager.updateEntity(patient);
    }

    public static Optional<Appointment> getAppointmentById(int id) {
        return dataManager.getAppointmentById(id);
    }

    public static List<Appointment> getAllAppointments() {
        return dataManager.getAllAppointments();
    }

    public static List<Appointment> getAppointmentsForDoctor(int doctorId) {
        return dataManager.getAppointmentsForDoctor(doctorId);
    }

    public static List<Appointment> getAppointmentsForPatient(int patientId) {
        return dataManager.getAppointmentsForPatient(patientId);
    }

    public static void addAppointment(Appointment appointment) {
        dataManager.addAppointment(appointment);
    }

    public static void updateAppointment(Appointment appointment) {
        dataManager.updateAppointment(appointment);
    }

    public static int getNextId() {
        return ticketIdCounter.getAndIncrement();
    }

    public static Optional<Doctor> getDoctorByUserId(int userId) {
        return dataManager.getDoctorById(userId);
    }

    public static Optional<Patient> getPatientByUserId(int userId) {
        return dataManager.getPatientById(userId);
    }

    public static Optional<Pharmacist> getPharmacistByUserId(int userId) {
        return dataManager.getPharmacistById(userId);
    }

    public static Optional<ITSupport> getITSupportByUserId(int userId) {
        return dataManager.getITSupportById(userId);
    }

    public static Optional<Dietician> getDieticianByUserId(int userId) {
        return dataManager.getDieticianById(userId);
    }

    public static Pharmacist getPharmacist(int id) {
        return dataManager.getPharmacistById(id).orElse(null);
    }

    public static ITSupport getITSupport(int id) {
        return dataManager.getITSupportById(id).orElse(null);
    }

    public static Dietician getDietician(int id) {
        return dataManager.getDieticianById(id).orElse(null);
    }

    public static void addConsultation(DietConsultation consultation) {
        logger.info("Adding consultation: {}", consultation.getId());
        consultations.put(consultation.getId(), consultation);
    }

    public static DietConsultation getConsultation(String id) {
        return consultations.get(Integer.parseInt(id));
    }

    public static void updateConsultation(DietConsultation consultation) {
        logger.info("Updating consultation: {}", consultation.getId());
        consultations.put(consultation.getId(), consultation);
    }

    public static List<DietConsultation> getConsultationsForDietician(int dieticianId) {
        return consultations.values().stream()
            .filter(c -> c.getDieticianId() == dieticianId)
            .collect(Collectors.toList());
    }

    public static void addTicket(Ticket ticket) {
        logger.debug("Adding ticket: {}", ticket);
        if (ticket != null) {
            tickets.put(ticket.getId(), ticket);
            logger.info("Successfully added ticket with ID: {}", ticket.getId());
        } else {
            logger.error("Attempted to add null ticket");
        }
    }

    public static void updateTicket(Ticket ticket) {
        logger.debug("Updating ticket: {}", ticket);
        if (ticket != null && tickets.containsKey(ticket.getId())) {
            tickets.put(ticket.getId(), ticket);
            logger.info("Successfully updated ticket with ID: {}", ticket.getId());
        } else {
            logger.warn("Ticket with ID {} not found for update", ticket != null ? ticket.getId() : "null");
        }
    }

    public static void deleteTicket(int ticketId) {
        logger.debug("Deleting ticket with ID: {}", ticketId);
        if (tickets.remove(ticketId) != null) {
            logger.info("Successfully deleted ticket with ID: {}", ticketId);
        } else {
            logger.warn("Ticket with ID {} not found for deletion", ticketId);
        }
    }

    public static Ticket getTicket(int ticketId) {
        logger.debug("Fetching ticket with ID: {}", ticketId);
        Ticket ticket = tickets.get(ticketId);
        if (ticket != null) {
            logger.debug("Found ticket: {}", ticket);
        } else {
            logger.warn("Ticket with ID {} not found", ticketId);
        }
        return ticket;
    }

    public static List<Ticket> getTicketsForITSupport(int itSupportId) {
        logger.debug("Fetching tickets for IT Support ID: {}", itSupportId);
        List<Ticket> supportTickets = tickets.values().stream()
            .filter(t -> String.valueOf(itSupportId).equals(t.getAssignedTo()))
            .collect(Collectors.toList());
        logger.debug("Found {} tickets for IT Support ID: {}", supportTickets.size(), itSupportId);
        return supportTickets;
    }

    public static List<Ticket> getTicketsByStatus(String status) {
        return tickets.values().stream()
            .filter(t -> t.getStatus().equals(status))
            .collect(Collectors.toList());
    }

    public static List<Ticket> getTicketsByPriority(String priority) {
        return tickets.values().stream()
            .filter(t -> t.getPriority().equals(priority))
            .collect(Collectors.toList());
    }

    public static List<Ticket> getTicketsByCategory(String category) {
        return tickets.values().stream()
            .filter(t -> t.getCategory().equals(category))
            .collect(Collectors.toList());
    }

    public static void addMedicationTransfer(MedicationTransfer transfer) {
        logger.debug("Adding medication transfer: {}", transfer);
        medicationTransfers.put(transfer.getId(), transfer);
        logger.info("Successfully added medication transfer with ID: {}", transfer.getId());
    }

    public static void updateMedicationTransfer(MedicationTransfer transfer) {
        logger.debug("Updating medication transfer: {}", transfer);
        if (medicationTransfers.containsKey(transfer.getId())) {
            medicationTransfers.put(transfer.getId(), transfer);
            logger.info("Successfully updated medication transfer with ID: {}", transfer.getId());
        } else {
            logger.warn("Medication transfer with ID {} not found for update", transfer.getId());
        }
    }

    public static void deleteMedicationTransfer(int transferId) {
        logger.debug("Deleting medication transfer with ID: {}", transferId);
        if (medicationTransfers.remove(transferId) != null) {
            logger.info("Successfully deleted medication transfer with ID: {}", transferId);
        } else {
            logger.warn("Medication transfer with ID {} not found for deletion", transferId);
        }
    }

    public static MedicationTransfer getMedicationTransfer(int transferId) {
        logger.debug("Fetching medication transfer with ID: {}", transferId);
        MedicationTransfer transfer = medicationTransfers.get(transferId);
        if (transfer != null) {
            logger.debug("Found medication transfer: {}", transfer);
        } else {
            logger.warn("Medication transfer with ID {} not found", transferId);
        }
        return transfer;
    }

    public static List<MedicationTransfer> getMedicationTransfersForPharmacist(int pharmacistId) {
        logger.debug("Fetching medication transfers for pharmacist ID: {}", pharmacistId);
        Pharmacist pharmacist = getPharmacist(pharmacistId);
        if (pharmacist == null) {
            logger.warn("Pharmacist with ID {} not found", pharmacistId);
            return new ArrayList<>();
        }

        List<MedicationTransfer> transfers = medicationTransfers.values().stream()
            .filter(t -> t.getSourceHospitalId() == pharmacist.getHospitalId() || 
                        t.getDestinationHospitalId() == pharmacist.getHospitalId())
            .collect(Collectors.toList());
        
        logger.debug("Found {} transfers for pharmacist ID: {}", transfers.size(), pharmacistId);
        return transfers;
    }

    public static List<MedicationTransfer> getAllMedicationTransfers() {
        logger.debug("Fetching all medication transfers");
        List<MedicationTransfer> transfers = new ArrayList<>(medicationTransfers.values());
        logger.debug("Found {} total transfers", transfers.size());
        return transfers;
    }

    public static void addDietConsultation(DietConsultation consultation) {
        logger.info("Adding diet consultation for patient: {}", consultation.getPatientId());
        dietConsultations.put(consultation.getId(), consultation);
    }

    public static DietConsultation getDietConsultation(int id) {
        logger.debug("Getting diet consultation with ID: {}", id);
        DietConsultation consultation = dietConsultations.get(id);
        if (consultation != null) {
            logger.info("Successfully retrieved diet consultation for patient: {}", consultation.getPatientId());
        } else {
            logger.warn("Diet consultation not found for ID: {}", id);
        }
        return consultation;
    }

    public static void updateDietConsultation(DietConsultation consultation) {
        logger.info("Updating diet consultation for patient: {}", consultation.getPatientId());
        dietConsultations.put(consultation.getId(), consultation);
    }

    public static void deleteDietConsultation(int id) {
        logger.info("Deleting diet consultation with ID: {}", id);
        dietConsultations.remove(id);
    }

    public static List<DietConsultation> getDietConsultationsForDietician(int dieticianId) {
        logger.debug("Getting diet consultations for dietician ID: {}", dieticianId);
        List<DietConsultation> consultations = dietConsultations.values().stream()
            .filter(consultation -> consultation.getDieticianId() == dieticianId)
            .collect(Collectors.toList());
        logger.info("Found {} diet consultations for dietician ID: {}", consultations.size(), dieticianId);
        return consultations;
    }

    public static void addDietPlan(DietPlan plan) {
        logger.info("Adding diet plan: {} for patient: {}", plan.getPlanName(), plan.getPatientId());
        dietPlans.put(plan.getId(), plan);
    }

    public static DietPlan getDietPlan(int id) {
        logger.debug("Getting diet plan with ID: {}", id);
        DietPlan plan = dietPlans.get(id);
        if (plan != null) {
            logger.info("Successfully retrieved diet plan: {}", plan.getPlanName());
        } else {
            logger.warn("Diet plan not found for ID: {}", id);
        }
        return plan;
    }

    public static void updateDietPlan(DietPlan plan) {
        logger.info("Updating diet plan: {} for patient: {}", plan.getPlanName(), plan.getPatientId());
        dietPlans.put(plan.getId(), plan);
    }

    public static void deleteDietPlan(int id) {
        logger.info("Deleting diet plan with ID: {}", id);
        dietPlans.remove(id);
    }

    public static List<DietPlan> getDietPlansForDietician(int dieticianId) {
        logger.debug("Getting diet plans for dietician ID: {}", dieticianId);
        List<DietPlan> plans = dietPlans.values().stream()
            .filter(plan -> plan.getDieticianId() == dieticianId)
            .collect(Collectors.toList());
        logger.info("Found {} diet plans for dietician ID: {}", plans.size(), dieticianId);
        return plans;
    }

    public static List<DietConsultation> getAllDietConsultations() {
        logger.debug("Fetching all diet consultations");
        List<DietConsultation> consultations = new ArrayList<>(dietConsultations.values());
        logger.debug("Found {} total consultations", consultations.size());
        return consultations;
    }

    public static List<DietConsultation> getDietConsultationsForPatient(int patientId) {
        logger.debug("Fetching diet consultations for patient ID: {}", patientId);
        List<DietConsultation> consultations = dietConsultations.values().stream()
            .filter(c -> c.getPatientId() == patientId)
            .collect(Collectors.toList());
        logger.debug("Found {} consultations for patient ID: {}", consultations.size(), patientId);
        return consultations;
    }

    public static List<DietPlan> getDietPlansForPatient(int patientId) {
        logger.debug("Fetching diet plans for patient ID: {}", patientId);
        List<DietPlan> plans = dietPlans.values().stream()
            .filter(p -> p.getPatientId() == patientId)
            .collect(Collectors.toList());
        logger.debug("Found {} diet plans for patient ID: {}", plans.size(), patientId);
        return plans;
    }

    public static List<DietPlan> getAllDietPlans() {
        logger.debug("Fetching all diet plans");
        List<DietPlan> allPlans = new ArrayList<>(dietPlans.values());
        logger.debug("Found {} total diet plans", allPlans.size());
        return allPlans;
    }

    public static void updateInventoryItem(PharmacyInventory item) {
        logger.debug("Updating inventory item: {}", item);
        if (item != null && inventory.containsKey(item.getId())) {
            inventory.put(item.getId(), item);
            logger.info("Successfully updated inventory item with ID: {}", item.getId());
        } else {
            logger.warn("Inventory item with ID {} not found for update", item != null ? item.getId() : "null");
        }
    }
} 