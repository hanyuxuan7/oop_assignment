package control;

import entity.*;
import data.DataManager;
import java.util.*;
import java.util.stream.Collectors;

public class CareerCenterStaffManager {
    private DataManager dataManager;

    public CareerCenterStaffManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    private void saveData() {
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
        dataManager.saveActivityLogs("data/activitylogs.txt");
    }

    public List<CompanyRepresentative> getPendingRegistrations() {
        return dataManager.getAllCompanyReps().stream()
                .filter(rep -> !rep.isApproved())
                .collect(Collectors.toList());
    }

    public boolean approveCompanyRepRegistration(String repID) {
        return approveCompanyRepRegistration(repID, null);
    }

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

    public boolean rejectCompanyRepRegistration(String repID) {
        return rejectCompanyRepRegistration(repID, null);
    }

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

    public boolean approveInternship(String internshipID) {
        return approveInternship(internshipID, null);
    }

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

    public boolean rejectInternship(String internshipID) {
        return rejectInternship(internshipID, null);
    }

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

    public List<Internship> getPendingInternships() {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getStatus().equals("Pending"))
                .collect(Collectors.toList());
    }

    public List<InternshipApplication> getPendingWithdrawals() {
        List<InternshipApplication> withdrawals = new ArrayList<>();
        for (InternshipApplication app : dataManager.getAllApplications()) {
            if (app.isWithdrawalRequested()) {
                withdrawals.add(app);
            }
        }
        return withdrawals;
    }

    public boolean approveWithdrawal(String applicationID) {
        return approveWithdrawal(applicationID, null);
    }

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

    public boolean rejectWithdrawal(String applicationID) {
        return rejectWithdrawal(applicationID, null);
    }

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

    public List<Internship> getInternshipsByFilter(String status, String preferredMajor, 
                                                   String level) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> status == null || internship.getStatus().equals(status))
                .filter(internship -> preferredMajor == null || internship.getPreferredMajor().equals(preferredMajor))
                .filter(internship -> level == null || internship.getLevel().equals(level))
                .collect(Collectors.toList());
    }
}
