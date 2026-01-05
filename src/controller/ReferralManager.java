package controller;

import model.Referral;
import util.CSVHandler;

public class ReferralManager {
    private static ReferralManager instance;
    private static final String CSV_FILE = "data/referrals.csv";

    private ReferralManager() {}

    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    public java.util.List<Referral> loadAllReferrals() {
        java.util.List<Referral> list = new java.util.ArrayList<>();
        java.util.List<String[]> data = CSVHandler.readCSV(CSV_FILE);
        for (String[] row : data) {
            try {
                String id = (row.length > 0) ? row[0].trim() : "";
                String patId = (row.length > 1) ? row[1].trim() : "";
                String refDoc = (row.length > 2) ? row[2].trim() : "";
                String toDoc = (row.length > 3) ? row[3].trim() : "";
                String refFac = (row.length > 4) ? row[4].trim() : "";
                String toFac = (row.length > 5) ? row[5].trim() : "";
                String date = (row.length > 6) ? row[6].trim() : "";
                String urg = (row.length > 7) ? row[7].trim() : "";
                String reason = (row.length > 8) ? row[8].trim() : "";
                String sum = (row.length > 9) ? row[9].trim() : "";
                String inv = (row.length > 10) ? row[10].trim() : "";
                String status = (row.length > 11) ? row[11].trim() : "";
                String apptId = (row.length > 12) ? row[12].trim() : "";
                String notes = (row.length > 13) ? row[13].trim() : "";
                String created = (row.length > 14) ? row[14].trim() : "";
                String lastUpdated = (row.length > 15) ? row[15].trim() : "";
                
                Referral r = new Referral(
                    id, patId, refDoc, toDoc, refFac, toFac, date, urg,
                    reason, sum, inv, status, apptId, notes, created, lastUpdated
                );
                list.add(r);
            } catch (Exception e) {
                System.err.println("Error parsing referral: " + java.util.Arrays.toString(row));
            }
        }
        return list;
    }

    public void createReferral(Referral referral) {
        CSVHandler.appendToCSV(CSV_FILE, referral.toCSV());
    }

    public void updateReferral(Referral updated) {
        java.util.List<Referral> all = loadAllReferrals();
        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add("referral_id,patient_id,referring_clinician_id,referred_to_clinician_id,referring_facility_id,referred_to_facility_id,referral_date,urgency_level,referral_reason,clinical_summary,requested_investigations,status,appointment_id,notes,created_date,last_updated");
        for (Referral r : all) {
            if (r.getReferralId().equals(updated.getReferralId())) {
                lines.add(updated.toCSV());
            } else {
                lines.add(r.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }

    public void deleteReferral(String id) {
        java.util.List<Referral> all = loadAllReferrals();
        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add("referral_id,patient_id,referring_clinician_id,referred_to_clinician_id,referring_facility_id,referred_to_facility_id,referral_date,urgency_level,referral_reason,clinical_summary,requested_investigations,status,appointment_id,notes,created_date,last_updated");
        for (Referral r : all) {
            if (!r.getReferralId().equals(id)) {
                lines.add(r.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }

    public void printReferral(Referral r) {
        String filename = "data/referral_" + r.getReferralId() + ".txt";
        StringBuilder sb = new StringBuilder();
        sb.append("=== REFERRAL LETTER ===\n");
        sb.append("Referral ID: ").append(r.getReferralId()).append("\n");
        sb.append("Date: ").append(r.getReferralDate()).append("\n\n");
        sb.append("FROM: Clinician ").append(r.getReferringClinicianId()).append(" (Facility: ").append(r.getReferringFacilityId()).append(")\n");
        sb.append("TO:   Clinician ").append(r.getReferredToClinicianId()).append(" (Facility: ").append(r.getReferredToFacilityId()).append(")\n\n");
        sb.append("PATIENT: ").append(r.getPatientId()).append("\n\n");
        sb.append("URGENCY: ").append(r.getUrgencyLevel()).append("\n");
        sb.append("REASON: ").append(r.getReferralReason()).append("\n");
        sb.append("CLINICAL SUMMARY:\n").append(r.getClinicalSummary()).append("\n\n");
        sb.append("STATUS: ").append(r.getStatus()).append("\n");
        sb.append("=======================\n");

        try {
            java.io.FileWriter fw = new java.io.FileWriter(filename);
            fw.write(sb.toString());
            fw.close();
        } catch (java.io.IOException e) {
            System.err.println("Error printing referral: " + e.getMessage());
        }
    }
}
