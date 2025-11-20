package data;

/**
 * Interface for managing data persistence operations.
 * Defines methods for loading and saving various entity data.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public interface PersistenceManager {
    /**
     * Loads all data from storage.
     */
    void loadAll();
    
    /**
     * Saves all data to storage.
     */
    void saveAll();
    
    /**
     * Saves student data.
     */
    void saveStudents();
    
    /**
     * Saves staff data.
     */
    void saveStaff();
    
    /**
     * Saves company representative data.
     */
    void saveCompanyReps();
    
    /**
     * Saves internship data.
     */
    void saveInternships();
    
    /**
     * Saves application data.
     */
    void saveApplications();
    
    /**
     * Saves activity logs.
     */
    void saveActivityLogs();
}
