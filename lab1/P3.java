import java.util.Scanner;

public class P3 {
    public static void main(String[] args) {
        double rate = 1.82;
        int start, end, increment;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter starting value: ");
        start = sc.nextInt();
        System.out.print("Enter ending value: ");
        end = sc.nextInt();
        System.out.print("Enter increment: ");
        increment = sc.nextInt();
        sc.close();

        if (start > end || increment <= 0) {
            System.out.println("Invalid input.");
            return;
        }

        System.out.println("US$\tS$\n----------------"); // header
        for (int i = start; i <= end; i += increment) { // i = i + increment until i > end
            System.out.print(i + "\t" +  i*rate + "\n");
        }
        System.out.println("\n");
        System.out.println("US$\tS$\n----------------"); // header
        int a = start;
        while (a <= end) {
            System.out.print(a + "\t" + a * rate + "\n"); 
            a += increment; // a = a + increment until start > end
        }
        System.out.println("\n");
        System.out.println("US$\tS$\n----------------"); // header
        int b = start;
        do {
            System.out.print(b + "\t" + b * rate + "\n");
            b += increment; // b = b + increment until start > end
        } while (b <= end);
    }
}
