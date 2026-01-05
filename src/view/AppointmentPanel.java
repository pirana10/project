package view;

import controller.AppointmentController;
import model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private AppointmentController controller;

    private JTextField txtPatientId, txtClinicianId, txtDate, txtTime, txtType, txtReason, txtFacilityId;
    private JComboBox<String> cmbStatus;

    public AppointmentPanel() {
        controller = new AppointmentController();
        setLayout(new BorderLayout());

        String[] columnNames = {"Appt ID", "Patient ID", "Clinician ID", "Facility ID", "Date", "Time", "Type", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        loadAppointments();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Appointment"));

        formPanel.add(new JLabel("Patient ID:"));
        txtPatientId = new JTextField();
        formPanel.add(txtPatientId);

        formPanel.add(new JLabel("Clinician ID:"));
        txtClinicianId = new JTextField();
        formPanel.add(txtClinicianId);

        formPanel.add(new JLabel("Facility ID:"));
        txtFacilityId = new JTextField();
        formPanel.add(txtFacilityId);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField();
        formPanel.add(txtDate);

        formPanel.add(new JLabel("Time (HH:MM):"));
        txtTime = new JTextField();
        formPanel.add(txtTime);

        formPanel.add(new JLabel("Type (e.g. Checkup):"));
        txtType = new JTextField();
        formPanel.add(txtType);

        formPanel.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"Scheduled", "Completed", "Cancelled", "No-Show"});
        formPanel.add(cmbStatus);

        formPanel.add(new JLabel("Reason:"));
        txtReason = new JTextField();
        formPanel.add(txtReason);
        
        southPanel.add(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnAdd = new JButton("Book Appointment");
        btnAdd.setBackground(new Color(51, 153, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addAppointment());
        buttonPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Selected");
        btnUpdate.addActionListener(e -> updateAppointment());
        buttonPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(e -> deleteAppointment());
        buttonPanel.add(btnDelete);

        southPanel.add(buttonPanel);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        List<Appointment> list = controller.loadAppointments(); 
        for (Appointment a : list) {
            tableModel.addRow(new Object[]{
                a.getAppointmentId(),
                a.getPatientId(),
                a.getClinicianId(),
                a.getFacilityId(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getAppointmentType(),
                a.getStatus()
            });
        }
    }

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtPatientId.setText((String) tableModel.getValueAt(row, 1));
            txtClinicianId.setText((String) tableModel.getValueAt(row, 2));
            txtFacilityId.setText((String) tableModel.getValueAt(row, 3));
            txtDate.setText((String) tableModel.getValueAt(row, 4));
            txtTime.setText((String) tableModel.getValueAt(row, 5));
            txtType.setText((String) tableModel.getValueAt(row, 6));
            cmbStatus.setSelectedItem((String) tableModel.getValueAt(row, 7));
        }
    }

    private void addAppointment() {
        try {
            String id = "A" + (System.currentTimeMillis() % 10000);
            Appointment a = createFromForm(id);
            controller.addAppointment(a);
            loadAppointments();
            JOptionPane.showMessageDialog(this, "Booked!");
            clearForm();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void updateAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Appointment a = createFromForm(id);
            controller.updateAppointment(a);
            loadAppointments();
            JOptionPane.showMessageDialog(this, "Updated!");
            clearForm();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void deleteAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        controller.deleteAppointment(id);
        loadAppointments();
        JOptionPane.showMessageDialog(this, "Deleted!");
        clearForm();
    }

    private Appointment createFromForm(String id) {
        return new Appointment(
            id, txtPatientId.getText(), txtClinicianId.getText(), 
            txtFacilityId.getText(),
            txtDate.getText(), txtTime.getText(), 15,
            txtType.getText(), (String) cmbStatus.getSelectedItem(),
            txtReason.getText(), "", "2024-01-01", ""
        );
    }

    private void clearForm() {
        txtPatientId.setText("");
        txtClinicianId.setText("");
        txtFacilityId.setText("");
        txtDate.setText("");
        txtTime.setText("");
        txtType.setText("");
        txtReason.setText("");
        table.clearSelection();
    }
}
