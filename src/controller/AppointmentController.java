package controller;

import model.Appointment;
import util.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {
    private static final String CSV_FILE = "data/appointments.csv";

    public List<Appointment> loadAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        List<String[]> data = CSVHandler.readCSV(CSV_FILE);

        for (String[] row : data) {
            try {
                String id = row[0].trim();
                String patId = row[1].trim();
                String clinId = row[2].trim();
                String facId = row[3].trim();
                
                int duration = 0;
                try {
                    duration = Integer.parseInt(row[6].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid duration for appointment " + id + ": " + row[6]);
                }

                String notes = (row.length > 10) ? row[10] : "";
                String created = (row.length > 11) ? row[11] : "";
                String modified = (row.length > 12) ? row[12] : "";

                Appointment appt = new Appointment(
                    id, patId, clinId, facId, row[4], row[5], duration, 
                    row[7], row[8], row[9], notes, created, modified
                );
                appointments.add(appt);
            } catch (Exception e) {
                System.err.println("Error parsing appointment row: " + java.util.Arrays.toString(row));
                e.printStackTrace();
            }
        }
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        CSVHandler.appendToCSV(CSV_FILE, appointment.toCSV());
    }

    public void deleteAppointment(String appId) {
        List<Appointment> all = loadAppointments();
        List<String> lines = new ArrayList<>();
        lines.add("appointment_id,patient_id,clinician_id,facility_id,appointment_date,appointment_time,duration_minutes,appointment_type,status,reason_for_visit,notes,created_date,last_modified");
        
        for (Appointment a : all) {
            if (!a.getAppointmentId().equals(appId)) {
                lines.add(a.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }

    public void updateAppointment(Appointment updatedAppt) {
        List<Appointment> all = loadAppointments();
        List<String> lines = new ArrayList<>();
        lines.add("appointment_id,patient_id,clinician_id,facility_id,appointment_date,appointment_time,duration_minutes,appointment_type,status,reason_for_visit,notes,created_date,last_modified");
        
        for (Appointment a : all) {
            if (a.getAppointmentId().equals(updatedAppt.getAppointmentId())) {
                lines.add(updatedAppt.toCSV());
            } else {
                lines.add(a.toCSV());
            }
        }
        CSVHandler.writeRaw(CSV_FILE, lines);
    }
}
