package control;

import entity.*;
import data.DataManager;
import java.time.LocalDate;
import java.util.*;

/**
 * Manager class for company representative operations.
 * Handles internship creation, modification, application reviews, and acceptance decisions.
 *
 * @version 1.0
 */
public class CompanyRepresentativeManager {
    private DataManager dataManager;

    /**
     * Constructs a CompanyRepresentativeManager with the specified DataManager.
     *
     * @param dataManager the DataManager instance for data operations
     */
    public CompanyRepresentativeManager(DataManager dataManager) {
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
     * Creates a new internship for the specified company representative.
     * Each company representative can create a maximum of 5 internships.
     *
     * @param rep the company representative creating the internship
     * @param title the title of the internship
     * @param description the description of the internship
     * @param level the level of the internship
     * @param preferredMajor the preferred major for this internship
     * @param openingDate the date when applications open
     * @param closingDate the date when applications close
     * @param numSlots the number of internship slots available
     * @return true if creation was successful, false if the rep has reached the creation limit
     */
    public boolean createInternship(CompanyRepresentative rep, String title, String description,
                                    String level, String preferredMajor, LocalDate openingDate,
                                    LocalDate closingDate, int numSlots) {
        if (rep.getCreatedInternships().size() >= 5) {
            return false;
        }

        String internshipID = "INT" + System.currentTimeMillis();
        Internship internship = new Internship(internshipID, title, description, level,
                                              preferredMajor, openingDate, closingDate,
                                              rep.getCompanyName(), rep.getUserID(), numSlots);

        rep.addInternship(internship);
        dataManager.addInternship(internship);
        
        ActivityLog log = new ActivityLog(rep.getUserID(), "CompanyRepresentative",
            "Created internship: " + title, internshipID);
        dataManager.addActivityLog(log);
        
        saveData();

        return true;
    }

    /**
     * Retrieves all internships created by the specified company representative.
     *
     * @param rep the company representative
     * @return a list of internships created by this representative
     */
    public List<Internship> getCreatedInternships(CompanyRepresentative rep) {
        return rep.getCreatedInternships();
    }

    /**
     * Retrieves all applications for a specific internship.
     *
     * @param internshipID the unique identifier of the internship
     * @return a list of applications for the internship, or an empty list if internship not found
     */
    public List<InternshipApplication> getApplicationsForInternship(String internshipID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null) {
            return internship.getApplications();
        }
        return new ArrayList<>();
    }

    /**
     * Approves a pending internship application by its ID.
     *
     * @param applicationID the unique identifier of the application
     * @return true if approval was successful, false otherwise
     */
    public boolean approveApplication(String applicationID) {
        return approveApplication(applicationID, null);
    }

