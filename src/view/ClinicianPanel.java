package view;

import controller.ClinicianController;
import model.Clinician;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClinicianPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ClinicianController controller;

    private JTextField txtTitle, txtFirstName, txtLastName, txtSpeciality, txtPhone, txtEmail, txtWorkplaceId, txtWorkplaceType;

    public ClinicianPanel() {
        controller = new ClinicianController();
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Title", "Name", "Speciality", "Phone", "Email", "Workplace"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        loadClinicians();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Clinician"));

        formPanel.add(new JLabel("Title:"));
        txtTitle = new JTextField();
        formPanel.add(txtTitle);

        formPanel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField();
        formPanel.add(txtFirstName);

        formPanel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField();
        formPanel.add(txtLastName);

        formPanel.add(new JLabel("Speciality:"));
        txtSpeciality = new JTextField();
        formPanel.add(txtSpeciality);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Workplace ID (e.g. H001):"));
        txtWorkplaceId = new JTextField();
        formPanel.add(txtWorkplaceId);

        formPanel.add(new JLabel("Workplace Type (Hospital/GP):"));
        txtWorkplaceType = new JTextField();
        formPanel.add(txtWorkplaceType);

        southPanel.add(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnAdd = new JButton("Add Clinician");
        btnAdd.setBackground(new Color(51, 153, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addCl());
        buttonPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Selected");
        btnUpdate.addActionListener(e -> updateCl());
        buttonPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(e -> deleteCl());
        buttonPanel.add(btnDelete);

        southPanel.add(buttonPanel);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadClinicians() {
        tableModel.setRowCount(0);
        List<Clinician> clinicians = controller.loadAllClinicians();
        for (Clinician c : clinicians) {
            tableModel.addRow(new Object[]{
                c.getClinicianId(),
                c.getTitle(),
                c.getFirstName() + " " + c.getLastName(),
                c.getSpeciality(),
                c.getPhoneNumber(),
                c.getEmail(),
                c.getWorkplaceId()
            });
        }
    }

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtTitle.setText((String) tableModel.getValueAt(row, 1));
            String fullName = (String) tableModel.getValueAt(row, 2);
            String[] names = fullName.split(" ", 2);
            txtFirstName.setText(names.length > 0 ? names[0] : "");
            txtLastName.setText(names.length > 1 ? names[1] : "");
            txtSpeciality.setText((String) tableModel.getValueAt(row, 3));
            txtPhone.setText((String) tableModel.getValueAt(row, 4));
            txtEmail.setText((String) tableModel.getValueAt(row, 5));
            txtWorkplaceId.setText((String) tableModel.getValueAt(row, 6));
            txtWorkplaceType.setText("Hospital");
        }
    }

    private void addCl() {
        try {
            String id = "C" + (System.currentTimeMillis() % 10000);
            Clinician c = createFromForm(id);
            controller.addClinician(c);
            loadClinicians();
            JOptionPane.showMessageDialog(this, "Added!");
            clearForm();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void updateCl() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Clinician c = createFromForm(id);
            controller.updateClinician(c);
            loadClinicians();
            JOptionPane.showMessageDialog(this, "Updated!");
            clearForm();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void deleteCl() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        controller.deleteClinician(id);
        loadClinicians();
        JOptionPane.showMessageDialog(this, "Deleted!");
        clearForm();
    }

    private Clinician createFromForm(String id) {
        return new Clinician(
            id, txtFirstName.getText(), txtLastName.getText(), txtTitle.getText(), txtSpeciality.getText(),
            "GMC000", txtPhone.getText(), txtEmail.getText(), 
            txtWorkplaceId.getText(), txtWorkplaceType.getText(), 
            "Active", "2024-01-01"
        );
    }

    private void clearForm() {
        txtTitle.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtSpeciality.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtWorkplaceId.setText("");
        txtWorkplaceType.setText("");
        table.clearSelection();
    }
}
