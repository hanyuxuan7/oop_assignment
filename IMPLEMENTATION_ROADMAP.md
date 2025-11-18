# SOLID Principles Implementation Roadmap

## Quick Summary

This document provides a step-by-step roadmap for integrating the SOLID principle enhancements into your existing codebase.

---

## Phase 1: Foundation (Week 1-2)
**Goal:** Add core infrastructure without breaking existing code

### Step 1: Add Enums
1. Copy `entity/ApplicationStatus.java` to your project
2. Copy `entity/InternshipStatus.java` to your project
3. **Action:** No code changes needed yet - enums are additive

### Step 2: Add Interfaces
1. Copy all repository interfaces:
   - `data/UserRepository.java`
   - `data/StudentRepository.java`
   - `data/InternshipRepository.java`
   - `data/ApplicationRepository.java`
2. Copy `data/PersistenceManager.java`
3. Copy `entity/RoleSpecificInterfaces.java`

### Step 3: Add Configuration
1. Copy `data/FilePathConfiguration.java`
2. **Action:** This centralizes all file paths for future use

### Validation
- Project still compiles with all existing code
- No existing functionality broken
- New interfaces available for gradual adoption

---

## Phase 2: Extract Services (Week 2-3)
**Goal:** Create new service classes alongside existing ones

### Step 1: Authentication Services
1. Copy `control/AuthenticationService.java`
2. Copy `control/AuthorizationService.java`
3. **Do NOT modify** existing `AuthenticationManager` yet

### Step 2: New Manager Services
1. Copy `control/DataPersistenceService.java`
2. Copy `control/StudentApplicationManager.java`
3. Copy `control/StudentInternshipDiscoveryManager.java`
4. Copy `control/InternshipFilterStrategy.java`

### Step 3: Update New Services to Match Current Behavior
- Verify `StudentApplicationManager` matches original logic
- Verify `StudentInternshipDiscoveryManager` matches original logic
- Test that new managers produce same results as old managers

### Validation
- Compile without errors
- New services can be unit tested with mocks
- Parallel implementation with existing code

---

## Phase 3: Create Repository Implementations (Week 3-4)
**Goal:** Implement repository interfaces using existing DataManager logic

### Create Implementation Classes

```java
// data/FileBasedUserRepository.java
public class FileBasedUserRepository implements UserRepository {
    private final Map<String, User> users;
    private final FilePathConfiguration config;
    
    public FileBasedUserRepository(FilePathConfiguration config) {
        this.config = config;
        this.users = new HashMap<>();
    }
    
    @Override
    public void loadUsers(String filePath) {
        // Copy logic from DataManager.loadStudents/loadStaff/loadCompanyReps
        // Loading code here
    }
    
    @Override
    public void saveUsers(String filePath) {
        // Copy logic from DataManager.saveStudents/saveStaff/saveCompanyReps
        // Saving code here
    }
    
    @Override
    public void addUser(User user) {
        users.put(user.getUserID(), user);
    }
    
    // Implement remaining methods...
}
```

**Steps:**
1. Create `FileBasedUserRepository`
2. Create `FileBasedStudentRepository`
3. Create `FileBasedInternshipRepository`
4. Create `FileBasedApplicationRepository`
5. Create `FileBasedPersistenceManager` implementing `PersistenceManager`

### Validation
- All repository implementations compile
- Can create instances with FilePathConfiguration
- Ready for dependency injection

---

## Phase 4: Migrate View Layer (Week 4-5)
**Goal:** Update menu classes to use new services

### Update StudentMenu

**Before:**
```java
public StudentMenu(Scanner scanner, DataManager dataManager, 
                   AuthenticationManager authManager,
                   StudentManager studentManager, 
                   FilterManager filterManager) {
    // ...
}
```

**After:**
```java
public StudentMenu(Scanner scanner, 
                   AuthenticationService authService,
                   StudentInternshipDiscoveryManager discoveryMgr,
                   StudentApplicationManager applicationMgr,
                   DataPersistenceService persistenceService) {
    // ...
}
```

**Steps for Each Menu Class:**
1. Update constructor parameters to inject new services
2. Replace DataManager calls with repository calls
3. Replace direct save calls with DataPersistenceService
4. Update status comparisons to use enums

### Example Refactoring

**Before:**
```java
private void viewAvailableInternships(Student student) {
    List<Internship> internships = studentManager.getAvailableInternships(student, !showAll);
    // Display logic
}

private void changePassword() {
    if (authManager.changePassword(oldPassword, newPassword)) {
        dataManager.saveAllData("data/students.txt", "data/staff.txt", 
                               "data/companyreps.txt", "data/internships.txt", 
                               "data/applications.txt");
        // ...
    }
}
```

