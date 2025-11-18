package control;

import entity.*;
import data.UserRepository;

public class AuthenticationService {
    private final UserRepository userRepository;
    private User currentUser;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUser = null;
    }

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

    public boolean resetPassword(String userID, String newPassword) {
        User user = userRepository.getUser(userID);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
