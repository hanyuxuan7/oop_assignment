package entity;

public enum InternshipLevel {
    BASIC("B", "Basic"),
    INTERMEDIATE("I", "Intermediate"),
    ADVANCED("A", "Advanced");

    private final String shortCode;
    private final String displayName;

    InternshipLevel(String shortCode, String displayName) {
        this.shortCode = shortCode;
        this.displayName = displayName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static InternshipLevel fromShortCode(String code) {
        for (InternshipLevel level : InternshipLevel.values()) {
            if (level.shortCode.equalsIgnoreCase(code)) {
                return level;
            }
        }
        return null;
    }
}
