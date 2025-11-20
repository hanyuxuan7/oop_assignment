package entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an activity log entry that records user actions within the internship management system.
 * Each log entry captures details about an activity performed by a user, including the timestamp,
 * activity description, and related entity information for audit trail purposes.
 *
 * @version 1.0
 */
public class ActivityLog {
    /** Unique identifier for the activity log entry */
    private String activityID;
    /** ID of the user who performed the activity */
    private String userID;
    /** Type of user (e.g., Student, Staff, CompanyRepresentative) */
    private String userType;
    /** Description of the activity performed */
    private String activityDescription;
    /** Timestamp when the activity was recorded */
    private LocalDateTime timestamp;
    /** ID of the entity related to this activity */
    private String relatedEntity;

    /**
     * Constructs a new ActivityLog entry with the specified parameters.
     * The activityID is auto-generated using UUID, and timestamp is set to current time.
     *
     * @param userID the ID of the user performing the activity
     * @param userType the type of user (Student, Staff, or CompanyRepresentative)
     * @param activityDescription the description of the activity performed
     * @param relatedEntity the ID of the entity related to this activity
     */
    public ActivityLog(String userID, String userType, String activityDescription, String relatedEntity) {
        this.activityID = UUID.randomUUID().toString();
        this.userID = userID;
        this.userType = userType;
        this.activityDescription = activityDescription;
        this.timestamp = LocalDateTime.now();
        this.relatedEntity = relatedEntity;
    }

    /**
     * Returns the unique identifier of this activity log entry.
     *
     * @return the activity ID
     */
    public String getActivityID() {
        return activityID;
    }

    /**
     * Returns the ID of the user who performed the activity.
     *
     * @return the user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Returns the type of user who performed the activity.
     *
     * @return the user type (e.g., Student, Staff, CompanyRepresentative)
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Returns the description of the activity performed.
     *
     * @return the activity description
     */
    public String getActivityDescription() {
        return activityDescription;
    }

    /**
     * Returns the timestamp when the activity was recorded.
     *
     * @return the timestamp in LocalDateTime format
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the ID of the entity related to this activity.
     *
     * @return the related entity ID
     */
    public String getRelatedEntity() {
        return relatedEntity;
    }

    /**
     * Returns a string representation of the activity log entry containing all relevant information.
     *
     * @return a formatted string with timestamp, user ID, user type, activity description, and related entity
     */
    @Override
    public String toString() {
        return String.format("[%s] %s (%s): %s - Related: %s", 
            timestamp, userID, userType, activityDescription, relatedEntity);
    }
}
