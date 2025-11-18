package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import entity.*;
import entity.InternshipLevel;
import data.DataManager;
import control.*;

public class CompanyRepDashboard extends JFrame {
    private CompanyRepresentative rep;
    private DataManager dataManager;
    private AuthenticationManager authManager;
    private CompanyRepresentativeManager companyRepManager;
    private JFrame parentFrame;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public CompanyRepDashboard(CompanyRepresentative rep, DataManager dataManager, AuthenticationManager authManager,
                              CompanyRepresentativeManager companyRepManager, JFrame parentFrame) {
        this.rep = rep;
        this.dataManager = dataManager;
        this.authManager = authManager;
        this.companyRepManager = companyRepManager;
        this.parentFrame = parentFrame;

        setTitle("Company Representative Dashboard - " + rep.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
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
        contentPanel.add(createCreateInternshipPanel(), "create");
        contentPanel.add(createViewInternshipsPanel(), "view");
        contentPanel.add(createEditInternshipPanel(), "edit");
        contentPanel.add(createApplicationsPanel(), "applications");
        contentPanel.add(createVisibilityPanel(), "visibility");
        contentPanel.add(createChangePasswordPanel(), "password");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + rep.getName() + " | Company: " + rep.getCompanyName() + " | Position: " + rep.getPosition());
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

        JLabel titleLabel = new JLabel("Company Representative Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        String[] buttons = {
            "Create Internship Opportunity",
            "View My Internship Opportunities",
            "Edit Internship Opportunity",
            "View Applications for Internship",
            "Approve/Reject Application",
            "Toggle Internship Visibility",
            "Change Password"
        };

        String[] panels = {
            "create",
            "view",
            "edit",
            "applications",
            null,
            "visibility",
            "password"
        };

        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            JButton button = new JButton(buttons[i]);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setPreferredSize(new Dimension(350, 50));

            button.addActionListener(e -> {
                if (index == 4) {
                    approveRejectApplicationDialog();
                } else {
                    cardLayout.show(contentPanel, panels[index]);
                }
            });

            gbc.gridy = i + 1;
            gbc.gridwidth = 2;
            panel.add(button, gbc);
        }

