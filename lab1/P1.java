import java.util.Scanner;

public class P1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String choice;
        System.out.print("Enter your choice A, C or D: ");
        choice = sc.next();
        sc.close();
        switch (choice) {
            case "a":
            case "A":
                System.out.println("Action movie fan\n");
                break;
            case "c":
            case "C":
                System.out.println("Comedy movie fan\n");
                break;
            case "d":
            case "D":
                System.out.println("Drama movie fan\n");
                break;
            default: // anything other than a, A, c, C, d, D
                System.out.println("Invalid choice\n");
                break;
        }
        }
    }