**After:**
```java
private void viewAvailableInternships(Student student) {
    List<Internship> internships = discoveryMgr.getAvailableInternships(student, !showAll);
    // Display logic (unchanged)
}

private void changePassword() {
    if (authService.changePassword(oldPassword, newPassword)) {
        persistenceService.persistAllData();
        // ...
    }
}
```

---

## Phase 5: Migrate Business Logic (Week 5-6)
**Goal:** Update managers to use new services

### Update Each Manager

**StudentManager → Split into two managers**
1. Move discovery logic to `StudentInternshipDiscoveryManager`
2. Move application logic to `StudentApplicationManager`
3. Delete or deprecate original `StudentManager`

**CareerCenterStaffManager**
1. Replace `DataManager` with repository interfaces
2. Replace direct save calls with `DataPersistenceService`
3. Update status strings to use enums

**CompanyRepresentativeManager**
1. Same as above

### Example: CareerCenterStaffManager

**Before:**
```java
public class CareerCenterStaffManager {
    private DataManager dataManager;
    
    private void saveData() {
        dataManager.saveAllData("data/students.txt", "data/staff.txt", 
                               "data/companyreps.txt", "data/internships.txt", 
                               "data/applications.txt");
    }
    
    public boolean approveInternship(String internshipID) {
        Internship internship = dataManager.getInternship(internshipID);
        if (internship != null && internship.getStatus().equals("Pending")) {
            internship.setStatus("Approved");
            saveData();
            return true;
        }
        return false;
    }
}
```

**After:**
```java
public class CareerCenterStaffManager {
    private final InternshipRepository internshipRepository;
    private final DataPersistenceService persistenceService;
    
    public CareerCenterStaffManager(InternshipRepository internshipRepository,
                                    DataPersistenceService persistenceService) {
        this.internshipRepository = internshipRepository;
        this.persistenceService = persistenceService;
    }
    
    public boolean approveInternship(String internshipID) {
        Internship internship = internshipRepository.getInternship(internshipID);
        if (internship != null && 
            internship.getStatus().equals(InternshipStatus.PENDING.getDisplayName())) {
            internship.setStatus(InternshipStatus.APPROVED.getDisplayName());
            persistenceService.persistInternshipData();
            return true;
        }
        return false;
    }
}
```

---

## Phase 6: Update InternshipApplication & Internship (Week 6)
**Goal:** Update entities to use enums

### InternshipApplication Changes

**Before:**
```java
public class InternshipApplication {
    private String status;
    
    public InternshipApplication(...) {
        this.status = "Pending";
    }
}
```

**After:**
```java
public class InternshipApplication {
    private ApplicationStatus status;
    
    public InternshipApplication(...) {
        this.status = ApplicationStatus.PENDING;
    }
    
    public void setStatus(String status) {
        this.status = ApplicationStatus.fromString(status);
    }
    
    public String getStatusAsString() {
        return status.getDisplayName();
    }
}
```

### Internship Changes
- Replace `String status` with `InternshipStatus status`
- Update all comparisons to use enums
- Update getters to return display names where needed

---

## Phase 7: Update DataManager (Week 6-7)
**Goal:** Make DataManager implement repository interfaces

### Implement Interfaces

```java
public class DataManager implements UserRepository, StudentRepository, 
                                    InternshipRepository, ApplicationRepository,
                                    PersistenceManager {
    // Keep existing implementations
    // Add new interface methods that delegate to existing code
}
```

### Minimal Changes to DataManager
- Add `implements` clauses
- No changes to existing methods
- All old code still works

---

## Phase 8: Dependency Injection Setup (Week 7-8)
**Goal:** Wire dependencies together

### Create Factory Class

```java
public class ManagerFactory {
    private final FilePathConfiguration config;
    private final DataManager dataManager;
    
    public ManagerFactory() {
        this.config = FilePathConfiguration.defaultPaths();
        this.dataManager = new DataManager();
        dataManager.loadAll();
    }
    
    public StudentInternshipDiscoveryManager createDiscoveryManager() {
        return new StudentInternshipDiscoveryManager(dataManager);
    }
    
    public StudentApplicationManager createApplicationManager() {
        return new StudentApplicationManager(
            dataManager, dataManager, dataManager, 
            new DataPersistenceService(dataManager)
        );
    }
    
    // Create other managers...
}
```

