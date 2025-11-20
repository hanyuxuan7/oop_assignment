package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an internship position offered by a company.
 * Contains details about the internship including position requirements, dates, status,
 * and manages applications from students. Each internship has a limited number of slots
 * and can be opened or closed based on its status.
 *
 * @author NTU Career Services Development Team
 * @version 1.0
 */
public class Internship {
    /** Unique identifier for the internship */
    private String internshipID;
    /** Title/name of the internship position */
    private String title;
    /** Detailed description of the internship role and responsibilities */
    private String description;
    /** Difficulty level of the internship (Basic, Intermediate, Advanced) */
    private String level;
    /** Preferred major/specialization for candidates */
    private String preferredMajor;
    /** Date when the internship application period opens */
    private LocalDate openingDate;
    /** Date when the internship application period closes */
    private LocalDate closingDate;
    /** Current status of the internship (Pending, Approved, Rejected, Filled) */
    private String status;
    /** Name of the company offering the internship */
    private String companyName;
    /** ID of the company representative responsible for this internship */
    private String repInCharge;
    /** Total number of available slots for this internship */
    private int numSlots;
    /** Number of slots that have been filled */
    private int filledSlots;
    /** Whether the internship is visible to students for applications */
    private boolean visible;
    /** List of applications received for this internship */
    private List<InternshipApplication> applications;

    /**
     * Constructs a new Internship with the specified details.
     *
     * @param internshipID the unique identifier for the internship
     * @param title the title of the internship position
     * @param description the description of the internship
     * @param level the difficulty level (Basic, Intermediate, Advanced)
     * @param preferredMajor the preferred major for candidates
     * @param openingDate the date when applications open
     * @param closingDate the date when applications close
     * @param companyName the name of the company
     * @param repInCharge the ID of the company representative
     * @param numSlots the number of available slots
     */
    public Internship(String internshipID, String title, String description, String level,
                      String preferredMajor, LocalDate openingDate, LocalDate closingDate,
                      String companyName, String repInCharge, int numSlots) {
        this.internshipID = internshipID;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = "Pending";
        this.companyName = companyName;
        this.repInCharge = repInCharge;
        this.numSlots = numSlots;
        this.filledSlots = 0;
        this.visible = false;
        this.applications = new ArrayList<>();
    }

    /**
     * Returns the unique identifier of this internship.
     *
     * @return the internship ID
     */
    public String getInternshipID() {
        return internshipID;
    }

    /**
     * Returns the title of this internship position.
     *
     * @return the internship title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the title of this internship.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of this internship.
     *
     * @return the internship description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of this internship.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the difficulty level of this internship.
     *
     * @return the level (Basic, Intermediate, or Advanced)
     */
    public String getLevel() {
        return level;
    }

    /**
     * Updates the difficulty level of this internship.
     *
     * @param level the new level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * Returns the preferred major for candidates of this internship.
     *
     * @return the preferred major
     */
    public String getPreferredMajor() {
        return preferredMajor;
    }

    /**
     * Updates the preferred major for this internship.
     *
     * @param preferredMajor the new preferred major
     */
    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    /**
     * Returns the date when applications for this internship open.
     *
     * @return the opening date
     */
    public LocalDate getOpeningDate() {
        return openingDate;
    }

    /**
     * Updates the opening date for applications.
     *
     * @param openingDate the new opening date
     */
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    /**
     * Returns the date when applications for this internship close.
     *
     * @return the closing date
     */
    public LocalDate getClosingDate() {
        return closingDate;
    }

    /**
     * Updates the closing date for applications.
     *
     * @param closingDate the new closing date
     */
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * Updates the total number of available slots for this internship.
     *
     * @param numSlots the new number of slots
     */
    public void setNumSlots(int numSlots) {
        this.numSlots = numSlots;
    }

    /**
     * Returns the current status of this internship.
     *
     * @return the status (Pending, Approved, Rejected, or Filled)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates the status of this internship.
     *
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the name of the company offering this internship.
     *
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Returns the ID of the company representative in charge of this internship.
     *
     * @return the representative ID
     */
    public String getRepInCharge() {
        return repInCharge;
    }

    /**
     * Returns the total number of available slots for this internship.
     *
     * @return the number of slots
     */
    public int getNumSlots() {
        return numSlots;
    }

    /**
     * Returns the number of slots that have been filled.
     *
     * @return the number of filled slots
     */
    public int getFilledSlots() {
        return filledSlots;
    }

    /**
     * Updates the number of filled slots.
     *
     * @param filledSlots the new number of filled slots
     */
    public void setFilledSlots(int filledSlots) {
        this.filledSlots = filledSlots;
    }

    /**
     * Checks if this internship is visible to students.
     *
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of this internship to students.
     *
     * @param visible true to make visible, false to hide
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns the list of applications received for this internship.
     *
     * @return a list of InternshipApplication objects
     */
    public List<InternshipApplication> getApplications() {
        return applications;
    }

    /**
     * Adds an application to this internship.
     *
     * @param application the application to add
     */
    public void addApplication(InternshipApplication application) {
        applications.add(application);
    }

    /**
     * Checks if all available slots for this internship have been filled.
     *
     * @return true if the internship is full, false otherwise
     */
    public boolean isFull() {
        return filledSlots >= numSlots;
    }

    /**
     * Checks if a student can apply for this internship on the given date.
     * The internship must be visible, approved, still accepting applications,
     * and have available slots.
     *
     * @param currentDate the date to check applicability for
     * @return true if the internship is open for applications, false otherwise
     */
    public boolean canApply(LocalDate currentDate) {
        return visible && status.equals("Approved") && 
               !currentDate.isAfter(closingDate) && !isFull();
    }

    /**
     * Returns a string representation of the internship with key information.
     *
     * @return a formatted string containing internship ID, title, level, company, and status
     */
    @Override
    public String toString() {
        return "Internship{" +
                "internshipID='" + internshipID + '\'' +
                ", title='" + title + '\'' +
                ", level='" + level + '\'' +
                ", companyName='" + companyName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
