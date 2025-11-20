package control;

import entity.*;
import data.InternshipRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager for internship discovery operations for students using repository pattern.
 * Handles retrieving available internships based on student eligibility.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public class StudentInternshipDiscoveryManager {
    private final InternshipRepository internshipRepository;

    public StudentInternshipDiscoveryManager(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    public List<Internship> getAvailableInternships(Student student) {
        return getAvailableInternships(student, true);
    }

    public List<Internship> getAvailableInternships(Student student, boolean openOnly) {
        List<Internship> availableInternships = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        String studentMajor = MajorUtils.getMajorName(Integer.parseInt(student.getMajor()));
        if (studentMajor == null) {
            studentMajor = student.getMajor();
        }

        for (Internship internship : internshipRepository.getAllInternships()) {
            if (internship.isVisible() && 
                internship.getStatus().equals(InternshipStatus.APPROVED.getDisplayName()) &&
                internship.getPreferredMajor().equals(studentMajor) &&
                student.canApplyForLevel(internship.getLevel()) &&
                !internship.isFull()) {
                
                if (openOnly) {
                    if (today.isAfter(internship.getOpeningDate()) && today.isBefore(internship.getClosingDate())) {
                        availableInternships.add(internship);
                    }
                } else {
                    availableInternships.add(internship);
                }
            }
        }

        return availableInternships;
    }

    public Internship getInternshipDetails(String internshipID) {
        return internshipRepository.getInternship(internshipID);
    }
}