        return panel;
    }

    private JPanel createCreateInternshipPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create New Internship Opportunity");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JTextField titleField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JComboBox<String> levelCombo = new JComboBox<>(new String[]{"Basic", "Intermediate", "Advanced"});
        JTextField majorField = new JTextField(20);
        JSpinner slotsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        JPanel datePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel openingDatePanel = createDatePanel("Opening Date:");
        JPanel closingDatePanel = createDatePanel("Closing Date:");
        datePanel.add(openingDatePanel);
        datePanel.add(closingDatePanel);

        addFormField(panel, "Title:", titleField, gbc, 1);
        addFormField(panel, "Description:", new JScrollPane(descriptionArea), gbc, 2);
        addFormField(panel, "Level:", levelCombo, gbc, 3);
        addFormField(panel, "Preferred Major:", majorField, gbc, 4);
        addFormField(panel, "Number of Slots:", slotsSpinner, gbc, 5);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(datePanel, gbc);

        JButton createButton = new JButton("Create Internship");
        gbc.gridy = 7;
        createButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String level = (String) levelCombo.getSelectedItem();
            String major = majorField.getText().trim();
            int slots = (Integer) slotsSpinner.getValue();

            if (title.isEmpty() || description.isEmpty() || major.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JSpinner[] yearSpinners = new JSpinner[2];
            JSpinner[] monthSpinners = new JSpinner[2];
            JSpinner[] daySpinners = new JSpinner[2];

            extractDateSpinners(openingDatePanel, yearSpinners, monthSpinners, daySpinners, 0);
            extractDateSpinners(closingDatePanel, yearSpinners, monthSpinners, daySpinners, 1);

            try {
                LocalDate openingDate = LocalDate.of(
                    (Integer) yearSpinners[0].getValue(),
                    (Integer) monthSpinners[0].getValue(),
                    (Integer) daySpinners[0].getValue()
                );
                LocalDate closingDate = LocalDate.of(
                    (Integer) yearSpinners[1].getValue(),
                    (Integer) monthSpinners[1].getValue(),
                    (Integer) daySpinners[1].getValue()
                );

                if (companyRepManager.createInternship(rep, title, description, level, major, openingDate, closingDate, slots)) {
                    JOptionPane.showMessageDialog(this, "Internship created successfully! Awaiting approval.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    titleField.setText("");
                    descriptionArea.setText("");
                    majorField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create internship. You may have reached the maximum limit of 5.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(createButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridy = 8;
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        panel.add(backButton, gbc);

        return panel;
    }

    private JPanel createDatePanel(String label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBorder(BorderFactory.createTitledBorder(label));

        panel.add(new JLabel("Year:"));
        panel.add(new JSpinner(new SpinnerNumberModel(2024, 2000, 2100, 1)));
        panel.add(new JLabel("Month:"));
        panel.add(new JSpinner(new SpinnerNumberModel(1, 1, 12, 1)));
        panel.add(new JLabel("Day:"));
        panel.add(new JSpinner(new SpinnerNumberModel(1, 1, 31, 1)));

        return panel;
    }

    private void extractDateSpinners(JPanel panel, JSpinner[] yearSpinners, JSpinner[] monthSpinners, JSpinner[] daySpinners, int index) {
        Component[] components = panel.getComponents();
        int spinnerIndex = 0;
        for (Component comp : components) {
            if (comp instanceof JSpinner) {
                if (spinnerIndex == 0) yearSpinners[index] = (JSpinner) comp;
                else if (spinnerIndex == 1) monthSpinners[index] = (JSpinner) comp;
                else if (spinnerIndex == 2) daySpinners[index] = (JSpinner) comp;
                spinnerIndex++;
            }
        }
    }

    private JPanel createViewInternshipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<Internship> internships = companyRepManager.getCreatedInternships(rep);

        if (internships.isEmpty()) {
            JLabel noDataLabel = new JLabel("You have no internship opportunities.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Title");
            model.addColumn("Status");
            model.addColumn("Level");
            model.addColumn("Filled Slots");
            model.addColumn("Total Slots");

            for (Internship internship : internships) {
                model.addRow(new Object[]{
                    internship.getInternshipID(),
                    internship.getTitle(),
                    internship.getStatus(),
                    internship.getLevel(),
                    internship.getFilledSlots(),
                    internship.getNumSlots()
                });
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createEditInternshipPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Edit Internship Opportunity");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        List<Internship> internships = companyRepManager.getCreatedInternships(rep);

        if (internships.isEmpty()) {
            JLabel noDataLabel = new JLabel("You have no internship opportunities to edit.");
            panel.add(noDataLabel, gbc);
        } else {
            JComboBox<String> internshipCombo = new JComboBox<>();
            for (Internship internship : internships) {
                internshipCombo.addItem(internship.getInternshipID() + " - " + internship.getTitle());
            }

            addFormField(panel, "Select Internship:", internshipCombo, gbc, 1);

            JTextField titleField = new JTextField(20);
            JTextArea descriptionArea = new JTextArea(3, 20);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            JComboBox<String> levelCombo = new JComboBox<>(new String[]{"", "Basic", "Intermediate", "Advanced"});
            JTextField majorField = new JTextField(20);
            JSpinner slotsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

            addFormField(panel, "New Title:", titleField, gbc, 2);
            addFormField(panel, "New Description:", new JScrollPane(descriptionArea), gbc, 3);
            addFormField(panel, "New Level:", levelCombo, gbc, 4);
            addFormField(panel, "New Major:", majorField, gbc, 5);
            addFormField(panel, "New Slots:", slotsSpinner, gbc, 6);

            JButton updateButton = new JButton("Update Internship");
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.gridwidth = 2;
            updateButton.addActionListener(e -> {
                int selectedIndex = internshipCombo.getSelectedIndex();
                if (selectedIndex < 0) {
                    JOptionPane.showMessageDialog(this, "Please select an internship.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Internship selected = internships.get(selectedIndex);
                String title = titleField.getText().trim().isEmpty() ? null : titleField.getText().trim();
                String description = descriptionArea.getText().trim().isEmpty() ? null : descriptionArea.getText().trim();
                String level = levelCombo.getSelectedItem().toString().isEmpty() ? null : levelCombo.getSelectedItem().toString();
                String major = majorField.getText().trim().isEmpty() ? null : majorField.getText().trim();
                int slots = (Integer) slotsSpinner.getValue();

                if (companyRepManager.updateInternshipDetails(selected.getInternshipID(), title, description, level, major, null, null, slots, rep.getUserID())) {
                    JOptionPane.showMessageDialog(this, "Internship updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update internship.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(updateButton, gbc);

            JButton backButton = new JButton("Back");
            gbc.gridy = 8;
            backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
            panel.add(backButton, gbc);
        }

        return panel;
    }

    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<Internship> internships = companyRepManager.getCreatedInternships(rep);

        if (internships.isEmpty()) {
            JLabel noDataLabel = new JLabel("You have no internship opportunities.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            JComboBox<String> internshipCombo = new JComboBox<>();
            for (Internship internship : internships) {
                internshipCombo.addItem(internship.getInternshipID() + " - " + internship.getTitle() + " (" + internship.getApplications().size() + " apps)");
            }

            JPanel selectionPanel = new JPanel();
            selectionPanel.add(new JLabel("Select Internship:"));
            selectionPanel.add(internshipCombo);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Application ID");
            model.addColumn("Student Name");
            model.addColumn("Student Email");
            model.addColumn("Status");

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);

            internshipCombo.addActionListener(e -> {
                model.setRowCount(0);
                int selectedIndex = internshipCombo.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Internship selected = internships.get(selectedIndex);
                    List<InternshipApplication> applications = companyRepManager.getApplicationsForInternship(selected.getInternshipID());
                    for (InternshipApplication app : applications) {
                        Student student = companyRepManager.getStudentDetails(app.getStudentID());
                        if (student != null) {
                            model.addRow(new Object[]{
                                app.getApplicationID(),
                                student.getName(),
                                student.getUserID(),
                                app.getStatus()
                            });
                        }
                    }
                }
            });

            panel.add(selectionPanel, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }

    private void approveRejectApplicationDialog() {
        String applicationID = JOptionPane.showInputDialog(this, "Enter Application ID:");
        if (applicationID != null && !applicationID.trim().isEmpty()) {
            String[] options = {"Approve", "Reject", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this, "Approve or Reject?", "Application Action", 
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

            if (choice == 0) {
                if (companyRepManager.approveApplication(applicationID, rep.getUserID())) {
                    JOptionPane.showMessageDialog(this, "Application approved.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to approve application.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (choice == 1) {
                if (companyRepManager.rejectApplication(applicationID, rep.getUserID())) {
                    JOptionPane.showMessageDialog(this, "Application rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reject application.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private JPanel createVisibilityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        JPanel topPanel = new JPanel();
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        List<Internship> internships = new java.util.ArrayList<>(dataManager.getAllInternships());

        if (internships.isEmpty()) {
            JLabel noDataLabel = new JLabel("No internships available.");
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Title");
            model.addColumn("Company");
            model.addColumn("Visibility");

            for (Internship internship : internships) {
                model.addRow(new Object[]{
                    internship.getInternshipID(),
                    internship.getTitle(),
                    internship.getCompanyName(),
                    internship.isVisible() ? "Visible" : "Hidden"
                });
            }

            JTable table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            JButton toggleButton = new JButton("Toggle Visibility");
            toggleButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String internshipID = (String) model.getValueAt(row, 0);
                    companyRepManager.toggleInternshipVisibility(internshipID, rep.getUserID());
                    String newVisibility = internships.get(row).isVisible() ? "Hidden" : "Visible";
                    model.setValueAt(newVisibility, row, 3);
                    JOptionPane.showMessageDialog(this, "Visibility toggled to: " + newVisibility, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an internship.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(toggleButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
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
        changeButton.addActionListener(e -> {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
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
        });
        panel.add(changeButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridy = 5;
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "menu"));
        panel.add(backButton, gbc);

        return panel;
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void addPasswordField(JPanel panel, String label, JPasswordField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
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
