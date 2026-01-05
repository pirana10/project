package view;

import controller.FacilityController;
import model.Facility;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacilityPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private FacilityController controller;
    private JTextField txtName, txtType, txtAddress, txtPostcode, txtPhone;

    public FacilityPanel() {
        controller = new FacilityController();
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Type", "Postcode", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        loadFacilities();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Facilities"));

        formPanel.add(new JLabel("Facility Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Type (e.g., Hospital):"));
        txtType = new JTextField();
        formPanel.add(txtType);

        formPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        formPanel.add(txtAddress);

        formPanel.add(new JLabel("Postcode:"));
        txtPostcode = new JTextField();
        formPanel.add(txtPostcode);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = new JButton("Add Facility");
        btnAdd.setBackground(new Color(51, 153, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addFacility());
        btnPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Selected");
        btnUpdate.addActionListener(e -> updateFacility());
        btnPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(e -> deleteFacility());
        btnPanel.add(btnDelete);

        southPanel.add(formPanel);
        southPanel.add(btnPanel);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadFacilities() {
        tableModel.setRowCount(0);
        List<Facility> list = controller.loadFacilities();
        for (Facility f : list) {
            tableModel.addRow(new Object[]{
                f.getFacilityId(),
                f.getFacilityName(),
                f.getFacilityType(),
                f.getPostcode(),
                f.getPhoneNumber()
            });
        }
    }

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtType.setText((String) tableModel.getValueAt(row, 2));
            txtPostcode.setText((String) tableModel.getValueAt(row, 3));
            txtPhone.setText((String) tableModel.getValueAt(row, 4));
        }
    }

    private void addFacility() {
        try {
            String id = "F" + (System.currentTimeMillis() % 10000);
            Facility f = createFromForm(id);
            controller.addFacility(f);
            loadFacilities();
            JOptionPane.showMessageDialog(this, "Added!");
            clearForm();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void updateFacility() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Facility f = createFromForm(id);
            controller.updateFacility(f);
            loadFacilities();
            JOptionPane.showMessageDialog(this, "Updated!");
            clearForm();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void deleteFacility() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        controller.deleteFacility(id);
        loadFacilities();
        JOptionPane.showMessageDialog(this, "Deleted!");
        clearForm();
    }

    private Facility createFromForm(String id) {
        return new Facility(
            id, txtName.getText(), txtType.getText(), txtAddress.getText(), txtPostcode.getText(),
            txtPhone.getText(), "test@nhs.uk", "9am-5pm", "Manager", "100", "General"
        );
    }

    private void clearForm() {
        txtName.setText("");
        txtType.setText("");
        txtAddress.setText("");
        txtPostcode.setText("");
        txtPhone.setText("");
        table.clearSelection();
    }
}
