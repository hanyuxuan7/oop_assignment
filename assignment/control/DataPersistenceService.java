package control;

import data.PersistenceManager;

/**
 * Service class for coordinating data persistence operations.
 * Provides methods to save different types of data to persistent storage.
 * Acts as a facade to the PersistenceManager for data saving operations.
 *
 * @version 1.0
 */
public class DataPersistenceService {
    /** Manager for persistent data operations */
    private final PersistenceManager persistenceManager;

    /**
     * Constructs a DataPersistenceService with a PersistenceManager dependency.
     *
     * @param persistenceManager the persistence manager to use for saving data
     */
    public DataPersistenceService(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    /**
     * Persists all data to storage.
     * Saves all entity data including students, internships, applications, and activity logs.
     */
    public void persistAllData() {
        persistenceManager.saveAll();
    }

    /**
     * Persists student data to storage.
     */
    public void persistStudentData() {
        persistenceManager.saveStudents();
    }

    /**
     * Persists internship data to storage.
     */
    public void persistInternshipData() {
        persistenceManager.saveInternships();
    }

    /**
     * Persists application data to storage.
     */
    public void persistApplicationData() {
        persistenceManager.saveApplications();
    }

    /**
     * Persists activity logs to storage.
     */
    public void persistActivityLogs() {
        persistenceManager.saveActivityLogs();
    }
}
