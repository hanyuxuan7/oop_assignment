package entity;

public enum ApplicationStatus {
    PENDING("Pending"),
    SUCCESSFUL("Successful"),
    UNSUCCESSFUL("Unsuccessful"),
    WITHDRAWN("Withdrawn");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ApplicationStatus fromString(String status) {
        for (ApplicationStatus appStatus : ApplicationStatus.values()) {
            if (appStatus.displayName.equalsIgnoreCase(status)) {
                return appStatus;
            }
        }
        return null;
    }
}
