package control;

import entity.*;
import data.DataManager;

/**
 * Manager class for handling user authentication and type determination.
 * Handles login, logout, password management, and user role identification.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public class AuthenticationManager {
    private DataManager dataManager;
    private User currentUser;

    public AuthenticationManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.currentUser = null;
    }

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

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

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

    public boolean resetPassword(String userID, String newPassword) {
        User user = dataManager.getUser(userID);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
