public class SalePerson implements Comparable<SalePerson> {
    String firstName;
    String lastName;
    int totalSales;
    public SalePerson (String firstName, String lastName, int totalSales) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalSales = totalSales;
    }

    public String toString() {
        return(lastName + ", " + firstName + " : " + totalSales);
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public boolean equals(SalePerson o) {
        return this.firstName.equals(o.firstName) && this.lastName.equals(o.lastName);
    }

    public int compareTo(SalePerson o) {
        if (o.totalSales > this.totalSales) return -1;
        if (o.totalSales < this.totalSales) return 1;
        return o.lastName.compareTo(this.lastName);
    }
}