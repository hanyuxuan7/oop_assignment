import java.util.Scanner;

public class P2 {
    public static void main(String[] args) {
        double salary, merit;
        String grade;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your salary: $");
        salary = sc.nextDouble();
        System.out.print("Enter your merit: ");
        merit = sc.nextDouble();
        sc.close();
        if (500 <= salary && salary <= 599) // always C
            grade = "Grade C";
        else if (600 <= salary && salary <= 649) // B or C
            if (merit >= 10)
                grade = "Grade B";
            else
            grade = "Grade C";
        else if (650 <= salary && salary <= 699) // always B
            grade = "Grade B";
        else if (700 <= salary && salary <= 799) // A or B
            if (merit >= 20)
                grade = "Grade A";
            else
            grade = "Grade B";
        else if (800 <= salary && salary <= 899) // always A
            grade = "Grade A";
        else grade = "Salary out of range.";
        System.out.println(grade);
    }
}
