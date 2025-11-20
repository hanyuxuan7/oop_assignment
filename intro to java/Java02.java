import java.util.Scanner;

public class Java02 {
    public static void main(String[] args) {
        int a,b,area; //initialise variables
        Scanner sc = new Scanner(System.in);
        System.out.println("Input the first length of the rectangle:");
        a = sc.nextInt();
        System.out.println("Input the second length of the rectangle:");
        b = sc.nextInt();
        sc.close(); //finish reading user input
        area = a*b;
        System.out.println("Area of rectangle is " + area);
    } 
}
