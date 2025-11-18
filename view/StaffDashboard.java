package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import entity.*;
import data.DataManager;
import control.*;

public class StaffDashboard extends JFrame {
    private CareerCenterStaff staff;
    private DataManager dataManager;
    private AuthenticationManager authManager;
    private CareerCenterStaffManager staffManager;
    private FilterManager filterManager;
    private JFrame parentFrame;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public StaffDashboard(CareerCenterStaff staff, DataManager dataManager, AuthenticationManager authManager,
                         CareerCenterStaffManager staffManager, FilterManager filterManager, JFrame parentFrame) {
        this.staff = staff;
        this.dataManager = dataManager;
        this.authManager = authManager;
        this.staffManager = staffManager;
        this.filterManager = filterManager;
        this.parentFrame = parentFrame;

        setTitle("Staff Dashboard - " + staff.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
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
        contentPanel.add(createCompanyRepPanel(), "companyreps");
        contentPanel.add(createInternshipsPanel(), "internships");
        contentPanel.add(createWithdrawalsPanel(), "withdrawals");
        contentPanel.add(createReportsPanel(), "reports");
        contentPanel.add(createActivityLogPanel(), "activitylog");
        contentPanel.add(createChangePasswordPanel(), "password");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + staff.getName() + " | Department: " + staff.getDepartment());
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

        JLabel titleLabel = new JLabel("Staff Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        String[] buttons = {
            "Manage Company Rep Registrations",
            "Approve/Reject Internship Opportunities",
            "Manage Withdrawal Requests",
            "Generate Reports",
            "View Activity Log",
            "Change Password"
        };

        String[] panels = {
            "companyreps",
            "internships",
            "withdrawals",
            "reports",
            "activitylog",
            "password"
        };

        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            JButton button = new JButton(buttons[i]);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setPreferredSize(new Dimension(350, 50));

            button.addActionListener(e -> cardLayout.show(contentPanel, panels[index]));

            gbc.gridy = i + 1;
            gbc.gridwidth = 2;
            panel.add(button, gbc);
        }

