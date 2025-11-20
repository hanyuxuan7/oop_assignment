package control;

import entity.*;
import data.UserRepository;

/**
 * Service class responsible for handling user authentication operations.
 * Manages user login, logout, password changes, and current user session tracking.
 * Validates credentials and enforces business rules such as company representative approval status.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public class AuthenticationService {
    /** Repository for accessing user data */
    private final UserRepository userRepository;
    /** Currently logged-in user, or null if no user is logged in */
    private User currentUser;

    /**
     * Constructs an AuthenticationService with a UserRepository dependency.
     *
     * @param userRepository the repository for accessing user data
     */
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUser = null;
    }

    /**
     * Authenticates a user with the provided credentials.
     * Returns different status codes based on the authentication result.
     *
     * @param userID the ID of the user attempting to login
     * @param password the password provided for authentication
     * @return "SUCCESS" if login is successful, "INVALID_USER" if user not found,
     *         "INVALID_PASSWORD" if password is incorrect, "NOT_APPROVED" if company
     *         representative is not approved by career center
     */
    public String login(String userID, String password) {
        User user = userRepository.getUser(userID);
        
        if (user == null) {
            return "INVALID_USER";
        }

        if (user instanceof CompanyRepresentative) {
            CompanyRepresentative rep = (CompanyRepresentative) user;
            if (!rep.isApproved()) {
                if (!user.validatePassword(password)) {
                    return "INVALID_PASSWORD";
                }
                return "NOT_APPROVED";
            }
        }

        if (user.validatePassword(password)) {
            this.currentUser = user;
            return "SUCCESS";
        }

        return "INVALID_PASSWORD";
    }

    /**
     * Logs out the currently logged-in user.
     * Clears the current user session.
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the current user, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Changes the password of the currently logged-in user.
     * Requires the old password to be validated first.
     *
     * @param oldPassword the user's current password
     * @param newPassword the new password to set
     * @return true if the password change is successful, false if user is not logged in or old password is incorrect
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }

        if (!currentUser.validatePassword(oldPassword)) {
            return false;
        }

        currentUser.setPassword(newPassword);
        return true;
    }

    /**
     * Resets the password for a specified user.
     * This operation does not require authentication and should be used by administrators.
     *
     * @param userID the ID of the user whose password is to be reset
     * @param newPassword the new password to set
     * @return true if the password is reset successfully, false if user is not found
     */
    public boolean resetPassword(String userID, String newPassword) {
        User user = userRepository.getUser(userID);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
