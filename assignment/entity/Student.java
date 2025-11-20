package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student user in the internship management system.
 * Students can apply for internship positions, manage their applications,
 * and accept internship placements. They have restrictions on which internship levels
 * they can apply for based on their year of study.
 *
 * @version 1.0
 */
public class Student extends User {
    /** The academic year of the student (1-4) */
    private int yearOfStudy;
    /** The major/specialization of the student */
    private String major;
    /** Email address of the student */
    private String email;
    /** List of internship applications submitted by the student */
    private List<InternshipApplication> applications;
    /** ID of the internship accepted by the student, null if none accepted */
    private String acceptedInternshipID;

    /**
     * Constructs a new Student with the specified details.
     *
     * @param userID the unique student ID
     * @param name the name of the student
     * @param password the password for authentication
     * @param yearOfStudy the academic year (1-4)
     * @param major the student's major/specialization
     * @param email the student's email address
     */
    public Student(String userID, String name, String password, int yearOfStudy, String major, String email) {
        super(userID, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.email = email;
        this.applications = new ArrayList<>();
        this.acceptedInternshipID = null;
    }

    /**
     * Returns the year of study for this student.
     *
     * @return the year of study (1-4)
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * Returns the major/specialization of this student.
     *
     * @return the student's major
     */
    public String getMajor() {
        return major;
    }

    /**
     * Returns the email address of this student.
     *
     * @return the student's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the list of internship applications submitted by this student.
     *
     * @return a list of InternshipApplication objects
     */
    public List<InternshipApplication> getApplications() {
        return applications;
    }

    /**
     * Adds an internship application for this student.
     * A student can have a maximum of 3 applications.
     *
     * @param application the internship application to add
     */
    public void addApplication(InternshipApplication application) {
        if (applications.size() < 3) {
            applications.add(application);
        }
    }

    /**
     * Removes an internship application from this student's list.
     *
     * @param application the internship application to remove
     */
    public void removeApplication(InternshipApplication application) {
        applications.remove(application);
    }

    /**
     * Returns the ID of the internship accepted by this student.
     *
     * @return the accepted internship ID, or null if none accepted
     */
    public String getAcceptedInternshipID() {
        return acceptedInternshipID;
    }

    /**
     * Sets the internship that this student has accepted.
     *
     * @param internshipID the ID of the accepted internship
     */
    public void setAcceptedInternshipID(String internshipID) {
        this.acceptedInternshipID = internshipID;
    }

    /**
     * Checks if this student can apply for internships at the specified level.
     * Students in Year 1-2 can only apply for Basic level internships.
     * Students in Year 3+ can apply for any level.
     *
     * @param level the internship level to check
     * @return true if the student can apply for this level, false otherwise
     */
    public boolean canApplyForLevel(String level) {
        if (yearOfStudy <= 2) {
            return level.equals("Basic");
        }
        return true;
    }

    /**
     * Returns a string representation of the student with key information.
     *
     * @return a formatted string containing user ID, name, year of study, and major
     */
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
