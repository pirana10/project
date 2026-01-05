package controller;

import model.Clinician;
import util.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class ClinicianController {
    private static final String CSV_FILE = "data/clinicians.csv";

    public List<Clinician> loadAllClinicians() {
        List<Clinician> clinicians = new ArrayList<>();
        List<String[]> data = CSVHandler.readCSV(CSV_FILE);

        for (String[] row : data) {
            try {
                Clinician c = new Clinician(
                   row[0].trim(), row[1], row[2], row[3], row[4], row[5], row[6], row[7], 
                   row[8].trim(), row[9], row[10], row[11]
                );
                clinicians.add(c);
            } catch (Exception e) {
                 System.err.println("Error parsing clinician row: " + java.util.Arrays.toString(row));
            }
        }
        return clinicians;
    }

    public void addClinician(Clinician c) {
        CSVHandler.appendToCSV(CSV_FILE, c.toCSV());
    }

    public void deleteClinician(String id) {
        List<Clinician> all = loadAllClinicians();
        List<String> lines = new ArrayList<>();
        lines.add("clinician_id,first_name,last_name,title,speciality,gmc_number,phone_number,email,workplace_id,workplace_type,employment_status,start_date");
        
        for (Clinician c : all) {
            if (!c.getClinicianId().equals(id)) {
                lines.add(c.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }

    public void updateClinician(Clinician updated) {
        List<Clinician> all = loadAllClinicians();
        List<String> lines = new ArrayList<>();
        lines.add("clinician_id,first_name,last_name,title,speciality,gmc_number,phone_number,email,workplace_id,workplace_type,employment_status,start_date");
        
        for (Clinician c : all) {
            if (c.getClinicianId().equals(updated.getClinicianId())) {
                lines.add(updated.toCSV());
            } else {
                lines.add(c.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }
}
