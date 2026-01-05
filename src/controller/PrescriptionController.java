package controller;

import model.Prescription;
import util.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionController {
    private static final String CSV_FILE = "data/prescriptions.csv";

    public List<Prescription> loadAllPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();
        List<String[]> data = CSVHandler.readCSV(CSV_FILE);

        for (String[] row : data) {
            try {
                String colDate = (row.length > 14) ? row[14].trim() : "";
                
                Prescription p = new Prescription(
                    row[0].trim(), row[1].trim(), row[2].trim(), row[3].trim(), 
                    row[4], row[5], row[6], row[7], row[8].trim(), row[9].trim(), 
                    row[10], row[11], row[12], row[13], colDate
                );
                prescriptions.add(p);
            } catch (Exception e) {
                System.err.println("Error parsing prescription row: " + java.util.Arrays.toString(row) + " - " + e.getMessage());
            }
        }
        return prescriptions;
    }

    public void addPrescription(Prescription px) {
        CSVHandler.appendToCSV(CSV_FILE, px.toCSV());
    }

    public void deletePrescription(String id) {
        List<Prescription> all = loadAllPrescriptions();
        List<String> lines = new ArrayList<>();
        lines.add("prescription_id,patient_id,clinician_id,appointment_id,prescription_date,medication_name,dosage,frequency,duration_days,quantity,instructions,pharmacy_name,status,issue_date,collection_date");
        
        for (Prescription p : all) {
            if (!p.getPrescriptionId().equals(id)) {
                lines.add(p.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }

    public void printPrescription(Prescription p) {
        String filename = "data/prescription_" + p.getPrescriptionId() + ".txt";
        StringBuilder sb = new StringBuilder();
        sb.append("=== PRESCRIPTION ===\n");
        sb.append("Prescription ID: ").append(p.getPrescriptionId()).append("\n");
        sb.append("Date: ").append(p.getPrescriptionDate()).append("\n\n");
        sb.append("PATIENT: ").append(p.getPatientId()).append("\n");
        sb.append("CLINICIAN: ").append(p.getClinicianId()).append("\n\n");
        sb.append("MEDICATION: ").append(p.getMedicationName()).append("\n");
        sb.append("DOSAGE:   ").append(p.getDosage()).append("\n");
        sb.append("QUANTITY: ").append(p.getQuantity()).append("\n");
        sb.append("INSTRUCTIONS: ").append(p.getInstructions()).append("\n\n");
        sb.append("PHARMACY: ").append(p.getPharmacyName()).append("\n");
        sb.append("STATUS:   ").append(p.getStatus()).append("\n");
        sb.append("====================\n");

        try {
            java.io.FileWriter fw = new java.io.FileWriter(filename);
            fw.write(sb.toString());
            fw.close();
        } catch (java.io.IOException e) {
            System.err.println("Error printing prescription: " + e.getMessage());
        }
    }

    public void updatePrescription(Prescription updated) {
        List<Prescription> all = loadAllPrescriptions();
        List<String> lines = new ArrayList<>();
        lines.add("prescription_id,patient_id,clinician_id,appointment_id,prescription_date,medication_name,dosage,frequency,duration_days,quantity,instructions,pharmacy_name,status,issue_date,collection_date");
        
        for (Prescription p : all) {
            if (p.getPrescriptionId().equals(updated.getPrescriptionId())) {
                lines.add(updated.toCSV());
            } else {
                lines.add(p.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }
}
