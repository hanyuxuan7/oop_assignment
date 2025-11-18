package view;

import java.util.Scanner;
import java.util.List;
import entity.*;
import data.DataManager;
import control.*;

public class StaffMenu {
    private Scanner scanner;
    private DataManager dataManager;
    private AuthenticationManager authManager;
    private CareerCenterStaffManager staffManager;
    private FilterManager filterManager;

    public StaffMenu(Scanner scanner, DataManager dataManager, AuthenticationManager authManager,
                     CareerCenterStaffManager staffManager, FilterManager filterManager) {
        this.scanner = scanner;
        this.dataManager = dataManager;
        this.authManager = authManager;
        this.staffManager = staffManager;
        this.filterManager = filterManager;
    }

    public void show(CareerCenterStaff staff) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n===== Career Center Staff Menu =====");
            System.out.println("Welcome, " + staff.getName());
            System.out.println("1. Manage Company Rep Registrations");
            System.out.println("2. Approve/Reject Internship Opportunities");
            System.out.println("3. Manage Withdrawal Requests");
            System.out.println("4. Generate Reports");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    manageCompanyRepRegistrations();
                    break;
                case "2":
                    manageInternshipApprovals();
                    break;
                case "3":
                    manageWithdrawals();
                    break;
                case "4":
                    generateReports();
                    break;
                case "5":
                    changePassword();
                    break;
                case "6":
                    authManager.logout();
                    inMenu = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void manageCompanyRepRegistrations() {
        System.out.println("\n===== Pending Company Rep Registrations =====");
        List<CompanyRepresentative> pendingReps = staffManager.getPendingRegistrations();

        if (pendingReps.isEmpty()) {
            System.out.println("No pending registrations.");
            return;
        }

        for (int i = 0; i < pendingReps.size(); i++) {
            CompanyRepresentative rep = pendingReps.get(i);
            System.out.println((i + 1) + ". " + rep.getName() + " - " + rep.getCompanyName());
            System.out.println("   Email: " + rep.getUserID());
            System.out.println("   Position: " + rep.getPosition());
        }

        System.out.print("Enter registration number to approve/reject (or 0 to go back): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice > 0 && choice <= pendingReps.size()) {
                CompanyRepresentative rep = pendingReps.get(choice - 1);
                System.out.print("Approve (A) or Reject (R): ");
                String action = scanner.nextLine().trim().toUpperCase();

                if (action.equals("A")) {
                    if (staffManager.approveCompanyRepRegistration(rep.getUserID())) {
                        System.out.println("Registration approved.");
                    }
                } else if (action.equals("R")) {
                    if (staffManager.rejectCompanyRepRegistration(rep.getUserID())) {
                        System.out.println("Registration rejected.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void manageInternshipApprovals() {
        System.out.println("\n===== Pending Internship Approvals =====");
        List<Internship> pendingInternships = staffManager.getPendingInternships();

        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships.");
            return;
        }

        for (int i = 0; i < pendingInternships.size(); i++) {
            Internship internship = pendingInternships.get(i);
            System.out.println((i + 1) + ". " + internship.getTitle());
            System.out.println("   Company: " + internship.getCompanyName());
            System.out.println("   Level: " + internship.getLevel());
        }

        System.out.print("Enter internship number to approve/reject (or 0 to go back): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice > 0 && choice <= pendingInternships.size()) {
                Internship internship = pendingInternships.get(choice - 1);
                System.out.print("Approve (A) or Reject (R): ");
                String action = scanner.nextLine().trim().toUpperCase();

                if (action.equals("A")) {
                    if (staffManager.approveInternship(internship.getInternshipID())) {
                        System.out.println("Internship approved.");
                    }
                } else if (action.equals("R")) {
                    if (staffManager.rejectInternship(internship.getInternshipID())) {
                        System.out.println("Internship rejected.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void manageWithdrawals() {
        System.out.println("\n===== Pending Withdrawal Requests =====");
        List<InternshipApplication> pendingWithdrawals = staffManager.getPendingWithdrawals();

        if (pendingWithdrawals.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }

        for (int i = 0; i < pendingWithdrawals.size(); i++) {
            InternshipApplication app = pendingWithdrawals.get(i);
            Student student = dataManager.getStudent(app.getStudentID());
            Internship internship = dataManager.getInternship(app.getInternshipID());

            if (student != null && internship != null) {
                System.out.println((i + 1) + ". " + student.getName() + " - " + internship.getTitle());
                System.out.println("   Reason: " + app.getWithdrawalReason());
            }
        }

        System.out.print("Enter withdrawal number to approve/reject (or 0 to go back): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice > 0 && choice <= pendingWithdrawals.size()) {
                InternshipApplication app = pendingWithdrawals.get(choice - 1);
                System.out.print("Approve (A) or Reject (R): ");
                String action = scanner.nextLine().trim().toUpperCase();

                if (action.equals("A")) {
                    if (staffManager.approveWithdrawal(app.getApplicationID())) {
                        System.out.println("Withdrawal approved.");
                    }
                } else if (action.equals("R")) {
                    if (staffManager.rejectWithdrawal(app.getApplicationID())) {
                        System.out.println("Withdrawal rejected.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void generateReports() {
        System.out.println("\n===== Report Generator =====");
        System.out.println("1. Filter by Status");
        System.out.println("2. Filter by Major");
        System.out.println("3. Filter by Level");
        System.out.println("4. View All Internships");
        System.out.print("Choose filter: ");

        String choice = scanner.nextLine().trim();
        List<Internship> results = null;

        switch (choice) {
            case "1":
                String status = getValidatedInput("Enter Status (Pending/Approved/Rejected/Filled): ");
                results = staffManager.getInternshipsByFilter(status, null, null);
                break;
            case "2":
                String major = getValidatedInput("Enter Major: ");
                results = staffManager.getInternshipsByFilter(null, major, null);
                break;
            case "3":
                String level = getValidatedInput("Enter Level (Basic/Intermediate/Advanced): ");
                results = staffManager.getInternshipsByFilter(null, null, level);
                break;
            case "4":
                results = new java.util.ArrayList<>(dataManager.getAllInternships());
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        if (results != null && !results.isEmpty()) {
            System.out.println("\n===== Report Results =====");
            for (Internship internship : results) {
                System.out.println("ID: " + internship.getInternshipID());
                System.out.println("Title: " + internship.getTitle());
                System.out.println("Company: " + internship.getCompanyName());
                System.out.println("Status: " + internship.getStatus());
                System.out.println("Level: " + internship.getLevel());
                System.out.println("Major: " + internship.getPreferredMajor());
                System.out.println();
            }
        } else {
            System.out.println("No results found.");
        }
    }

    private void changePassword() {
        String oldPassword = getValidatedInput("Enter old password: ");
        String newPassword = getValidatedInput("Enter new password: ");
        String confirmPassword = getValidatedInput("Confirm new password: ");

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("New passwords do not match.");
            return;
        }

        if (authManager.changePassword(oldPassword, newPassword)) {
            dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
            dataManager.saveActivityLogs("data/activitylogs.txt");
            System.out.println("Password changed successfully! Please log in again.");
            authManager.logout();
        } else {
            System.out.println("Failed to change password. Old password is incorrect.");
        }
    }

    private String getValidatedInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }
}
