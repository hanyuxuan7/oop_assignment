package data;

import entity.*;
import java.util.Collection;

/**
 * Repository interface for accessing and managing user data.
 * Defines operations for CRUD operations on User entities.
 *
 * @version 1.0
 */
public interface UserRepository {
    /**
     * Adds a new user to the repository.
     *
     * @param user the user to add
     */
    void addUser(User user);
    
    /**
     * Retrieves a user by their ID.
     *
     * @param userID the ID of the user to retrieve
     * @return the user with the specified ID, or null if not found
     */
    User getUser(String userID);
    
    /**
     * Retrieves all users in the repository.
     *
     * @return a collection of all users
     */
    Collection<User> getAllUsers();
    
    /**
     * Removes a user from the repository by their ID.
     *
     * @param userID the ID of the user to remove
     */
    void removeUser(String userID);
    
    /**
     * Saves all users to a file.
     *
     * @param filePath the file path where users will be saved
     */
    void saveUsers(String filePath);
    
    /**
     * Loads all users from a file.
     *
     * @param filePath the file path from which to load users
     */
    void loadUsers(String filePath);
}
