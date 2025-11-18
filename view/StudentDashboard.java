package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import entity.*;
import data.DataManager;
import control.*;

public class StudentDashboard extends JFrame {
    private Student student;
    private DataManager dataManager;
    private AuthenticationManager authManager;
    private StudentManager studentManager;
    private FilterManager filterManager;
    private JFrame parentFrame;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public StudentDashboard(Student student, DataManager dataManager, AuthenticationManager authManager,
                           StudentManager studentManager, FilterManager filterManager, JFrame parentFrame) {
        this.student = student;
        this.dataManager = dataManager;
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.filterManager = filterManager;
        this.parentFrame = parentFrame;

        setTitle("Student Dashboard - " + student.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        createUI();
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createMenuPanel(), "menu");
        contentPanel.add(createInternshipsPanel(), "internships");
        contentPanel.add(createApplicationsPanel(), "applications");
        contentPanel.add(createChangePasswordPanel(), "password");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName() + " | Year: " + student.getYearOfStudy() + " | Major: " + student.getMajor());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Student Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        String[] buttons = {
            "View Available Internships",
            "Apply for Internship",
            "View My Applications",
            "Accept Placement",
            "Request Withdrawal",
            "Change Password"
        };

        String[] panels = {
            "internships",
            null,
            "applications",
            null,
            null,
            "password"
        };

        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            JButton button = new JButton(buttons[i]);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setPreferredSize(new Dimension(300, 50));

            button.addActionListener(e -> handleMenuAction(index, panels[index]));

            gbc.gridy = i + 1;
            gbc.gridwidth = 2;
            panel.add(button, gbc);
        }

        return panel;
    }

    private void handleMenuAction(int index, String panelName) {
        switch (index) {
            case 0:
                cardLayout.show(contentPanel, "internships");
                break;
            case 1:
                applyForInternshipDialog();
                break;
            case 2:
                cardLayout.show(contentPanel, "applications");
                break;
            case 3:
                acceptPlacementDialog();
                break;
            case 4:
                requestWithdrawalDialog();
                break;
            case 5:
                cardLayout.show(contentPanel, panelName);
                break;
        }
    }

    private JPanel createInternshipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<Internship> internships = studentManager.getAvailableInternships(student);

        if (internships.isEmpty()) {
            JLabel noDataLabel = new JLabel("No internships available for your profile.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Internship ID");
            model.addColumn("Title");
            model.addColumn("Company");
            model.addColumn("Level");
            model.addColumn("Closing Date");

            for (Internship internship : internships) {
                model.addRow(new Object[]{
                    internship.getInternshipID(),
                    internship.getTitle(),
                    internship.getCompanyName(),
                    internship.getLevel(),
                    internship.getClosingDate()
                });
            }

            JTable table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.addMouseListener(new MouseAdapter() {
                public void mouseClickEvent(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = table.getSelectedRow();
                        if (row >= 0) {
                            String internshipID = (String) model.getValueAt(row, 0);
                            Internship internship = studentManager.getInternshipDetails(internshipID);
                            if (internship != null) {
                                showInternshipDetails(internship);
                            }
                        }
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            JLabel instructionLabel = new JLabel("Double-click a row to view details");
            panel.add(instructionLabel, BorderLayout.SOUTH);
        }

        return panel;
    }

    private void showInternshipDetails(Internship internship) {
        JDialog dialog = new JDialog(this, "Internship Details", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addDetailField(panel, "Internship ID:", internship.getInternshipID(), gbc, 0);
        addDetailField(panel, "Title:", internship.getTitle(), gbc, 1);
        addDetailField(panel, "Company:", internship.getCompanyName(), gbc, 2);
        addDetailField(panel, "Level:", internship.getLevel(), gbc, 3);
        addDetailField(panel, "Preferred Major:", internship.getPreferredMajor(), gbc, 4);
        addDetailField(panel, "Opening Date:", internship.getOpeningDate().toString(), gbc, 5);
        addDetailField(panel, "Closing Date:", internship.getClosingDate().toString(), gbc, 6);
        addDetailField(panel, "Available Slots:", String.valueOf(internship.getNumSlots() - internship.getFilledSlots()), gbc, 7);

        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JTextArea descriptionArea = new JTextArea(internship.getDescription(), 5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        panel.add(new JScrollPane(descriptionArea), gbc);

        JButton closeButton = new JButton("Close");
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(closeButton, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addDetailField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JLabel valueLabel = new JLabel(value != null ? value : "N/A");
        panel.add(valueLabel, gbc);
    }

    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<InternshipApplication> applications = studentManager.getStudentApplications(student);

        if (applications.isEmpty()) {
            JLabel noDataLabel = new JLabel("You have no applications.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Application ID");
            model.addColumn("Internship ID");
            model.addColumn("Internship Title");
            model.addColumn("Status");
            model.addColumn("Withdrawal Status");

            for (InternshipApplication app : applications) {
                Internship internship = studentManager.getInternshipDetails(app.getInternshipID());
                String internshipTitle = internship != null ? internship.getTitle() : "Unknown";
                String withdrawalStatus = app.isWithdrawalRequested() ? "Pending Approval" : "None";

                model.addRow(new Object[]{
                    app.getApplicationID(),
                    app.getInternshipID(),
                    internshipTitle,
                    app.getStatus(),
                    withdrawalStatus
                });
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createChangePasswordPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        addPasswordField(panel, "Old Password:", oldPasswordField, gbc, 1);
        addPasswordField(panel, "New Password:", newPasswordField, gbc, 2);
        addPasswordField(panel, "Confirm Password:", confirmPasswordField, gbc, 3);

        JButton changeButton = new JButton("Change Password");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        changeButton.addActionListener(e -> changePassword(
            new String(oldPasswordField.getPassword()),
            new String(newPasswordField.getPassword()),
            new String(confirmPasswordField.getPassword())
        ));
        panel.add(changeButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridy = 5;
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        panel.add(backButton, gbc);

        return panel;
    }

    private void addPasswordField(JPanel panel, String label, JPasswordField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void applyForInternshipDialog() {
        String internshipID = JOptionPane.showInputDialog(this, "Enter Internship ID to apply:");
        if (internshipID != null && !internshipID.trim().isEmpty()) {
            if (studentManager.applyForInternship(student, internshipID)) {
                JOptionPane.showMessageDialog(this, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to apply. Check application limits or internship status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void acceptPlacementDialog() {
        if (student.getAcceptedInternshipID() != null) {
            JOptionPane.showMessageDialog(this, "You have already accepted a placement.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String applicationID = JOptionPane.showInputDialog(this, "Enter Application ID to accept:");
        if (applicationID != null && !applicationID.trim().isEmpty()) {
            if (studentManager.acceptPlacement(student, applicationID)) {
                JOptionPane.showMessageDialog(this, "Placement accepted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to accept placement. Application may not be successful.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void requestWithdrawalDialog() {
        String applicationID = JOptionPane.showInputDialog(this, "Enter Application ID to withdraw:");
        if (applicationID != null && !applicationID.trim().isEmpty()) {
            if (studentManager.withdrawApplication(student, applicationID)) {
                JOptionPane.showMessageDialog(this, "Withdrawal request submitted for approval.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to request withdrawal.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (oldPassword.equals(newPassword)) {
            JOptionPane.showMessageDialog(this, "New password must be different from the old password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authManager.changePassword(oldPassword, newPassword)) {
            dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
            JOptionPane.showMessageDialog(this, "Password changed successfully! Please log in again.", "Success", JOptionPane.INFORMATION_MESSAGE);
            logout();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to change password. Old password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        authManager.logout();
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
        this.dispose();
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}
