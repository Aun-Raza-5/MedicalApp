import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel userLabel, currentTimeLabel;
    private JList<String> patientList;
    private DefaultListModel<String> listModel;
    private MedicalRecordSystem system;
    private String householdID;
    private JButton addPatientButton, deletePatientButton, logoutButton;
    private JPanel bottomPanel, patientActionPanel;
    private JButton viewSummaryButton, editPatientButton;
    private boolean isPatientSelected = false;

    public MainPanel(MedicalRecordSystem system, String householdID) {
        this.system = system;
        this.householdID = householdID;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // **Top Panel - Heading & Time**
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(34, 45, 65));

        userLabel = new JLabel("<html>➕ Health App</html>", SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.BOLD, 42));
        userLabel.setForeground(Color.WHITE);
        topPanel.add(userLabel, BorderLayout.CENTER);

        currentTimeLabel = new JLabel();
        currentTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        currentTimeLabel.setForeground(Color.WHITE);
        topPanel.add(currentTimeLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // **Center Panel - Patient List**
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel patientBoxPanel = new JPanel(new BorderLayout());
        patientBoxPanel.setBackground(Color.WHITE);
        patientBoxPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Registered Patients", 
            TitledBorder.CENTER, TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 20), Color.DARK_GRAY));
        patientBoxPanel.setPreferredSize(new Dimension(300, 400));

        listModel = new DefaultListModel<>();
        patientList = new JList<>(listModel);
        patientList.setCellRenderer(new CustomCellRenderer());
        patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientList.setFont(new Font("Arial", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(patientList);
        scrollPane.setPreferredSize(new Dimension(250, 300));

        patientBoxPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(patientBoxPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // **Bottom Panel - Equal Button Spacing**
        bottomPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // 3 buttons, equal spacing
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Color buttonColor = new Color(34, 45, 65);
        Color buttonTextColor = Color.WHITE;

        addPatientButton = new JButton("Add Patient");
        styleButton(addPatientButton, buttonColor, buttonTextColor);
        addPatientButton.addActionListener(e -> addPatient());

        deletePatientButton = new JButton("Delete Patient");
        styleButton(deletePatientButton, buttonColor, buttonTextColor);
        deletePatientButton.addActionListener(e -> deletePatient());

        logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(255, 69, 0), Color.WHITE);
        logoutButton.addActionListener(e -> logout());

        bottomPanel.add(addPatientButton);
        bottomPanel.add(deletePatientButton);
        bottomPanel.add(logoutButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // **Patient Action Panel**
        patientActionPanel = new JPanel(new FlowLayout());
        patientActionPanel.setBackground(Color.WHITE);
        patientActionPanel.setVisible(false);

        viewSummaryButton = new JButton("View Summary");
        styleButton(viewSummaryButton, buttonColor, buttonTextColor);
        viewSummaryButton.addActionListener(e -> viewPatientSummary());
        viewSummaryButton.setEnabled(false);

        editPatientButton = new JButton("Edit Patient");
        styleButton(editPatientButton, buttonColor, buttonTextColor);
        editPatientButton.addActionListener(e -> editPatient());
        editPatientButton.setEnabled(false);

        patientActionPanel.add(viewSummaryButton);
        patientActionPanel.add(editPatientButton);
        centerPanel.add(patientActionPanel, BorderLayout.SOUTH);

        // **List Selection Behavior (Toggle Select/Deselect)**
        patientList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedIndex = patientList.getSelectedIndex();
                if (selectedIndex != -1) {
                    isPatientSelected = !isPatientSelected;
                    togglePatientActionButtons(isPatientSelected);
                }
            }
        });


        startClock();
        updatePatientList(system.getRecordsForHousehold(householdID));
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void togglePatientActionButtons(boolean enable) {
        viewSummaryButton.setEnabled(enable);
        editPatientButton.setEnabled(enable);
        patientActionPanel.setVisible(enable); // Make panel visible when a patient is selected
    }

    
    public void updatePatientList(List<MedicalRecord> records) {
        listModel.clear();
        for (int i = 0; i < records.size(); i++) {
            String patientName = records.get(i).getName();
            listModel.addElement((i + 1) + ". " + patientName);
        }
    }


    private void addPatient() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.setContentPane(new AddPatientPanel(parentFrame, system, householdID));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void deletePatient() {
        int selectedIndex = patientList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedPatientName = listModel.get(selectedIndex).replaceAll("^[0-9]+\\.", "").trim();
            int confirmation = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete " + selectedPatientName + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                system.deleteRecord(selectedPatientName, householdID);
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                updatePatientList(system.getRecordsForHousehold(householdID));
                togglePatientActionButtons(false);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.");
        }
    }

    private void logout() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.setContentPane(new LoginPanel(parentFrame, system));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
        currentTimeLabel.setText(formatter.format(new Date()));
    }

    private void viewPatientSummary() {
        int selectedIndex = patientList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedPatientName = listModel.get(selectedIndex).replaceAll("^[0-9]+\\.", "").trim();
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            parentFrame.setContentPane(new ViewSummaryPanel(parentFrame, system, householdID, selectedPatientName));
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }

    private void editPatient() {
        int selectedIndex = patientList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedPatientName = listModel.get(selectedIndex).replaceAll("^[0-9]+\\.", "").trim();
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            parentFrame.setContentPane(new EditPatientPanel(parentFrame, system, householdID, system.getPatientRecordByName(selectedPatientName)));
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }
}
