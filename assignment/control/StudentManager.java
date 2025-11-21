package control;

import entity.*;
import data.DataManager;
import java.util.*;
import java.time.LocalDate;

/**
 * Manager class for student-related operations including internship discovery and application management.
 * Handles student interactions with internships, application processing, and placement confirmation.
 *
 * @version 1.0
 */
public class StudentManager {
    private DataManager dataManager;

    /**
     * Constructs a StudentManager with the specified DataManager.
     *
     * @param dataManager the DataManager instance for data operations
     */
    public StudentManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Saves all current data to their respective file paths.
     */
    private void saveData() {
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
    }

    /**
     * Retrieves available internships for the specified student that match their major and level requirements.
     * Only returns internships that are currently open for applications.
     *
     * @param student the student to get available internships for
     * @return a list of available internships
     */
    public List<Internship> getAvailableInternships(Student student) {
        return getAvailableInternships(student, true);
    }

    /**
     * Retrieves available internships for the specified student that match their major and level requirements.
     *
     * @param student the student to get available internships for
     * @param openOnly if true, only returns internships currently open for applications; if false, returns all available
     * @return a list of available internships
     */
    public List<Internship> getAvailableInternships(Student student, boolean openOnly) {
        List<Internship> availableInternships = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        String studentMajor = student.getMajor();

        for (Internship internship : dataManager.getAllInternships()) {
            if (internship.isVisible() && 
                internship.getStatus().equals("Approved") &&
                internship.getPreferredMajor().equals(studentMajor) &&
                student.canApplyForLevel(internship.getLevel()) &&
                !internship.isFull()) {
                
                if (openOnly) {
                    if (today.isAfter(internship.getOpeningDate()) && today.isBefore(internship.getClosingDate())) {
                        availableInternships.add(internship);
                    }
                } else {
                    availableInternships.add(internship);
                }
            }
        }

        return availableInternships;
    }

    /**
     * Submits an application for an internship on behalf of the student.
     * Student must not have reached the application limit (3 max) and meet all requirements.
     *
     * @param student the student applying for the internship
     * @param internshipID the ID of the internship to apply for
     * @return true if application was successful, false otherwise
     */
    public boolean applyForInternship(Student student, String internshipID) {
        if (student.getApplications().size() >= 3) {
            return false;
        }

        Internship internship = dataManager.getInternship(internshipID);
        if (internship == null) {
            return false;
        }

        if (!internship.canApply(java.time.LocalDate.now())) {
            return false;
        }

        if (!student.canApplyForLevel(internship.getLevel())) {
            return false;
        }

        String applicationID = "APP" + System.currentTimeMillis();
        InternshipApplication application = new InternshipApplication(applicationID, student.getUserID(), internshipID);
        
        student.addApplication(application);
        internship.addApplication(application);
        dataManager.addApplication(application);
        saveData();

        return true;
    }

    /**
     * Requests withdrawal of an internship application.
     * If the application has been confirmed, the withdrawal is marked as requested for staff review.
     *
     * @param student the student withdrawing the application
     * @param applicationID the ID of the application to withdraw
     * @return true if withdrawal request was successful, false otherwise
     */
    public boolean withdrawApplication(Student student, String applicationID) {
        for (InternshipApplication app : student.getApplications()) {
            if (app.getApplicationID().equals(applicationID)) {
                if (app.isConfirmed()) {
                    app.requestWithdrawal("Withdrawal after confirmation");
                } else {
                    app.requestWithdrawal("Withdrawal request");
                }
                saveData();
                return true;
            }
        }
        return false;
    }

    /**
     * Accepts and confirms an internship placement for the student.
     * Only successful applications can be accepted. Accepting one placement withdraws other pending applications.
     *
     * @param student the student accepting the placement
     * @param applicationID the ID of the application to accept
     * @return true if acceptance was successful, false otherwise
     */
    public boolean acceptPlacement(Student student, String applicationID) {
        if (student.getAcceptedInternshipID() != null) {
            return false;
        }

        for (InternshipApplication app : student.getApplications()) {
            if (app.getApplicationID().equals(applicationID) && app.getStatus().equals("Successful")) {
                student.setAcceptedInternshipID(app.getInternshipID());
                app.setConfirmed(true);

                Internship internship = dataManager.getInternship(app.getInternshipID());
                if (internship != null) {
                    internship.setFilledSlots(internship.getFilledSlots() + 1);
                    if (internship.isFull()) {
                        internship.setStatus("Filled");
                    }
                }

                for (InternshipApplication otherApp : student.getApplications()) {
                    if (!otherApp.getApplicationID().equals(applicationID) && 
                        !otherApp.getStatus().equals("Withdrawn") &&
                        !otherApp.getStatus().equals("Unsuccessful")) {
                        otherApp.setStatus("Withdrawn");
                    }
                }

                saveData();
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all internship applications submitted by the student.
     *
     * @param student the student whose applications to retrieve
     * @return a list of the student's internship applications
     */
    public List<InternshipApplication> getStudentApplications(Student student) {
        return student.getApplications();
    }

    /**
     * Retrieves details of an internship by its ID.
     *
     * @param internshipID the unique identifier of the internship
     * @return the internship object, or null if not found
     */
    public Internship getInternshipDetails(String internshipID) {
        return dataManager.getInternship(internshipID);
    }
}
