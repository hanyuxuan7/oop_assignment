package entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActivityLog {
    private String activityID;
    private String userID;
    private String userType;
    private String activityDescription;
    private LocalDateTime timestamp;
    private String relatedEntity;

    public ActivityLog(String userID, String userType, String activityDescription, String relatedEntity) {
        this.activityID = UUID.randomUUID().toString();
        this.userID = userID;
        this.userType = userType;
        this.activityDescription = activityDescription;
        this.timestamp = LocalDateTime.now();
        this.relatedEntity = relatedEntity;
    }

    public String getActivityID() {
        return activityID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserType() {
        return userType;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getRelatedEntity() {
        return relatedEntity;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s): %s - Related: %s", 
            timestamp, userID, userType, activityDescription, relatedEntity);
    }
}
