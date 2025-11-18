package control;

import entity.*;
import data.DataManager;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FilterManager {
    private DataManager dataManager;

    public FilterManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

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

    public List<Internship> searchInternships(String keyword) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        internship.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        internship.getCompanyName().toLowerCase().contains(keyword.toLowerCase()))
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }

    public List<Internship> getInternshipsByStatus(String status) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getStatus().equals(status))
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }

    public List<Internship> getInternshipsByMajor(String major) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getPreferredMajor().equals(major))
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }

    public List<Internship> getInternshipsByLevel(String level) {
        return dataManager.getAllInternships().stream()
                .filter(internship -> internship.getLevel().equals(level))
                .sorted(Comparator.comparing(Internship::getTitle))
                .collect(Collectors.toList());
    }

    public List<Internship> sortInternshipsByClosingDate(List<Internship> internships) {
        return internships.stream()
                .sorted(Comparator.comparing(Internship::getClosingDate))
                .collect(Collectors.toList());
    }
}
