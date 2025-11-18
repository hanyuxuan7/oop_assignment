# SOLID Principles Refactoring Examples

## 1. Eliminating Hard-Coded Status Strings (OCP)

### BEFORE: Hard-Coded String
```java
// StudentManager.java (Original)
public boolean acceptPlacement(Student student, String applicationID) {
    // ...
    if (app.getStatus().equals("Successful")) {  // Hard-coded string
        student.setAcceptedInternshipID(app.getInternshipID());
        app.setConfirmed(true);
        
        if (internship.isFull()) {
            internship.setStatus("Filled");  // Another hard-coded string
        }
    }
}
```

**Problems:**
- Typos cause bugs
- Multiple files to update when changing status
- No compile-time safety
- Inconsistent naming across codebase

### AFTER: Using Enums
```java
// StudentApplicationManager.java (Enhanced)
public boolean acceptPlacement(Student student, String applicationID) {
    // ...
    if (app.getStatus().equals(ApplicationStatus.SUCCESSFUL.getDisplayName())) {
        student.setAcceptedInternshipID(app.getInternshipID());
        app.setConfirmed(true);
        
        if (internship.isFull()) {
            internship.setStatus(InternshipStatus.FILLED.getDisplayName());
        }
    }
}
```

**Benefits:**
- Compile-time checking
- IDE autocomplete support
- All statuses defined in one place
- Easy to add new statuses

---

## 2. Breaking Up God Classes (SRP)

### BEFORE: StudentManager Mixing Multiple Concerns
```java
// StudentManager.java (Original) - 136 lines, multiple responsibilities
public class StudentManager {
    private DataManager dataManager;

    public StudentManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // RESPONSIBILITY 1: Discovering internships
    public List<Internship> getAvailableInternships(Student student) { }
    public List<Internship> getAvailableInternships(Student student, boolean openOnly) { }

    // RESPONSIBILITY 2: Managing applications
    public boolean applyForInternship(Student student, String internshipID) { }
    public boolean withdrawApplication(Student student, String applicationID) { }
    public boolean acceptPlacement(Student student, String applicationID) { }

    // RESPONSIBILITY 3: Saving data
    private void saveData() {
        dataManager.saveAllData("data/students.txt", "data/staff.txt", 
                               "data/companyreps.txt", "data/internships.txt", 
                               "data/applications.txt");
    }
}
```

**Problems:**
- Hard to understand what the class does
- Multiple reasons to change
- Hard to test in isolation
- Can't reuse discovery logic without application logic

### AFTER: Split into Focused Managers
```java
// StudentInternshipDiscoveryManager.java (Enhanced)
public class StudentInternshipDiscoveryManager {
    private final InternshipRepository internshipRepository;

    public StudentInternshipDiscoveryManager(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    // SINGLE RESPONSIBILITY: Discovering internships
    public List<Internship> getAvailableInternships(Student student) { }
    public List<Internship> getAvailableInternships(Student student, boolean openOnly) { }
    public Internship getInternshipDetails(String internshipID) { }
}

// StudentApplicationManager.java (Enhanced)
public class StudentApplicationManager {
    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final DataPersistenceService persistenceService;

    // SINGLE RESPONSIBILITY: Managing applications
    public boolean applyForInternship(Student student, String internshipID) { }
    public boolean withdrawApplication(Student student, String applicationID) { }
    public boolean acceptPlacement(Student student, String applicationID) { }
    // NOTE: Saving handled by injected DataPersistenceService
}
```

**Benefits:**
- Each class has clear single responsibility
- Easier to understand and maintain
- Can test discovery without application logic
- Easier to extend individually

---

## 3. Dependency Inversion (DIP)

### BEFORE: Tight Coupling
```java
// StudentMenu.java (Original)
public class StudentMenu {
    private Scanner scanner;
    private DataManager dataManager;        // Concrete dependency
    private AuthenticationManager authManager;  // Concrete dependency
    private StudentManager studentManager;  // Concrete dependency
    private FilterManager filterManager;    // Concrete dependency

    public StudentMenu(Scanner scanner, DataManager dataManager, 
                       AuthenticationManager authManager,
                       StudentManager studentManager, 
                       FilterManager filterManager) {
        this.scanner = scanner;
        this.dataManager = dataManager;
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.filterManager = filterManager;
    }

    // Hard to test - needs real DataManager with real files
    private void changePassword() {
        // ...
        dataManager.saveAllData("data/students.txt", "data/staff.txt", 
                               "data/companyreps.txt", "data/internships.txt", 
                               "data/applications.txt");
    }
}
```

**Problems:**
- Depends on concrete classes, not abstractions
- Can't unit test without real file system
- Hard to swap implementations
- Tight coupling makes refactoring risky

