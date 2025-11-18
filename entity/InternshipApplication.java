package entity;

public class InternshipApplication {
    private String applicationID;
    private String studentID;
    private String internshipID;
    private String status;
    private boolean withdrawalRequested;
    private String withdrawalReason;
    private boolean confirmed;

    public InternshipApplication(String applicationID, String studentID, String internshipID) {
        this.applicationID = applicationID;
        this.studentID = studentID;
        this.internshipID = internshipID;
        this.status = "Pending";
        this.withdrawalRequested = false;
        this.withdrawalReason = null;
        this.confirmed = false;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getInternshipID() {
        return internshipID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    public void requestWithdrawal(String reason) {
        this.withdrawalRequested = true;
        this.withdrawalReason = reason;
    }

    public void cancelWithdrawalRequest() {
        this.withdrawalRequested = false;
        this.withdrawalReason = null;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

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
