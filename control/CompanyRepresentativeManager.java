package control;

import entity.*;
import data.DataManager;
import java.time.LocalDate;
import java.util.*;

public class CompanyRepresentativeManager {
    private DataManager dataManager;

    public CompanyRepresentativeManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    private void saveData() {
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
        dataManager.saveActivityLogs("data/activitylogs.txt");
    }

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

    public List<Internship> getCreatedInternships(CompanyRepresentative rep) {
        return rep.getCreatedInternships();
    }

    public List<InternshipApplication> getApplicationsForInternship(String internshipID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null) {
            return internship.getApplications();
        }
        return new ArrayList<>();
    }

    public boolean approveApplication(String applicationID) {
        return approveApplication(applicationID, null);
    }

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

    public boolean rejectApplication(String applicationID) {
        return rejectApplication(applicationID, null);
    }

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

    public Student getStudentDetails(String studentID) {
        return dataManager.getStudent(studentID);
    }

    public void toggleInternshipVisibility(String internshipID) {
        toggleInternshipVisibility(internshipID, null);
    }

    public void toggleInternshipVisibility(String internshipID, String repID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null) {
            boolean newVisibility = !internship.isVisible();
            internship.setVisible(newVisibility);
            if (repID != null) {
                ActivityLog log = new ActivityLog(repID, "CompanyRepresentative",
                    "Toggled internship visibility to " + (newVisibility ? "visible" : "hidden"), internshipID);
                dataManager.addActivityLog(log);
            }
            saveData();
        }
    }

    public Internship getInternshipDetails(String internshipID) {
        return dataManager.getInternship(internshipID);
    }

    public boolean updateInternshipDetails(String internshipID, String title, String description, String level,
                                           String preferredMajor, LocalDate openingDate, LocalDate closingDate, int numSlots) {
        return updateInternshipDetails(internshipID, title, description, level, preferredMajor, 
                                      openingDate, closingDate, numSlots, null);
    }

    public boolean updateInternshipDetails(String internshipID, String title, String description, String level,
                                           String preferredMajor, LocalDate openingDate, LocalDate closingDate, int numSlots, String repID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship == null) {
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
}
