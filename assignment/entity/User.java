package entity;

/**
 * Abstract base class representing a user in the internship management system.
 * All user types (Student, CareerCenterStaff, CompanyRepresentative) extend this class.
 * Provides common functionality for user authentication and basic user information management.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public abstract class User {
    /** Unique identifier for the user */
    protected String userID;
    /** Full name of the user */
    protected String name;
    /** Password for user authentication */
    protected String password;

    /**
     * Constructs a new User with the specified ID, name, and password.
     *
     * @param userID the unique identifier for the user
     * @param name the full name of the user
     * @param password the password for authentication
     */
    public User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = password;
    }

    /**
     * Returns the unique identifier of this user.
     *
     * @return the user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Returns the name of this user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the password of this user.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the password for this user.
     *
     * @param newPassword the new password to set
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Validates the provided password against the user's stored password.
     *
     * @param inputPassword the password to validate
     * @return true if the password matches, false otherwise
     */
    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    /**
     * Returns a string representation of the user with user ID and name.
     *
     * @return a formatted string containing user ID and name
     */
    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
