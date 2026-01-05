package view;

import controller.PrescriptionController;
import model.Prescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PrescriptionPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private PrescriptionController controller;

    private JTextField txtPatientId, txtClinicianId, txtAppointmentId, txtMedication, txtDosage, txtQuantity;

    public PrescriptionPanel() {
        controller = new PrescriptionController();
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Patient", "Clinician", "Appt ID", "Medication", "Dosage", "Qty", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        loadPrescriptions();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Issue Prescription"));

        formPanel.add(new JLabel("Patient ID:"));
        txtPatientId = new JTextField();
        formPanel.add(txtPatientId);

        formPanel.add(new JLabel("Clinician ID:"));
        txtClinicianId = new JTextField();
        formPanel.add(txtClinicianId);

        formPanel.add(new JLabel("Appointment ID:"));
        txtAppointmentId = new JTextField();
        formPanel.add(txtAppointmentId);

        formPanel.add(new JLabel("Medication:"));
        txtMedication = new JTextField();
        formPanel.add(txtMedication);

        formPanel.add(new JLabel("Dosage:"));
        txtDosage = new JTextField();
        formPanel.add(txtDosage);

        formPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        formPanel.add(txtQuantity);

        southPanel.add(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnSave = new JButton("Issue Prescription");
        btnSave.setBackground(new Color(51, 153, 255));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> savePrescription());
        buttonPanel.add(btnSave);

        JButton btnUpdate = new JButton("Update Selected");
        btnUpdate.addActionListener(e -> updatePrescription());
        buttonPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(e -> deletePrescription());
        buttonPanel.add(btnDelete);

        JButton btnPrint = new JButton("Print Prescription");
        btnPrint.addActionListener(e -> printPrescriptionToFile());
        buttonPanel.add(btnPrint);

        southPanel.add(buttonPanel);

        add(southPanel, BorderLayout.SOUTH);
    }
    
    private void printPrescriptionToFile() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a prescription first.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        java.util.List<Prescription> all = controller.loadAllPrescriptions();
        for (Prescription p : all) {
            if (p.getPrescriptionId().equals(id)) {
                controller.printPrescription(p);
                JOptionPane.showMessageDialog(this, "Prescription Printed to File!");
                return;
            }
        }
    }

    private void loadPrescriptions() {
        tableModel.setRowCount(0);
        List<Prescription> list = controller.loadAllPrescriptions();
        for (Prescription p : list) {
            tableModel.addRow(new Object[]{
                p.getPrescriptionId(),
                p.getPatientId(),
                p.getClinicianId(),
                p.getAppointmentId(),
                p.getMedicationName(),
                p.getDosage(),
                p.getQuantity(),
                p.getStatus()
            });
        }
    }

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtPatientId.setText((String) tableModel.getValueAt(row, 1));
            txtClinicianId.setText((String) tableModel.getValueAt(row, 2));
            txtAppointmentId.setText((String) tableModel.getValueAt(row, 3));
            txtMedication.setText((String) tableModel.getValueAt(row, 4));
            txtDosage.setText((String) tableModel.getValueAt(row, 5));
            txtQuantity.setText((String) tableModel.getValueAt(row, 6));
        }
    }

    private void savePrescription() {
        try {
            String id = "PR" + (System.currentTimeMillis() % 10000);
            Prescription p = createFromForm(id);
            controller.addPrescription(p);
            loadPrescriptions();
            JOptionPane.showMessageDialog(this, "Issued!");
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updatePrescription() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Prescription p = createFromForm(id);
            controller.updatePrescription(p);
            loadPrescriptions();
            JOptionPane.showMessageDialog(this, "Updated!");
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deletePrescription() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        controller.deletePrescription(id);
        loadPrescriptions();
        JOptionPane.showMessageDialog(this, "Deleted!");
        clearForm();
    }

    private Prescription createFromForm(String id) {
        return new Prescription(
                id, 
                txtPatientId.getText(), 
                txtClinicianId.getText(), 
                txtAppointmentId.getText(),
                "2024-01-01",
                txtMedication.getText(),
                txtDosage.getText(),
                "Once daily", "7 days", txtQuantity.getText(), 
                "Take with food", "Pharmacy", "Issued", "2024-01-01", ""
        );
    }

    private void clearForm() {
        txtPatientId.setText("");
        txtClinicianId.setText("");
        txtAppointmentId.setText("");
        txtMedication.setText("");
        txtDosage.setText("");
        txtQuantity.setText("");
        table.clearSelection();
    }
}
