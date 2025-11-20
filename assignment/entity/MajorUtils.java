package entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing academic majors and specializations available in the system.
 * Provides static methods for converting between major names and numbers,
 * as well as validating and retrieving all available majors.
 * This class acts as a centralized reference for all supported academic disciplines.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public class MajorUtils {
    /** Array containing all available majors */
    private static final String[] majors = {
        "Computer Science",
        "Computer Engineering",
        "Mechanical Engineering",
        "Electrical & Electronics Engineering",
        "Bio Engineering",
        "Chemical Engineering",
        "Business",
        "Aerospace Engineering",
        "Accounting",
        "Economics",
        "Art"
    };

    /**
     * Converts a major number to its corresponding major name.
     * Major numbers are 1-indexed (1 through the length of the majors array).
     *
     * @param majorNumber the numeric code for the major (1-indexed)
     * @return the name of the major, or null if the major number is invalid
     */
    public static String getMajorName(int majorNumber) {
        if (majorNumber >= 1 && majorNumber <= majors.length) {
            return majors[majorNumber - 1];
        }
        return null;
    }

    /**
     * Converts a major name to its corresponding major number.
     * Major numbers are 1-indexed.
     *
     * @param majorName the name of the major
     * @return the numeric code for the major (1-indexed), or -1 if the major name is not found
     */
    public static int getMajorNumber(String majorName) {
        for (int i = 0; i < majors.length; i++) {
            if (majors[i].equals(majorName)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Returns an array containing all available majors.
     *
     * @return an array of all major names
     */
    public static String[] getAllMajors() {
        return majors;
    }

    /**
     * Checks if a major number is valid.
     * Valid major numbers are 1-indexed and within the bounds of the majors array.
     *
     * @param majorNumber the major number to validate
     * @return true if the major number is valid, false otherwise
     */
    public static boolean isValidMajor(int majorNumber) {
        return majorNumber >= 1 && majorNumber <= majors.length;
    }
}
