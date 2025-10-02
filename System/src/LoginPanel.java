import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel dateTimeLabel, titleLabel;
    private JFrame parentFrame;
    private MedicalRecordSystem system;
    private JComboBox<String> householdComboBox;
    private JButton addHouseholdButton, deleteHouseholdButton, loginButton;

    public LoginPanel(JFrame parentFrame, MedicalRecordSystem system) {
        this.parentFrame = parentFrame;
        this.system = system;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // Light gray background

        // Top panel with title and timestamp
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(34, 45, 65)); // Dark navy blue

        titleLabel = new JLabel("<html>➕ Health App</html>", SwingConstants.CENTER); // Updated to hospital plus symbol
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        dateTimeLabel = new JLabel();
        dateTimeLabel.setForeground(Color.WHITE);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(dateTimeLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center panel with login form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(255, 255, 255)); // White background
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel loginScreenLabel = new JLabel("Secure Access Portal", SwingConstants.CENTER); // Text without emoji
        loginScreenLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Consistent font
        loginScreenLabel.setHorizontalAlignment(SwingConstants.CENTER); // Ensures proper alignment

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Spans both columns

        centerPanel.add(loginScreenLabel, gbc);

        
        gbc.gridwidth = 1;
        JLabel householdLabel = new JLabel("<html>🏠 Select Household:</html>");
        householdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 1;
        gbc.gridx = 0;
        centerPanel.add(householdLabel, gbc);

        householdComboBox = new JComboBox<>(getHouseholds());
        gbc.gridx = 1;
        centerPanel.add(householdComboBox, gbc);

        JLabel usernameLabel = new JLabel("<html>👤 Enter Username:</html>");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 2;
        gbc.gridx = 0;
        centerPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        centerPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("<html>🔒 Enter Password:</html>");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 3;
        gbc.gridx = 0;
        centerPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);

        loginButton = new JButton("Sign In");
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> authenticateUser());
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        centerPanel.add(loginButton, gbc);

        addHouseholdButton = new JButton("<html>➕ Add Household</html>");
        addHouseholdButton.setBackground(new Color(255, 165, 0));
        addHouseholdButton.setForeground(Color.WHITE);
        addHouseholdButton.setFont(new Font("Arial", Font.BOLD, 14));
        addHouseholdButton.setFocusPainted(false);
        addHouseholdButton.addActionListener(e -> addHousehold());
        gbc.gridy = 5;
        centerPanel.add(addHouseholdButton, gbc);

        deleteHouseholdButton = new JButton("<html>➖ Delete Household</html>");
        deleteHouseholdButton.setBackground(new Color(220, 53, 69));
        deleteHouseholdButton.setForeground(Color.WHITE);
        deleteHouseholdButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteHouseholdButton.setFocusPainted(false);
        deleteHouseholdButton.addActionListener(e -> deleteHousehold());
        gbc.gridy = 6;
        centerPanel.add(deleteHouseholdButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
        startClock();
    }
    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedHousehold = (String) householdComboBox.getSelectedItem();

        if (system.authenticateUser(username, password)) {
            ((HealthApp) parentFrame).switchToMainPanel(username, selectedHousehold);
        } else {
            JOptionPane.showMessageDialog(this, "<html>❌ Invalid credentials.</html>");
        }
    }

    private void addHousehold() {
        String householdName = JOptionPane.showInputDialog(this, "Enter new household name:");
        String username = JOptionPane.showInputDialog(this, "Enter username for the household:");
        String password = JOptionPane.showInputDialog(this, "Enter password for the household:");

        if (householdName != null && username != null && password != null) {
            system.addUser(username, password, householdName);
            JOptionPane.showMessageDialog(this, "<html>✔ Household added successfully.</html>");
            householdComboBox.setModel(new DefaultComboBoxModel<>(getHouseholds()));
        }
    }

    private void deleteHousehold() {
        String selectedHousehold = (String) householdComboBox.getSelectedItem();
        String password = JOptionPane.showInputDialog(this, "Enter password for the household:");

        if (selectedHousehold != null && password != null && system.authenticateUser(selectedHousehold, password)) {
            system.deleteUser(selectedHousehold);
            JOptionPane.showMessageDialog(this, "<html>✔ Household deleted successfully.</html>");
            householdComboBox.setModel(new DefaultComboBoxModel<>(getHouseholds()));
        } else {
            JOptionPane.showMessageDialog(this, "<html>❌ Invalid password.</html>");
        }
    }

    private String[] getHouseholds() {
        Set<String> householdSet = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("userData.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) householdSet.add(parts[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return householdSet.toArray(new String[0]);
    }

    private void startClock() {
        new Timer(1000, e -> dateTimeLabel.setText(new SimpleDateFormat("hh:mm:ss a").format(new Date()))).start();
    }
}