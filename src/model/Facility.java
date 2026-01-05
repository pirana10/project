package model;

public class Facility {
    private String facilityId;
    private String facilityName;
    private String facilityType;
    private String address;
    private String postcode;
    private String phoneNumber;
    private String email;
    private String openingHours;
    private String managerName;
    private String capacity;
    private String specialities;

    public Facility(String facilityId, String facilityName, String facilityType, String address, String postcode,
                    String phoneNumber, String email, String openingHours, String managerName, String capacity,
                    String specialities) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityType = facilityType;
        this.address = address;
        this.postcode = postcode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.openingHours = openingHours;
        this.managerName = managerName;
        this.capacity = capacity;
        this.specialities = specialities;
    }

    public String getFacilityId() { return facilityId; }
    public String getFacilityName() { return facilityName; }
    public String getFacilityType() { return facilityType; }
    public String getAddress() { return address; }
    public String getPostcode() { return postcode; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getOpeningHours() { return openingHours; }
    public String getManagerName() { return managerName; }
    public String getCapacity() { return capacity; }
    public String getSpecialities() { return specialities; }

    public String toCSV() {
        return facilityId + "," + facilityName + "," + facilityType + "," + address + "," + postcode + "," +
               phoneNumber + "," + email + "," + openingHours + "," + managerName + "," + capacity + "," + specialities;
    }

    @Override
    public String toString() {
        return facilityName;
    }
}
