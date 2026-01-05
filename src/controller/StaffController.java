package controller;

import model.Staff;
import util.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class StaffController {
    private static final String CSV_FILE = "data/staff.csv";

    public List<Staff> loadAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        List<String[]> data = CSVHandler.readCSV(CSV_FILE);

        for (String[] row : data) {
            try {
                String manager = (row.length > 10) ? row[10] : "";
                String access = (row.length > 11) ? row[11] : "";
                
                Staff s = new Staff(
                    row[0].trim(), row[1].trim(), row[2].trim(), row[3].trim(), 
                    row[4].trim(), row[5].trim(), row[6].trim(), row[7].trim(), 
                    row[8].trim(), row[9].trim(), manager, access
                );
                staffList.add(s);
            } catch (Exception e) {
                System.err.println("Error parsing staff row: " + java.util.Arrays.toString(row) + " - " + e.getMessage());
            }
        }
        return staffList;
    }

    public void addStaff(Staff s) {
        CSVHandler.appendToCSV(CSV_FILE, s.toCSV());
    }

    public void updateStaff(Staff updated) {
        List<Staff> all = loadAllStaff();
        List<String> lines = new ArrayList<>();
        lines.add("staff_id,first_name,last_name,role,department,facility_id,phone_number,email,employment_status,start_date,line_manager,access_level");

        for (Staff s : all) {
            if (s.getStaffId().equals(updated.getStaffId())) {
                lines.add(updated.toCSV());
            } else {
                lines.add(s.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }

    public void deleteStaff(String id) {
        List<Staff> all = loadAllStaff();
        List<String> lines = new ArrayList<>();
        lines.add("staff_id,first_name,last_name,role,department,facility_id,phone_number,email,employment_status,start_date,line_manager,access_level");

        for (Staff s : all) {
            if (!s.getStaffId().equals(id)) {
                lines.add(s.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }
}
