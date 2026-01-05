package view;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("Healthcare Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Patients", new PatientPanel());
        tabbedPane.addTab("Clinicians", new ClinicianPanel());
        tabbedPane.addTab("Appointments", new AppointmentPanel());
        tabbedPane.addTab("Prescriptions", new PrescriptionPanel());
        tabbedPane.addTab("Referrals", new ReferralPanel());
        tabbedPane.addTab("Staff", new StaffPanel());
        tabbedPane.addTab("Facilities", new FacilityPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
