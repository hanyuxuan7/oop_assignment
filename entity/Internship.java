package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Internship {
    private String internshipID;
    private String title;
    private String description;
    private String level;
    private String preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private String status;
    private String companyName;
    private String repInCharge;
    private int numSlots;
    private int filledSlots;
    private boolean visible;
    private List<InternshipApplication> applications;

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

    public String getInternshipID() {
        return internshipID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public void setNumSlots(int numSlots) {
        this.numSlots = numSlots;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRepInCharge() {
        return repInCharge;
    }

    public int getNumSlots() {
        return numSlots;
    }

    public int getFilledSlots() {
        return filledSlots;
    }

    public void setFilledSlots(int filledSlots) {
        this.filledSlots = filledSlots;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<InternshipApplication> getApplications() {
        return applications;
    }

    public void addApplication(InternshipApplication application) {
        applications.add(application);
    }

    public boolean isFull() {
        return filledSlots >= numSlots;
    }

    public boolean canApply(LocalDate currentDate) {
        return visible && status.equals("Approved") && 
               !currentDate.isAfter(closingDate) && !isFull();
    }

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
