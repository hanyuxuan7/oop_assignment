package data;

import entity.Internship;
import java.util.Collection;

/**
 * Repository interface for accessing and managing internship data.
 * Defines operations for CRUD operations on Internship entities.
 *
 * @version 1.0
 */
public interface InternshipRepository {
    /**
     * Adds a new internship to the repository.
     *
     * @param internship the internship to add
     */
    void addInternship(Internship internship);
    
    /**
     * Retrieves an internship by its ID.
     *
     * @param internshipID the ID of the internship to retrieve
     * @return the internship with the specified ID, or null if not found
     */
    Internship getInternship(String internshipID);
    
    /**
     * Retrieves all internships in the repository.
     *
     * @return a collection of all internships
     */
    Collection<Internship> getAllInternships();
    
    /**
     * Removes an internship from the repository by its ID.
     *
     * @param internshipID the ID of the internship to remove
     */
    void removeInternship(String internshipID);
    
    /**
     * Saves all internships to a file.
     *
     * @param filePath the file path where internships will be saved
     */
    void saveInternships(String filePath);
    
    /**
     * Loads all internships from a file.
     *
     * @param filePath the file path from which to load internships
     */
    void loadInternships(String filePath);
}
