package view;

import controller.ReferralManager;
import model.Referral;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReferralPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ReferralManager manager;

    private JTextField txtPatientId, txtRefDocId, txtRefToDocId, txtReason;
    private JComboBox<String> cmbUrgency, cmbStatus;

    public ReferralPanel() {
        manager = ReferralManager.getInstance();
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Patient", "From Doc", "To Doc", "Date", "Urgency", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        loadReferrals();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Referral"));

        formPanel.add(new JLabel("Patient ID:"));
        txtPatientId = new JTextField();
        formPanel.add(txtPatientId);

        formPanel.add(new JLabel("From Clinician ID:"));
        txtRefDocId = new JTextField();
        formPanel.add(txtRefDocId);

        formPanel.add(new JLabel("To Clinician ID:"));
        txtRefToDocId = new JTextField();
        formPanel.add(txtRefToDocId);

        formPanel.add(new JLabel("Urgency:"));
        cmbUrgency = new JComboBox<>(new String[]{"Routine", "Urgent", "Two-Week Wait"});
        formPanel.add(cmbUrgency);

        formPanel.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"New", "Pending", "Approved", "Completed", "Declined"});
        formPanel.add(cmbStatus);

        formPanel.add(new JLabel("Reason:"));
        txtReason = new JTextField();
        formPanel.add(txtReason);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = new JButton("Create Referral");
        btnAdd.setBackground(new Color(51, 153, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addReferral());
        btnPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Selected");
        btnUpdate.addActionListener(e -> updateReferral());
        btnPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(e -> deleteReferral());
        btnPanel.add(btnDelete);

        JButton btnPrint = new JButton("Print Letter");
        btnPrint.addActionListener(e -> printReferralLetter());
        btnPanel.add(btnPrint);

        southPanel.add(formPanel);
        southPanel.add(btnPanel);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    private void printReferralLetter() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a referral first.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        java.util.List<Referral> all = ReferralManager.getInstance().loadAllReferrals();
        for (Referral r : all) {
            if (r.getReferralId().equals(id)) {
                ReferralManager.getInstance().printReferral(r);
                JOptionPane.showMessageDialog(this, "Referral Letter Generated!");
                return;
            }
        }
    }

    private void loadReferrals() {
        tableModel.setRowCount(0);
        List<Referral> list = manager.loadAllReferrals();
        for (Referral r : list) {
            tableModel.addRow(new Object[]{
                r.getReferralId(),
                r.getPatientId(),
                r.getReferringClinicianId(),
                r.getReferredToClinicianId(),
                r.getReferralDate(),
                r.getUrgencyLevel(),
                r.getStatus()
            });
        }
    }

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtPatientId.setText((String) tableModel.getValueAt(row, 1));
            txtRefDocId.setText((String) tableModel.getValueAt(row, 2));
            txtRefToDocId.setText((String) tableModel.getValueAt(row, 3));
            cmbUrgency.setSelectedItem((String) tableModel.getValueAt(row, 5));
            cmbStatus.setSelectedItem((String) tableModel.getValueAt(row, 6));
        }
    }

    private void addReferral() {
        try {
            String id = "R" + (System.currentTimeMillis() % 10000);
            Referral r = createFromForm(id);
            manager.createReferral(r);
            loadReferrals();
            JOptionPane.showMessageDialog(this, "Referral Created!");
            clearForm();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void updateReferral() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            Referral r = createFromForm(id);
            manager.updateReferral(r);
            loadReferrals();
            JOptionPane.showMessageDialog(this, "Updated!");
            clearForm();
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void deleteReferral() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String id = (String) tableModel.getValueAt(row, 0);
        manager.deleteReferral(id);
        loadReferrals();
        JOptionPane.showMessageDialog(this, "Deleted!");
        clearForm();
    }

    private Referral createFromForm(String id) {
        return new Referral(
            id, txtPatientId.getText(), txtRefDocId.getText(), txtRefToDocId.getText(),
            "S001", "H001", "2024-01-01", (String) cmbUrgency.getSelectedItem(),
            txtReason.getText(), "Summary", "Investigations", (String) cmbStatus.getSelectedItem(),
            "A000", "Notes", "2024-01-01", "2024-01-01"
        );
    }

    private void clearForm() {
        txtPatientId.setText("");
        txtRefDocId.setText("");
        txtRefToDocId.setText("");
        txtReason.setText("");
        table.clearSelection();
    }
}
