package entity;

/**
 * Enumeration representing the three difficulty levels for internship positions.
 * Each internship is categorized by one of the following levels:
 * BASIC - Entry-level internship suitable for Year 1-2 students
 * INTERMEDIATE - Intermediate-level internship suitable for Year 2-3 students
 * ADVANCED - Advanced-level internship suitable for Year 3+ students
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public enum InternshipLevel {
    /** Basic/Entry-level internship (short code: B) */
    BASIC("B", "Basic"),
    /** Intermediate-level internship (short code: I) */
    INTERMEDIATE("I", "Intermediate"),
    /** Advanced-level internship (short code: A) */
    ADVANCED("A", "Advanced");

    /** Short code for the internship level */
    private final String shortCode;
    /** Display name for the internship level */
    private final String displayName;

    /**
     * Constructs an InternshipLevel enum with the specified short code and display name.
     *
     * @param shortCode the short code representation (B, I, or A)
     * @param displayName the full display name
     */
    InternshipLevel(String shortCode, String displayName) {
        this.shortCode = shortCode;
        this.displayName = displayName;
    }

    /**
     * Returns the short code representation of this internship level.
     *
     * @return the short code (B, I, or A)
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * Returns the display name of this internship level.
     *
     * @return the full display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a short code to its corresponding InternshipLevel enum value.
     * The comparison is case-insensitive.
     *
     * @param code the short code to convert (B, I, or A)
     * @return the corresponding InternshipLevel enum value, or null if no match is found
     */
    public static InternshipLevel fromShortCode(String code) {
        for (InternshipLevel level : InternshipLevel.values()) {
            if (level.shortCode.equalsIgnoreCase(code)) {
                return level;
            }
        }
        return null;
    }
}
