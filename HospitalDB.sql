-- Create the HospitalDB database (optional, since databases in PostgreSQL are usually created separately)
-- CREATE DATABASE HospitalDB;

-- Doctors Table
CREATE TABLE Doctors (
    DoctorID SERIAL PRIMARY KEY,
    DoctorName VARCHAR(100),
    Specialty VARCHAR(100),
    Phone VARCHAR(20)
);

-- Patients Table
CREATE TABLE Patients (
    PatientID SERIAL PRIMARY KEY,
    PatientName VARCHAR(100),
    Age INT,
    Gender VARCHAR(10),
    Phone VARCHAR(20)
);

-- Appointments Table
CREATE TABLE Appointments (
    AppointmentID SERIAL PRIMARY KEY,
    DoctorID INT REFERENCES Doctors(DoctorID) ON DELETE CASCADE,
    PatientID INT REFERENCES Patients(PatientID) ON DELETE CASCADE,
    AppointmentDate DATE,
    Status VARCHAR(50)
);

-- Billing Table
CREATE TABLE Billing (
    BillID SERIAL PRIMARY KEY,
    AppointmentID INT REFERENCES Appointments(AppointmentID) ON DELETE CASCADE,
    TotalAmount DECIMAL(10, 2)
);

-- Feedback Table
CREATE TABLE Feedback (
    FeedbackID SERIAL PRIMARY KEY,
    DoctorID INT REFERENCES Doctors(DoctorID) ON DELETE CASCADE,
    PatientID INT REFERENCES Patients(PatientID) ON DELETE CASCADE,
    Rating INT CHECK (Rating >= 1 AND Rating <= 5),
    Comments VARCHAR(500)
);

-- Inserting Doctors
INSERT INTO Doctors (DoctorName, Specialty, Phone)
VALUES 
('Dr. Smith', 'Cardiologist', '123-456-7890'),
('Dr. Johnson', 'Neurologist', '987-654-3210');

-- Inserting Patients
INSERT INTO Patients (PatientName, Age, Gender, Phone)
VALUES 
('John Doe', 30, 'Male', '555-555-5555'),
('Jane Doe', 25, 'Female', '666-666-6666');

-- Insert New Appointment
INSERT INTO Appointments (DoctorID, PatientID, AppointmentDate, Status)
VALUES (1, 1, '2024-10-10', 'Scheduled');

-- Update Appointment Status
UPDATE Appointments
SET Status = 'Completed'
WHERE AppointmentID = 1;

-- Insert Billing Record
INSERT INTO Billing (AppointmentID, TotalAmount)
VALUES (1, 500);

-- Insert Feedback
INSERT INTO Feedback (DoctorID, PatientID, Rating, Comments)
VALUES (1, 1, 5, 'Excellent service and consultation');

-- View All Appointments
SELECT * FROM Appointments;

-- View Doctor Feedback
SELECT D.DoctorName, F.Rating, F.Comments
FROM Feedback F
JOIN Doctors D ON F.DoctorID = D.DoctorID;

-- Generate Billing Report
SELECT P.PatientName, B.TotalAmount
FROM Billing B
JOIN Appointments A ON B.AppointmentID = A.AppointmentID
JOIN Patients P ON A.PatientID = P.PatientID;