import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class ViewSummaryPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MedicalRecordSystem system;
    private String householdID;
    private String patientName;
    private JButton closeButton;
    private JButton printButton;

    public ViewSummaryPanel(JFrame parentFrame, MedicalRecordSystem system, String householdID, String patientName) {
        this.system = system;
        this.householdID = householdID;
        this.patientName = patientName;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // Light gray background

        // **Top Panel - Heading**
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(34, 45, 65)); // Dark navy blue

        JLabel titleLabel = new JLabel("<html>➕ Patient Details</html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // **Center Panel - Patient Details Grid**
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; // Ensure the components are left-aligned
        gbc.insets = new Insets(5, 5, 5, 5);

        // Retrieve the patient details from the system
        String patientDetails = system.getPatientDetails(patientName);
        String[] detailsArray = patientDetails.split("\n");

        // Loop through the patient details and add to the grid
        int gridY = 0;

        // Name (Check if it exists)
        if (detailsArray.length > 0) {
            String name = detailsArray[0].split(":")[1].trim();
            addDetailToPanel(centerPanel, gbc, "Name:", name, gridY++);
        }

        // Age (Check if it exists)
        if (detailsArray.length > 1) {
            String age = detailsArray[1].split(":")[1].trim();
            addDetailToPanel(centerPanel, gbc, "Age:", age, gridY++);
        }

        // Blood Group (Check if it exists)
        if (detailsArray.length > 2) {
            String bloodGroup = detailsArray[2].split(":")[1].trim();
            addDetailToPanel(centerPanel, gbc, "Blood Group:", bloodGroup, gridY++);
        }

        // Blood Sugar (Check if it exists)
        if (detailsArray.length > 3) {
            String bloodSugar = detailsArray[3].split(":")[1].trim();
            addDetailToPanel(centerPanel, gbc, "Blood Sugar:", bloodSugar, gridY++);
        }

        // Blood Pressure (Check if it exists)
        if (detailsArray.length > 4) {
            String bloodPressure = detailsArray[4].split(":")[1].trim();
            addDetailToPanel(centerPanel, gbc, "Blood Pressure:", bloodPressure, gridY++);
        }

        // Allergies (Check if it exists)
        if (detailsArray.length > 5) {
            String allergies = detailsArray[5].split(":")[1].trim();
            addDetailToPanel(centerPanel, gbc, "Allergies:", allergies, gridY++);
        }

        // Medications (Check if it exists)
        if (detailsArray.length > 6) {
            String medications = detailsArray[6].split(":")[1].trim();
            addDetailToPanel(centerPanel, gbc, "Medications:", medications, gridY++);
        }

        // Checkup Reminders (Check if it exists)
        if (detailsArray.length > 7) {
            String checkupReminders = detailsArray[7].split(":")[1].trim();
            addDetailToPanel(centerPanel, gbc, "Checkup Reminders:", checkupReminders, gridY++);
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setPreferredSize(new Dimension(600, 350));  // Adjust size for better visibility
        add(scrollPane, BorderLayout.CENTER);

        // **Bottom Panel - Print and Back Buttons**
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);

        // Print Button
        printButton = new JButton("Print");
        printButton.setBackground(new Color(34, 45, 65)); // Dark navy blue color
        printButton.setForeground(Color.WHITE);
        printButton.setFont(new Font("Arial", Font.BOLD, 14));
        printButton.addActionListener(e -> printPatientDetails());
        bottomPanel.add(printButton);

        // Close (Back) Button
        closeButton = new JButton("Back");
        closeButton.setBackground(new Color(255, 69, 0)); // Red-Orange color for Close button
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> returnToMainPanel(parentFrame));
        bottomPanel.add(closeButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void returnToMainPanel(JFrame parentFrame) {
        parentFrame.setContentPane(new MainPanel(system, householdID));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void printPatientDetails() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        // Set up the print job
        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            // Set up the graphics to draw the patient details
            graphics.setFont(new Font("Arial", Font.PLAIN, 16));
            graphics.drawString("Patient Details for " + patientName, 100, 100);

            // Draw each detail of the patient
            String[] detailsArray = system.getPatientDetails(patientName).split("\n");
            int yPosition = 120;
            for (String detail : detailsArray) {
                graphics.drawString(detail, 100, yPosition);
                yPosition += 20;
            }

            return Printable.PAGE_EXISTS;
        });

        // Show the print dialog and print if the user approves
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                JOptionPane.showMessageDialog(this, "Patient details printed successfully.");
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "An error occurred while printing.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addDetailToPanel(JPanel panel, GridBagConstraints gbc, String label, String value, int gridY) {
        // Create label with non-bold font
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Non-bold font for labels
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 1;
        panel.add(jLabel, gbc);

        // Create larger, bold non-editable text field with border
        JTextField jTextField = new JTextField(value);
        jTextField.setFont(new Font("Arial", Font.BOLD, 16)); // Bold font for values
        jTextField.setEditable(false); // Make the text field non-editable
        jTextField.setBackground(Color.WHITE);
        jTextField.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1)); // Border around text field
        jTextField.setPreferredSize(new Dimension(300, 40)); // Larger size for text fields

        gbc.gridx = 1;
        panel.add(jTextField, gbc);
    }
}

