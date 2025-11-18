package view;

import java.util.Scanner;
import java.util.List;
import entity.*;
import data.DataManager;
import control.*;

public class StudentMenu {
    private Scanner scanner;
    private DataManager dataManager;
    private AuthenticationManager authManager;
    private StudentManager studentManager;
    private FilterManager filterManager;

    public StudentMenu(Scanner scanner, DataManager dataManager, AuthenticationManager authManager,
                       StudentManager studentManager, FilterManager filterManager) {
        this.scanner = scanner;
        this.dataManager = dataManager;
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.filterManager = filterManager;
    }

    public void show(Student student) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n===== Student Menu =====");
            System.out.println("Welcome, " + student.getName());
            System.out.println("1. View Available Internships");
            System.out.println("2. View My Applications");
            System.out.println("3. Accept Placement");
            System.out.println("4. Request Withdrawal");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAvailableInternships(student);
                    break;
                case "2":
                    viewApplications(student);
                    break;
                case "3":
                    acceptPlacement(student);
                    break;
                case "4":
                    requestWithdrawal(student);
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

    private void viewAvailableInternships(Student student) {
        boolean showAll = false;
        
        while (true) {
            System.out.println("\n===== Available Internships =====");
            List<Internship> internships = studentManager.getAvailableInternships(student, !showAll);
            
            if (showAll) {
                System.out.println("[Showing ALL internships - including closed and not-yet-open]");
            } else {
                System.out.println("[Showing OPEN internships only]");
            }

            if (internships.isEmpty()) {
                System.out.println("No internships available for your profile.");
                System.out.print("(T)oggle to " + (showAll ? "open internships only" : "show all internships") + " or (0) go back: ");
                String option = scanner.nextLine().trim().toUpperCase();
                if (option.equals("T")) {
                    showAll = !showAll;
                    continue;
                } else {
                    break;
                }
            }

            for (int i = 0; i < internships.size(); i++) {
                Internship internship = internships.get(i);
                System.out.println((i + 1) + ". " + internship.getTitle() + " - " + internship.getCompanyName());
                System.out.println("   Level: " + internship.getLevel());
                System.out.println("   Opening Date: " + internship.getOpeningDate());
                System.out.println("   Closing Date: " + internship.getClosingDate());
            }

            System.out.println("\n(T)oggle view | Enter number to view details | (0) Go back");
            System.out.print("Choice: ");
            String input = scanner.nextLine().trim().toUpperCase();
            
            if (input.equals("T")) {
                showAll = !showAll;
            } else if (input.equals("0")) {
                break;
            } else {
                try {
                    int choice = Integer.parseInt(input);
                    if (choice > 0 && choice <= internships.size()) {
                        viewInternshipDetails(internships.get(choice - 1), student);
                    } else {
                        System.out.println("Invalid input.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }
        }
    }

    private void viewInternshipDetails(Internship internship, Student student) {
        System.out.println("\n===== Internship Details =====");
        System.out.println("Title: " + internship.getTitle());
        System.out.println("Company: " + internship.getCompanyName());
        System.out.println("Level: " + internship.getLevel());
        System.out.println("Major: " + internship.getPreferredMajor());
        System.out.println("Description: " + internship.getDescription());
        System.out.println("Opening Date: " + internship.getOpeningDate());
        System.out.println("Closing Date: " + internship.getClosingDate());
        System.out.println("Available Slots: " + (internship.getNumSlots() - internship.getFilledSlots()));

        boolean detailsMenuActive = true;
        while (detailsMenuActive) {
            System.out.println("\n(A) Apply | (0) Go Back");
            System.out.print("Enter option: ");
            String option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "A":
                    if (studentManager.applyForInternship(student, internship.getInternshipID())) {
                        System.out.println("Application submitted successfully!");
                        detailsMenuActive = false;
                    } else {
                        System.out.println("Failed to apply for internship. Check application limits or internship status.");
                    }
                    break;
                case "0":
                    detailsMenuActive = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter A to apply or 0 to go back.");
            }
        }
    }

    private void viewApplications(Student student) {
        System.out.println("\n===== My Applications =====");
        List<InternshipApplication> applications = studentManager.getStudentApplications(student);

        if (applications.isEmpty()) {
            System.out.println("You have no applications.");
            return;
        }

        for (InternshipApplication app : applications) {
            Internship internship = studentManager.getInternshipDetails(app.getInternshipID());
            if (internship != null) {
                System.out.println("Application ID: " + app.getApplicationID());
                System.out.println("Internship: " + internship.getTitle());
                System.out.println("Status: " + app.getStatus());
                if (app.isWithdrawalRequested()) {
                    System.out.println("Withdrawal Status: Pending Approval");
                }
                System.out.println();
            }
        }
    }

    private void acceptPlacement(Student student) {
        if (student.getAcceptedInternshipID() != null) {
            System.out.println("You have already accepted a placement.");
            return;
        }

        String applicationID = getValidatedInput("Enter Application ID to accept: ");

        if (studentManager.acceptPlacement(student, applicationID)) {
            System.out.println("Placement accepted successfully!");
        } else {
            System.out.println("Failed to accept placement. Application may not be successful.");
        }
    }

    private void requestWithdrawal(Student student) {
        String applicationID = getValidatedInput("Enter Application ID to withdraw: ");

        if (studentManager.withdrawApplication(student, applicationID)) {
            System.out.println("Withdrawal request submitted for approval.");
        } else {
            System.out.println("Failed to request withdrawal.");
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

        if (oldPassword.equals(newPassword)) {
            System.out.println("New password must be different from the old password.");
            return;
        }

        if (authManager.changePassword(oldPassword, newPassword)) {
            dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
            System.out.println("Password changed successfully! Please log in again.");
            authManager.logout();
            return;
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