    /**
     * Approves a pending internship application with optional activity logging.
     *
     * @param applicationID the unique identifier of the application
     * @param repID the representative ID performing the approval (for activity logging)
     * @return true if approval was successful, false otherwise
     */
    public boolean approveApplication(String applicationID, String repID) {
        InternshipApplication application = dataManager.getApplication(applicationID);
        if (application != null && application.getStatus().equals("Pending")) {
            application.setStatus("Successful");
            if (repID != null) {
                ActivityLog log = new ActivityLog(repID, "CompanyRepresentative",
                    "Approved application", applicationID);
                dataManager.addActivityLog(log);
            }
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Rejects a pending internship application by its ID.
     *
     * @param applicationID the unique identifier of the application
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectApplication(String applicationID) {
        return rejectApplication(applicationID, null);
    }

    /**
     * Rejects a pending internship application with optional activity logging.
     *
     * @param applicationID the unique identifier of the application
     * @param repID the representative ID performing the rejection (for activity logging)
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectApplication(String applicationID, String repID) {
        InternshipApplication application = dataManager.getApplication(applicationID);
        if (application != null && application.getStatus().equals("Pending")) {
            application.setStatus("Unsuccessful");
            if (repID != null) {
                ActivityLog log = new ActivityLog(repID, "CompanyRepresentative",
                    "Rejected application", applicationID);
                dataManager.addActivityLog(log);
            }
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Retrieves student details by their ID.
     *
     * @param studentID the unique identifier of the student
     * @return the student object, or null if not found
     */
    public Student getStudentDetails(String studentID) {
        return dataManager.getStudent(studentID);
    }

    /**
     * Toggles the visibility status of an internship by its ID.
     *
     * @param internshipID the unique identifier of the internship
     */
    public void toggleInternshipVisibility(String internshipID) {
        toggleInternshipVisibility(internshipID, null);
    }

    /**
     * Toggles the visibility status of an internship with optional activity logging.
     *
     * @param internshipID the unique identifier of the internship
     * @param repID the representative ID performing the action (for activity logging)
     * @return true if toggle was successful, false if internship not approved
     */
    public boolean toggleInternshipVisibility(String internshipID, String repID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null && internship.getStatus().equals("Approved")) {
            boolean newVisibility = !internship.isVisible();
            internship.setVisible(newVisibility);
            if (repID != null) {
                ActivityLog log = new ActivityLog(repID, "CompanyRepresentative",
                    "Toggled internship visibility to " + (newVisibility ? "visible" : "hidden"), internshipID);
                dataManager.addActivityLog(log);
            }
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Automatically sets an internship as visible if it is approved and currently within the open period.
     *
     * @param internshipID the unique identifier of the internship
     */
    public void autoSetVisibilityForApprovedInternship(String internshipID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null && internship.getStatus().equals("Approved")) {
            LocalDate today = LocalDate.now();
            if (!today.isBefore(internship.getOpeningDate()) && !today.isAfter(internship.getClosingDate())) {
                internship.setVisible(true);
                saveData();
            }
        }
    }

    /**
     * Retrieves internship details by its ID.
     *
     * @param internshipID the unique identifier of the internship
     * @return the internship object, or null if not found
     */
    public Internship getInternshipDetails(String internshipID) {
        return dataManager.getInternship(internshipID);
    }

    /**
     * Updates internship details by its ID.
     * Only pending internships can be updated.
     *
     * @param internshipID the unique identifier of the internship
     * @param title the new title (or null to keep existing)
     * @param description the new description (or null to keep existing)
     * @param level the new level (or null to keep existing)
     * @param preferredMajor the new preferred major (or null to keep existing)
     * @param openingDate the new opening date (or null to keep existing)
     * @param closingDate the new closing date (or null to keep existing)
     * @param numSlots the new number of slots (0 or negative to keep existing)
     * @return true if update was successful, false if internship not pending or not found
     */
    public boolean updateInternshipDetails(String internshipID, String title, String description, String level,
                                           String preferredMajor, LocalDate openingDate, LocalDate closingDate, int numSlots) {
        return updateInternshipDetails(internshipID, title, description, level, preferredMajor, 
                                      openingDate, closingDate, numSlots, null);
    }

    /**
     * Updates internship details with optional activity logging.
     * Only pending internships can be updated.
     *
     * @param internshipID the unique identifier of the internship
     * @param title the new title (or null to keep existing)
     * @param description the new description (or null to keep existing)
     * @param level the new level (or null to keep existing)
     * @param preferredMajor the new preferred major (or null to keep existing)
     * @param openingDate the new opening date (or null to keep existing)
     * @param closingDate the new closing date (or null to keep existing)
     * @param numSlots the new number of slots (0 or negative to keep existing)
     * @param repID the representative ID performing the update (for activity logging)
     * @return true if update was successful, false if internship not pending or not found
     */
    public boolean updateInternshipDetails(String internshipID, String title, String description, String level,
                                           String preferredMajor, LocalDate openingDate, LocalDate closingDate, int numSlots, String repID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship == null) {
            return false;
        }

        if (!internship.getStatus().equals("Pending")) {
            return false;
        }

        if (title != null && !title.isEmpty()) internship.setTitle(title);
        if (description != null && !description.isEmpty()) internship.setDescription(description);
        if (level != null && !level.isEmpty()) internship.setLevel(level);
        if (preferredMajor != null && !preferredMajor.isEmpty()) internship.setPreferredMajor(preferredMajor);
        if (openingDate != null) internship.setOpeningDate(openingDate);
        if (closingDate != null) internship.setClosingDate(closingDate);
        if (numSlots > 0) internship.setNumSlots(numSlots);

        if (repID != null) {
            ActivityLog log = new ActivityLog(repID, "CompanyRepresentative",
                "Updated internship details", internshipID);
            dataManager.addActivityLog(log);
        }

        saveData();
        return true;
    }

    /**
     * Deletes an internship if it belongs to the specified company representative.
     * Removes the internship from both the representative's list and the global data store.
     * Creates an activity log entry for audit trail purposes.
     *
     * @param internshipID the unique identifier of the internship
     * @param rep the company representative who should own the internship
     * @return true if deletion was successful, false if internship not found or not owned by rep
     */
    public boolean deleteInternship(String internshipID, CompanyRepresentative rep) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship == null || !internship.getRepInCharge().equals(rep.getUserID())) {
            return false;
        }

        rep.getCreatedInternships().remove(internship);
        dataManager.removeInternship(internshipID);
        ActivityLog log = new ActivityLog(rep.getUserID(), "CompanyRepresentative",
            "Deleted internship: " + internship.getTitle(), internshipID);
        dataManager.addActivityLog(log);
        saveData();
        return true;
    }
}
