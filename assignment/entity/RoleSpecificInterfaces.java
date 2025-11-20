package entity;

import java.util.List;

/**
 * Container class for role-specific interfaces following the Interface Segregation Principle.
 * These interfaces define capabilities for different user roles and entity types,
 * allowing for flexible permission and responsibility management.
 * Each interface represents a specific capability that can be implemented by relevant classes.
 *
 * @version 1.0
 */
public class RoleSpecificInterfaces {
    
    /**
     * Interface for entities that can be approved or rejected (e.g., CompanyRepresentative, Internship).
     * Enables the tracking and management of approval status for various entities.
     */
    public interface Approvable {
        /**
         * Checks if this entity has been approved.
         *
         * @return true if approved, false otherwise
         */
        boolean isApproved();
        
        /**
         * Sets the approval status of this entity.
         *
         * @param approved true to approve, false to disapprove
         */
        void setApproved(boolean approved);
    }

    /**
     * Interface for entities that can manage applications (e.g., Student, Internship).
     * Handles collections of InternshipApplication objects and their lifecycle.
     */
    public interface Applicationable {
        /**
         * Returns the list of applications associated with this entity.
         *
         * @return a list of InternshipApplication objects
         */
        List<InternshipApplication> getApplications();
        
        /**
         * Adds an application to this entity.
         *
         * @param application the application to add
         */
        void addApplication(InternshipApplication application);
        
        /**
         * Removes an application from this entity.
         *
         * @param application the application to remove
         */
        void removeApplication(InternshipApplication application);
    }

    /**
     * Interface for entities that can manage internships (e.g., CompanyRepresentative).
     * Handles collections of Internship objects created by the entity.
     */
    public interface Internshipable {
        /**
         * Returns the list of internships created by this entity.
         *
         * @return a list of Internship objects
         */
        List<Internship> getCreatedInternships();
        
        /**
         * Adds an internship created by this entity.
         *
         * @param internship the internship to add
         */
        void addInternship(Internship internship);
    }

    /**
     * Interface for activity log entries that can be logged (e.g., ActivityLog).
     * Provides access to audit trail information for user actions.
     */
    public interface Loggable {
        /**
         * Returns the unique identifier of the log entry.
         *
         * @return the activity log ID
         */
        String getActivityID();
        
        /**
         * Returns the ID of the user who performed the activity.
         *
         * @return the user ID
         */
        String getUserID();
        
        /**
         * Returns the type of user who performed the activity.
         *
         * @return the user type
         */
        String getUserType();
        
        /**
         * Returns a description of the activity performed.
         *
         * @return the activity description
         */
        String getActivityDescription();
    }

    /**
     * Interface for entities that support password management (e.g., User).
     * Handles password changes and validation for authentication purposes.
     */
    public interface PasswordChangeable {
        /**
         * Updates the password for this entity.
         *
         * @param newPassword the new password to set
         */
        void setPassword(String newPassword);
        
        /**
         * Validates the provided password against the stored password.
         *
         * @param inputPassword the password to validate
         * @return true if the password matches, false otherwise
         */
        boolean validatePassword(String inputPassword);
    }

    /**
     * Interface for entities that can be registered in the system (e.g., User).
     * Provides basic identification information for registration and lookup purposes.
     */
    public interface Registrable {
        /**
         * Returns the unique identifier of this entity.
         *
         * @return the entity ID
         */
        String getUserID();
        
        /**
         * Returns the name of this entity.
         *
         * @return the entity name
         */
        String getName();
    }
}
