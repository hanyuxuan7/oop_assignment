package data;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import entity.*;

public class DataManager {
    private Map<String, User> users;
    private Map<String, Internship> internships;
    private Map<String, InternshipApplication> applications;
    private Map<String, Student> students;
    private Map<String, CompanyRepresentative> companyReps;
    private Map<String, CareerCenterStaff> staffMembers;
    private List<ActivityLog> activityLogs;

    public DataManager() {
        this.users = new HashMap<>();
        this.internships = new HashMap<>();
        this.applications = new HashMap<>();
        this.students = new HashMap<>();
        this.companyReps = new HashMap<>();
        this.staffMembers = new HashMap<>();
        this.activityLogs = new ArrayList<>();
    }

    public void loadStudents(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\|");
                if (parts.length >= 5) {
                    String userID = parts[0].trim();
                    String name = parts[1].trim();
                    String password = parts[2].trim();
                    int yearOfStudy = Integer.parseInt(parts[3].trim());
                    String major = parts[4].trim();

                    Student student = new Student(userID, name, password, yearOfStudy, major);
                    students.put(userID, student);
                    users.put(userID, student);
                } else if (parts.length >= 4) {
                    String userID = parts[0].trim();
                    String name = parts[1].trim();
                    int yearOfStudy = Integer.parseInt(parts[2].trim());
                    String major = parts[3].trim();

                    Student student = new Student(userID, name, "password", yearOfStudy, major);
                    students.put(userID, student);
                    users.put(userID, student);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    public void loadStaff(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\|");
                if (parts.length >= 4) {
                    String userID = parts[0].trim();
                    String name = parts[1].trim();
                    String password = parts[2].trim();
                    String department = parts[3].trim();

                    CareerCenterStaff staff = new CareerCenterStaff(userID, name, password, department);
                    staffMembers.put(userID, staff);
                    users.put(userID, staff);
                } else if (parts.length >= 3) {
                    String userID = parts[0].trim();
                    String name = parts[1].trim();
                    String department = parts[2].trim();

                    CareerCenterStaff staff = new CareerCenterStaff(userID, name, "password", department);
                    staffMembers.put(userID, staff);
                    users.put(userID, staff);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading staff: " + e.getMessage());
        }
    }

    public void loadCompanyReps(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\|");
                if (parts.length >= 6) {
                    String userID = parts[0].trim();
                    String name = parts[1].trim();
                    String password = parts[2].trim();
                    String companyName = parts[3].trim();
                    String department = parts[4].trim();
                    String position = parts[5].trim();
                    boolean approved = Boolean.parseBoolean(parts[6].trim());

                    CompanyRepresentative rep = new CompanyRepresentative(userID, name, password, companyName, department, position);
                    rep.setApproved(approved);
                    companyReps.put(userID, rep);
                    users.put(userID, rep);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading company reps: " + e.getMessage());
        }
    }

    public void loadInternships(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\|");
                if (parts.length >= 11) {
                    String internshipID = parts[0].trim();
                    String title = parts[1].trim();
                    String description = parts[2].trim();
                    String level = parts[3].trim();
                    String preferredMajor = parts[4].trim();
                    LocalDate openingDate = LocalDate.parse(parts[5].trim());
                    LocalDate closingDate = LocalDate.parse(parts[6].trim());
                    String status = parts[7].trim();
                    String companyName = parts[8].trim();
                    String repInCharge = parts[9].trim();
                    int numSlots = Integer.parseInt(parts[10].trim());
                    int filledSlots = Integer.parseInt(parts[11].trim());
                    boolean visible = Boolean.parseBoolean(parts[12].trim());

                    Internship internship = new Internship(internshipID, title, description, level, preferredMajor,
                                                          openingDate, closingDate, companyName, repInCharge, numSlots);
                    internship.setStatus(status);
                    internship.setFilledSlots(filledSlots);
                    internship.setVisible(visible);
                    internships.put(internshipID, internship);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading internships: " + e.getMessage());
        }
    }

    public void loadApplications(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\|");
                if (parts.length >= 5) {
                    String applicationID = parts[0].trim();
                    String studentID = parts[1].trim();
                    String internshipID = parts[2].trim();
                    String status = parts[3].trim();
                    boolean confirmed = Boolean.parseBoolean(parts[4].trim());
                    boolean withdrawalRequested = Boolean.parseBoolean(parts[5].trim());
                    String withdrawalReason = parts.length > 6 ? parts[6].trim() : null;

                    InternshipApplication application = new InternshipApplication(applicationID, studentID, internshipID);
                    application.setStatus(status);
                    application.setConfirmed(confirmed);
                    if (withdrawalRequested) {
                        application.requestWithdrawal(withdrawalReason != null ? withdrawalReason : "");
                    }
                    applications.put(applicationID, application);

                    Student student = students.get(studentID);
                    if (student != null) {
                        student.addApplication(application);
                    }

                    Internship internship = internships.get(internshipID);
                    if (internship != null) {
                        internship.addApplication(application);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading applications: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        users.put(user.getUserID(), user);
        if (user instanceof Student) {
            students.put(user.getUserID(), (Student) user);
        } else if (user instanceof CompanyRepresentative) {
            companyReps.put(user.getUserID(), (CompanyRepresentative) user);
        } else if (user instanceof CareerCenterStaff) {
            staffMembers.put(user.getUserID(), (CareerCenterStaff) user);
        }
    }

    public User getUser(String userID) {
        return users.get(userID);
    }

    public Student getStudent(String studentID) {
        return students.get(studentID);
    }

    public CompanyRepresentative getCompanyRep(String repID) {
        return companyReps.get(repID);
    }

    public CareerCenterStaff getStaff(String staffID) {
        return staffMembers.get(staffID);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Collection<CompanyRepresentative> getAllCompanyReps() {
        return companyReps.values();
    }

    public Collection<CareerCenterStaff> getAllStaff() {
        return staffMembers.values();
    }

    public void addInternship(Internship internship) {
        internships.put(internship.getInternshipID(), internship);
    }

    public Internship getInternship(String internshipID) {
        return internships.get(internshipID);
    }

    public Collection<Internship> getAllInternships() {
        return internships.values();
    }

    public void addApplication(InternshipApplication application) {
        applications.put(application.getApplicationID(), application);
    }

    public InternshipApplication getApplication(String applicationID) {
        return applications.get(applicationID);
    }

    public Collection<InternshipApplication> getAllApplications() {
        return applications.values();
    }

    public void removeCompanyRepRegistration(String repID) {
        companyReps.remove(repID);
        users.remove(repID);
    }

    public void saveCompanyReps(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (CompanyRepresentative rep : companyReps.values()) {
                String line = rep.getUserID() + "|" + rep.getName() + "|" + rep.getPassword() + "|" +
                             rep.getCompanyName() + "|" + rep.getDepartment() + "|" + rep.getPosition() + "|" +
                             rep.isApproved();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving company reps: " + e.getMessage());
        }
    }

    public void saveInternships(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Internship internship : internships.values()) {
                String line = internship.getInternshipID() + "|" + internship.getTitle() + "|" +
                             internship.getDescription() + "|" + internship.getLevel() + "|" +
                             internship.getPreferredMajor() + "|" + internship.getOpeningDate() + "|" +
                             internship.getClosingDate() + "|" + internship.getStatus() + "|" +
                             internship.getCompanyName() + "|" + internship.getRepInCharge() + "|" +
                             internship.getNumSlots() + "|" + internship.getFilledSlots() + "|" +
                             internship.isVisible();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving internships: " + e.getMessage());
        }
    }

    public void saveApplications(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (InternshipApplication app : applications.values()) {
                String withdrawalReason = app.getWithdrawalReason() != null ? app.getWithdrawalReason() : "";
                String line = app.getApplicationID() + "|" + app.getStudentID() + "|" +
                             app.getInternshipID() + "|" + app.getStatus() + "|" + app.isConfirmed() + "|" +
                             app.isWithdrawalRequested() + "|" + withdrawalReason;
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving applications: " + e.getMessage());
        }
    }

    public void saveStudents(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Student student : students.values()) {
                String line = student.getUserID() + "|" + student.getName() + "|" +
                             student.getPassword() + "|" + student.getYearOfStudy() + "|" +
                             student.getMajor();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    public void saveStaff(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (CareerCenterStaff staff : staffMembers.values()) {
                String line = staff.getUserID() + "|" + staff.getName() + "|" +
                             staff.getPassword() + "|" + staff.getDepartment();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving staff: " + e.getMessage());
        }
    }

    public void addActivityLog(ActivityLog log) {
        activityLogs.add(log);
    }

    public List<ActivityLog> getAllActivityLogs() {
        return new ArrayList<>(activityLogs);
    }

    public void loadActivityLogs(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\|");
                if (parts.length >= 5) {
                    String activityID = parts[0].trim();
                    String userID = parts[1].trim();
                    String userType = parts[2].trim();
                    String activityDescription = parts[3].trim();
                    LocalDateTime timestamp = LocalDateTime.parse(parts[4].trim());
                    String relatedEntity = parts.length > 5 ? parts[5].trim() : "";

                    ActivityLog log = new ActivityLog(userID, userType, activityDescription, relatedEntity);
                    activityLogs.add(log);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading activity logs: " + e.getMessage());
        }
    }

    public void saveActivityLogs(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (ActivityLog log : activityLogs) {
                String line = log.getActivityID() + "|" + log.getUserID() + "|" +
                             log.getUserType() + "|" + log.getActivityDescription() + "|" +
                             log.getTimestamp() + "|" + log.getRelatedEntity();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving activity logs: " + e.getMessage());
        }
    }

    public void saveAllData(String studentsPath, String staffPath, String companyRepsPath, String internshipsPath, String applicationsPath) {
        saveStudents(studentsPath);
        saveStaff(staffPath);
        saveCompanyReps(companyRepsPath);
        saveInternships(internshipsPath);
        saveApplications(applicationsPath);
    }
}
