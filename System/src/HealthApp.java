import javax.swing.*;

public class HealthApp extends JFrame {
    private static final long serialVersionUID = 1L;
    private MedicalRecordSystem system; // Declare system at the class level

    public HealthApp() {
        setTitle("Health App");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        system = new MedicalRecordSystem(); // Initialize the system once
        initLoginPanel(); // Start directly with the login panel
    }

    // Initialize Login Panel
    private void initLoginPanel() {
        LoginPanel loginPanelInstance = new LoginPanel(this, system); // Pass the system instance
        setContentPane(loginPanelInstance);
        revalidate();
        repaint();
    }

    // Method to switch to the MainPanel after successful login
    public void switchToMainPanel(String username, String householdID) {
        MainPanel mainPanel = new MainPanel(system, householdID); // Pass the same system instance
        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HealthApp().setVisible(true));
    }
}
