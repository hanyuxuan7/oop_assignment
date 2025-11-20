package entity;

/**
 * Represents a career center staff member in the internship management system.
 * Career center staff are responsible for approving internship postings, reviewing
 * student applications, and managing the overall internship program.
 * They work within a specific department and can be contacted via email.
 *
 * @version 1.0
 */
public class CareerCenterStaff extends User {
    /** The department within the career center where the staff member works */
    private String department;
    /** Email address for contact */
    private String email;

    /**
     * Constructs a new CareerCenterStaff member with the specified details.
     *
     * @param userID the unique staff ID
     * @param name the name of the staff member
     * @param password the password for authentication
     * @param department the department of the staff member
     * @param email the email address
     */
    public CareerCenterStaff(String userID, String name, String password, String department, String email) {
        super(userID, name, password);
        this.department = department;
        this.email = email;
    }

    /**
     * Returns the department of this staff member.
     *
     * @return the department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Returns the email address of this staff member.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns a string representation of the staff member with key information.
     *
     * @return a formatted string containing staff ID, name, and department
     */
    @Override
    public String toString() {
        return "CareerCenterStaff{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
