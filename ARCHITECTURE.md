# Internship Management System - Architecture Documentation

## System Overview

This is a multi-layered Java application for managing internship opportunities, applications, and approvals. It includes both CLI and GUI interfaces built with Swing.

## Package Structure

```
assignment/
├── app/
│   ├── InternshipApp.java       (CLI Entry Point)
│   └── GuiApp.java              (GUI Entry Point)
├── entity/
│   ├── User.java                (Abstract base class)
│   ├── Student.java             (Extends User)
│   ├── CareerCenterStaff.java    (Extends User)
│   ├── CompanyRepresentative.java (Extends User)
│   ├── Internship.java
│   ├── InternshipApplication.java
│   └── InternshipLevel.java      (Enum)
├── control/
│   ├── AuthenticationManager.java
│   ├── StudentManager.java
│   ├── CareerCenterStaffManager.java
│   ├── CompanyRepresentativeManager.java
│   └── FilterManager.java
├── data/
│   └── DataManager.java          (Persistence Layer)
└── view/
    ├── LoginFrame.java           (GUI - Login Screen)
    ├── StudentDashboard.java      (GUI - Student Interface)
    ├── StaffDashboard.java        (GUI - Staff Interface)
    ├── CompanyRepDashboard.java   (GUI - Company Rep Interface)
    ├── MainMenu.java              (CLI - Main Menu)
    ├── StudentMenu.java           (CLI - Student Menu)
    ├── StaffMenu.java             (CLI - Staff Menu)
    └── CompanyRepMenu.java        (CLI - Company Rep Menu)
└── data/
    ├── students.txt
    ├── staff.txt
    ├── companyreps.txt
    ├── internships.txt
    └── applications.txt
```

## Layered Architecture

### 1. **Entity Layer** (Domain Model)
Located in: `entity/`

Represents core business objects:
- **User (Abstract)**: Base class for all users with authentication
  - **Student**: Year of study, major, applications list
  - **CareerCenterStaff**: Department affiliation
  - **CompanyRepresentative**: Company details, approval status, internships created
- **Internship**: Job posting with slots, dates, requirements
- **InternshipApplication**: Link between students and internships
- **InternshipLevel**: Enum for difficulty levels (Basic, Intermediate, Advanced)

**Key Features:**
- Immutable IDs
- Status tracking
- Validation logic (e.g., `canApply()`, `isFull()`)

### 2. **Business Logic Layer** (Controllers)
Located in: `control/`

Manages operations and workflows:
- **AuthenticationManager**: User login, password management
- **StudentManager**: Available internships, applications, placements
- **CareerCenterStaffManager**: Approvals for companies and internships, withdrawal management
- **CompanyRepresentativeManager**: Create internships, manage applications
- **FilterManager**: Search and filter operations

**Key Features:**
- Validation of business rules
- Multi-step workflows
- Access control

### 3. **Data Access Layer**
Located in: `data/`

**DataManager** handles:
- Loading data from text files
- Storing objects in memory (Maps)
- Saving data back to files
- Centralized data access for all layers

**Supported Operations:**
- CRUD for all entities
- Bulk loading/saving
- Query operations

### 4. **View Layer**
Located in: `view/`

#### GUI Components (Swing)
- **LoginFrame**: Authentication and company registration
- **StudentDashboard**: Student operations (browse, apply, accept placements)
- **StaffDashboard**: Staff management (approve registrations/internships, handle withdrawals)
- **CompanyRepDashboard**: Company operations (create/edit internships, review applications)

#### CLI Components (Legacy)
- **MainMenu**: Entry point for CLI
- **StudentMenu**: Terminal-based student interface
- **StaffMenu**: Terminal-based staff interface
- **CompanyRepMenu**: Terminal-based company interface

## Data Flow

### User Registration & Login Flow
```
LoginFrame (GUI)
    ↓
AuthenticationManager.login()
    ↓
DataManager.getUser()
    ↓
Redirect to appropriate Dashboard
```

### Internship Application Flow
```
StudentDashboard.getAvailableInternships()
    ↓
StudentManager.getAvailableInternships()
    ↓
StudentManager.applyForInternship()
    ↓
InternshipApplication created
    ↓
DataManager.addApplication()
    ↓
File persistence
```

### Approval Workflow
```
CompanyRepDashboard.approveApplication()
    ↓
CompanyRepresentativeManager.approveApplication()
    ↓
InternshipApplication status → "Approved"
    ↓
DataManager.saveAllData()
```

## Key Design Patterns

