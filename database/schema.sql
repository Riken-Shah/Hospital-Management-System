CREATE DATABASE hospital_management_system;

USE hospital_management_system;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE hospitals (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    contact_number VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE doctors (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    hospital_id INT,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    contact_number VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

CREATE TABLE patients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    contact_number VARCHAR(20),
    address VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE appointments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT,
    patient_id INT,
    appointment_date DATETIME,
    status VARCHAR(20),
    notes TEXT,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE TABLE communities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    population INT
);

CREATE TABLE medications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    stock_quantity INT,
    hospital_id INT,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

CREATE TABLE lab_tests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT,
    test_name VARCHAR(100),
    test_date DATETIME,
    results TEXT,
    technician_id INT,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (technician_id) REFERENCES users(id)
);

CREATE TABLE diet_plans (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT,
    dietician_id INT,
    diet_description TEXT,
    start_date DATE,
    end_date DATE,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (dietician_id) REFERENCES users(id)
);

CREATE TABLE it_support_tickets (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20),
    priority VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE medication_transfers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    from_hospital_id INT,
    to_hospital_id INT,
    medication_id INT,
    quantity INT,
    transfer_date TIMESTAMP,
    status VARCHAR(20),
    FOREIGN KEY (from_hospital_id) REFERENCES hospitals(id),
    FOREIGN KEY (to_hospital_id) REFERENCES hospitals(id),
    FOREIGN KEY (medication_id) REFERENCES medications(id)
);

CREATE TABLE pharmacy_inventory (
    id INT PRIMARY KEY AUTO_INCREMENT,
    medication_id INT,
    hospital_id INT,
    batch_number VARCHAR(50),
    expiry_date DATE,
    quantity INT,
    unit_price DECIMAL(10,2),
    FOREIGN KEY (medication_id) REFERENCES medications(id),
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

CREATE TABLE community_health_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    community_id INT,
    record_date DATE,
    health_metric VARCHAR(100),
    value TEXT,
    recorded_by INT,
    FOREIGN KEY (community_id) REFERENCES communities(id),
    FOREIGN KEY (recorded_by) REFERENCES users(id)
); 