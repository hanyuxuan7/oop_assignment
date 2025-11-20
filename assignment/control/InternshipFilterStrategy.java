package control;

import entity.Internship;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy class for filtering internships using predicate-based filtering.
 * Provides flexible filtering capabilities for internship data.
 *
 * @version 1.0
 */
public class InternshipFilterStrategy {
    
    /**
     * Functional interface for testing internship objects against a specific predicate condition.
     */
    @FunctionalInterface
    public interface InternshipPredicate {
        /**
         * Tests whether an internship satisfies this predicate.
         *
         * @param internship the internship to test
         * @return true if the internship satisfies the predicate, false otherwise
         */
        boolean test(Internship internship);
    }

    private List<Internship> internships;

    /**
     * Constructs an InternshipFilterStrategy with the specified list of internships.
     *
     * @param internships the list of internships to filter
     */
    public InternshipFilterStrategy(List<Internship> internships) {
        this.internships = internships;
    }

    /**
     * Filters internships by their status.
     *
     * @param status the status to filter by
     * @return a list of internships matching the specified status
     */
    public List<Internship> filterByStatus(String status) {
        return filter(i -> i.getStatus().equals(status));
    }

    /**
     * Filters internships by their preferred major.
     *
     * @param major the major to filter by
     * @return a list of internships matching the specified major
     */
    public List<Internship> filterByMajor(String major) {
        return filter(i -> i.getPreferredMajor().equals(major));
    }

    /**
     * Filters internships by their level.
     *
     * @param level the level to filter by
     * @return a list of internships matching the specified level
     */
    public List<Internship> filterByLevel(String level) {
        return filter(i -> i.getLevel().equals(level));
    }

    /**
     * Filters internships by their closing date.
     *
     * @param before the closing date threshold
     * @return a list of internships closing on or before the specified date
     */
    public List<Internship> filterByClosingDate(LocalDate before) {
        return filter(i -> !i.getClosingDate().isAfter(before));
    }

    /**
     * Filters internships by their visibility status.
     *
     * @param visible true to get visible internships, false for hidden ones
     * @return a list of internships matching the specified visibility
     */
    public List<Internship> filterByVisibility(boolean visible) {
        return filter(i -> i.isVisible() == visible);
    }

    /**
     * Filters internships by whether they are currently available for applications.
     *
     * @param currentDate the current date to check availability against
     * @return a list of internships available for application on the specified date
     */
    public List<Internship> filterByAvailability(LocalDate currentDate) {
        return filter(i -> i.canApply(currentDate));
    }

    /**
     * Filters internships using a custom predicate.
     *
     * @param predicate the predicate to filter by
     * @return a list of internships matching the predicate
     */
    public List<Internship> filter(InternshipPredicate predicate) {
        return internships.stream()
                .filter(predicate::test)
                .collect(Collectors.toList());
    }

    /**
     * Filters internships against multiple predicates (all must be satisfied).
     *
     * @param predicates variable number of predicates that must all be satisfied
     * @return a list of internships matching all specified predicates
     */
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
