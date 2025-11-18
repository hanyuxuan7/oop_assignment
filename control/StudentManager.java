package control;

import entity.*;
import data.DataManager;
import java.util.*;
import java.time.LocalDate;

public class StudentManager {
    private DataManager dataManager;

    public StudentManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    private void saveData() {
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
    }

    public List<Internship> getAvailableInternships(Student student) {
        return getAvailableInternships(student, true);
    }

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
                        otherApp.getStatus().equals("Successful")) {
                        otherApp.setStatus("Withdrawn");
                    }
                }

                saveData();
                return true;
            }
        }
        return false;
    }

    public List<InternshipApplication> getStudentApplications(Student student) {
        return student.getApplications();
    }

    public Internship getInternshipDetails(String internshipID) {
        return dataManager.getInternship(internshipID);
    }
}
