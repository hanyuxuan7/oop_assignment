package control;

import entity.*;
import data.DataManager;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager for filtering and sorting internship data based on various criteria.
 * Provides methods to search, filter, and sort internships by status, major, level, and other attributes.
 *
 * @version 1.0
 */
public class FilterManager {
    private DataManager dataManager;

    /**
     * Constructs a FilterManager with the specified DataManager.
     *
     * @param dataManager the DataManager instance for accessing internship data
     */
    public FilterManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Filters internships based on multiple criteria.
     * Passing null for any parameter skips filtering on that attribute.
     *
     * @param status the internship status to filter by (or null to skip)
     * @param preferredMajor the preferred major to filter by (or null to skip)
     * @param level the internship level to filter by (or null to skip)
     * @param closingDateBefore filter internships closing on or before this date (or null to skip)
     * @param sortAlphabetically if true, sorts results alphabetically by title
     * @return a list of internships matching the specified criteria
     */
    public List<Internship> filterInternships(String status, String preferredMajor, 
                                               String level, LocalDate closingDateBefore,
                                               boolean sortAlphabetically) {
        List<Internship> filtered = dataManager.getAllInternships().stream()
                .filter(internship -> status == null || internship.getStatus().equals(status))
                .filter(internship -> preferredMajor == null || internship.getPreferredMajor().equals(preferredMajor))
                .filter(internship -> level == null || internship.getLevel().equals(level))
                .filter(internship -> closingDateBefore == null || !internship.getClosingDate().isAfter(closingDateBefore))
                .collect(Collectors.toList());

        if (sortAlphabetically) {
            filtered.sort(Comparator.comparing(Internship::getTitle));
        }

        return filtered;
    }

    /**
     * Searches for internships by keyword in title, description, or company name.
     * Results are sorted alphabetically by title.
     *
     * @param keyword the search keyword (case-insensitive)
     * @return a list of internships matching the keyword
     */
    public List<Internship> searchInternships(String keyword) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        internship.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        internship.getCompanyName().toLowerCase().contains(keyword.toLowerCase()))
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all internships with the specified status.
     * Results are sorted alphabetically by title.
     *
     * @param status the internship status to filter by
     * @return a list of internships with the specified status
     */
    public List<Internship> getInternshipsByStatus(String status) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getStatus().equals(status))
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all internships for the specified preferred major.
     * Results are sorted alphabetically by title.
     *
     * @param major the preferred major to filter by
     * @return a list of internships for the specified major
     */
    public List<Internship> getInternshipsByMajor(String major) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getPreferredMajor().equals(major))
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all internships with the specified difficulty level.
     * Results are sorted alphabetically by title.
     *
     * @param level the internship level to filter by (Basic, Intermediate, or Advanced)
     * @return a list of internships with the specified level
     */
    public List<Internship> getInternshipsByLevel(String level) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getLevel().equals(level))
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }

    /**
     * Sorts the specified list of internships by their closing date in ascending order.
     *
     * @param internships the list of internships to sort
     * @return a new sorted list of internships
     */
    public List<Internship> sortInternshipsByClosingDate(List<Internship> internships) {
        return internships.stream()
                .sorted(Comparator.comparing(Internship::getClosingDate))
                .collect(Collectors.toList());
    }
}
