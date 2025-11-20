package tutorials.tut1;
import java.util.Scanner;

public class CircleApp {
    public static void main(String[] args) {
        
    System.out.println("== Circle Computation ==");
    System.out.println("|1. Create a new circle |");
    System.out.println("|2. Print Area          |");
    System.out.println("|3. Print circumference |");
    System.out.println("|4. Quit                |");
    System.out.println("========================");
    

    Scanner sc = new Scanner(System.in);
    Circle c = null;

    do {
        System.out.println("Choose option (1-4):");
        int option = sc.nextInt();
        switch (option) {
            case 1:
                System.out.println("Enter the radius to compute the area and circumference:");
                double rad = sc.nextDouble();
                c = new Circle(rad);
                break;
            case 2:
                c.printArea();
                break;
            case 3:
                c.printCircumference();
                break;
            case 4:
                System.err.println("Thank you!");
                sc.close();
                return;

        };
    }
    while(true);
}
}





