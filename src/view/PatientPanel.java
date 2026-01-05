package view;

import controller.PatientController;
import model.Patient;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private PatientController controller;

    private JTextField txtFirstName, txtLastName, txtDob, txtNhsNumber, txtPhone, txtEmail;

    public PatientPanel() {
        controller = new PatientController();
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "DOB", "NHS Number", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        loadPatients();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Patient"));

        formPanel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField();
        formPanel.add(txtFirstName);

        formPanel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField();
        formPanel.add(txtLastName);

        formPanel.add(new JLabel("DOB (YYYY-MM-DD):"));
        txtDob = new JTextField();
        formPanel.add(txtDob);

        formPanel.add(new JLabel("NHS Number:"));
        txtNhsNumber = new JTextField();
        formPanel.add(txtNhsNumber);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        southPanel.add(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnAdd = new JButton("Add Patient");
        btnAdd.setBackground(new Color(51, 153, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addPatient());
        buttonPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Selected");
        btnUpdate.addActionListener(e -> updatePatient());
        buttonPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(e -> deletePatient());
        buttonPanel.add(btnDelete);

        southPanel.add(buttonPanel);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadPatients() {
        tableModel.setRowCount(0);
        List<Patient> patients = controller.loadAllPatients();
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                p.getPatientId(),
                p.getFirstName() + " " + p.getLastName(),
                p.getDateOfBirth(),
                p.getNhsNumber(),
                p.getPhoneNumber(),
                p.getEmail()
            });
        }
    }

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String fullName = (String) tableModel.getValueAt(row, 1);
            String[] names = fullName.split(" ", 2);
            txtFirstName.setText(names.length > 0 ? names[0] : "");
            txtLastName.setText(names.length > 1 ? names[1] : "");
            
            txtDob.setText((String) tableModel.getValueAt(row, 2));
            txtNhsNumber.setText((String) tableModel.getValueAt(row, 3));
            txtPhone.setText((String) tableModel.getValueAt(row, 4));
            txtEmail.setText((String) tableModel.getValueAt(row, 5));
        }
    }

    private void addPatient() {
        try {
            String id = "P" + (System.currentTimeMillis() % 10000);
            Patient p = createPatientFromForm(id);
            controller.addPatient(p);
            loadPatients();
            JOptionPane.showMessageDialog(this, "Patient Added!");
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updatePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient to update.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Patient p = createPatientFromForm(id);
            controller.updatePatient(p);
            loadPatients();
            JOptionPane.showMessageDialog(this, "Patient Updated!");
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient to delete.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.deletePatient(id);
            loadPatients();
            JOptionPane.showMessageDialog(this, "Patient Deleted!");
            clearForm();
        }
    }

    private Patient createPatientFromForm(String id) {
        return new Patient(
            id, 
            txtFirstName.getText(), 
            txtLastName.getText(), 
            txtDob.getText(), 
            txtNhsNumber.getText(), 
            "Unknown",
            txtPhone.getText(), 
            txtEmail.getText(), 
            "Address", "Postcode", "Contact", "Phone", "2024-01-01", "G001"
        );
    }

    private void clearForm() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtDob.setText("");
        txtNhsNumber.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }
}
