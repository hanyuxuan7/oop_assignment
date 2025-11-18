package view;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;
import entity.*;
import entity.InternshipLevel;
import data.DataManager;
import control.*;

public class CompanyRepMenu {
    private Scanner scanner;
    private DataManager dataManager;
    private AuthenticationManager authManager;
    private CompanyRepresentativeManager companyRepManager;

    public CompanyRepMenu(Scanner scanner, DataManager dataManager, AuthenticationManager authManager,
                          CompanyRepresentativeManager companyRepManager) {
        this.scanner = scanner;
        this.dataManager = dataManager;
        this.authManager = authManager;
        this.companyRepManager = companyRepManager;
    }

    public void show(CompanyRepresentative rep) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n===== Company Representative Menu =====");
            System.out.println("Welcome, " + rep.getName());
            System.out.println("Company: " + rep.getCompanyName());
            System.out.println("1. Create Internship Opportunity");
            System.out.println("2. View My Internship Opportunities");
            System.out.println("3. Edit Internship Opportunity");
            System.out.println("4. View Applications for Internship");
            System.out.println("5. Approve/Reject Application");
            System.out.println("6. Toggle Internship Visibility");
            System.out.println("7. Change Password");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createInternship(rep);
                    break;
                case "2":
                    viewInternships(rep);
                    break;
                case "3":
                    editInternship(rep);
                    break;
                case "4":
                    viewApplications(rep);
                    break;
                case "5":
                    approveRejectApplication();
                    break;
                case "6":
                    toggleVisibility();
                    break;
                case "7":
                    changePassword();
                    break;
                case "8":
                    authManager.logout();
                    inMenu = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createInternship(CompanyRepresentative rep) {
        System.out.println("===== Create New Internship Opportunity =====");
        
        String title = inputFieldWithValidation("Enter Internship Title: ", true);
        String description = inputFieldWithValidation("Enter Description: ", true);
        String level = inputLevelRequired();
        String major = inputFieldWithValidation("Enter Preferred Major: ", true);
        
        System.out.println("Enter Opening Date:");
        LocalDate openingDate = inputDateRequired("opening");
        
        System.out.println("Enter Closing Date:");
        LocalDate closingDate = inputDateRequired("closing");
        
        System.out.print("Enter Number of Slots (max 10): ");
        int numSlots = 0;
        while (numSlots <= 0 || numSlots > 10) {
            try {
                numSlots = Integer.parseInt(scanner.nextLine().trim());
                if (numSlots <= 0 || numSlots > 10) {
                    System.out.print("Please enter a number between 1 and 10: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number between 1 and 10: ");
            }
        }

        if (companyRepManager.createInternship(rep, title, description, level, major, 
                                              openingDate, closingDate, numSlots)) {
            System.out.println("Internship created successfully! Awaiting approval from Career Center Staff.");
        } else {
            System.out.println("Failed to create internship. You may have reached the maximum limit of 5.");
        }
    }

    private void viewInternships(CompanyRepresentative rep) {
        System.out.println("\n===== My Internship Opportunities =====");
        List<Internship> internships = companyRepManager.getCreatedInternships(rep);

        if (internships.isEmpty()) {
            System.out.println("You have no internship opportunities.");
            return;
        }

        for (int i = 0; i < internships.size(); i++) {
            Internship internship = internships.get(i);
            System.out.println((i + 1) + ". " + internship.getTitle());
            System.out.println("   Status: " + internship.getStatus());
            System.out.println("   Level: " + internship.getLevel());
            System.out.println("   Filled Slots: " + internship.getFilledSlots() + "/" + internship.getNumSlots());
        }
    }

    private void viewApplications(CompanyRepresentative rep) {
        List<Internship> internships = companyRepManager.getCreatedInternships(rep);

        if (internships.isEmpty()) {
            System.out.println("You have no internship opportunities.");
            return;
        }

        System.out.println("\n===== Select Internship to View Applications =====");
        for (int i = 0; i < internships.size(); i++) {
            Internship internship = internships.get(i);
            System.out.println((i + 1) + ". [" + internship.getInternshipID() + "] " + internship.getTitle());
            System.out.println("   Applications: " + internship.getApplications().size());
        }

        System.out.print("Enter internship number to view applications (or 0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice <= 0 || choice > internships.size()) {
                System.out.println("Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        Internship selectedInternship = internships.get(choice - 1);
        String internshipID = selectedInternship.getInternshipID();

        List<InternshipApplication> applications = companyRepManager.getApplicationsForInternship(internshipID);

        if (applications.isEmpty()) {
            System.out.println("No applications for this internship.");
            return;
        }

        System.out.println("\n===== Applications for " + selectedInternship.getTitle() + " =====");
        for (InternshipApplication app : applications) {
            Student student = companyRepManager.getStudentDetails(app.getStudentID());
            if (student != null) {
                System.out.println("Application ID: " + app.getApplicationID());
                System.out.println("Student: " + student.getName() + " (" + student.getUserID() + ")");
                System.out.println("Status: " + app.getStatus());
                System.out.println();
            }
        }
    }

    private void approveRejectApplication() {
        String applicationID = inputFieldWithValidation("Enter Application ID: ", true);
        System.out.print("Approve (A) or Reject (R): ");
        String action = scanner.nextLine().trim().toUpperCase();

        if (action.equals("A")) {
            if (companyRepManager.approveApplication(applicationID)) {
                System.out.println("Application approved.");
            } else {
                System.out.println("Failed to approve application.");
            }
        } else if (action.equals("R")) {
            if (companyRepManager.rejectApplication(applicationID)) {
                System.out.println("Application rejected.");
            } else {
                System.out.println("Failed to reject application.");
            }
        }
    }

    private void toggleVisibility() {
        List<Internship> internships = new java.util.ArrayList<>(dataManager.getAllInternships());
        
        if (internships.isEmpty()) {
            System.out.println("No internships available.");
            return;
        }

        System.out.println("\n===== Internship Visibility Control =====");
        for (int i = 0; i < internships.size(); i++) {
            Internship internship = internships.get(i);
            String visibility = internship.isVisible() ? "Visible" : "Hidden";
            System.out.println((i + 1) + ". [" + internship.getInternshipID() + "] " + internship.getTitle() + " (" + visibility + ")");
        }

        System.out.print("Enter internship number to toggle visibility (or 0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice <= 0 || choice > internships.size()) {
                System.out.println("Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        Internship internship = internships.get(choice - 1);
        companyRepManager.toggleInternshipVisibility(internship.getInternshipID());
        System.out.println("Visibility toggled. Current visibility: " + (internship.isVisible() ? "Hidden" : "Visible"));
    }

    private void editInternship(CompanyRepresentative rep) {
        System.out.println("\n===== My Internship Opportunities =====");
        List<Internship> internships = companyRepManager.getCreatedInternships(rep);

        if (internships.isEmpty()) {
            System.out.println("You have no internship opportunities to edit.");
            return;
        }

        for (int i = 0; i < internships.size(); i++) {
            Internship internship = internships.get(i);
            System.out.println((i + 1) + ". [" + internship.getInternshipID() + "] " + internship.getTitle());
            System.out.println("   Status: " + internship.getStatus());
        }

        System.out.print("Enter internship number to edit (or 0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice <= 0 || choice > internships.size()) {
                System.out.println("Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        Internship internship = internships.get(choice - 1);
        String internshipID = internship.getInternshipID();

        System.out.println("\n===== Edit Internship Details =====");
        System.out.println("Current Details:");
        System.out.println("Title: " + internship.getTitle());
        System.out.println("Description: " + internship.getDescription());
        System.out.println("Level: " + internship.getLevel());
        System.out.println("Major: " + internship.getPreferredMajor());
        System.out.println("Opening Date: " + internship.getOpeningDate());
        System.out.println("Closing Date: " + internship.getClosingDate());
        System.out.println("Slots: " + internship.getNumSlots());

        String title = inputFieldWithValidation("Enter new title (or press Enter to skip): ", false);
        String description = inputFieldWithValidation("Enter new description (or press Enter to skip): ", false);
        String level = inputLevel();
        String major = inputFieldWithValidation("Enter preferred major (or press Enter to skip): ", false);
        LocalDate openingDate = inputDate("opening");
        LocalDate closingDate = inputDate("closing");

        System.out.print("Enter number of slots (or 0 to skip): ");
        int numSlots = 0;
        try {
            numSlots = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            numSlots = 0;
        }

        if (companyRepManager.updateInternshipDetails(internshipID, title, description, level, major, openingDate, closingDate, numSlots)) {
            System.out.println("Internship updated successfully!");
        } else {
            System.out.println("Failed to update internship.");
        }
    }

    private String inputFieldWithValidation(String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                if (required) {
                    System.out.println("This field cannot be empty. Please try again.");
                    continue;
                }
                return null;
            }
            return input;
        }
    }

    private String inputLevel() {
        while (true) {
            System.out.println("Select Level:");
            System.out.println("(B)asic");
            System.out.println("(I)ntermediate");
            System.out.println("(A)dvanced");
            System.out.print("Enter level or press Enter to skip: ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            InternshipLevel level = InternshipLevel.fromShortCode(input);
            if (level != null) {
                return level.getDisplayName();
            }

            System.out.println("Invalid selection. Please enter B, I, or A.");
        }
    }

    private String inputLevelRequired() {
        while (true) {
            System.out.println("Select Level:");
            System.out.println("(B)asic");
            System.out.println("(I)ntermediate");
            System.out.println("(A)dvanced");
            System.out.print("Enter level: ");

            String input = scanner.nextLine().trim();

            InternshipLevel level = InternshipLevel.fromShortCode(input);
            if (level != null) {
                return level.getDisplayName();
            }

            System.out.println("Invalid selection. Please enter B, I, or A.");
        }
    }

    private LocalDate inputDate(String dateType) {
        System.out.println("Enter " + dateType + " date (or press Enter to skip):");
        
        System.out.print("Year (YYYY): ");
        String yearInput = scanner.nextLine().trim();
        if (yearInput.isEmpty()) {
            return null;
        }

        int year;
        try {
            year = Integer.parseInt(yearInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid year. Skipping date input.");
            return null;
        }

        System.out.print("Month (MM): ");
        String monthInput = scanner.nextLine().trim();
        int month;
        try {
            month = Integer.parseInt(monthInput);
            if (month < 1 || month > 12) {
                System.out.println("Invalid month. Skipping date input.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid month. Skipping date input.");
            return null;
        }

        System.out.print("Day (DD): ");
        String dayInput = scanner.nextLine().trim();
        int day;
        try {
            day = Integer.parseInt(dayInput);
            if (day < 1 || day > 31) {
                System.out.println("Invalid day. Skipping date input.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid day. Skipping date input.");
            return null;
        }

        try {
            LocalDate date = LocalDate.of(year, month, day);
            String formattedDate = String.format("%02d-%02d-%04d", day, month, year);
            System.out.print("Confirm date " + formattedDate + "? (Y/N): ");
            String confirm = scanner.nextLine().trim().toUpperCase();

            if (confirm.equals("Y")) {
                return date;
            } else {
                System.out.println("Date input cancelled.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Invalid date. Skipping date input.");
            return null;
        }
    }

    private LocalDate inputDateRequired(String dateType) {
        while (true) {
            System.out.print("Year (YYYY): ");
            String yearInput = scanner.nextLine().trim();
            
            int year;
            try {
                year = Integer.parseInt(yearInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid year. Please try again.");
                continue;
            }

            System.out.print("Month (MM): ");
            String monthInput = scanner.nextLine().trim();
            int month;
            try {
                month = Integer.parseInt(monthInput);
                if (month < 1 || month > 12) {
                    System.out.println("Invalid month. Please enter a value between 1 and 12.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid month. Please try again.");
                continue;
            }

            System.out.print("Day (DD): ");
            String dayInput = scanner.nextLine().trim();
            int day;
            try {
                day = Integer.parseInt(dayInput);
                if (day < 1 || day > 31) {
                    System.out.println("Invalid day. Please enter a value between 1 and 31.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid day. Please try again.");
                continue;
            }

            try {
                LocalDate date = LocalDate.of(year, month, day);
                String formattedDate = String.format("%02d-%02d-%04d", day, month, year);
                System.out.print("Confirm date " + formattedDate + "? (Y/N): ");
                String confirm = scanner.nextLine().trim().toUpperCase();

                if (confirm.equals("Y")) {
                    return date;
                } else {
                    System.out.println("Please re-enter the date.");
                }
            } catch (Exception e) {
                System.out.println("Invalid date. Please try again.");
            }
        }
    }

    private void changePassword() {
        System.out.print("Enter old password: ");
        String oldPassword = scanner.nextLine().trim();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();

        if (authManager.changePassword(oldPassword, newPassword)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Old password is incorrect.");
        }
    }
}