### 1. **MVC Pattern**
- **Model**: Entity classes
- **View**: Dashboard and Menu classes
- **Controller**: Manager classes

### 2. **Layered Architecture**
- Clear separation of concerns
- Entity → Business Logic → Data → View
- Easy to test each layer independently

### 3. **Data Access Object (DAO)**
- DataManager acts as DAO
- Abstracts data storage details
- Single point of data access

### 4. **Facade Pattern**
- Manager classes provide simplified interface to complex entity operations
- Hides business logic complexity from view layer

### 5. **Strategy Pattern**
- Different managers for different user types
- Each implements specialized operations

## User Roles & Permissions

### 1. **Student**
**Permissions:**
- Browse approved internships (matching major & level)
- Apply for up to 3 internships
- View own applications
- Accept a placement
- Request withdrawal from accepted placement
- Change password

**Constraints:**
- Year 1-2: Only "Basic" level internships
- Year 3+: All levels
- Cannot apply if already accepted a placement
- Application limit: 3 active applications

### 2. **Career Center Staff**
**Permissions:**
- Approve/reject company registrations
- Approve/reject internship postings
- Approve/reject withdrawal requests
- Generate reports with filters (status, major, level)
- Change password

**No permissions:**
- Create internships
- Modify student/company data

### 3. **Company Representative**
**Permissions:**
- Create internship postings (max 5)
- Edit own postings
- View applications for own internships
- Approve/reject student applications
- Toggle internship visibility
- Change password

**Constraints:**
- Must be approved before posting internships
- Cannot apply for internships as student

### 4. **Admin/Staff**
All system permissions (implicit - can moderate all approvals)

## Data Persistence

### File Format

**students.txt**
```
email|name|password|yearOfStudy|major
```

**staff.txt**
```
email|name|password|department
```

**companyreps.txt**
```
email|name|password|companyName|department|position|approved
```

**internships.txt**
```
id|title|description|level|preferredMajor|openingDate|closingDate|status|companyName|repInCharge|numSlots|filledSlots|visible
```

**applications.txt**
```
id|studentID|internshipID|status|withdrawalRequested|withdrawalReason|confirmed
```

## Running the Application

### CLI Version
```bash
cd assignment
javac -d . app/*.java view/*.java entity/*.java control/*.java data/*.java
java app.InternshipApp
```

### GUI Version
```bash
cd assignment
javac -d . app/*.java view/*.java entity/*.java control/*.java data/*.java
java app.GuiApp
```

## Class Dependencies Graph

```
View Layer → Control Layer → Data Layer
                ↓              ↓
              Entity Layer ← Entity Layer
```

### Detailed Dependencies

**View Classes:**
- All GUI dashboards → AuthenticationManager, ManagerTypes (Student/Staff/CompanyRep), FilterManager
- All Menu classes → DataManager, AuthenticationManager, ManagerTypes

**Manager Classes:**
- All Managers → DataManager, Entity classes

**DataManager:**
- File I/O, Entity classes for parsing/serialization

## Error Handling

### Login Failures
- Invalid user ID → "INVALID_USER"
- Wrong password → "INVALID_PASSWORD"
- Unapproved company → "NOT_APPROVED"

### Application Operations
- Application limit exceeded → Operation rejected
- Internship full → Cannot apply
- Level mismatch → Cannot apply
- Withdrawal already requested → Handled gracefully

### Data Persistence
- File not found → Default empty collections
- Parsing errors → Logged, data skipped
- Write failures → Logged to console

## Security Considerations

### Current Implementation
- Passwords stored as plain text (development only)
- No encryption
- File-based storage without access control

### Recommendations for Production
- Hash passwords with bcrypt/Argon2
- Implement role-based access control (RBAC)
- Use database with SQL injection prevention
- Add audit logging
- Implement session management
- Use HTTPS/SSL for data transmission

## Testing Recommendations

### Unit Testing
- Test manager business logic with mock DataManager
- Validate entity state transitions
- Test password validation

### Integration Testing
- Test full workflows (registration → application → approval)
- Verify data persistence
- Test authorization checks

### System Testing
- End-to-end test scenarios for each user role
- Multi-user concurrent operations
- Data integrity after crashes

## Future Enhancements

1. **Database Backend**: Replace file-based storage
2. **Email Notifications**: Notify on status changes
3. **Advanced Reporting**: Dashboard with analytics
4. **Mobile App**: React Native or Flutter
5. **Authentication**: OAuth2/Single Sign-On
6. **Full-text Search**: Search internships by description
7. **Interview Scheduling**: Built-in scheduling system
8. **Feedback System**: Student/Company ratings
