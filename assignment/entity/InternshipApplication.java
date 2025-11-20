package entity;

/**
 * Represents a student's application for an internship position.
 * Tracks the application status, withdrawal requests, and confirmation status.
 * Each application links a student to an internship opportunity and manages
 * the application lifecycle from submission to confirmation or rejection.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public class InternshipApplication {
    /** Unique identifier for the application */
    private String applicationID;
    /** ID of the student submitting the application */
    private String studentID;
    /** ID of the internship being applied for */
    private String internshipID;
    /** Current status of the application (Pending, Successful, Unsuccessful, Withdrawn) */
    private String status;
    /** Whether the student has requested withdrawal of this application */
    private boolean withdrawalRequested;
    /** Reason for withdrawal request, if applicable */
    private String withdrawalReason;
    /** Whether the student has confirmed acceptance of this internship */
    private boolean confirmed;

    /**
     * Constructs a new InternshipApplication with the specified IDs.
     * Initial status is set to Pending, and no withdrawal or confirmation.
     *
     * @param applicationID the unique identifier for the application
     * @param studentID the ID of the student applying
     * @param internshipID the ID of the internship being applied for
     */
    public InternshipApplication(String applicationID, String studentID, String internshipID) {
        this.applicationID = applicationID;
        this.studentID = studentID;
        this.internshipID = internshipID;
        this.status = "Pending";
        this.withdrawalRequested = false;
        this.withdrawalReason = null;
        this.confirmed = false;
    }

    /**
     * Returns the unique identifier of this application.
     *
     * @return the application ID
     */
    public String getApplicationID() {
        return applicationID;
    }

    /**
     * Returns the ID of the student who submitted this application.
     *
     * @return the student ID
     */
    public String getStudentID() {
        return studentID;
    }

    /**
     * Returns the ID of the internship this application is for.
     *
     * @return the internship ID
     */
    public String getInternshipID() {
        return internshipID;
    }

    /**
     * Returns the current status of this application.
     *
     * @return the application status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates the status of this application.
     *
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Checks if withdrawal has been requested for this application.
     *
     * @return true if withdrawal is requested, false otherwise
     */
    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    /**
     * Requests withdrawal of this application with a specified reason.
     *
     * @param reason the reason for withdrawal
     */
    public void requestWithdrawal(String reason) {
        this.withdrawalRequested = true;
        this.withdrawalReason = reason;
    }

    /**
     * Cancels a withdrawal request for this application.
     */
    public void cancelWithdrawalRequest() {
        this.withdrawalRequested = false;
        this.withdrawalReason = null;
    }

    /**
     * Returns the reason for withdrawal request, if applicable.
     *
     * @return the withdrawal reason, or null if no withdrawal is requested
     */
    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    /**
     * Checks if the student has confirmed acceptance of this internship.
     *
     * @return true if confirmed, false otherwise
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Sets the confirmation status of this application.
     *
     * @param confirmed true if the student confirms acceptance, false otherwise
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * Returns a string representation of the application with key information.
     *
     * @return a formatted string containing application ID, student ID, internship ID, status, and confirmation
     */
    @Override
    public String toString() {
        return "InternshipApplication{" +
                "applicationID='" + applicationID + '\'' +
                ", studentID='" + studentID + '\'' +
                ", internshipID='" + internshipID + '\'' +
                ", status='" + status + '\'' +
                ", confirmed=" + confirmed +
                '}';
    }
}