        return panel;
    }

    private JPanel createCompanyRepPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<CompanyRepresentative> pendingReps = staffManager.getPendingRegistrations();

        if (pendingReps.isEmpty()) {
            JLabel noDataLabel = new JLabel("No pending registrations.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Email");
            model.addColumn("Company");
            model.addColumn("Position");
            model.addColumn("Department");

            for (CompanyRepresentative rep : pendingReps) {
                model.addRow(new Object[]{
                    rep.getName(),
                    rep.getUserID(),
                    rep.getCompanyName(),
                    rep.getPosition(),
                    rep.getDepartment()
                });
            }

            JTable table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton approveButton = new JButton("Approve Selected");
            JButton rejectButton = new JButton("Reject Selected");

            approveButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String email = (String) model.getValueAt(row, 1);
                    if (staffManager.approveCompanyRepRegistration(email, staff.getUserID())) {
                        JOptionPane.showMessageDialog(this, "Registration approved.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        model.removeRow(row);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a registration.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            rejectButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String email = (String) model.getValueAt(row, 1);
                    if (staffManager.rejectCompanyRepRegistration(email, staff.getUserID())) {
                        JOptionPane.showMessageDialog(this, "Registration rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        model.removeRow(row);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a registration.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            buttonPanel.add(approveButton);
            buttonPanel.add(rejectButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel createInternshipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<Internship> pendingInternships = staffManager.getPendingInternships();

        if (pendingInternships.isEmpty()) {
            JLabel noDataLabel = new JLabel("No pending internships.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Title");
            model.addColumn("Company");
            model.addColumn("Level");
            model.addColumn("Status");

            for (Internship internship : pendingInternships) {
                model.addRow(new Object[]{
                    internship.getInternshipID(),
                    internship.getTitle(),
                    internship.getCompanyName(),
                    internship.getLevel(),
                    internship.getStatus()
                });
            }

            JTable table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton approveButton = new JButton("Approve Selected");
            JButton rejectButton = new JButton("Reject Selected");

            approveButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String internshipID = (String) model.getValueAt(row, 0);
                    if (staffManager.approveInternship(internshipID, staff.getUserID())) {
                        JOptionPane.showMessageDialog(this, "Internship approved.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        model.removeRow(row);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an internship.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            rejectButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String internshipID = (String) model.getValueAt(row, 0);
                    if (staffManager.rejectInternship(internshipID, staff.getUserID())) {
                        JOptionPane.showMessageDialog(this, "Internship rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        model.removeRow(row);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an internship.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            buttonPanel.add(approveButton);
            buttonPanel.add(rejectButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel createWithdrawalsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<InternshipApplication> pendingWithdrawals = staffManager.getPendingWithdrawals();

        if (pendingWithdrawals.isEmpty()) {
            JLabel noDataLabel = new JLabel("No pending withdrawal requests.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Application ID");
            model.addColumn("Student Name");
            model.addColumn("Internship Title");
            model.addColumn("Withdrawal Reason");

            for (InternshipApplication app : pendingWithdrawals) {
                Student student = dataManager.getStudent(app.getStudentID());
                Internship internship = dataManager.getInternship(app.getInternshipID());

                if (student != null && internship != null) {
                    model.addRow(new Object[]{
                        app.getApplicationID(),
                        student.getName(),
                        internship.getTitle(),
                        app.getWithdrawalReason()
                    });
                }
            }

            JTable table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton approveButton = new JButton("Approve Selected");
            JButton rejectButton = new JButton("Reject Selected");

            approveButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String applicationID = (String) model.getValueAt(row, 0);
                    if (staffManager.approveWithdrawal(applicationID, staff.getUserID())) {
                        JOptionPane.showMessageDialog(this, "Withdrawal approved.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        model.removeRow(row);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a withdrawal request.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            rejectButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String applicationID = (String) model.getValueAt(row, 0);
                    if (staffManager.rejectWithdrawal(applicationID, staff.getUserID())) {
                        JOptionPane.showMessageDialog(this, "Withdrawal rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        model.removeRow(row);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a withdrawal request.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            buttonPanel.add(approveButton);
            buttonPanel.add(rejectButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Options"));

        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Status", "Major", "Level", "All Internships"});
        JTextField filterField = new JTextField(15);
        JButton filterButton = new JButton("Apply Filter");

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Title");
        model.addColumn("Company");
        model.addColumn("Status");
        model.addColumn("Level");
        model.addColumn("Major");

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        filterButton.addActionListener(e -> {
            String filterType = (String) filterCombo.getSelectedItem();
            String filterValue = filterField.getText().trim();

            model.setRowCount(0);

            List<Internship> results = null;
            switch (filterType) {
                case "Status":
                    results = staffManager.getInternshipsByFilter(filterValue, null, null);
                    break;
                case "Major":
                    results = staffManager.getInternshipsByFilter(null, filterValue, null);
                    break;
                case "Level":
                    results = staffManager.getInternshipsByFilter(null, null, filterValue);
                    break;
                case "All Internships":
                    results = new java.util.ArrayList<>(dataManager.getAllInternships());
                    break;
            }

            if (results != null) {
                for (Internship internship : results) {
                    model.addRow(new Object[]{
                        internship.getInternshipID(),
                        internship.getTitle(),
                        internship.getCompanyName(),
                        internship.getStatus(),
                        internship.getLevel(),
                        internship.getPreferredMajor()
                    });
                }
            }
        });

        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(filterCombo);
        filterPanel.add(filterField);
        filterPanel.add(filterButton);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActivityLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<ActivityLog> activityLogs = dataManager.getAllActivityLogs();

        if (activityLogs.isEmpty()) {
            JLabel noDataLabel = new JLabel("No activities recorded.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Timestamp");
            model.addColumn("User ID");
            model.addColumn("User Type");
            model.addColumn("Activity");
            model.addColumn("Related Entity");

            for (ActivityLog log : activityLogs) {
                model.addRow(new Object[]{
                    log.getTimestamp(),
                    log.getUserID(),
                    log.getUserType(),
                    log.getActivityDescription(),
                    log.getRelatedEntity()
                });
            }

            JTable table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
            dataManager.saveActivityLogs("data/activitylogs.txt");
            JOptionPane.showMessageDialog(this, "Password changed successfully! Please log in again.", "Success", JOptionPane.INFORMATION_MESSAGE);
            logout();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to change password. Old password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        authManager.logout();
        dataManager.saveAllData("data/students.txt", "data/staff.txt", "data/companyreps.txt", "data/internships.txt", "data/applications.txt");
        dataManager.saveActivityLogs("data/activitylogs.txt");
        this.dispose();
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}
