package tutorials.tut1;
import java.util.Scanner;

public class DiceApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press any key to roll the dice");
        String input = sc.nextLine();
        if (input != null) {
            Dice d1 = new Dice();
            System.out.println("Current value is " + d1.getDiceValue());
            System.out.println("Press any key to roll the dice");
            String input2 = sc.nextLine();
            if (input2 != null) {
                Dice d2 = new Dice();
                d2.setDiceValue();
                System.out.println("Current value is " + d2.getDiceValue());
                System.out.println("Your total value is " + (d1.getDiceValue() + d2.getDiceValue()));
        }        
        sc.close();
    }
}
}
