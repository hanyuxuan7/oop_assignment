package entity;

/**
 * Enumeration representing the possible statuses of an internship posting.
 * Each internship can be in one of the following states:
 * PENDING - Internship posting is awaiting approval from career center staff
 * APPROVED - Internship posting has been approved and is available for applications
 * REJECTED - Internship posting has been rejected
 * FILLED - Internship posting has been filled with a student
 *
 * @version 1.0
 */
public enum InternshipStatus {
    /** Internship is pending approval */
    PENDING("Pending"),
    /** Internship has been approved */
    APPROVED("Approved"),
    /** Internship has been rejected */
    REJECTED("Rejected"),
    /** Internship has been filled */
    FILLED("Filled");

    /** Display name for the internship status */
    private final String displayName;

    /**
     * Constructs an InternshipStatus enum with the specified display name.
     *
     * @param displayName the display name for this status
     */
    InternshipStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of this internship status.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a string to its corresponding InternshipStatus enum value.
     * The comparison is case-insensitive.
     *
     * @param status the status string to convert
     * @return the corresponding InternshipStatus enum value, or null if no match is found
     */
    public static InternshipStatus fromString(String status) {
        for (InternshipStatus internshipStatus : InternshipStatus.values()) {
            if (internshipStatus.displayName.equalsIgnoreCase(status)) {
                return internshipStatus;
            }
        }
        return null;
    }
}
