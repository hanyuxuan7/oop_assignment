package entity;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<InternshipApplication> applications;
    private String acceptedInternshipID;

    public Student(String userID, String name, String password, int yearOfStudy, String major) {
        super(userID, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
        this.acceptedInternshipID = null;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public List<InternshipApplication> getApplications() {
        return applications;
    }

    public void addApplication(InternshipApplication application) {
        if (applications.size() < 3) {
            applications.add(application);
        }
    }

    public void removeApplication(InternshipApplication application) {
        applications.remove(application);
    }

    public String getAcceptedInternshipID() {
        return acceptedInternshipID;
    }

    public void setAcceptedInternshipID(String internshipID) {
        this.acceptedInternshipID = internshipID;
    }

    public boolean canApplyForLevel(String level) {
        if (yearOfStudy <= 2) {
            return level.equals("Basic");
        }
        return true;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", yearOfStudy=" + yearOfStudy +
                ", major='" + major + '\'' +
                '}';
    }
}
