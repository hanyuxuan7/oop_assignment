package entity;

import java.util.HashMap;
import java.util.Map;

public class MajorUtils {
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

    public static String getMajorName(int majorNumber) {
        if (majorNumber >= 1 && majorNumber <= majors.length) {
            return majors[majorNumber - 1];
        }
        return null;
    }

    public static int getMajorNumber(String majorName) {
        for (int i = 0; i < majors.length; i++) {
            if (majors[i].equals(majorName)) {
                return i + 1;
            }
        }
        return -1;
    }

    public static String[] getAllMajors() {
        return majors;
    }

    public static boolean isValidMajor(int majorNumber) {
        return majorNumber >= 1 && majorNumber <= majors.length;
    }
}
