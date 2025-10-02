import javax.swing.*;

import javax.swing.border.TitledBorder;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AddPatientPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField nameField, ageField, allergiesField, medicationsField, checkupRemindersField, bloodGroupField, bloodSugarField, bloodPressureField;
    private MedicalRecordSystem system;
    private String householdID;
    private JLabel currentTimeLabel;  // Label for current time

    public AddPatientPanel(JFrame parentFrame, MedicalRecordSystem system, String householdID) {
        this.system = system;
        this.householdID = householdID;
        setLayout(new BorderLayout());  // Set layout for the main panel

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());  // Set layout to BorderLayout
        topPanel.setBackground(new Color(34, 45, 65));  // Set background color

        // Title label in the center
        JLabel titleLabel = new JLabel("Add Patient", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));  // Increased font size
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);  // Add title in the center

        // Current time label on the right
        currentTimeLabel = new JLabel();
        currentTimeLabel.setFont(new Font("Arial", Font.PLAIN, 16));  // Increased font size
        currentTimeLabel.setForeground(Color.WHITE);
        topPanel.add(currentTimeLabel, BorderLayout.EAST);  // Add time on the right side
        startClock();

        // Add topPanel to your main panel (use BorderLayout for your main panel)
        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Form for Patient Details
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Patient Details", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 20), Color.DARK_GRAY));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Increased padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.PLAIN, 18);  // Larger font for labels
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);  // Larger font for text fields

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(fieldFont);
        formPanel.add(nameField, gbc);

        // Age field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        formPanel.add(ageLabel, gbc);
        gbc.gridx = 1;
        ageField = new JTextField(20);
        ageField.setFont(fieldFont);
        formPanel.add(ageField, gbc);

        // Allergies field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel allergiesLabel = new JLabel("Allergies (comma separated):");
        allergiesLabel.setFont(labelFont);
        formPanel.add(allergiesLabel, gbc);
        gbc.gridx = 1;
        allergiesField = new JTextField(20);
        allergiesField.setFont(fieldFont);
        formPanel.add(allergiesField, gbc);

        // Medications field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel medicationsLabel = new JLabel("Medications:");
        medicationsLabel.setFont(labelFont);
        formPanel.add(medicationsLabel, gbc);
        gbc.gridx = 1;
        medicationsField = new JTextField(20);
        medicationsField.setFont(fieldFont);
        formPanel.add(medicationsField, gbc);

        // Checkup Reminders field
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel checkupRemindersLabel = new JLabel("Checkup Reminders:");
        checkupRemindersLabel.setFont(labelFont);
        formPanel.add(checkupRemindersLabel, gbc);
        gbc.gridx = 1;
        checkupRemindersField = new JTextField(20);
        checkupRemindersField.setFont(fieldFont);
        formPanel.add(checkupRemindersField, gbc);

        // Blood Group field
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel bloodGroupLabel = new JLabel("Blood Group:");
        bloodGroupLabel.setFont(labelFont);
        formPanel.add(bloodGroupLabel, gbc);
        gbc.gridx = 1;
        bloodGroupField = new JTextField(20);
        bloodGroupField.setFont(fieldFont);
        formPanel.add(bloodGroupField, gbc);

        // Blood Sugar field
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel bloodSugarLabel = new JLabel("Blood Sugar (mg/dL):");
        bloodSugarLabel.setFont(labelFont);
        formPanel.add(bloodSugarLabel, gbc);
        gbc.gridx = 1;
        bloodSugarField = new JTextField(20);
        bloodSugarField.setFont(fieldFont);
        formPanel.add(bloodSugarField, gbc);

        // Blood Pressure field
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel bloodPressureLabel = new JLabel("Blood Pressure (e.g., 120/80):");
        bloodPressureLabel.setFont(labelFont);
        formPanel.add(bloodPressureLabel, gbc);
        gbc.gridx = 1;
        bloodPressureField = new JTextField(20);
        bloodPressureField.setFont(fieldFont);
        formPanel.add(bloodPressureField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Bottom Panel - Buttons
        JPanel buttonPanel = new JPanel();

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(40, 167, 69));  // Set Save button to green
        saveButton.setForeground(Color.WHITE);  // Set text color to white
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));  // Set font to bold and larger
        saveButton.addActionListener(e -> savePatient());
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(220, 53, 69));  // Set Cancel button to red
        cancelButton.setForeground(Color.WHITE);  // Set text color to white
        cancelButton.setFont(new Font("Arial", Font.BOLD, 18));  // Set font to bold and larger
        cancelButton.addActionListener(e -> cancel());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Start the clock to update the time every second
    private void startClock() {
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    // Update the current time and set it on the label
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  // Format: HH:mm:ss
        String currentTime = sdf.format(new Date());
        currentTimeLabel.setText(currentTime);
    }

    private void savePatient() {
        String patientName = nameField.getText();
        String ageString = ageField.getText();
        String allergiesString = allergiesField.getText();
        String medications = medicationsField.getText();
        String checkupReminders = checkupRemindersField.getText();
        String bloodGroup = bloodGroupField.getText();
        String bloodSugarString = bloodSugarField.getText();
        String bloodPressure = bloodPressureField.getText();

        try {
            int age = Integer.parseInt(ageString);
            double bloodSugar = Double.parseDouble(bloodSugarString);

            if (!patientName.trim().isEmpty()) {
                // Convert comma-separated allergies into lists, even if only one item
                List<String> allergiesList = new ArrayList<>();
                if (!allergiesString.trim().isEmpty()) {
                    allergiesList = new ArrayList<>(Arrays.asList(allergiesString.split(","))); // Split using comma
                }

                // Convert comma-separated medications into lists, even if only one item
                List<String> medicationsList = new ArrayList<>();
                if (!medications.trim().isEmpty()) {
                    medicationsList = new ArrayList<>(Arrays.asList(medications.split(","))); // Split using comma
                }

                // Create a new MedicalRecord with the updated list-based fields
                MedicalRecord record = new MedicalRecord(patientName, age);
                record.setAllergies(allergiesList);  // Set the allergies as a list
                record.setMedications(medicationsList);  // Set the medications as a list
                record.setCheckupReminders(checkupReminders);
                record.setBloodGroup(bloodGroup);
                record.setBloodSugar(bloodSugar);
                record.setBloodPressure(bloodPressure);

                // Add the record to the system
                system.addRecord(record, householdID);
                JOptionPane.showMessageDialog(this, "Patient added successfully!");

                // After saving, return to the main panel
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                parentFrame.setContentPane(new MainPanel(system, householdID));
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid age or blood sugar format.");
        }
    }



    private void cancel() {
        // Cancel and return to main panel
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.setContentPane(new MainPanel(system, this.householdID));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
