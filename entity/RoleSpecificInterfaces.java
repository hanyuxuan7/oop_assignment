package entity;

import java.util.List;

public class RoleSpecificInterfaces {
    
    public interface Approvable {
        boolean isApproved();
        void setApproved(boolean approved);
    }

    public interface Applicationable {
        List<InternshipApplication> getApplications();
        void addApplication(InternshipApplication application);
        void removeApplication(InternshipApplication application);
    }

    public interface Internshipable {
        List<Internship> getCreatedInternships();
        void addInternship(Internship internship);
    }

    public interface Loggable {
        String getActivityID();
        String getUserID();
        String getUserType();
        String getActivityDescription();
    }

    public interface PasswordChangeable {
        void setPassword(String newPassword);
        boolean validatePassword(String inputPassword);
    }

    public interface Registrable {
        String getUserID();
        String getName();
    }
}
