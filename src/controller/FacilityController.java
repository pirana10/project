package controller;

import model.Facility;
import util.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class FacilityController {
    private static final String CSV_FILE = "data/facilities.csv";

    public List<Facility> loadFacilities() {
        List<Facility> list = new ArrayList<>();
        List<String[]> data = CSVHandler.readCSV(CSV_FILE);

        for (String[] row : data) {
            try {
                String specialities = (row.length > 10) ? row[10] : "";
                
                Facility f = new Facility(
                    row[0].trim(), row[1].trim(), row[2].trim(), row[3].trim(), 
                    row[4].trim(), row[5].trim(), row[6].trim(), row[7].trim(), 
                    row[8].trim(), row[9].trim(), specialities
                );
                list.add(f);
            } catch (Exception e) {
            }
        }
        return list;
    }

    public void addFacility(Facility f) {
        CSVHandler.appendToCSV(CSV_FILE, f.toCSV());
    }

    public void updateFacility(Facility updated) {
        List<Facility> all = loadFacilities();
        List<String> lines = new ArrayList<>();
        lines.add("facility_id,facility_name,facility_type,address,postcode,phone_number,email,opening_hours,manager_name,capacity,specialities_offered");

        for (Facility f : all) {
            if (f.getFacilityId().equals(updated.getFacilityId())) {
                lines.add(updated.toCSV());
            } else {
                lines.add(f.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }

    public void deleteFacility(String id) {
        List<Facility> all = loadFacilities();
        List<String> lines = new ArrayList<>();
        lines.add("facility_id,facility_name,facility_type,address,postcode,phone_number,email,opening_hours,manager_name,capacity,specialities_offered");

        for (Facility f : all) {
            if (!f.getFacilityId().equals(id)) {
                lines.add(f.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }
}
