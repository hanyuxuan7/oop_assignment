package entity;

import java.util.ArrayList;
import java.util.List;

public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private boolean approved;
    private List<Internship> createdInternships;

    public CompanyRepresentative(String userID, String name, String password, 
                                 String companyName, String department, String position) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.approved = false;
        this.createdInternships = new ArrayList<>();
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public List<Internship> getCreatedInternships() {
        return createdInternships;
    }

    public void addInternship(Internship internship) {
        if (createdInternships.size() < 5) {
            createdInternships.add(internship);
        }
    }

    @Override
    public String toString() {
        return "CompanyRepresentative{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", companyName='" + companyName + '\'' +
                ", approved=" + approved +
                '}';
    }
}
