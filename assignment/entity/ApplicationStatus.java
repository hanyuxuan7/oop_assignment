package entity;

/**
 * Enumeration representing the possible statuses of an internship application.
 * Each application can be in one of the following states:
 * PENDING - Application is awaiting review from career center staff
 * SUCCESSFUL - Application has been approved by career center staff
 * UNSUCCESSFUL - Application has been rejected by career center staff
 * WITHDRAWN - Student has withdrawn the application
 *
 * @version 1.0
 */
public enum ApplicationStatus {
    /** Application is pending review */
    PENDING("Pending"),
    /** Application has been successfully approved */
    SUCCESSFUL("Successful"),
    /** Application has been rejected */
    UNSUCCESSFUL("Unsuccessful"),
    /** Application has been withdrawn by the student */
    WITHDRAWN("Withdrawn");

    /** Display name for the application status */
    private final String displayName;

    /**
     * Constructs an ApplicationStatus enum with the specified display name.
     *
     * @param displayName the display name for this status
     */
    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of this application status.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a string to its corresponding ApplicationStatus enum value.
     * The comparison is case-insensitive.
     *
     * @param status the status string to convert
     * @return the corresponding ApplicationStatus enum value, or null if no match is found
     */
    public static ApplicationStatus fromString(String status) {
        for (ApplicationStatus appStatus : ApplicationStatus.values()) {
            if (appStatus.displayName.equalsIgnoreCase(status)) {
                return appStatus;
            }
        }
        return null;
    }
}
