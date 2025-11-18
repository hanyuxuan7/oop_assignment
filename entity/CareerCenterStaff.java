package entity;

public class CareerCenterStaff extends User {
    private String department;
    private String email;

    public CareerCenterStaff(String userID, String name, String password, String department, String email) {
        super(userID, name, password);
        this.department = department;
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "CareerCenterStaff{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
