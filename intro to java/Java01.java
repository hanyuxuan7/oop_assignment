import java.util.Scanner;

public class Java01 {
    public static void main(String[] args) { //main method
        double amtUsDollars, amtSingDollars, exchRate;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the amount of US dollars:");
        amtUsDollars = sc.nextDouble();
        System.out.println("Enter the exchange rate:");
        exchRate = sc.nextDouble();
        sc.close();
        amtSingDollars = amtUsDollars * exchRate;
        System.out.println(amtUsDollars + " USD = " + amtSingDollars + "SGD");
    }
}