### Update Main Application

**Before:**
```java
DataManager dataManager = new DataManager();
StudentManager studentManager = new StudentManager(dataManager);
StudentMenu menu = new StudentMenu(..., studentManager, ...);
```

**After:**
```java
ManagerFactory factory = new ManagerFactory();
StudentInternshipDiscoveryManager discoveryMgr = factory.createDiscoveryManager();
StudentApplicationManager applicationMgr = factory.createApplicationManager();
StudentMenu menu = new StudentMenu(..., discoveryMgr, applicationMgr, ...);
```

---

## Phase 9: Testing & Validation (Week 8-9)
**Goal:** Verify everything works

### Unit Tests

```java
public class StudentApplicationManagerTest {
    
    @Test
    public void testApplyForInternship_Success() {
        StudentRepository mockStudentRepo = mock(StudentRepository.class);
        InternshipRepository mockInternshipRepo = mock(InternshipRepository.class);
        ApplicationRepository mockAppRepo = mock(ApplicationRepository.class);
        DataPersistenceService mockPersist = mock(DataPersistenceService.class);
        
        Student student = new Student("S001", "John", "pass", 2, "CS");
        Internship internship = new Internship("I001", "Title", "Desc", "Basic",
                                               "CS", LocalDate.now().plusDays(1),
                                               LocalDate.now().plusDays(10), "Company", "R001", 5);
        
        when(mockStudentRepo.getStudent("S001")).thenReturn(student);
        when(mockInternshipRepo.getInternship("I001")).thenReturn(internship);
        
        StudentApplicationManager manager = new StudentApplicationManager(
            mockStudentRepo, mockInternshipRepo, mockAppRepo, mockPersist
        );
        
        boolean result = manager.applyForInternship(student, "I001");
        
        assertTrue(result);
        verify(mockAppRepo).addApplication(any(InternshipApplication.class));
        verify(mockPersist).persistAllData();
    }
}
```

### Integration Tests
- Test entire flow: login → view internships → apply → accept placement
- Test with real file system
- Verify data persistence

### Regression Tests
- Run all existing tests with new code
- Verify no functionality broken
- Compare output with original implementation

---

## Phase 10: Cleanup & Documentation (Week 9-10)
**Goal:** Remove deprecated code

### Cleanup Tasks
1. Remove or deprecate old `StudentManager`
2. Remove `FilterManager` if no longer needed
3. Remove duplicate code
4. Clean up imports

### Documentation
1. Update class documentation
2. Update README with new architecture
3. Document new interfaces and services

---

## Rollback Plan

If something breaks at any phase:

1. **Revert to last working commit**
2. **Identify the issue:**
   - Does a test fail?
   - Does integration test fail?
   - Is it backward compatibility?
3. **Fix the issue:**
   - Update the implementation
   - Add intermediate compatibility layer if needed
   - Test again
4. **Continue to next phase**

---

## Success Criteria

✅ **Phase Complete When:**
- All new code compiles without errors
- All tests pass (old and new)
- No existing functionality broken
- Code follows SOLID principles
- Dependency injection working

✅ **Full Migration Complete When:**
- All views use new services
- All old managers refactored or removed
- All hard-coded values replaced with enums
- 100% backward compatibility maintained
- Comprehensive unit test coverage
- All integration tests passing

---

## Estimated Timeline

| Phase | Duration | Complexity | Risk |
|---|---|---|---|
| 1: Foundation | 1-2 weeks | Low | None - additive |
| 2: Extract Services | 1-2 weeks | Low | None - parallel |
| 3: Repositories | 1-2 weeks | Medium | Testing needed |
| 4: Migrate Views | 1-2 weeks | Medium | Breaking changes |
| 5: Migrate Logic | 1-2 weeks | High | Core changes |
| 6: Update Entities | 3-5 days | Medium | Data model change |
| 7: Update DataManager | 3-5 days | Low | Additions only |
| 8: Dependency Injection | 3-5 days | Medium | Wiring |
| 9: Testing | 1-2 weeks | High | Validation |
| 10: Cleanup | 3-5 days | Low | Maintenance |

**Total: 8-12 weeks for full migration**

---

## Conclusion

By following this roadmap:
- ✅ Code becomes more maintainable
- ✅ Testing becomes easier
- ✅ Dependencies become clearer
- ✅ SOLID principles are followed
- ✅ Future changes become safer

Each phase builds on the previous, allowing for gradual improvement without breaking existing functionality.
