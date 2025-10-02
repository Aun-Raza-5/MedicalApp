import java.util.ArrayList;
import java.util.List;

/**
 * Represents a medical record for a patient.
 */
public class MedicalRecord {

    private String name;
    private int age;
    private List<String> allergies; // List of allergies
    private List<String> medications; // List of medications
    private String checkupReminders;
    private String bloodGroup;
    private double bloodSugar;
    private String bloodPressure;

    /**
     * Constructor to create a MedicalRecord object with basic information.
     *
     * @param name Patient's name.
     * @param age Patient's age.
     */
    public MedicalRecord(String name, int age) {
        this.name = name;
        this.age = age;
        this.allergies = new ArrayList<>();
        this.medications = new ArrayList<>();
        this.checkupReminders = "None";
        this.bloodGroup = "Unknown";
        this.bloodSugar = 0.0;
        this.bloodPressure = "Normal";
    }

    /**
     * Constructor to create a MedicalRecord object with all fields initialized.
     *
     * @param name             Patient's name.
     * @param age              Patient's age.
     * @param allergies        List of patient's allergies.
     * @param medications      List of patient's current medications.
     * @param checkupReminders Patient's reminders for checkups.
     * @param bloodGroup       Patient's blood group.
     * @param bloodSugar       Patient's blood sugar level.
     * @param bloodPressure    Patient's blood pressure.
     */
    public MedicalRecord(String name, int age, List<String> allergies, List<String> medications,
                         String checkupReminders, String bloodGroup, double bloodSugar, String bloodPressure) {
        this.name = name;
        this.age = age;
        this.allergies = allergies != null ? allergies : new ArrayList<>();
        this.medications = medications != null ? medications : new ArrayList<>();
        this.checkupReminders = checkupReminders != null ? checkupReminders : "None";
        this.bloodGroup = bloodGroup != null ? bloodGroup : "Unknown";
        this.bloodSugar = bloodSugar >= 0 ? bloodSugar : 0.0; // Ensure non-negative blood sugar level
        this.bloodPressure = bloodPressure != null ? bloodPressure : "Normal";
    }

    // Getters and Setters for the fields

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup != null ? bloodGroup : "Unknown";
    }

    public double getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(double bloodSugar) {
        if (bloodSugar >= 0) {
            this.bloodSugar = bloodSugar;
        } else {
            throw new IllegalArgumentException("Blood sugar cannot be negative.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "Unknown";
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies != null ? allergies : new ArrayList<>();
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications != null ? medications : new ArrayList<>();
    }

    public String getCheckupReminders() {
        return checkupReminders;
    }

    public void setCheckupReminders(String checkupReminders) {
        this.checkupReminders = checkupReminders != null ? checkupReminders : "None";
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure != null ? bloodPressure : "Normal";
    }

    /**
     * Provides a summary string representation of the medical record.
     *
     * @return A string summarizing the patient's medical record.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Name (on a separate line)
        sb.append(String.format("Name: %s\n", name));

        // Age (on a separate line)
        sb.append(String.format("Age: %d\n", age));

        // Blood Group (on a separate line)
        sb.append(String.format("Blood Group: %s\n", bloodGroup));

        // Blood Sugar (on a separate line)
        sb.append(String.format("Blood Sugar: %.2f\n", bloodSugar));

        // Blood Pressure (on a separate line)
        sb.append(String.format("Blood Pressure: %s\n", bloodPressure));

        // Add Allergies, if any (on a separate line)
        if (allergies != null && !allergies.isEmpty()) {
            sb.append("Allergies: ");
            for (String allergy : allergies) {
                sb.append(allergy).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length()); // Remove trailing comma
            sb.append("\n");
        } else {
            sb.append("Allergies: None\n");
        }

        // Add Medications, if any (on a separate line)
        if (medications != null && !medications.isEmpty()) {
            sb.append("Medications: ");
            for (String medication : medications) {
                sb.append(medication).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length()); // Remove trailing comma
            sb.append("\n");
        } else {
            sb.append("Medications: None\n");
        }

        // Add Checkup Reminders (on a separate line)
        sb.append(String.format("Checkup Reminders: %s\n", checkupReminders));

        return sb.toString();
    }
}