### AFTER: Interface-Based Dependencies
```java
// StudentMenuRefactored.java (Enhanced)
public class StudentMenuRefactored {
    private final Scanner scanner;
    private final AuthenticationService authService;        // Interface
    private final StudentInternshipDiscoveryManager discoveryMgr;  // Focused
    private final StudentApplicationManager applicationMgr; // Focused
    private final DataPersistenceService persistenceService; // Interface

    public StudentMenuRefactored(Scanner scanner, 
                                 AuthenticationService authService,
                                 StudentInternshipDiscoveryManager discoveryMgr,
                                 StudentApplicationManager applicationMgr,
                                 DataPersistenceService persistenceService) {
        this.scanner = scanner;
        this.authService = authService;
        this.discoveryMgr = discoveryMgr;
        this.applicationMgr = applicationMgr;
        this.persistenceService = persistenceService;
    }

    private void changePassword() {
        if (authService.changePassword(oldPassword, newPassword)) {
            persistenceService.persistAllData();
            // ...
        }
    }
}

// Easy to test with mocks:
class StudentMenuTest {
    @Test
    public void testChangePasswordSavesData() {
        AuthenticationService mockAuth = mock(AuthenticationService.class);
        DataPersistenceService mockPersist = mock(DataPersistenceService.class);
        
        when(mockAuth.changePassword("old", "new")).thenReturn(true);
        
        // Create menu with mocks
        StudentMenuRefactored menu = new StudentMenuRefactored(
            scanner, mockAuth, mockDiscovery, mockApplication, mockPersist
        );
        
        // Test without touching file system
        menu.changePassword("old", "new");
        verify(mockPersist).persistAllData();
    }
}
```

**Benefits:**
- Depends on abstractions (interfaces)
- Can mock dependencies for testing
- Swap implementations without changing code
- Loose coupling = safe refactoring

---

## 4. Interface Segregation (ISP)

### BEFORE: Fat Interface
```java
// DataManager.java (Original) - 398 lines with ALL methods
public class DataManager {
    // User methods - not all managers need these
    public void addUser(User user) { }
    public User getUser(String userID) { }
    public Collection<User> getAllUsers() { }
    
    // Student-specific - StudentMenu doesn't need these
    public void loadStudents(String filePath) { }
    public Student getStudent(String studentID) { }
    
    // Company rep-specific - StudentMenu doesn't need these
    public CompanyRepresentative getCompanyRep(String repID) { }
    
    // Internship-specific - not all need filtering
    public void loadInternships(String filePath) { }
    public Collection<Internship> getAllInternships() { }
    
    // Application-specific
    public void loadApplications(String filePath) { }
    public InternshipApplication getApplication(String applicationID) { }
    
    // Activity log-specific
    public void loadActivityLogs(String filePath) { }
    public List<ActivityLog> getAllActivityLogs() { }
    
    // Save methods
    public void saveStudents(String filePath) { }
    public void saveStaff(String filePath) { }
    public void saveCompanyReps(String filePath) { }
    public void saveInternships(String filePath) { }
    public void saveApplications(String filePath) { }
    public void saveActivityLogs(String filePath) { }
    public void saveAllData(String studentsPath, String staffPath, ...) { }
}

// StudentManager depends on ALL of DataManager even though it only needs:
public class StudentManager {
    private DataManager dataManager;  // Overkill dependency
    // Only uses: getAllInternships(), getInternship(), getStudent(),
    //           addApplication(), saveAllData()
}
```

**Problems:**
- Clients depend on methods they don't use
- Hard to understand what's actually needed
- Can't mock just what you need
- Changes to unused methods affect all clients

### AFTER: Segregated Interfaces
```java
// StudentInternshipDiscoveryManager only depends on:
public interface InternshipRepository {
    Internship getInternship(String internshipID);
    Collection<Internship> getAllInternships();
}

public interface StudentRepository {
    Student getStudent(String studentID);
}

// StudentApplicationManager depends on:
public interface StudentRepository {
    Student getStudent(String studentID);
    void addStudent(Student student);  // For updates
}

public interface InternshipRepository {
    Internship getInternship(String internshipID);
    void addInternship(Internship internship);  // For updates
}

public interface ApplicationRepository {
    void addApplication(InternshipApplication application);
    InternshipApplication getApplication(String applicationID);
    Collection<InternshipApplication> getAllApplications();
}

public interface PersistenceManager {
    void saveAll();
    void saveApplications();
}

// Usage: Each class depends only on what it needs
public class StudentApplicationManager {
    private final StudentRepository studentRepository;        // Only student ops
    private final InternshipRepository internshipRepository;  // Only internship ops
    private final ApplicationRepository applicationRepository; // Only app ops
    private final DataPersistenceService persistenceService;  // Only save ops
}
```

