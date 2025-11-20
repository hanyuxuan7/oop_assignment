package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a company representative who posts internship positions on the platform.
 * Company representatives are responsible for creating and managing internship postings,
 * viewing applications from students, and accepting interns. Each representative must be
 * approved by the career center staff before they can post internships.
 * They can create up to 5 internship postings.
 *
 * @version 1.0
 */
public class CompanyRepresentative extends User {
    /** Name of the company the representative works for */
    private String companyName;
    /** Department within the company */
    private String department;
    /** Position/title of the representative */
    private String position;
    /** Whether this representative has been approved by career center staff */
    private boolean approved;
    /** List of internships created by this representative */
    private List<Internship> createdInternships;

    /**
     * Constructs a new CompanyRepresentative with the specified details.
     *
     * @param userID the unique representative ID
     * @param name the name of the representative
     * @param password the password for authentication
     * @param companyName the name of the company
     * @param department the department within the company
     * @param position the position/title of the representative
     */
    public CompanyRepresentative(String userID, String name, String password, 
                                 String companyName, String department, String position) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.approved = false;
        this.createdInternships = new ArrayList<>();
    }

    /**
     * Returns the name of the company this representative works for.
     *
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Returns the department within the company.
     *
     * @return the department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Returns the position/title of this representative.
     *
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Checks if this representative has been approved by career center staff.
     *
     * @return true if approved, false otherwise
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Sets the approval status of this representative.
     *
     * @param approved true to approve, false to disapprove
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Returns the list of internships created by this representative.
     *
     * @return a list of Internship objects
     */
    public List<Internship> getCreatedInternships() {
        return createdInternships;
    }

    /**
     * Adds an internship created by this representative.
     * A representative can create a maximum of 5 internships.
     *
     * @param internship the internship to add
     */
    public void addInternship(Internship internship) {
        if (createdInternships.size() < 5) {
            createdInternships.add(internship);
        }
    }

    /**
     * Returns a string representation of the representative with key information.
     *
     * @return a formatted string containing representative ID, name, company, and approval status
     */
    @Override
    public String toString() {
        return "CompanyRepresentative{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", companyName='" + companyName + '\'' +
                ", approved=" + approved +
                '}';
    }
}
