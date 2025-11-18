package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import entity.*;
import data.DataManager;
import control.*;

public class LoginFrame extends JFrame {
    private DataManager dataManager;
    private AuthenticationManager authManager;
    private StudentManager studentManager;
    private CompanyRepresentativeManager companyRepManager;
    private CareerCenterStaffManager staffManager;
    private FilterManager filterManager;

    private JTabbedPane tabbedPane;
    private JPanel loginPanel;
    private JPanel registerPanel;

    public LoginFrame() {
        setTitle("Internship Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        dataManager = new DataManager();
        authManager = new AuthenticationManager(dataManager);
        studentManager = new StudentManager(dataManager);
        companyRepManager = new CompanyRepresentativeManager(dataManager);
        staffManager = new CareerCenterStaffManager(dataManager);
        filterManager = new FilterManager(dataManager);

        loadData();
        createUI();
    }

    private void loadData() {
        dataManager.loadStudents("data/students.txt");
        dataManager.loadStaff("data/staff.txt");
        dataManager.loadCompanyReps("data/companyreps.txt");
        dataManager.loadInternships("data/internships.txt");
        dataManager.loadApplications("data/applications.txt");
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Internship Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();

        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Register Company", registerPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userIDLabel = new JLabel("User ID (Email):");
        JTextField userIDField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userIDLabel, gbc);
        gbc.gridx = 1;
        panel.add(userIDField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        JButton forgotPasswordButton = new JButton("Forgot Password?");
        gbc.gridy = 3;
        panel.add(forgotPasswordButton, gbc);

        loginButton.addActionListener(e -> handleLogin(userIDField.getText(), new String(passwordField.getPassword())));
        forgotPasswordButton.addActionListener(e -> showResetPasswordDialog(userIDField.getText()));

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField emailField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField companyField = new JTextField(20);
        JTextField deptField = new JTextField(20);
        JTextField positionField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        addFormField(panel, "Email:", emailField, gbc, 0);
        addFormField(panel, "Name:", nameField, gbc, 1);
        addFormField(panel, "Company Name:", companyField, gbc, 2);
        addFormField(panel, "Department:", deptField, gbc, 3);
        addFormField(panel, "Position:", positionField, gbc, 4);
        addFormField(panel, "Password:", passwordField, gbc, 5);
        addFormField(panel, "Confirm Password:", confirmPasswordField, gbc, 6);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> handleCompanyRepRegistration(
                emailField.getText(),
                nameField.getText(),
                companyField.getText(),
                deptField.getText(),
                positionField.getText(),
                new String(passwordField.getPassword()),
                new String(confirmPasswordField.getPassword())
        ));

        return panel;
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void handleLogin(String userID, String password) {
        String result = authManager.login(userID, password);

        switch (result) {
            case "SUCCESS":
                openDashboard();
                break;
            case "INVALID_USER":
                JOptionPane.showMessageDialog(this, "Login failed. User ID not found.", "Login Error", JOptionPane.ERROR_MESSAGE);
                break;
            case "INVALID_PASSWORD":
                JOptionPane.showMessageDialog(this, "Login failed. Invalid password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                break;
            case "NOT_APPROVED":
                JOptionPane.showMessageDialog(this, "Your company registration is pending approval.", "Login Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Login failed. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCompanyRepRegistration(String email, String name, String company, String dept, String position, String password, String confirmPassword) {
        if (email.isEmpty() || name.isEmpty() || company.isEmpty() || dept.isEmpty() || position.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CompanyRepresentative rep = new CompanyRepresentative(email, name, password, company, dept, position);
        dataManager.addUser(rep);
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
        JOptionPane.showMessageDialog(this, "Registration submitted for approval.", "Success", JOptionPane.INFORMATION_MESSAGE);
        tabbedPane.setSelectedIndex(0);
    }

    private void showResetPasswordDialog(String userID) {
        JDialog dialog = new JDialog(this, "Reset Password", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userIDLabel = new JLabel("User ID:");
        JTextField userIDField = new JTextField(userID, 20);
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField(20);
        JLabel confirmLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userIDLabel, gbc);
        gbc.gridx = 1;
        panel.add(userIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(newPasswordLabel, gbc);
        gbc.gridx = 1;
        panel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(confirmLabel, gbc);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        JButton resetButton = new JButton("Reset");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(resetButton, gbc);

        resetButton.addActionListener(e -> {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authManager.resetPassword(userIDField.getText(), newPassword)) {
                dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
                JOptionPane.showMessageDialog(dialog, "Password reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Password reset failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openDashboard() {
        User user = authManager.getCurrentUser();
        
        if (user instanceof Student) {
            new StudentDashboard((Student) user, dataManager, authManager, studentManager, filterManager, this).setVisible(true);
        } else if (user instanceof CompanyRepresentative) {
            new CompanyRepDashboard((CompanyRepresentative) user, dataManager, authManager, companyRepManager, this).setVisible(true);
        } else if (user instanceof CareerCenterStaff) {
            new StaffDashboard((CareerCenterStaff) user, dataManager, authManager, staffManager, filterManager, this).setVisible(true);
        }

        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
