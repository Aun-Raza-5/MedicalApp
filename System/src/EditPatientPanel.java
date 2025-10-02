import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EditPatientPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField nameField, ageField, allergiesField, medicationsField, checkupRemindersField, bloodGroupField, bloodSugarField, bloodPressureField;
    private JButton saveButton, cancelButton;
    private MedicalRecord record;
    private MedicalRecordSystem system;
    private String householdID;
    private JLabel currentTimeLabel;  // Label for current time
    private JLabel topPanelLabel;  // Label for top panel

    public EditPatientPanel(JFrame parentFrame, MedicalRecordSystem system, String householdID, MedicalRecord record) {
        this.system = system;
        this.householdID = householdID;
        this.record = record;

        setLayout(new BorderLayout());

        // Top Panel with title and timer
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(34, 45, 65));  // Background color for consistency with AddPatientPanel
        
        topPanelLabel = new JLabel("Editing Patient", SwingConstants.CENTER);
        topPanelLabel.setFont(new Font("Arial", Font.BOLD, 28));
        topPanelLabel.setForeground(Color.WHITE);
        topPanel.add(topPanelLabel, BorderLayout.CENTER);

        // Current time label on the right side of the top panel
        currentTimeLabel = new JLabel();
        currentTimeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        currentTimeLabel.setForeground(Color.WHITE);
        topPanel.add(currentTimeLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Start timer to update the time every second
        startClock();

        // Main content area - form for editing patient details
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding for consistency with AddPatientPanel
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.PLAIN, 18);  // Larger font for labels
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);  // Larger font for text fields

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(record.getName(), 20);
        nameField.setFont(fieldFont);
        contentPanel.add(nameField, gbc);

        // Age field
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(String.valueOf(record.getAge()), 20);
        ageField.setFont(fieldFont);
        contentPanel.add(ageField, gbc);

        // Allergies field
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(new JLabel("Allergies (comma separated):"), gbc);
        gbc.gridx = 1;
        allergiesField = new JTextField(String.join(", ", record.getAllergies()), 20);
        allergiesField.setFont(fieldFont);
        contentPanel.add(allergiesField, gbc);

        // Medications field
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(new JLabel("Medications (comma separated):"), gbc);
        gbc.gridx = 1;
        medicationsField = new JTextField(String.join(", ", record.getMedications()), 20);  // Changed to handle list of medications
        medicationsField.setFont(fieldFont);
        contentPanel.add(medicationsField, gbc);

        // Checkup reminders field
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(new JLabel("Checkup Reminders:"), gbc);
        gbc.gridx = 1;
        checkupRemindersField = new JTextField(record.getCheckupReminders(), 20);
        checkupRemindersField.setFont(fieldFont);
        contentPanel.add(checkupRemindersField, gbc);

        // Blood group field
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(new JLabel("Blood Group:"), gbc);
        gbc.gridx = 1;
        bloodGroupField = new JTextField(record.getBloodGroup(), 20);
        bloodGroupField.setFont(fieldFont);
        contentPanel.add(bloodGroupField, gbc);

        // Blood sugar field
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(new JLabel("Blood Sugar (mg/dL):"), gbc);
        gbc.gridx = 1;
        bloodSugarField = new JTextField(String.valueOf(record.getBloodSugar()), 20);
        bloodSugarField.setFont(fieldFont);
        contentPanel.add(bloodSugarField, gbc);

        // Blood pressure field
        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPanel.add(new JLabel("Blood Pressure (e.g., 120/80):"), gbc);
        gbc.gridx = 1;
        bloodPressureField = new JTextField(record.getBloodPressure(), 20);
        bloodPressureField.setFont(fieldFont);
        contentPanel.add(bloodPressureField, gbc);

        // Add some space before the buttons
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;  // Make sure the buttons span the full width
        contentPanel.add(Box.createVerticalStrut(20), gbc);  // Adds space between fields and buttons

        // Button panel for Save and Cancel buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));  // Center buttons with some spacing
        
        saveButton = new JButton("Save");
        saveButton.setBackground(new Color(40, 167, 69));  // Green button for save
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.addActionListener(e -> savePatientChanges(parentFrame));
        buttonPanel.add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(220, 53, 69));  // Red button for cancel
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 18));
        cancelButton.addActionListener(e -> cancelEdit(parentFrame));
        buttonPanel.add(cancelButton);

        // Add button panel to the content panel
        contentPanel.add(buttonPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);
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

    private void savePatientChanges(JFrame parentFrame) {
        String newPatientName = nameField.getText().trim();
        int newAge;
        String allergiesString = allergiesField.getText().trim();
        String medicationsString = medicationsField.getText().trim();  // Changed to handle list of medications
        String checkupReminders = checkupRemindersField.getText().trim();
        String bloodGroup = bloodGroupField.getText().trim();
        double bloodSugar;
        String bloodPressure = bloodPressureField.getText().trim();

        try {
            newAge = Integer.parseInt(ageField.getText().trim());
            bloodSugar = Double.parseDouble(bloodSugarField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid age or blood sugar format.");
            return;
        }

        if (!newPatientName.isEmpty()) {
            // Delete the old record before adding the updated one
            system.deleteRecord(record.getName(), householdID);

            // Convert medications string to list
            List<String> medicationsList = Arrays.asList(medicationsString.split(","));

            // Create the updated record
            MedicalRecord updatedRecord = new MedicalRecord(newPatientName, newAge);
            updatedRecord.setAllergies(Arrays.asList(allergiesString.split(",")));
            updatedRecord.setMedications(medicationsList);  // Use medications list
            updatedRecord.setCheckupReminders(checkupReminders);
            updatedRecord.setBloodGroup(bloodGroup);
            updatedRecord.setBloodSugar(bloodSugar);
            updatedRecord.setBloodPressure(bloodPressure);

            // Add the updated record to the system
            system.addRecord(updatedRecord, householdID);

            JOptionPane.showMessageDialog(this, "Patient details updated successfully!");

            // Return to main panel after saving
            parentFrame.setContentPane(new MainPanel(system, householdID));
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }

    private void cancelEdit(JFrame parentFrame) {
        parentFrame.setContentPane(new MainPanel(system, householdID));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
