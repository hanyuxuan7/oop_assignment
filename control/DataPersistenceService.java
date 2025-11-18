package control;

import data.PersistenceManager;

public class DataPersistenceService {
    private final PersistenceManager persistenceManager;

    public DataPersistenceService(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public void persistAllData() {
        persistenceManager.saveAll();
    }

    public void persistStudentData() {
        persistenceManager.saveStudents();
    }

    public void persistInternshipData() {
        persistenceManager.saveInternships();
    }

    public void persistApplicationData() {
        persistenceManager.saveApplications();
    }

    public void persistActivityLogs() {
        persistenceManager.saveActivityLogs();
    }
}
