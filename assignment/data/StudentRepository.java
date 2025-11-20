package data;

import entity.Student;
import java.util.Collection;

/**
 * Repository interface for accessing and managing student data.
 * Defines operations for CRUD operations on Student entities.
 *
 * @version 1.0
 */
public interface StudentRepository {
    /**
     * Adds a new student to the repository.
     *
     * @param student the student to add
     */
    void addStudent(Student student);
    
    /**
     * Retrieves a student by their ID.
     *
     * @param studentID the ID of the student to retrieve
     * @return the student with the specified ID, or null if not found
     */
    Student getStudent(String studentID);
    
    /**
     * Retrieves all students in the repository.
     *
     * @return a collection of all students
     */
    Collection<Student> getAllStudents();
    
    /**
     * Removes a student from the repository by their ID.
     *
     * @param studentID the ID of the student to remove
     */
    void removeStudent(String studentID);
    
    /**
     * Saves all students to a file.
     *
     * @param filePath the file path where students will be saved
     */
    void saveStudents(String filePath);
    
    /**
     * Loads all students from a file.
     *
     * @param filePath the file path from which to load students
     */
    void loadStudents(String filePath);
}
