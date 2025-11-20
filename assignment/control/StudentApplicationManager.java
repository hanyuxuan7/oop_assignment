package control;

import entity.*;
import data.InternshipRepository;
import data.ApplicationRepository;
import data.StudentRepository;
import java.time.LocalDate;

/**
 * Manager for student application operations using repository pattern.
 * Handles application submission, withdrawal, and acceptance.
 *
 * @version 1.0
 */
public class StudentApplicationManager {
    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final DataPersistenceService persistenceService;

    public StudentApplicationManager(StudentRepository studentRepository,
                                    InternshipRepository internshipRepository,
                                    ApplicationRepository applicationRepository,
                                    DataPersistenceService persistenceService) {
        this.studentRepository = studentRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.persistenceService = persistenceService;
    }

    public boolean applyForInternship(Student student, String internshipID) {
        if (student.getApplications().size() >= 3) {
            return false;
        }

        Internship internship = internshipRepository.getInternship(internshipID);
        if (internship == null) {
            return false;
        }

        if (!internship.canApply(LocalDate.now())) {
            return false;
        }

        if (!student.canApplyForLevel(internship.getLevel())) {
            return false;
        }

        String applicationID = "APP" + System.currentTimeMillis();
        InternshipApplication application = new InternshipApplication(applicationID, student.getUserID(), internshipID);
        application.setStatus(ApplicationStatus.PENDING.getDisplayName());
        
        student.addApplication(application);
        internship.addApplication(application);
        applicationRepository.addApplication(application);
        persistenceService.persistAllData();

        return true;
    }

    public boolean withdrawApplication(Student student, String applicationID) {
        for (InternshipApplication app : student.getApplications()) {
            if (app.getApplicationID().equals(applicationID)) {
                if (app.isConfirmed()) {
                    app.requestWithdrawal("Withdrawal after confirmation");
                } else {
                    app.requestWithdrawal("Withdrawal request");
                }
                persistenceService.persistAllData();
                return true;
            }
        }
        return false;
    }

    public boolean acceptPlacement(Student student, String applicationID) {
        if (student.getAcceptedInternshipID() != null) {
            return false;
        }

        for (InternshipApplication app : student.getApplications()) {
            if (app.getApplicationID().equals(applicationID) && 
                app.getStatus().equals(ApplicationStatus.SUCCESSFUL.getDisplayName())) {
                
                student.setAcceptedInternshipID(app.getInternshipID());
                app.setConfirmed(true);

                Internship internship = internshipRepository.getInternship(app.getInternshipID());
                if (internship != null) {
                    internship.setFilledSlots(internship.getFilledSlots() + 1);
                    if (internship.isFull()) {
                        internship.setStatus(InternshipStatus.FILLED.getDisplayName());
                    }
                }

                for (InternshipApplication otherApp : student.getApplications()) {
                    if (!otherApp.getApplicationID().equals(applicationID) && 
                        otherApp.getStatus().equals(ApplicationStatus.PENDING.getDisplayName())) {
                        otherApp.setStatus(ApplicationStatus.WITHDRAWN.getDisplayName());
                    }
                }

                persistenceService.persistAllData();
                return true;
            }
        }
        return false;
    }
}
