package tutorials.tut2;
import java.util.Scanner;

public class VendingMachine {
    Scanner sc = new Scanner(System.in);
    private int drinkChoice;
    private String inputAmount;
    double amount = 0;
    public VendingMachine() {}
    public double selectDrink() {
        System.out.println("===== Vending Machine =====");
        System.out.println("|1. Buy Beer      ($3.00) |");
        System.out.println("|2. Buy Coke      ($1.00) |");
        System.out.println("|3. Buy Green Tea ($5.00) |");
        System.out.println("Please enter selection:");
        drinkChoice = sc.nextInt();
        switch (drinkChoice) {
            case 1:
                return 3.00;
            case 2:
                return 1.00;
            case 3:
                return 5.00;
            default:
                System.out.println("Invalid choice");
                return 0.00;
        
    }}
    public double insertCoin(double drinkCost) {
        System.out.println("Please insert coins:");
        System.out.println("======== Coins Input ========");
        System.out.println("|Enter Q for 10 cents input. |");
        System.out.println("|Enter T for 20 cents input. |");
        System.out.println("|Enter F for 50 cents input. |");
        System.out.println("|Enter N for 1 dollar input. |");
        System.out.println("=============================");
        inputAmount = sc.next();
        switch (inputAmount) {
            case "Q":
                amount += 0.10;
                break;
            case "T":
                amount += 0.20;
                break;
            case "F":
                amount += 0.50;
                break;
            case "N":
                amount += 1.00;
                break;
            default:
                System.out.println("Invalid choice.");

        }
        return amount;

    }
    public void checkChange(double amount, double drinkCost) {
        System.out.printf("Change: $%.2f%n", (amount - drinkCost));
    }
    public void printReceipt() {

        System.out.println("Please collect your drink.");
        System.out.println("Thank you!!");
    }
}