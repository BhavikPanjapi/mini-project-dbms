import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HospitalManagementSystem extends JFrame {

    private Connection conn;

    // GUI Components
    private JTextField doctorNameField, specialtyField, doctorPhoneField;
    private JTextField patientNameField, ageField, genderField, patientPhoneField;
    private JTextField appointmentDoctorIdField, appointmentPatientIdField, appointmentDateField, appointmentStatusField;
    private JTextField billingAppointmentIdField, totalAmountField;
    private JTextArea viewArea; // Text area to display view information

    public HospitalManagementSystem() {
        // Set up the database connection
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HospitalDB", "postgres", "Bhavik@321");
            System.out.println("Database connection successful.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to database.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        setTitle("Hospital Management System");
        setSize(800, 600);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Doctors Tab
        JPanel doctorPanel = new JPanel(new GridLayout(5, 2));
        doctorNameField = new JTextField();
        specialtyField = new JTextField();
        doctorPhoneField = new JTextField();
        JButton addDoctorButton = new JButton("Add Doctor");
        doctorPanel.add(new JLabel("Doctor Name:"));
        doctorPanel.add(doctorNameField);
        doctorPanel.add(new JLabel("Specialty:"));
        doctorPanel.add(specialtyField);
        doctorPanel.add(new JLabel("Phone:"));
        doctorPanel.add(doctorPhoneField);
        doctorPanel.add(addDoctorButton);
        tabbedPane.addTab("Doctors", doctorPanel);

        // Patients Tab
        JPanel patientPanel = new JPanel(new GridLayout(6, 2));
        patientNameField = new JTextField();
        ageField = new JTextField();
        genderField = new JTextField();
        patientPhoneField = new JTextField();
        JButton addPatientButton = new JButton("Add Patient");
        patientPanel.add(new JLabel("Patient Name:"));
        patientPanel.add(patientNameField);
        patientPanel.add(new JLabel("Age:"));
        patientPanel.add(ageField);
        patientPanel.add(new JLabel("Gender:"));
        patientPanel.add(genderField);
        patientPanel.add(new JLabel("Phone:"));
        patientPanel.add(patientPhoneField);
        patientPanel.add(addPatientButton);
        tabbedPane.addTab("Patients", patientPanel);

        // Appointments Tab
        JPanel appointmentPanel = new JPanel(new GridLayout(5, 2));
        appointmentDoctorIdField = new JTextField();
        appointmentPatientIdField = new JTextField();
        appointmentDateField = new JTextField();
        appointmentStatusField = new JTextField();
        JButton addAppointmentButton = new JButton("Add Appointment");
        appointmentPanel.add(new JLabel("Doctor ID:"));
        appointmentPanel.add(appointmentDoctorIdField);
        appointmentPanel.add(new JLabel("Patient ID:"));
        appointmentPanel.add(appointmentPatientIdField);
        appointmentPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        appointmentPanel.add(appointmentDateField);
        appointmentPanel.add(new JLabel("Status:"));
        appointmentPanel.add(appointmentStatusField);
        appointmentPanel.add(addAppointmentButton);
        tabbedPane.addTab("Appointments", appointmentPanel);

        // Billing Tab
        JPanel billingPanel = new JPanel(new GridLayout(4, 2));
        billingAppointmentIdField = new JTextField();
        totalAmountField = new JTextField();
        JButton addBillingButton = new JButton("Add Billing");
        billingPanel.add(new JLabel("Appointment ID:"));
        billingPanel.add(billingAppointmentIdField);
        billingPanel.add(new JLabel("Total Amount:"));
        billingPanel.add(totalAmountField);
        billingPanel.add(addBillingButton);
        tabbedPane.addTab("Billing", billingPanel);

        // View Tab
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(viewArea);
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        JButton refreshButton = new JButton("Refresh View");
        viewPanel.add(refreshButton, BorderLayout.SOUTH);
        tabbedPane.addTab("View", viewPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Action listeners for adding records
        addDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDoctor();
                refreshView();
            }
        });

        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
                refreshView();
            }
        });

        addAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAppointment();
                refreshView();
            }
        });

        addBillingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBilling();
                refreshView();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshView();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addDoctor() {
        try {
            String query = "INSERT INTO Doctors (DoctorName, Specialty, Phone) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, doctorNameField.getText());
            pstmt.setString(2, specialtyField.getText());
            pstmt.setString(3, doctorPhoneField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addPatient() {
        try {
            String query = "INSERT INTO Patients (PatientName, Age, Gender, Phone) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, patientNameField.getText());
            pstmt.setInt(2, Integer.parseInt(ageField.getText()));
            pstmt.setString(3, genderField.getText());
            pstmt.setString(4, patientPhoneField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding patient.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addAppointment() {
        try {
            String query = "INSERT INTO Appointments (DoctorID, PatientID, AppointmentDate, Status) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(appointmentDoctorIdField.getText()));
            pstmt.setInt(2, Integer.parseInt(appointmentPatientIdField.getText()));
            pstmt.setDate(3, Date.valueOf(appointmentDateField.getText()));
            pstmt.setString(4, appointmentStatusField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addBilling() {
        try {
            String query = "INSERT INTO Billing (AppointmentID, TotalAmount) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(billingAppointmentIdField.getText()));
            pstmt.setBigDecimal(2, new java.math.BigDecimal(totalAmountField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Billing added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding billing.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Method to refresh the view area
    private void refreshView() {
        try {
            StringBuilder result = new StringBuilder();
            
            // View Doctors
            result.append("Doctors:\n");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Doctors");
            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("DoctorID")).append(", Name: ").append(rs.getString("DoctorName"))
                      .append(", Specialty: ").append(rs.getString("Specialty")).append(", Phone: ").append(rs.getString("Phone")).append("\n");
            }

            // View Patients
            result.append("\nPatients:\n");
            rs = stmt.executeQuery("SELECT * FROM Patients");
            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("PatientID")).append(", Name: ").append(rs.getString("PatientName"))
                      .append(", Age: ").append(rs.getInt("Age")).append(", Gender: ").append(rs.getString("Gender"))
                      .append(", Phone: ").append(rs.getString("Phone")).append("\n");
            }

            // View Appointments
            result.append("\nAppointments:\n");
            rs = stmt.executeQuery("SELECT * FROM Appointments");
            while (rs.next()) {
                result.append("Appointment ID: ").append(rs.getInt("AppointmentID")).append(", Doctor ID: ").append(rs.getInt("DoctorID"))
                      .append(", Patient ID: ").append(rs.getInt("PatientID")).append(", Date: ").append(rs.getDate("AppointmentDate"))
                      .append(", Status: ").append(rs.getString("Status")).append("\n");
            }

            // View Billing
            result.append("\nBilling:\n");
            rs = stmt.executeQuery("SELECT * FROM Billing");
            while (rs.next()) {
                result.append("Bill ID: ").append(rs.getInt("BillID")).append(", Appointment ID: ").append(rs.getInt("AppointmentID"))
                      .append(", Total Amount: ").append(rs.getBigDecimal("TotalAmount")).append("\n");
            }

            // View Feedback
            result.append("\nFeedback:\n");
            rs = stmt.executeQuery("SELECT D.DoctorName, F.Rating, F.Comments FROM Feedback F JOIN Doctors D ON F.DoctorID = D.DoctorID");
            while (rs.next()) {
                result.append("Doctor: ").append(rs.getString("DoctorName")).append(", Rating: ").append(rs.getInt("Rating"))
                      .append(", Comments: ").append(rs.getString("Comments")).append("\n");
            }

            viewArea.setText(result.toString());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error refreshing view.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HospitalManagementSystem();
    }
}