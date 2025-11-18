package view;

import java.util.Scanner;
import entity.*;
import data.DataManager;
import control.*;

public class MainMenu {
    private Scanner scanner;
    private DataManager dataManager;
    private AuthenticationManager authManager;
    private StudentManager studentManager;
    private CompanyRepresentativeManager companyRepManager;
    private CareerCenterStaffManager staffManager;
    private FilterManager filterManager;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.dataManager = new DataManager();
        this.authManager = new AuthenticationManager(dataManager);
        this.studentManager = new StudentManager(dataManager);
        this.companyRepManager = new CompanyRepresentativeManager(dataManager);
        this.staffManager = new CareerCenterStaffManager(dataManager);
        this.filterManager = new FilterManager(dataManager);
    }

    public void start() {
        dataManager.loadStudents("data/students.txt");
        dataManager.loadStaff("data/staff.txt");
        dataManager.loadCompanyReps("data/companyreps.txt");
        dataManager.loadInternships("data/internships.txt");
        dataManager.linkInternshipsToReps();
        dataManager.loadApplications("data/applications.txt");

        while (true) {
            if (!authManager.isLoggedIn()) {
                showLoginMenu();
            } else {
                User user = authManager.getCurrentUser();
                if (user instanceof Student) {
                    new StudentMenu(scanner, dataManager, authManager, studentManager, filterManager).show((Student) user);
                } else if (user instanceof CompanyRepresentative) {
                    new CompanyRepMenu(scanner, dataManager, authManager, companyRepManager).show((CompanyRepresentative) user);
                } else if (user instanceof CareerCenterStaff) {
                    new StaffMenu(scanner, dataManager, authManager, staffManager, filterManager).show((CareerCenterStaff) user);
                }
                dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n===== Internship Management System =====");
        System.out.println("1. Login");
        System.out.println("2. Register as Company Representative");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                registerCompanyRep();
                break;
            case "3":
                System.out.println("Thank you for using the system!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void login() {
        String userID = getValidatedInput("Enter User ID: ");
        String password = getValidatedInput("Enter Password: ");

        String result = authManager.login(userID, password);
        
        switch (result) {
            case "SUCCESS":
                System.out.println("Login successful!");
                break;
            case "INVALID_USER":
                System.out.println("Login failed. User ID not found.");
                break;
            case "INVALID_PASSWORD":
                System.out.println("Login failed. Invalid password.");
                System.out.print("Did you forget your password? (Y/N): ");
                String choice = scanner.nextLine().trim().toUpperCase();
                if (choice.equals("Y")) {
                    resetPassword(userID);
                }
                break;
            case "NOT_APPROVED":
                System.out.println("Login failed. Your company registration is pending approval from Career Center Staff.");
                break;
            default:
                System.out.println("Login failed. Please try again.");
        }
    }

    private void resetPassword(String userID) {
        User user = dataManager.getUser(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Password Reset");
        String newPassword = getValidatedInput("Enter new password: ");
        String confirmPassword = getValidatedInput("Confirm password: ");

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Password reset cancelled.");
            return;
        }

        if (authManager.resetPassword(userID, newPassword)) {
            dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
            System.out.println("Password reset successfully!");
        } else {
            System.out.println("Password reset failed.");
        }
    }

    private void registerCompanyRep() {
        String userID = getValidatedInput("Enter Email (User ID): ");
        String name = getValidatedInput("Enter Name: ");
        String companyName = getValidatedInput("Enter Company Name: ");
        String department = getValidatedInput("Enter Department: ");
        String position = getValidatedInput("Enter Position: ");
        String password = getValidatedInput("Enter Password: ");
        String confirmPassword = getValidatedInput("Confirm Password: ");

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Registration cancelled.");
            return;
        }

        CompanyRepresentative rep = new CompanyRepresentative(userID, name, password, companyName, department, position);
        dataManager.addUser(rep);
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
        System.out.println("Registration submitted for approval. You will be notified once approved.");
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
