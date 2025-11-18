package entity;

public class CareerCenterStaff extends User {
    private String department;

    public CareerCenterStaff(String userID, String name, String password, String department) {
        super(userID, name, password);
        this.department = department;
    }

    public String getDepartment() {
        return department;
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
