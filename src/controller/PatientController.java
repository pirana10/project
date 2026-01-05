package controller;

import model.Patient;
import util.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class PatientController {
    private static final String CSV_FILE = "data/patients.csv";

    public List<Patient> loadAllPatients() {
        List<Patient> patients = new ArrayList<>();
        List<String[]> data = CSVHandler.readCSV(CSV_FILE);

        for (String[] row : data) {
            try {
                String id = row[0].trim();
                String gpId = (row.length > 13) ? row[13].trim() : "";
                
                Patient p = new Patient(
                    id, row[1], row[2], row[3], row[4], row[5], row[6], row[7], 
                    row[8], row[9], row[10], row[11], row[12], gpId
                );
                patients.add(p);
            } catch (Exception e) {
                System.err.println("Error parsing patient row: " +  java.util.Arrays.toString(row));
                e.printStackTrace();
            }
        }
        return patients;
    }

    public void addPatient(Patient patient) {
        CSVHandler.appendToCSV(CSV_FILE, patient.toCSV());
    }

    public void deletePatient(String patientId) {
        List<Patient> all = loadAllPatients();
        List<String> lines = new ArrayList<>();
        lines.add("patient_id,first_name,last_name,date_of_birth,nhs_number,gender,phone_number,email,address,postcode,emergency_contact_name,emergency_contact_phone,registration_date,gp_surgery_id");
        
        for (Patient p : all) {
            if (!p.getPatientId().equals(patientId)) {
                lines.add(p.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }

    public void updatePatient(Patient updatedPatient) {
        List<Patient> all = loadAllPatients();
        List<String> lines = new ArrayList<>();
        lines.add("patient_id,first_name,last_name,date_of_birth,nhs_number,gender,phone_number,email,address,postcode,emergency_contact_name,emergency_contact_phone,registration_date,gp_surgery_id");
        
        for (Patient p : all) {
            if (p.getPatientId().equals(updatedPatient.getPatientId())) {
                lines.add(updatedPatient.toCSV());
            } else {
                lines.add(p.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }
}
