package control;

import entity.*;
import data.DataManager;

/**
 * Manager class for handling user authentication and type determination.
 * Handles login, logout, password management, and user role identification.
 *
 * @version 1.0
 */
public class AuthenticationManager {
    private DataManager dataManager;
    private User currentUser;

    /**
     * Constructs a new AuthenticationManager with the specified DataManager.
     *
     * @param dataManager the DataManager instance for accessing user data
     */
    public AuthenticationManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.currentUser = null;
    }

    /**
     * Authenticates a user with the provided user ID and password.
     * Returns a status string indicating the result of the login attempt.
     *
     * @param userID the user's unique identifier
     * @param password the user's password
     * @return a status string: "SUCCESS", "INVALID_USER", "INVALID_PASSWORD", or "NOT_APPROVED"
     */
    public String login(String userID, String password) {
        User user = dataManager.getUser(userID);
        
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
     * Logs out the currently authenticated user.
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Retrieves the currently logged-in user.
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
     * The old password must be correct for the change to succeed.
     *
     * @param oldPassword the current password
     * @param newPassword the new password to set
     * @return true if password was successfully changed, false otherwise
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
     * Determines and returns the type/role of the specified user.
     *
     * @param user the user whose type is to be determined
     * @return a string representing the user type: "Student", "Company Representative", "Career Center Staff", or "Unknown"
     */
    public String getUserType(User user) {
        if (user instanceof Student) {
            return "Student";
        } else if (user instanceof CompanyRepresentative) {
            return "Company Representative";
        } else if (user instanceof CareerCenterStaff) {
            return "Career Center Staff";
        }
        return "Unknown";
    }

    /**
     * Resets the password for a user with the specified ID.
     *
     * @param userID the unique identifier of the user whose password is to be reset
     * @param newPassword the new password to set
     * @return true if password was successfully reset, false if user not found
     */
    public boolean resetPassword(String userID, String newPassword) {
        User user = dataManager.getUser(userID);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
