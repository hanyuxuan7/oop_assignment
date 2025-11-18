package control;

import entity.Internship;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class InternshipFilterStrategy {
    
    @FunctionalInterface
    public interface InternshipPredicate {
        boolean test(Internship internship);
    }

    private List<Internship> internships;

    public InternshipFilterStrategy(List<Internship> internships) {
        this.internships = internships;
    }

    public List<Internship> filterByStatus(String status) {
        return filter(i -> i.getStatus().equals(status));
    }

    public List<Internship> filterByMajor(String major) {
        return filter(i -> i.getPreferredMajor().equals(major));
    }

    public List<Internship> filterByLevel(String level) {
        return filter(i -> i.getLevel().equals(level));
    }

    public List<Internship> filterByClosingDate(LocalDate before) {
        return filter(i -> !i.getClosingDate().isAfter(before));
    }

    public List<Internship> filterByVisibility(boolean visible) {
        return filter(i -> i.isVisible() == visible);
    }

    public List<Internship> filterByAvailability(LocalDate currentDate) {
        return filter(i -> i.canApply(currentDate));
    }

    public List<Internship> filter(InternshipPredicate predicate) {
        return internships.stream()
                .filter(predicate::test)
                .collect(Collectors.toList());
    }

    public List<Internship> filterMultiple(InternshipPredicate... predicates) {
        return internships.stream()
                .filter(i -> {
                    for (InternshipPredicate predicate : predicates) {
                        if (!predicate.test(i)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
