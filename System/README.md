# Medical Record System

This is a Java Swing application for managing household-based medical records. It allows users to log in, add, edit, view, and delete patient records, as well as print patient summaries.

## Features

- **User Authentication:** Login with username and password, organized by household.
- **Patient Management:** Add, edit, delete, and view patient records.
- **Medical Data:** Track allergies, medications, checkup reminders, blood group, blood sugar, and blood pressure.
- **Printing:** Print patient summaries directly from the app.
- **Persistent Storage:** Data is saved in `userData.txt` and `medicalData.txt`.

## Project Structure

```
System/
  src/
    AddPatientPanel.java
    EditPatientPanel.java
    ViewSummaryPanel.java
    MedicalRecord.java
    MedicalRecordSystem.java
    MainPanel.java
    LoginPanel.java
    HealthApp.java
    CustomCellRenderer.java
  userData.txt
  medicalData.txt
  .classpath
  .project
  .settings/
```

## How to Run

1. Open the project in Eclipse or any Java IDE.
2. Ensure Java 17+ is installed.
3. Run `HealthApp.java` from the `src` folder.

## Data Files

- `userData.txt`: Stores user credentials and household IDs.
- `medicalData.txt`: Stores patient medical records.

## Notes

- All patient and user data is stored in plain text files.
- The UI uses Java Swing for a desktop experience.

## License

This project is for educational purposes.