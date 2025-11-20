package data;

import entity.InternshipApplication;
import java.util.Collection;

/**
 * Repository interface for accessing and managing internship application data.
 * Defines operations for CRUD operations on InternshipApplication entities.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public interface ApplicationRepository {
    /**
     * Adds a new application to the repository.
     *
     * @param application the application to add
     */
    void addApplication(InternshipApplication application);
    
    /**
     * Retrieves an application by its ID.
     *
     * @param applicationID the ID of the application to retrieve
     * @return the application with the specified ID, or null if not found
     */
    InternshipApplication getApplication(String applicationID);
    
    /**
     * Retrieves all applications in the repository.
     *
     * @return a collection of all applications
     */
    Collection<InternshipApplication> getAllApplications();
    
    /**
     * Removes an application from the repository by its ID.
     *
     * @param applicationID the ID of the application to remove
     */
    void removeApplication(String applicationID);
    
    /**
     * Saves all applications to a file.
     *
     * @param filePath the file path where applications will be saved
     */
    void saveApplications(String filePath);
    
    /**
     * Loads all applications from a file.
     *
     * @param filePath the file path from which to load applications
     */
    void loadApplications(String filePath);
}
