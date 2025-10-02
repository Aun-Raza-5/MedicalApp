import java.io.*;
import java.util.*;

public class MedicalRecordSystem {
    private Map<String, String[]> users; // username -> [password, householdID]
    private Map<String, MedicalRecord> records; // patient name -> MedicalRecord
    private Map<String, List<String>> households; // householdID -> list of patient names

    public MedicalRecordSystem() {
        users = new HashMap<>();
        records = new HashMap<>();
        households = new HashMap<>();
        loadUsersFromFile();
        loadRecordsFromFile();
    }

    // Load users from file
    private void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("userData.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) { // username, password, householdID
                    users.put(parts[0], new String[]{parts[1], parts[2]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecordsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("medicalData.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
            	// Match commas outside of square brackets and double quotes
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)(?=(?:[^\\[]*\\[[^\\]]*\\])*[^\\]]*$)");
                // Ensure the parts are enough (at least 7 fields expected)
                if (parts.length >= 7) {
                    String name = parts[0].trim();
                    int age = Integer.parseInt(parts[1].trim());
                    String allergiesString = parts[2].trim();
                    String medicationsString = parts[3].trim();
                    String checkupReminders = parts[4].trim();
                    String bloodGroup = parts[5].trim();
                    String householdID = parts[6].trim();

                    MedicalRecord record = new MedicalRecord(name, age);

                    // Parse allergies, which are separated by semicolons
                    List<String> allergies = new ArrayList<>();
                    if (!allergiesString.isEmpty()) {
                        allergies = Arrays.asList(allergiesString.split(";"));
                    }
                    record.setAllergies(allergies);

                    // Parse medications, which are inside square brackets
                    List<String> medications = new ArrayList<>();
                    if (medicationsString.startsWith("[") && medicationsString.endsWith("]")) {
                        // Remove the square brackets and split by commas inside the brackets
                        String medicationsContent = medicationsString.substring(1, medicationsString.length() - 1).trim();
                        medications = Arrays.asList(medicationsContent.split(","));
                    } else if (!medicationsString.isEmpty()) {
                        // If no brackets, treat the whole string as a single medication
                        medications.add(medicationsString);
                    }
                    record.setMedications(medications);

                    // Set checkup reminders and blood group (trim if empty)
                    record.setCheckupReminders(checkupReminders.isEmpty() ? "" : checkupReminders);
                    record.setBloodGroup(bloodGroup.isEmpty() ? "" : bloodGroup);

                    // Store the record in the records map
                    records.put(name, record);

                    // Add patient to household list
                    households.computeIfAbsent(householdID, k -> new ArrayList<>()).add(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    // Authenticate user login
    public boolean authenticateUser(String username, String password) {
        return users.containsKey(username) && users.get(username)[0].equals(password);
    }

    // Add a new user
    public void addUser(String username, String password, String householdID) {
        users.put(username, new String[]{password, householdID});
        saveUsersToFile();
    }

    // Save users data to file
    private void saveUsersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("userData.txt"))) {
            for (Map.Entry<String, String[]> entry : users.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue()[0] + "," + entry.getValue()[1] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add a new medical record for a patient
    public void addRecord(MedicalRecord record, String householdID) {
        if (!records.containsKey(record.getName())) {
            records.put(record.getName(), record);
            households.computeIfAbsent(householdID, k -> new ArrayList<>()).add(record.getName());
            saveRecordsToFile();
        }
    }

    private void saveRecordsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("medicalData.txt"))) {
            for (MedicalRecord record : records.values()) {
                String allergiesString = String.join(";", record.getAllergies());
                String medicationsString = "[" + String.join(",", record.getMedications()) + "]"; // Add brackets for medications

                // Write data to file in the format: name, age, allergies, medications, reminders, blood group, household ID
                bw.write(record.getName() + "," + record.getAge() + "," + allergiesString + "," +
                        medicationsString + "," + record.getCheckupReminders() + "," + record.getBloodGroup() +
                        "," + getHouseholdIDForRecord(record) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Delete a specific medical record
    public void deleteRecord(String name, String householdID) {
        if (households.containsKey(householdID) && households.get(householdID).contains(name)) {
            records.remove(name);
            households.get(householdID).remove(name);
            saveRecordsToFile();
        }
    }

    public List<MedicalRecord> getRecordsForHousehold(String householdID) {
        List<String> patientNames = households.getOrDefault(householdID, new ArrayList<>());
        List<MedicalRecord> householdRecords = new ArrayList<>();


        for (String patientName : patientNames) {
            MedicalRecord record = records.get(patientName);
            if (record != null) {
                householdRecords.add(record);
            } else {
                System.out.println("Warning: No record found for " + patientName);
            }
        }
        return householdRecords;
    }


 // Get patient details by patient name directly (without checking household patients list)
    public String getPatientDetails(String patientName) {
        // Check if the patient record exists
        MedicalRecord record = records.get(patientName);
        if (record != null) {
            return record.toString();  // Return the patient's details
        }
        return "Patient not found.";  // If the patient does not exist
    }


    // Get the household ID for a user
    public String getUserHousehold(String username) {
        return users.getOrDefault(username, new String[]{"", null})[1];
    }

    public MedicalRecord getPatientRecordByName(String patientName) {
        // Check if the records map contains the patient's name
        MedicalRecord record = records.get(patientName);
        
        if (record != null) {
            return record; // Return the record if found
        } else {
            // Return null or a message if no record is found
            System.out.println("No record found for patient: " + patientName);
            return null; // Patient record not found
        }
    }



    // Get household ID for a record
    private String getHouseholdIDForRecord(MedicalRecord record) {
        for (Map.Entry<String, List<String>> entry : households.entrySet()) {
            if (entry.getValue().contains(record.getName())) {
                return entry.getKey();
            }
        }
        return "";
    }

    // Delete a user from the system
    public void deleteUser(String householdID) {
        File inputFile = new File("userData.txt");
        File tempFile = new File("userData_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts.length == 3 && parts[2].trim().equalsIgnoreCase(householdID.trim())) {
                    continue; // Skip this line
                }
                writer.write(currentLine + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (!inputFile.delete()) {
            System.out.println("Could not delete original file");
            return;
        }

        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename temp file to original");
        }
    }
}
