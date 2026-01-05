package view;

import controller.StaffController;
import model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private StaffController controller;
    private JTextField txtName, txtRole, txtDepartment, txtPhone, txtFacilityId;

    public StaffPanel() {
        controller = new StaffController();
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Role", "Dept", "Phone", "Email", "Facility"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        loadStaff();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Staff"));

        formPanel.add(new JLabel("Name (First Last):"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Role:"));
        txtRole = new JTextField();
        formPanel.add(txtRole);

        formPanel.add(new JLabel("Department:"));
        txtDepartment = new JTextField();
        formPanel.add(txtDepartment);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);
        
        formPanel.add(new JLabel("Facility ID (e.g. S001):"));
        txtFacilityId = new JTextField();
        formPanel.add(txtFacilityId);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = new JButton("Add Staff");
        btnAdd.setBackground(new Color(51, 153, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addStaff());
        btnPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Selected");
        btnUpdate.addActionListener(e -> updateStaff());
        btnPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(e -> deleteStaff());
        btnPanel.add(btnDelete);

        southPanel.add(formPanel);
        southPanel.add(btnPanel);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadStaff() {
        tableModel.setRowCount(0);
        List<Staff> list = controller.loadAllStaff();
        for (Staff s : list) {
            tableModel.addRow(new Object[]{
                s.getStaffId(),
                s.getFirstName() + " " + s.getLastName(),
                s.getRole(),
                s.getDepartment(),
                s.getPhoneNumber(),
                s.getEmail(),
                s.getFacilityId()
            });
        }
    }

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String fullName = (String) tableModel.getValueAt(row, 1);
            txtName.setText(fullName);
            txtRole.setText((String) tableModel.getValueAt(row, 2));
            txtDepartment.setText((String) tableModel.getValueAt(row, 3));
            txtPhone.setText((String) tableModel.getValueAt(row, 4));
            txtFacilityId.setText((String) tableModel.getValueAt(row, 6)); 
        }
    }

    private void addStaff() {
        try {
            String id = "S" + (System.currentTimeMillis() % 10000);
            Staff s = createFromForm(id);
            controller.addStaff(s);
            loadStaff();
            JOptionPane.showMessageDialog(this, "Staff Added!");
            clearForm();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateStaff() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Staff s = createFromForm(id);
            controller.updateStaff(s);
            loadStaff();
            JOptionPane.showMessageDialog(this, "Staff Updated!");
            clearForm();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void deleteStaff() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        controller.deleteStaff(id);
        loadStaff();
        JOptionPane.showMessageDialog(this, "Staff Deleted!");
        clearForm();
    }

    private Staff createFromForm(String id) {
        String[] parts = txtName.getText().trim().split(" ");
        String first = parts.length > 0 ? parts[0] : "";
        String last = parts.length > 1 ? parts[1] : "";
        
        return new Staff(
            id, first, last, txtRole.getText(), txtDepartment.getText(), 
            txtFacilityId.getText(),
            txtPhone.getText(), "email@staff.com", "Active", "2024-01-01", "Manager", "Level 1"
        );
    }

    private void clearForm() {
        txtName.setText("");
        txtRole.setText("");
        txtDepartment.setText("");
        txtPhone.setText("");
        txtFacilityId.setText("");
        table.clearSelection();
    }
}
