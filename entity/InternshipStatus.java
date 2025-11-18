package entity;

public enum InternshipStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    FILLED("Filled");

    private final String displayName;

    InternshipStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static InternshipStatus fromString(String status) {
        for (InternshipStatus internshipStatus : InternshipStatus.values()) {
            if (internshipStatus.displayName.equalsIgnoreCase(status)) {
                return internshipStatus;
            }
        }
        return null;
    }
}
