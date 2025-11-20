package tutorials.tut2;

public class VendingMachineApp {
    public static void main(String[] args) {
        VendingMachine vend = new VendingMachine();
        double cost = vend.selectDrink();
        double amount = 0.0;
        while (amount < cost) {
            amount = vend.insertCoin(cost);
            System.out.printf("Coins inserted: %.2f%n", amount);
        }
        vend.checkChange(amount, cost);
        vend.printReceipt();
    }
}
