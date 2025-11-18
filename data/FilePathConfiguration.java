package data;

public class FilePathConfiguration {
    private final String studentFilePath;
    private final String staffFilePath;
    private final String companyRepFilePath;
    private final String internshipFilePath;
    private final String applicationFilePath;
    private final String activityLogFilePath;

    public FilePathConfiguration(String studentFilePath, String staffFilePath,
                                String companyRepFilePath, String internshipFilePath,
                                String applicationFilePath, String activityLogFilePath) {
        this.studentFilePath = studentFilePath;
        this.staffFilePath = staffFilePath;
        this.companyRepFilePath = companyRepFilePath;
        this.internshipFilePath = internshipFilePath;
        this.applicationFilePath = applicationFilePath;
        this.activityLogFilePath = activityLogFilePath;
    }

    public static FilePathConfiguration defaultPaths() {
        return new FilePathConfiguration(
            "data/students.txt",
            "data/staff.txt",
            "data/companyreps.txt",
            "data/internships.txt",
            "data/applications.txt",
            "data/activitylogs.txt"
        );
    }

    public String getStudentFilePath() { return studentFilePath; }
    public String getStaffFilePath() { return staffFilePath; }
    public String getCompanyRepFilePath() { return companyRepFilePath; }
    public String getInternshipFilePath() { return internshipFilePath; }
    public String getApplicationFilePath() { return applicationFilePath; }
    public String getActivityLogFilePath() { return activityLogFilePath; }
}
