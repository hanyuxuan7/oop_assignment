package control;

import entity.*;
import data.DataManager;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager class for career center staff operations.
 * Handles registration approval/rejection, internship approval, and application review.
 *
 * @version 1.0
 */
public class CareerCenterStaffManager {
    private DataManager dataManager;

    /**
     * Constructs a CareerCenterStaffManager with the specified DataManager.
     *
     * @param dataManager the DataManager instance for data operations
     */
    public CareerCenterStaffManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Saves all current data to their respective file paths.
     */
    private void saveData() {
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
        dataManager.saveActivityLogs("data/activitylogs.txt");
    }

    /**
     * Retrieves all company representatives with pending (unapproved) registration.
     *
     * @return a list of unapproved company representatives
     */
    public List<CompanyRepresentative> getPendingRegistrations() {
        return dataManager.getAllCompanyReps().stream()
                .filter(rep -> !rep.isApproved())
                .collect(Collectors.toList());
    }

    /**
     * Approves a company representative registration by their ID.
     *
     * @param repID the unique identifier of the company representative
     * @return true if approval was successful, false otherwise
     */
    public boolean approveCompanyRepRegistration(String repID) {
        return approveCompanyRepRegistration(repID, null);
    }

    /**
     * Approves a company representative registration with optional activity logging.
     *
     * @param repID the unique identifier of the company representative
     * @param staffID the staff ID performing the approval (for activity logging)
     * @return true if approval was successful, false otherwise
     */
    public boolean approveCompanyRepRegistration(String repID, String staffID) {
        CompanyRepresentative rep = dataManager.getCompanyRep(repID);
        if (rep != null) {
            rep.setApproved(true);
            if (staffID != null) {
                ActivityLog log = new ActivityLog(staffID, "CareerCenterStaff",
                    "Approved company rep registration", repID);
                dataManager.addActivityLog(log);
            }
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Rejects a company representative registration by their ID.
     *
     * @param repID the unique identifier of the company representative
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectCompanyRepRegistration(String repID) {
        return rejectCompanyRepRegistration(repID, null);
    }

    /**
     * Rejects a company representative registration with optional activity logging.
     *
     * @param repID the unique identifier of the company representative
     * @param staffID the staff ID performing the rejection (for activity logging)
     * @return true if rejection was successful
     */
    public boolean rejectCompanyRepRegistration(String repID, String staffID) {
        dataManager.removeCompanyRepRegistration(repID);
        if (staffID != null) {
            ActivityLog log = new ActivityLog(staffID, "CareerCenterStaff",
                "Rejected company rep registration", repID);
            dataManager.addActivityLog(log);
        }
        saveData();
        return true;
    }

    /**
     * Approves a pending internship by its ID.
     *
     * @param internshipID the unique identifier of the internship
     * @return true if approval was successful, false otherwise
     */
    public boolean approveInternship(String internshipID) {
        return approveInternship(internshipID, null);
    }

    /**
     * Approves a pending internship with optional activity logging.
     *
     * @param internshipID the unique identifier of the internship
     * @param staffID the staff ID performing the approval (for activity logging)
     * @return true if approval was successful, false otherwise
     */
    public boolean approveInternship(String internshipID, String staffID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null && internship.getStatus().equals("Pending")) {
            internship.setStatus("Approved");
            if (staffID != null) {
                ActivityLog log = new ActivityLog(staffID, "CareerCenterStaff",
                    "Approved internship", internshipID);
                dataManager.addActivityLog(log);
            }
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Rejects a pending internship by its ID.
     *
     * @param internshipID the unique identifier of the internship
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectInternship(String internshipID) {
        return rejectInternship(internshipID, null);
    }

    /**
     * Rejects a pending internship with optional activity logging.
     *
     * @param internshipID the unique identifier of the internship
     * @param staffID the staff ID performing the rejection (for activity logging)
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectInternship(String internshipID, String staffID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null && internship.getStatus().equals("Pending")) {
            internship.setStatus("Rejected");
            if (staffID != null) {
                ActivityLog log = new ActivityLog(staffID, "CareerCenterStaff",
                    "Rejected internship", internshipID);
                dataManager.addActivityLog(log);
            }
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Retrieves all pending internships awaiting approval.
     *
     * @return a list of internships with "Pending" status
     */
    public List<Internship> getPendingInternships() {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getStatus().equals("Pending"))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all internship applications with pending withdrawal requests.
     *
     * @return a list of applications with withdrawal requests
     */
    public List<InternshipApplication> getPendingWithdrawals() {
        List<InternshipApplication> withdrawals = new ArrayList<>();
        for (InternshipApplication app : dataManager.getAllApplications()) {
            if (app.isWithdrawalRequested()) {
                withdrawals.add(app);
            }
        }
        return withdrawals;
    }

    /**
     * Approves a withdrawal request for an internship application by its ID.
     *
     * @param applicationID the unique identifier of the application
     * @return true if approval was successful, false otherwise
     */
    public boolean approveWithdrawal(String applicationID) {
        return approveWithdrawal(applicationID, null);
    }

    /**
     * Approves a withdrawal request for an internship application with optional activity logging.
     *
     * @param applicationID the unique identifier of the application
     * @param staffID the staff ID performing the approval (for activity logging)
     * @return true if approval was successful, false otherwise
     */
    public boolean approveWithdrawal(String applicationID, String staffID) {
        InternshipApplication application = dataManager.getApplication(applicationID);
        if (application != null && application.isWithdrawalRequested()) {
            application.setStatus("Withdrawn");
            application.cancelWithdrawalRequest();

            if (application.isConfirmed()) {
                Internship internship = dataManager.getInternship(application.getInternshipID());
                if (internship != null) {
                    internship.setFilledSlots(internship.getFilledSlots() - 1);
                    if (!internship.isFull() && internship.getStatus().equals("Filled")) {
                        internship.setStatus("Approved");
                    }
                }

                Student student = dataManager.getStudent(application.getStudentID());
                if (student != null && student.getAcceptedInternshipID() != null &&
                    student.getAcceptedInternshipID().equals(application.getInternshipID())) {
                    student.setAcceptedInternshipID(null);
                }
            }

            if (staffID != null) {
                ActivityLog log = new ActivityLog(staffID, "CareerCenterStaff",
                    "Approved withdrawal request", applicationID);
                dataManager.addActivityLog(log);
            }
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Rejects a withdrawal request for an internship application by its ID.
     *
     * @param applicationID the unique identifier of the application
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectWithdrawal(String applicationID) {
        return rejectWithdrawal(applicationID, null);
    }

    /**
     * Rejects a withdrawal request for an internship application with optional activity logging.
     *
     * @param applicationID the unique identifier of the application
     * @param staffID the staff ID performing the rejection (for activity logging)
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectWithdrawal(String applicationID, String staffID) {
        InternshipApplication application = dataManager.getApplication(applicationID);
        if (application != null && application.isWithdrawalRequested()) {
            application.cancelWithdrawalRequest();
            if (staffID != null) {
                ActivityLog log = new ActivityLog(staffID, "CareerCenterStaff",
                    "Rejected withdrawal request", applicationID);
                dataManager.addActivityLog(log);
            }
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Filters internships by status, preferred major, and/or level.
     * Passing null for any parameter skips filtering on that attribute.
     *
     * @param status the internship status to filter by (or null to skip)
     * @param preferredMajor the preferred major to filter by (or null to skip)
     * @param level the internship level to filter by (or null to skip)
     * @return a list of internships matching the specified criteria
     */
    public List<Internship> getInternshipsByFilter(String status, String preferredMajor, 
                                                   String level) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> status == null || internship.getStatus().equals(status))
                .filter(internship -> preferredMajor == null || internship.getPreferredMajor().equals(preferredMajor))
                .filter(internship -> level == null || internship.getLevel().equals(level))
                .collect(Collectors.toList());
    }
}
