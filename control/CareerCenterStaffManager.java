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
    }

    public List<CompanyRepresentative> getPendingRegistrations() {
        return dataManager.getAllCompanyReps().stream()
                .filter(rep -> !rep.isApproved())
                .collect(Collectors.toList());
    }

    public boolean approveCompanyRepRegistration(String repID) {
        CompanyRepresentative rep = dataManager.getCompanyRep(repID);
        if (rep != null) {
            rep.setApproved(true);
            saveData();
            return true;
        }
        return false;
    }

    public boolean rejectCompanyRepRegistration(String repID) {
        dataManager.removeCompanyRepRegistration(repID);
        saveData();
        return true;
    }

    public boolean approveInternship(String internshipID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null && internship.getStatus().equals("Pending")) {
            internship.setStatus("Approved");
            saveData();
            return true;
        }
        return false;
    }

    public boolean rejectInternship(String internshipID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null && internship.getStatus().equals("Pending")) {
            internship.setStatus("Rejected");
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

            saveData();
            return true;
        }
        return false;
    }

    public boolean rejectWithdrawal(String applicationID) {
        InternshipApplication application = dataManager.getApplication(applicationID);
        if (application != null && application.isWithdrawalRequested()) {
            application.cancelWithdrawalRequest();
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
