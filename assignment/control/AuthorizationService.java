package control;

import entity.*;

/**
 * Service class responsible for authorization and user type determination.
 * Provides methods to check user roles and permissions within the system.
 * Determines what actions each user type is allowed to perform.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public class AuthorizationService {
    
    /**
     * Determines the user type of the provided user.
     *
     * @param user the user to determine the type for
     * @return a string representing the user type: "Student", "Company Representative",
     *         "Career Center Staff", or "Unknown"
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
     * Checks if a user is a student.
     *
     * @param user the user to check
     * @return true if the user is a Student, false otherwise
     */
    public boolean isStudent(User user) {
        return user instanceof Student;
    }

    /**
     * Checks if a user is a company representative.
     *
     * @param user the user to check
     * @return true if the user is a CompanyRepresentative, false otherwise
     */
    public boolean isCompanyRepresentative(User user) {
        return user instanceof CompanyRepresentative;
    }

    /**
     * Checks if a user is career center staff.
     *
     * @param user the user to check
     * @return true if the user is CareerCenterStaff, false otherwise
     */
    public boolean isCareerCenterStaff(User user) {
        return user instanceof CareerCenterStaff;
    }

    /**
     * Checks if a user is approved to access the system.
     * Company representatives must be explicitly approved; other user types are always approved.
     *
     * @param user the user to check
     * @return true if the user is approved, false otherwise
     */
    public boolean isApproved(User user) {
        if (user instanceof CompanyRepresentative) {
            return ((CompanyRepresentative) user).isApproved();
        }
        return true;
    }
}
