package control;

import entity.*;

public class AuthorizationService {
    
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

    public boolean isStudent(User user) {
        return user instanceof Student;
    }

    public boolean isCompanyRepresentative(User user) {
        return user instanceof CompanyRepresentative;
    }

    public boolean isCareerCenterStaff(User user) {
        return user instanceof CareerCenterStaff;
    }

    public boolean isApproved(User user) {
        if (user instanceof CompanyRepresentative) {
            return ((CompanyRepresentative) user).isApproved();
        }
        return true;
    }
}