**Benefits:**
- Depend only on needed methods
- Clear contract of what's used
- Easier to mock in tests
- Reduced coupling between components

---

## 5. Polymorphism Instead of Type Checking (OCP + Liskov)

### BEFORE: instanceof Checks
```java
// AuthenticationManager.java (Original)
public String getUserType(User user) {
    if (user instanceof Student) {
        return "Student";
    } else if (user instanceof CompanyRepresentative) {
        return "Company Representative";
    } else if (user instanceof CareerCenterStaff) {
        return "Career Center Staff";
    }
    return "Unknown";
}

// Violates OCP - must modify when adding new user types
public String login(String userID, String password) {
    User user = dataManager.getUser(userID);
    if (user instanceof CompanyRepresentative) {  // Type check
        CompanyRepresentative rep = (CompanyRepresentative) user;
        if (!rep.isApproved()) {  // Specific behavior
            if (!user.validatePassword(password)) {
                return "INVALID_PASSWORD";
            }
            return "NOT_APPROVED";
        }
    }
    // ...
}
```

**Problems:**
- instanceof checks scattered everywhere
- Must modify code when adding new user types
- Violates OCP and LSP

### AFTER: Polymorphism with Authorization Service
```java
// AuthorizationService.java (Enhanced)
public class AuthorizationService {
    public boolean isApproved(User user) {
        if (user instanceof CompanyRepresentative) {
            return ((CompanyRepresentative) user).isApproved();
        }
        return true;  // Other users are implicitly approved
    }
}

// AuthenticationService.java (Enhanced)
public class AuthenticationService {
    private final AuthorizationService authorizationService;
    
    public String login(String userID, String password) {
        User user = userRepository.getUser(userID);
        
        if (user == null) {
            return "INVALID_USER";
        }

        if (!authorizationService.isApproved(user)) {
            if (!user.validatePassword(password)) {
                return "INVALID_PASSWORD";
            }
            return "NOT_APPROVED";
        }

        if (user.validatePassword(password)) {
            this.currentUser = user;
            return "SUCCESS";
        }
        return "INVALID_PASSWORD";
    }
}
```

**Benefits:**
- Type checking centralized in one place
- Can add new user types without modifying login logic
- Follows OCP and LSP
- Easier to test

---

## 6. Configuration Management (DIP)

### BEFORE: Hard-Coded Paths
```java
// StudentManager.java (Original)
private void saveData() {
    dataManager.saveAllData(
        "data/students.txt",        // Hard-coded
        "data/staff.txt",           // Hard-coded
        "data/companyreps.txt",     // Hard-coded
        "data/internships.txt",     // Hard-coded
        "data/applications.txt"     // Hard-coded
    );
}

// StudentMenu.java (Original)
private void changePassword() {
    dataManager.saveAllData(
        "data/students.txt",        // Duplicated hard-coded
        "data/staff.txt",
        "data/companyreps.txt",
        "data/internships.txt",
        "data/applications.txt"
    );
}

// StaffMenu.java (Original)
private void changePassword() {
    dataManager.saveAllData(
        "data/students.txt",        // Duplicated again
        "data/staff.txt",
        "data/companyreps.txt",
        "data/internships.txt",
        "data/applications.txt"
    );
}
```

**Problems:**
- Paths duplicated in multiple files
- Changing paths requires updating everywhere
- No single source of truth
- Hard to switch between dev/test/prod configs

### AFTER: Centralized Configuration
```java
// FilePathConfiguration.java (Enhanced)
public class FilePathConfiguration {
    private final String studentFilePath;
    private final String staffFilePath;
    private final String companyRepFilePath;
    private final String internshipFilePath;
    private final String applicationFilePath;
    private final String activityLogFilePath;

    public static FilePathConfiguration defaultPaths() {
        return new FilePathConfiguration(
            "data/students.txt",
            "data/staff.txt",
            "data/companyreps.txt",
            "data/internships.txt",
            "data/applications.txt",
            "data/activitylogs.txt"
        );
    }

    public static FilePathConfiguration testPaths() {
        return new FilePathConfiguration(
            "test/students.txt",
            "test/staff.txt",
            "test/companyreps.txt",
            "test/internships.txt",
            "test/applications.txt",
            "test/activitylogs.txt"
        );
    }

    public String getStudentFilePath() { return studentFilePath; }
    // ... other getters
}

// Usage: Single source of truth
DataPersistenceService service = new DataPersistenceService(
    new FileBasedPersistenceManager(FilePathConfiguration.defaultPaths())
);
```

**Benefits:**
- Paths defined in one place
- Easy to add new configurations (dev, test, prod)
- No duplication
- DIP - depends on configuration abstraction
