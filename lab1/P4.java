import java.util.Scanner;

public class P4 {    
    public static void main(String[] args) {
        int height;
        String letter;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the height: ");
        height = sc.nextInt();
        sc.close();
        if (height == 0) { //height must be greater than 0
            System.out.println("Error input!!");
            return;
        }
        else {
            for (int i = 1; i <= height; i++) {
                if (i%2 == 1) letter = "A"; //if i is odd start with A
                else letter = "B"; //if i is even start with B
                for (int j = 1; j <= i; j++) {
                    System.out.print(letter+letter);//prints XX j times
                    if (letter == "A") letter = "B";
                    else if (letter == "B") letter = "A"; //alternates between A and B
                }
                System.out.println();//new line after each row
            }
        }
    }
}
