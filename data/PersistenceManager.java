package data;

public interface PersistenceManager {
    void loadAll();
    void saveAll();
    void saveStudents();
    void saveStaff();
    void saveCompanyReps();
    void saveInternships();
    void saveApplications();
    void saveActivityLogs();
}
